package io.dropwizard.logging.json;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.logging.ConsoleAppenderFactory;
import io.dropwizard.logging.DefaultLoggingFactory;
import io.dropwizard.logging.layout.DiscoverableLayoutFactory;
import io.dropwizard.request.logging.LogbackAccessRequestLogFactory;
import io.dropwizard.validation.BaseValidator;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class LayoutDeserializationTests {

    static {
        BootstrapLogging.bootstrap(Level.INFO);
    }

    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final YamlConfigurationFactory<ConsoleAppenderFactory> factory = new YamlConfigurationFactory<>(
        ConsoleAppenderFactory.class, BaseValidator.newValidator(), objectMapper, "dw");

    @Before
    public void setUp() {
        objectMapper.getSubtypeResolver().registerSubtypes(AccessJsonLayoutBaseFactory.class, EventJsonLayoutBaseFactory.class);
    }

    @Test
    public void testDeserializeJson() throws Exception {
        ConsoleAppenderFactory<ILoggingEvent> consoleAppenderFactory =
            factory.build(new File(Resources.getResource("yaml/json-log.yml").toURI()));
        DiscoverableLayoutFactory layout = requireNonNull(consoleAppenderFactory.getLayout());
        assertThat(layout).isInstanceOf(EventJsonLayoutBaseFactory.class);
        EventJsonLayoutBaseFactory eventJsonLayoutBaseFactory = (EventJsonLayoutBaseFactory) layout;
        assertThat(eventJsonLayoutBaseFactory).isNotNull();
        assertThat(eventJsonLayoutBaseFactory.isPrettyPrint()).isTrue();
        assertThat(eventJsonLayoutBaseFactory.getTimestampFormat()).isEqualTo("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    @Test
    public void testDeserializeAccessJson() throws Exception {
        ConsoleAppenderFactory<IAccessEvent> consoleAppenderFactory =
            factory.build(new File(Resources.getResource("yaml/json-access-log.yml").toURI()));
        DiscoverableLayoutFactory layout = requireNonNull(consoleAppenderFactory.getLayout());
        assertThat(layout).isInstanceOf(AccessJsonLayoutBaseFactory.class);
        AccessJsonLayoutBaseFactory factory = (AccessJsonLayoutBaseFactory) layout;
        assertThat(factory.getTimestampFormat()).isEqualTo("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        assertThat(factory.isPrettyPrint()).isFalse();
        assertThat(factory.isIncludeTimestamp()).isTrue();
        assertThat(factory.isAppendLineSeparator()).isTrue();
        assertThat(factory.isIncludeRemoteAddr()).isFalse();
        assertThat(factory.isIncludeRemoteUser()).isTrue();
        assertThat(factory.isIncludeRequestTime()).isFalse();
        assertThat(factory.isIncludeRequestURI()).isFalse();
        assertThat(factory.isIncludeStatusCode()).isTrue();
        assertThat(factory.isIncludeMethod()).isTrue();
        assertThat(factory.isIncludeProtocol()).isFalse();
        assertThat(factory.isIncludeContentLength()).isFalse();
        assertThat(factory.isIncludeRequestURL()).isTrue();
        assertThat(factory.isIncludeRemoteHost()).isTrue();
        assertThat(factory.isIncludeServerName()).isFalse();
        assertThat(factory.isIncludeRequestHeaders()).isFalse();
        assertThat(factory.isIncludeRequestParameters()).isTrue();
        assertThat(factory.isIncludeLocalPort()).isFalse();
        assertThat(factory.isIncludeRequestContent()).isTrue();
        assertThat(factory.isIncludeResponseContent()).isFalse();
    }

    @Test
    public void testLogJsonToConsole() throws Exception {
        ConsoleAppenderFactory<ILoggingEvent> consoleAppenderFactory =
            factory.build(new File(Resources.getResource("yaml/json-log.yml").toURI()));

        DefaultLoggingFactory defaultLoggingFactory = new DefaultLoggingFactory();
        defaultLoggingFactory.setAppenders(ImmutableList.of(consoleAppenderFactory));

        PrintStream old = System.out;
        ByteArrayOutputStream redirectedStream = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(redirectedStream));
            defaultLoggingFactory.configure(new MetricRegistry(), "json-log-test");
            LoggerFactory.getLogger("com.example.app").info("Application log");
            Thread.sleep(100); // Need to wait, because the logger is async
        } finally {
            System.setOut(old);
        }

        JsonNode jsonNode = objectMapper.readTree(redirectedStream.toString());
        assertThat(jsonNode).isNotNull();
        assertThat(jsonNode.get("level").asText()).isEqualTo("INFO");
        assertThat(jsonNode.get("logger").asText()).isEqualTo("com.example.app");
        assertThat(jsonNode.get("message").asText()).isEqualTo("Application log");
    }

    @Test
    public void testLogExceptionsAsJson() throws Exception {
        ConsoleAppenderFactory<ILoggingEvent> consoleAppenderFactory =
            factory.build(new File(Resources.getResource("yaml/json-log.yml").toURI()));

        DefaultLoggingFactory defaultLoggingFactory = new DefaultLoggingFactory();
        defaultLoggingFactory.setAppenders(ImmutableList.of(consoleAppenderFactory));

        PrintStream old = System.out;
        ByteArrayOutputStream redirectedStream = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(redirectedStream));
            defaultLoggingFactory.configure(new MetricRegistry(), "json-log-test");
            LoggerFactory.getLogger("com.example.app").error("Unable to execute an SQL query",
                new IllegalStateException("Database is down!"));
            Thread.sleep(100); // Need to wait, because the logger is async
        } finally {
            System.setOut(old);
        }

        System.out.println(redirectedStream.toString());
        JsonNode jsonNode = objectMapper.readTree(redirectedStream.toString());
        assertThat(jsonNode).isNotNull();
        assertThat(jsonNode.get("level").asText()).isEqualTo("ERROR");
        assertThat(jsonNode.get("logger").asText()).isEqualTo("com.example.app");
        assertThat(jsonNode.get("message").asText()).isEqualTo("Unable to execute an SQL query");
        assertThat(jsonNode.get("exception").asText()).startsWith("java.lang.IllegalStateException: Database is down!\n");
    }

    @Test
    public void testLogAccessJsonToConsole() throws Exception {
        ConsoleAppenderFactory<IAccessEvent> consoleAppenderFactory = factory.build(new File(
            Resources.getResource("yaml/json-access-log-default.yml").toURI()));
        // Use sys.err, because there are some other log configuration messages in std.out
        consoleAppenderFactory.setTarget(ConsoleAppenderFactory.ConsoleStream.STDERR);

        final LogbackAccessRequestLogFactory requestLogHandler = new LogbackAccessRequestLogFactory();
        requestLogHandler.setAppenders(ImmutableList.of(consoleAppenderFactory));

        PrintStream old = System.err;
        ByteArrayOutputStream redirectedStream = new ByteArrayOutputStream();
        try {
            System.setErr(new PrintStream(redirectedStream));
            RequestLog requestLog = requestLogHandler.build("json-access-log-test");

            Request request = mock(Request.class);
            when(request.getRemoteAddr()).thenReturn("10.0.0.1");
            when(request.getTimeStamp()).thenReturn(TimeUnit.SECONDS.toMillis(1353042047));
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestURI()).thenReturn("/test/users?age=22&city=LA");
            when(request.getProtocol()).thenReturn("HTTP/1.1");
            when(request.getParameterNames()).thenReturn(new Vector<>(ImmutableList.of("age", "city")).elements());
            when(request.getParameterValues("age")).thenReturn(new String[]{"22"});
            when(request.getParameterValues("city")).thenReturn(new String[]{"LA"});
            when(request.getAttributeNames()).thenReturn(new Vector<String>().elements());
            when(request.getHeaderNames()).thenReturn(new Vector<>(ImmutableList.of("Connection", "User-Agent")).elements());
            when(request.getHeader("Connection")).thenReturn("keep-alive");
            when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0");

            Response response = mock(Response.class);
            when(response.getStatus()).thenReturn(200);
            when(response.getContentCount()).thenReturn(8290L);
            HttpFields httpFields = new HttpFields();
            httpFields.add("Date", "Mon, 16 Nov 2012 05:00:48 GMT");
            httpFields.add("Server", "Apache/2.4.12");
            when(response.getHttpFields()).thenReturn(httpFields);
            when(response.getHeaderNames()).thenReturn(ImmutableList.of("Date", "Server"));
            when(response.getHeader("Date")).thenReturn("Mon, 16 Nov 2012 05:00:48 GMT");
            when(response.getHeader("Server")).thenReturn("Apache/2.4.12");

            requestLog.log(request, response);
            Thread.sleep(100); // Need to wait, because the logger is async
        } finally {
            System.setErr(old);
        }

        JsonNode jsonNode = objectMapper.readTree(redirectedStream.toString());
        System.out.println(jsonNode);
        assertThat(jsonNode).isNotNull();
        assertThat(jsonNode.get("timestamp")).isNotNull();
        assertThat(jsonNode.get("requestTime")).isNotNull();
        assertThat(jsonNode.get("remoteAddress").asText()).isEqualTo("10.0.0.1");
        assertThat(jsonNode.get("status").asText()).isEqualTo("200");
        assertThat(jsonNode.get("method").asText()).isEqualTo("GET");
        assertThat(jsonNode.get("uri").asText()).isEqualTo("/test/users?age=22&city=LA");
        assertThat(jsonNode.get("protocol").asText()).isEqualTo("HTTP/1.1");
        assertThat(jsonNode.get("userAgent").asText()).isEqualTo("Mozilla/5.0");
        assertThat(jsonNode.get("contentLength").asText()).isEqualTo("8290");
        assertThat(jsonNode.get("params").get("city").get(0).asText()).isEqualTo("LA");
        assertThat(jsonNode.get("params").get("age").get(0).asText()).isEqualTo("22");
    }
}

package io.dropwizard.logging.json;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.PrefixedRootCauseFirstThrowableProxyConverter;
import io.dropwizard.logging.json.layout.EventJsonLayout;

import java.util.TimeZone;

/**
 * <p/>
 * <table>
 * <tr>
 * <th>Name</th>
 * <th>Default</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td >{@code includeLevel}</td>
 * <td >true</td>
 * <td>Whether to include the logging level.</code></td>
 * </tr>
 * <tr>
 * <td>{@code includeThreadName}</td>
 * <td>true</td>
 * <td>Whether to include the thread name.</td>
 * </tr>
 * <tr>
 * <td>{@code includeMdc}</td>
 * <td>true</td>
 * <td>Whether to include the MDC properties.</td>
 * </tr>
 * <tr>
 * <td>{@code includeLoggerName}</td>
 * <td>true</td>
 * <td>Whether to include the logger name.</td>
 * </tr>
 * <tr>
 * <td >{@code includeFormattedMessage}</td>
 * <td>true</td>
 * <td>Whether to include the formatted message. The raw (unformatted) message is available as {@code includeMessage}.
 * Most people will want the formatted message as the raw message does not reflect any log message arguments.</td>
 * </tr>
 * <tr>
 * <td >{@code includeMessage}</td>
 * <td>true</td>
 * <td>Whether to include the raw message.</code></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td >{@code includeException}</td>
 * <td>true</td>
 * <td>If there is no exception, this property will not be added to the JSON map. If there is an exception, it
 * will be formatted to a string added to the JSON map.</td>
 * </tr>
 * <tr>
 * <td >{@code context}</td>
 * <td>false</td>
 * <td>The name of the logger context. Defaults to <em>default</em>.</td>
 * </tr>
 * </table>
 */
@JsonTypeName("json")
public class EventJsonLayoutBaseFactory extends AbstractJsonLayoutBaseFactory<ILoggingEvent> {

    private boolean includeLevel = true;
    private boolean includeThreadName = true;
    private boolean includeMDC = true;
    private boolean includeLoggerName = true;
    private boolean includeFormattedMessage = true;
    private boolean includeMessage = false;
    private boolean includeException = true;
    private boolean includeContextName = false;

    @JsonProperty
    public boolean isIncludeLevel() {
        return includeLevel;
    }

    @JsonProperty
    public void setIncludeLevel(boolean includeLevel) {
        this.includeLevel = includeLevel;
    }

    @JsonProperty
    public boolean isIncludeThreadName() {
        return includeThreadName;
    }

    @JsonProperty
    public void setIncludeThreadName(boolean includeThreadName) {
        this.includeThreadName = includeThreadName;
    }

    @JsonProperty
    public boolean isIncludeMDC() {
        return includeMDC;
    }

    @JsonProperty
    public void setIncludeMDC(boolean includeMDC) {
        this.includeMDC = includeMDC;
    }

    @JsonProperty
    public boolean isIncludeLoggerName() {
        return includeLoggerName;
    }

    @JsonProperty
    public void setIncludeLoggerName(boolean includeLoggerName) {
        this.includeLoggerName = includeLoggerName;
    }

    @JsonProperty
    public boolean isIncludeFormattedMessage() {
        return includeFormattedMessage;
    }

    @JsonProperty
    public void setIncludeFormattedMessage(boolean includeFormattedMessage) {
        this.includeFormattedMessage = includeFormattedMessage;
    }

    @JsonProperty
    public boolean isIncludeMessage() {
        return includeMessage;
    }

    @JsonProperty
    public void setIncludeMessage(boolean includeMessage) {
        this.includeMessage = includeMessage;
    }

    @JsonProperty
    public boolean isIncludeException() {
        return includeException;
    }

    @JsonProperty
    public void setIncludeException(boolean includeException) {
        this.includeException = includeException;
    }

    @JsonProperty
    public boolean isIncludeContextName() {
        return includeContextName;
    }

    @JsonProperty
    public void setIncludeContextName(boolean includeContextName) {
        this.includeContextName = includeContextName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public LayoutBase<ILoggingEvent> build(LoggerContext context, TimeZone timeZone) {
        final EventJsonLayout jsonLayout = new EventJsonLayout(createDropwizardJsonFormatter());
        jsonLayout.setContext(context);
        jsonLayout.setTimestampFormat(getTimestampFormat());
        jsonLayout.setTimestampFormatTimezoneId(timeZone.getID());
        jsonLayout.setAppendLineSeparator(isAppendLineSeparator());
        jsonLayout.setThrowableProxyConverter(createThrowableProxyConverter());

        jsonLayout.setIncludeLevel(includeLevel);
        jsonLayout.setIncludeThreadName(includeThreadName);
        jsonLayout.setIncludeMDC(includeMDC);
        jsonLayout.setIncludeLoggerName(includeLoggerName);
        jsonLayout.setIncludeFormattedMessage(includeFormattedMessage);
        jsonLayout.setIncludeMessage(includeMessage);
        jsonLayout.setIncludeException(includeException);
        jsonLayout.setIncludeContextName(includeContextName);
        return jsonLayout;
    }

    protected ThrowableHandlingConverter createThrowableProxyConverter() {
        return new RootCauseFirstThrowableProxyConverter();
    }

}

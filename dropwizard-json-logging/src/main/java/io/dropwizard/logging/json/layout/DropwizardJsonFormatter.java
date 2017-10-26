package io.dropwizard.logging.json.layout;

import ch.qos.logback.core.CoreConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * A custom JSON formatter which is simpler and faster than the default one from `logback-contrib`.
 */
public class DropwizardJsonFormatter {

    private static final int DEFAULT_BUFFER_SIZE = 512;

    private final ObjectMapper objectMapper;
    private final boolean doesAppendLineSeparator;
    private final int bufferSize;

    public DropwizardJsonFormatter(ObjectMapper objectMapper, boolean prettyPrint, boolean doesAppendLineSeparator,
                                   int bufferSize) {
        this.objectMapper = prettyPrint ? objectMapper.enable(SerializationFeature.INDENT_OUTPUT) : objectMapper;
        this.doesAppendLineSeparator = doesAppendLineSeparator;
        this.bufferSize = bufferSize;
    }

    public DropwizardJsonFormatter(ObjectMapper objectMapper, boolean prettyPrint, boolean doesAppendLineSeparator) {
        this(objectMapper, prettyPrint, doesAppendLineSeparator, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Converts the provided map as a JSON object according to the configured JSON mapper.
     *
     * @param m the provided map
     * @return the JSON as a string
     */
    public String toJson(Map m) {
        final StringWriter writer = new StringWriter(bufferSize);
        try {
            objectMapper.writeValue(writer, m);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to format map as a JSON", e);
        }
        if (doesAppendLineSeparator) {
            writer.append(CoreConstants.LINE_SEPARATOR);
        }
        return writer.toString();
    }
}

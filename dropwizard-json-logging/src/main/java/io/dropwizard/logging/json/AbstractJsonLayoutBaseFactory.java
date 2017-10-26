package io.dropwizard.logging.json;

import ch.qos.logback.core.spi.DeferredProcessingAware;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.logging.json.layout.DropwizardJsonFormatter;
import io.dropwizard.logging.layout.DiscoverableLayoutFactory;

import javax.annotation.Nullable;

/**
 * <table>
 * <tr>
 * <th>Name</th>
 * <th>Default</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>{@code includeTimestamp}</td>
 * <td>true</td>
 * <td>Whether to include the timestamp of the event to the JSON map.</td>
 * </tr>
 * <tr>
 * <td>{@code timestampFormat}</td>
 * <td>(none)</td>
 * <td>By default, the timestamp is not formatted; To format the timestamp using set the property with the
 * corresponding DateTimeFormatter string, for example, {@code yyyy-MM-dd HH:mm:ss.SSS}</td>
 * </tr>
 * <tr>
 * <td>{@code prettyPrint}</td>
 * <td>{@code false}</td>
 * <td>Whether jackson json printing should beautify the output for human readability</td>
 * </tr>
 * <tr>
 * <td>{@code appendLineSeparator}</td>
 * <td>{@code true}</td>
 * <td>Whether to append a line separator at the end of the message formatted as JSON.</td>
 * </tr>
 * </table>
 */
public abstract class AbstractJsonLayoutBaseFactory<E extends DeferredProcessingAware>
    implements DiscoverableLayoutFactory<E> {

    private boolean includeTimestamp = true;

    @Nullable
    private String timestampFormat;

    private boolean prettyPrint;
    private boolean appendLineSeparator = true;

    @JsonProperty
    @Nullable
    public String getTimestampFormat() {
        return timestampFormat;
    }

    @JsonProperty
    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

    @JsonProperty
    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    @JsonProperty
    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    @JsonProperty
    public boolean isIncludeTimestamp() {
        return includeTimestamp;
    }

    @JsonProperty
    public void setIncludeTimestamp(boolean includeTimestamp) {
        this.includeTimestamp = includeTimestamp;
    }

    @JsonProperty
    public boolean isAppendLineSeparator() {
        return appendLineSeparator;
    }

    @JsonProperty
    public void setAppendLineSeparator(boolean appendLineSeparator) {
        this.appendLineSeparator = appendLineSeparator;
    }

    protected DropwizardJsonFormatter createDropwizardJsonFormatter() {
        return new DropwizardJsonFormatter(Jackson.newObjectMapper(), isPrettyPrint(), isAppendLineSeparator());
    }
}

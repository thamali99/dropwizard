package io.dropwizard.logging.json.layout;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * A custom JSON layout for logging events.
 */
public class EventJsonLayout extends JsonLayout {

    private final DropwizardJsonFormatter dropwizardJsonFormatter;

    public EventJsonLayout(DropwizardJsonFormatter dropwizardJsonFormatter) {
        this.dropwizardJsonFormatter = dropwizardJsonFormatter;
    }

    @Override
    @Nullable
    public String doLayout(ILoggingEvent event) {
        final Map map = toJsonMap(event);
        if (map == null || map.isEmpty()) {
            return null;
        }
        return dropwizardJsonFormatter.toJson(map);
    }
}

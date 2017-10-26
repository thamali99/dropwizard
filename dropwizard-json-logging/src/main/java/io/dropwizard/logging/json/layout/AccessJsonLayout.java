package io.dropwizard.logging.json.layout;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.contrib.json.access.JsonLayout;
import com.google.common.net.HttpHeaders;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * A custom JSON layout for access log events
 */
public class AccessJsonLayout extends JsonLayout {

    private DropwizardJsonFormatter dropwizardJsonFormatter;
    private boolean includeUserAgent;
    private boolean includeResponseHeaders;

    public AccessJsonLayout(DropwizardJsonFormatter dropwizardJsonFormatter, boolean includeUserAgent, boolean includeResponseHeaders) {
        this.dropwizardJsonFormatter = dropwizardJsonFormatter;
        this.includeUserAgent = includeUserAgent;
        this.includeResponseHeaders = includeResponseHeaders;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map toJsonMap(IAccessEvent event) {
        Map map = super.toJsonMap(event);
        add("userAgent", includeUserAgent, event.getRequestHeader(HttpHeaders.USER_AGENT), map);
        add("contentLength", includeContentLength, event.getResponseHeader(HttpHeaders.CONTENT_LENGTH), map);
        addMap("responseHeaders", includeResponseHeaders, event.getResponseHeaderMap(), map);
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Nullable
    public String doLayout(IAccessEvent event) {
        final Map map = toJsonMap(event);
        if (map == null || map.isEmpty()) {
            return null;
        }

        return dropwizardJsonFormatter.toJson(map);
    }
}

package io.dropwizard.logging.json;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.contrib.json.access.JsonLayout;
import ch.qos.logback.core.LayoutBase;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.logging.json.layout.AccessJsonLayout;
import io.dropwizard.logging.json.layout.DropwizardJsonFormatter;

import java.util.TimeZone;

/**
 * <table>
 * <tr>
 * <th>Name</th>
 * <th>Default</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>{@code includeRemoteAddress}</td>
 * <td>true</td>
 * <td>Whether to include the IP address of the client or last proxy that sent the request.</td>
 * </tr>
 * <tr>
 * <td>{@code includeRemoteUser}</td>
 * <td>true</td>
 * <td>Whether to include information about the remote user</code></td>
 * </tr>
 * <tr>
 * <td>{@code includeRequestTime}</td>
 * <td>true</td>
 * <td>Whether to include the time elapsed between receiving the request and logging it. By default, the value is formatted as {@code s.SSS}</td>
 * </tr>
 * <tr>
 * <td>{@code includeUri}</td>
 * <td>true</td>
 * <td>Whether tp include the uri of the request.</td>
 * </tr>
 * <tr>
 * <td>{@code includeStatusCode}</td>
 * <td>true</td>
 * <td>Whether to include the status code of the response.</td>
 * </tr>
 * <tr>
 * <td>{@code includeMethod}</td>
 * <td>true</td>
 * <td>Whether to include the request HTTP method.</td>
 * </tr>
 * <tr>
 * <td>{@code includeProtocol}</td>
 * <td>true</td>
 * <td>Whether to include the request HTTP protocol.</td>
 * </tr>
 * <tr>
 * <td>{@code includeContentLength}</td>
 * <td>true</td>
 * <td>Whether to include the response content length, if it's known.</td>
 * </tr>
 * <tr>
 * <td>{@code includeRequestURL}</td>
 * <td>false</td>
 * <td>Whether to include the request URL (method, URI, query parameters, protocol).</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>{@code includeRemoteHost}</td>
 * <td>false</td>
 * <td>Whether to include the fully qualified name of the client or the last proxy that sent the request.</td>
 * </tr>
 * <tr>
 * <td>{@code includeServerName}</td>
 * <td>false</td>
 * <td>Whether to include the name of the server to which the request was sent.</td>
 * </tr>
 * <tr>
 * <td>{@code includeRequestHeaders}</td>
 * <td>true</td>
 * <td>Whether to include request headers</td>
 * </tr>
 * <tr>
 * <td>{@code includeRequestParameters}</td>
 * <td>true</td>
 * <td>Whether to include request parameters.</td>
 * </tr>
 * <tr>
 * <td>{@code includeUserAgent}</td>
 * <td>true</td>
 * <td>Whether to include the user agent of the request.</td>
 * </tr>
 * <tr>
 * <td>{@code includeResponseHeaders}</td>
 * <td>false</td>
 * <td>Whether to include the response headers.</td>
 * </tr>
 * <tr>
 * <tr>
 * <td>{@code includeLocalPort}</td>
 * <td>false</td>
 * <td>Whether to include the port number of the interface on which the request was received.</code></td>
 * </tr>
 * <tr>
 * <td>{@code includeRequestContent}</td>
 * <td>false</td>
 * <td>Whether to include the port number of the interface on which the request body.</td>
 * </tr>
 * <tr>
 * <td>{@code includeResponseContent}</td>
 * <td>false</td>
 * <td>Whether to include the port number of the interface on which the response body.</td>
 * </tr>
 * </table>
 */
@JsonTypeName("access-json")
public class AccessJsonLayoutBaseFactory extends AbstractJsonLayoutBaseFactory<IAccessEvent> {

    private boolean includeRemoteAddr = true;
    private boolean includeRemoteUser = true;
    private boolean includeRequestTime = true;
    private boolean includeRequestURI = true;
    private boolean includeStatusCode = true;
    private boolean includeMethod = true;
    private boolean includeProtocol = true;
    private boolean includeContentLength = true;
    private boolean includeRequestURL = false;
    private boolean includeRemoteHost = false;
    private boolean includeServerName = false;
    private boolean includeRequestHeaders = false;
    private boolean includeRequestParameters = true;
    private boolean includeUserAgent = true;
    private boolean includeResponseHeaders = false;
    private boolean includeLocalPort = false;
    private boolean includeRequestContent = false;
    private boolean includeResponseContent = false;

    @JsonProperty
    public boolean isIncludeRemoteAddr() {
        return includeRemoteAddr;
    }

    @JsonProperty
    public void setIncludeRemoteAddr(boolean includeRemoteAddr) {
        this.includeRemoteAddr = includeRemoteAddr;
    }

    @JsonProperty
    public boolean isIncludeRemoteUser() {
        return includeRemoteUser;
    }

    @JsonProperty
    public void setIncludeRemoteUser(boolean includeRemoteUser) {
        this.includeRemoteUser = includeRemoteUser;
    }

    @JsonProperty
    public boolean isIncludeRequestTime() {
        return includeRequestTime;
    }

    @JsonProperty
    public void setIncludeRequestTime(boolean includeRequestTime) {
        this.includeRequestTime = includeRequestTime;
    }

    @JsonProperty
    public boolean isIncludeRequestURI() {
        return includeRequestURI;
    }

    @JsonProperty
    public void setIncludeRequestURI(boolean includeRequestURI) {
        this.includeRequestURI = includeRequestURI;
    }

    @JsonProperty
    public boolean isIncludeStatusCode() {
        return includeStatusCode;
    }

    @JsonProperty
    public void setIncludeStatusCode(boolean includeStatusCode) {
        this.includeStatusCode = includeStatusCode;
    }

    @JsonProperty
    public boolean isIncludeMethod() {
        return includeMethod;
    }

    @JsonProperty
    public void setIncludeMethod(boolean includeMethod) {
        this.includeMethod = includeMethod;
    }

    @JsonProperty
    public boolean isIncludeProtocol() {
        return includeProtocol;
    }

    @JsonProperty
    public void setIncludeProtocol(boolean includeProtocol) {
        this.includeProtocol = includeProtocol;
    }

    @JsonProperty
    public boolean isIncludeContentLength() {
        return includeContentLength;
    }

    @JsonProperty
    public void setIncludeContentLength(boolean includeContentLength) {
        this.includeContentLength = includeContentLength;
    }

    @JsonProperty
    public boolean isIncludeRequestURL() {
        return includeRequestURL;
    }

    @JsonProperty
    public void setIncludeRequestURL(boolean includeRequestURL) {
        this.includeRequestURL = includeRequestURL;
    }

    @JsonProperty
    public boolean isIncludeRemoteHost() {
        return includeRemoteHost;
    }

    @JsonProperty
    public void setIncludeRemoteHost(boolean includeRemoteHost) {
        this.includeRemoteHost = includeRemoteHost;
    }

    @JsonProperty
    public boolean isIncludeServerName() {
        return includeServerName;
    }

    @JsonProperty
    public void setIncludeServerName(boolean includeServerName) {
        this.includeServerName = includeServerName;
    }

    @JsonProperty
    public boolean isIncludeRequestHeaders() {
        return includeRequestHeaders;
    }

    @JsonProperty
    public void setIncludeRequestHeaders(boolean includeRequestHeaders) {
        this.includeRequestHeaders = includeRequestHeaders;
    }

    @JsonProperty
    public boolean isIncludeRequestParameters() {
        return includeRequestParameters;
    }

    @JsonProperty
    public void setIncludeRequestParameters(boolean includeRequestParameters) {
        this.includeRequestParameters = includeRequestParameters;
    }

    @JsonProperty
    public boolean isIncludeLocalPort() {
        return includeLocalPort;
    }

    @JsonProperty
    public void setIncludeLocalPort(boolean includeLocalPort) {
        this.includeLocalPort = includeLocalPort;
    }

    @JsonProperty
    public boolean isIncludeRequestContent() {
        return includeRequestContent;
    }

    @JsonProperty
    public void setIncludeRequestContent(boolean includeRequestContent) {
        this.includeRequestContent = includeRequestContent;
    }

    @JsonProperty
    public boolean isIncludeResponseContent() {
        return includeResponseContent;
    }

    @JsonProperty
    public void setIncludeResponseContent(boolean includeResponseContent) {
        this.includeResponseContent = includeResponseContent;
    }

    public boolean isIncludeUserAgent() {
        return includeUserAgent;
    }

    public void setIncludeUserAgent(boolean includeUserAgent) {
        this.includeUserAgent = includeUserAgent;
    }

    public boolean isIncludeResponseHeaders() {
        return includeResponseHeaders;
    }

    public void setIncludeResponseHeaders(boolean includeResponseHeaders) {
        this.includeResponseHeaders = includeResponseHeaders;
    }

    @Override
    public LayoutBase<IAccessEvent> build(LoggerContext context, TimeZone timeZone) {
        final JsonLayout jsonLayout = new AccessJsonLayout(createDropwizardJsonFormatter(),
            includeUserAgent, includeResponseHeaders);
        jsonLayout.setContext(context);
        jsonLayout.setTimestampFormat(getTimestampFormat());
        jsonLayout.setTimestampFormatTimezoneId(timeZone.getID());
        jsonLayout.setAppendLineSeparator(isAppendLineSeparator());

        jsonLayout.setIncludeRemoteAddr(includeRemoteAddr);
        jsonLayout.setIncludeRequestContent(includeRequestContent);
        jsonLayout.setIncludeRequestHeader(includeRequestHeaders);
        jsonLayout.setIncludeRequestParameter(includeRequestParameters);
        jsonLayout.setIncludeRequestTime(includeRequestTime);
        jsonLayout.setIncludeRequestURI(includeRequestURI);
        jsonLayout.setIncludeRequestURL(includeRequestURL);
        jsonLayout.setIncludeRemoteUser(includeRemoteUser);
        jsonLayout.setIncludeContentLength(includeContentLength);
        jsonLayout.setIncludeLocalPort(includeLocalPort);
        jsonLayout.setIncludeMethod(includeMethod);
        jsonLayout.setIncludeProtocol(includeProtocol);
        jsonLayout.setIncludeRemoteHost(includeRemoteHost);
        jsonLayout.setIncludeResponseContent(includeResponseContent);
        jsonLayout.setIncludeServerName(includeServerName);
        jsonLayout.setIncludeStatusCode(includeStatusCode);
        return jsonLayout;
    }
}

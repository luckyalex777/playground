package com.alexswd.top.network.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request with URL, headers, and parameters.
 *
 * <p>
 * HttpRequest uses a builder pattern to construct requests. Headers and parameters are stored
 * immutably once the request is built.
 *
 * @since 1.0
 */
public final class HttpRequest {

  private final String url;
  private final Map<String, String> headers;
  private final Map<String, String> parameters;

  /**
   * Private constructor for HttpRequest. Use {@link Builder} to construct instances.
   *
   * @param builder the builder containing request configuration
   */
  private HttpRequest(Builder builder) {
    this.url = builder.url;
    this.headers = Collections.unmodifiableMap(new HashMap<>(builder.headers));
    this.parameters = Collections.unmodifiableMap(new HashMap<>(builder.parameters));
  }

  /**
   * Returns the request URL.
   *
   * @return the URL string
   */
  public String getUrl() {
    return url;
  }

  /**
   * Returns the request headers.
   *
   * @return an unmodifiable map of headers
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * Returns the request parameters.
   *
   * @return an unmodifiable map of parameters
   */
  public Map<String, String> getParameters() {
    return parameters;
  }

  /**
   * Builder for constructing HttpRequest instances.
   *
   * <p>
   * Supports fluent API for adding headers and parameters to the request.
   */
  public static final class Builder {

    private final String url;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> parameters = new HashMap<>();

    /**
     * Constructs a new Builder with the specified URL.
     *
     * @param url the request URL
     */
    public Builder(String url) {
      this.url = url;
    }

    /**
     * Adds a header to the request.
     *
     * @param key the header name
     * @param value the header value
     * @return this builder for method chaining
     */
    public Builder addHeader(String key, String value) {
      headers.put(key, value);
      return this;
    }

    /**
     * Adds a parameter to the request.
     *
     * @param key the parameter name
     * @param value the parameter value
     * @return this builder for method chaining
     */
    public Builder addParameter(String key, String value) {
      parameters.put(key, value);
      return this;
    }

    /**
     * Builds and returns the HttpRequest.
     *
     * @return a new HttpRequest instance
     */
    public HttpRequest build() {
      return new HttpRequest(this);
    }
  }
}

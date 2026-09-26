package com.alexswd.top.network.http;

/**
 * Represents an HTTP response with status code and body.
 *
 * <p>
 * HttpResponse is an immutable data class containing the HTTP status code and response body.
 *
 * @since 1.0
 */
public final class HttpResponse {

  private final int status;
  private final String body;

  /**
   * Constructs an HttpResponse with the specified status code and body.
   *
   * @param status the HTTP status code (e.g., 200, 404, 500)
   * @param body the response body content
   */
  public HttpResponse(int status, String body) {
    this.status = status;
    this.body = body;
  }

  /**
   * Returns the HTTP status code.
   *
   * @return the status code
   */
  public int getStatus() {
    return status;
  }

  /**
   * Returns the response body.
   *
   * @return the response body content
   */
  public String getBody() {
    return body;
  }
}

package com.alexswd.network.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HttpResponse}.
 *
 * <p>
 * Tests verify HttpResponse construction, status code and body accessors, and response data
 * integrity.
 */
@DisplayName("HttpResponse")
class HttpResponseTest {

  @Test
  @DisplayName("should construct response with status and body")
  void testConstructResponse() {
    int status = 200;
    String body = "Success";

    HttpResponse response = new HttpResponse(status, body);

    assertThat(response.getStatus(), is(status));
    assertThat(response.getBody(), is(body));
  }

  @Test
  @DisplayName("should handle success status code")
  void testSuccessStatus() {
    HttpResponse response = new HttpResponse(200, "OK");

    assertThat(response.getStatus(), is(200));
    assertThat(response.getBody(), is("OK"));
  }

  @Test
  @DisplayName("should handle created status code")
  void testCreatedStatus() {
    HttpResponse response = new HttpResponse(201, "Created");

    assertThat(response.getStatus(), is(201));
    assertThat(response.getBody(), is("Created"));
  }

  @Test
  @DisplayName("should handle client error status code")
  void testClientErrorStatus() {
    HttpResponse response = new HttpResponse(404, "Not Found");

    assertThat(response.getStatus(), is(404));
    assertThat(response.getBody(), is("Not Found"));
  }

  @Test
  @DisplayName("should handle server error status code")
  void testServerErrorStatus() {
    HttpResponse response = new HttpResponse(500, "Internal Server Error");

    assertThat(response.getStatus(), is(500));
    assertThat(response.getBody(), is("Internal Server Error"));
  }

  @Test
  @DisplayName("should handle empty body")
  void testEmptyBody() {
    HttpResponse response = new HttpResponse(204, "");

    assertThat(response.getStatus(), is(204));
    assertThat(response.getBody(), is(""));
  }

  @Test
  @DisplayName("should handle multiline body")
  void testMultilineBody() {
    String body = "Line 1\nLine 2\nLine 3";
    HttpResponse response = new HttpResponse(200, body);

    assertThat(response.getStatus(), is(200));
    assertThat(response.getBody(), is(body));
  }

  @Test
  @DisplayName("should handle JSON body")
  void testJsonBody() {
    String jsonBody = "{\"status\":\"success\",\"data\":{\"id\":123}}";
    HttpResponse response = new HttpResponse(200, jsonBody);

    assertThat(response.getStatus(), is(200));
    assertThat(response.getBody(), is(jsonBody));
  }
}

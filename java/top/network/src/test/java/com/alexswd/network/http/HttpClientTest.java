package com.alexswd.top.network.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HttpClient}.
 *
 * <p>
 * Tests verify HttpClient instantiation with default and custom sockets, and basic structure. Full
 * integration tests would require a mock HTTP server.
 */
@DisplayName("HttpClient")
@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "NP_NULL_PARAM_DEREF_NONVIRTUAL",
    justification = "Test validates that null socket parameter is properly rejected with NullPointerException")
class HttpClientTest {

  @Test
  @DisplayName("should create HttpClient instance with default PlainSocket")
  void testHttpClientInstantiationDefault() throws IOException {
    HttpClient client = new HttpClient();
    assertThat(client, notNullValue());
  }

  @Test
  @DisplayName("should throw NullPointerException when socket is null")
  void testHttpClientInstantiationWithNullSocket() {
    org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class,
        () -> new HttpClient(null));
  }

  @Test
  @DisplayName("should create request with builder")
  void testCreateRequest() {
    HttpRequest request =
        new HttpRequest.Builder("http://example.com").addHeader("User-Agent", "Test").build();

    assertThat(request, notNullValue());
    assertThat(request.getUrl(), notNullValue());
  }

  @Test
  @DisplayName("should create response")
  void testCreateResponse() {
    HttpResponse response = new HttpResponse(200, "test body");

    assertThat(response, notNullValue());
    assertThat(response.getStatus(), notNullValue());
    assertThat(response.getBody(), notNullValue());
  }
}

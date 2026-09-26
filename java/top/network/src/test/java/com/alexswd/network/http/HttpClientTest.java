package com.alexswd.top.network.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HttpClient}.
 *
 * <p>
 * Tests verify HttpClient instantiation and basic structure. Full integration tests would require a
 * mock HTTP server.
 */
@DisplayName("HttpClient")
class HttpClientTest {

  @Test
  @DisplayName("should create HttpClient instance")
  void testHttpClientInstantiation() {
    HttpClient client = new HttpClient();
    assertThat(client, notNullValue());
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

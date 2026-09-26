package com.alexswd.network.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HttpRequest}.
 *
 * <p>
 * Tests verify HttpRequest builder functionality, parameter and header management, and immutability
 * of the request once built.
 */
@DisplayName("HttpRequest")
class HttpRequestTest {

  @Test
  @DisplayName("should create request with builder and URL")
  void testBuilderWithUrl() {
    String url = "https://example.com";
    HttpRequest request = new HttpRequest.Builder(url).build();

    assertThat(request.getUrl(), is(url));
    assertThat(request.getHeaders().isEmpty(), is(true));
    assertThat(request.getParameters().isEmpty(), is(true));
  }

  @Test
  @DisplayName("should add single header to request")
  void testAddSingleHeader() {
    HttpRequest request = new HttpRequest.Builder("https://example.com")
        .addHeader("Content-Type", "application/json").build();

    assertThat(request.getHeaders(), hasEntry("Content-Type", "application/json"));
  }

  @Test
  @DisplayName("should add multiple headers to request")
  void testAddMultipleHeaders() {
    HttpRequest request = new HttpRequest.Builder("https://example.com")
        .addHeader("Content-Type", "application/json").addHeader("Authorization", "Bearer token123")
        .addHeader("User-Agent", "HttpClient/1.0").build();

    Map<String, String> headers = request.getHeaders();
    assertThat(headers.size(), is(3));
    assertThat(headers, hasEntry("Content-Type", "application/json"));
    assertThat(headers, hasEntry("Authorization", "Bearer token123"));
    assertThat(headers, hasEntry("User-Agent", "HttpClient/1.0"));
  }

  @Test
  @DisplayName("should add single parameter to request")
  void testAddSingleParameter() {
    HttpRequest request =
        new HttpRequest.Builder("https://example.com").addParameter("key", "value").build();

    assertThat(request.getParameters(), hasEntry("key", "value"));
  }

  @Test
  @DisplayName("should add multiple parameters to request")
  void testAddMultipleParameters() {
    HttpRequest request = new HttpRequest.Builder("https://example.com").addParameter("page", "1")
        .addParameter("limit", "10").addParameter("sort", "name").build();

    Map<String, String> params = request.getParameters();
    assertThat(params.size(), is(3));
    assertThat(params, hasEntry("page", "1"));
    assertThat(params, hasEntry("limit", "10"));
    assertThat(params, hasEntry("sort", "name"));
  }

  @Test
  @DisplayName("should add headers and parameters together")
  void testAddHeadersAndParameters() {
    HttpRequest request = new HttpRequest.Builder("https://example.com")
        .addHeader("Content-Type", "application/json").addParameter("key", "value")
        .addHeader("Authorization", "Bearer token").addParameter("id", "123").build();

    assertThat(request.getHeaders().size(), is(2));
    assertThat(request.getParameters().size(), is(2));
    assertThat(request.getHeaders(), hasEntry("Content-Type", "application/json"));
    assertThat(request.getParameters(), hasEntry("key", "value"));
  }

  @Test
  @DisplayName("should return unmodifiable collections")
  void testUnmodifiableCollections() {
    HttpRequest request = new HttpRequest.Builder("https://example.com")
        .addHeader("X-Custom", "value").addParameter("q", "search").build();

    // Attempt to modify should fail with UnsupportedOperationException
    try {
      request.getHeaders().put("X-New", "header");
      // If we get here, the collection was mutable (test failure)
      assertThat(false, is(true));
    } catch (UnsupportedOperationException e) {
      // Expected - collection should be unmodifiable
      assertThat(true, is(true));
    }
  }
}

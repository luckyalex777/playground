package com.alexswd.top.network.http;

import com.alexswd.top.network.tcp.ISocket;
import com.alexswd.top.network.tcp.PlainSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Simple HTTP client supporting GET requests.
 *
 * <p>
 * HttpClient executes HTTP GET requests using {@link ISocket} and returns the response as an
 * {@link HttpResponse} containing status code and body. The client uses {@link java.net.URL} for
 * URL parsing and constructs raw HTTP requests.
 *
 * @since 1.0
 */
public final class HttpClient {

  /** Default HTTP port. */
  private static final int DEFAULT_HTTP_PORT = 80;

  /** Default HTTPS port. */
  private static final int DEFAULT_HTTPS_PORT = 443;

  /** CRLF line terminator for HTTP protocol. */
  private static final String CRLF = "\r\n";

  /** HTTP GET method. */
  private static final String GET_METHOD = "GET";

  /**
   * Executes a GET request and returns the response.
   *
   * @param request the HTTP request to execute
   * @return the HTTP response
   * @throws IOException if an I/O error occurs
   * @throws IllegalArgumentException if the request URL is invalid
   */
  @SuppressWarnings("PMD.CloseResource")
  public HttpResponse get(HttpRequest request) throws IOException {
    URL url = new URL(request.getUrl());

    // Get host and port
    String hostName = url.getHost();
    int port = url.getPort();
    if (port == -1) {
      port = "https".equalsIgnoreCase(url.getProtocol()) ? DEFAULT_HTTPS_PORT : DEFAULT_HTTP_PORT;
    }

    // Resolve hostname to IP address
    InetAddress address = InetAddress.getByName(hostName);

    // Create socket
    ISocket socket = new PlainSocket();
    try {
      // Connect to host
      socket.connect(address, port);

      // Build and send HTTP request
      String httpRequest = buildHttpRequest(url, request);
      OutputStream out = socket.getOutputStream();
      out.write(httpRequest.getBytes(StandardCharsets.UTF_8));
      out.flush();

      // Read and parse response
      InputStream in = socket.getInputStream();
      return parseResponse(in);
    } finally {
      socket.close();
    }
  }

  /**
   * Builds the HTTP GET request string.
   *
   * @param url the parsed URL
   * @param request the HTTP request
   * @return the HTTP request as a string
   */
  private String buildHttpRequest(URL url, HttpRequest request) {
    StringBuilder sb = new StringBuilder();

    // Request line: GET /path?query HTTP/1.1
    String path = url.getPath();
    if (path == null || path.isEmpty()) {
      path = "/";
    }

    // Add parameters as query string
    String queryString = buildQueryString(request.getParameters());
    if (queryString != null && !queryString.isEmpty()) {
      path += "?" + queryString;
    }

    // Add URL's existing query string if present
    if (url.getQuery() != null && !url.getQuery().isEmpty()) {
      if (queryString != null && !queryString.isEmpty()) {
        path += "&" + url.getQuery();
      } else {
        path += "?" + url.getQuery();
      }
    }

    sb.append(GET_METHOD).append(" ").append(path).append(" HTTP/1.1").append(CRLF);

    // Host header
    sb.append("Host: ").append(url.getHost());
    if (url.getPort() != -1) {
      sb.append(":").append(url.getPort());
    }
    sb.append(CRLF);

    // Connection close header
    sb.append("Connection: close").append(CRLF);

    // Add custom headers
    for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
      sb.append(header.getKey()).append(": ").append(header.getValue()).append(CRLF);
    }

    // Empty line to signal end of headers
    sb.append(CRLF);

    return sb.toString();
  }

  /**
   * Builds the query string from parameters.
   *
   * @param parameters the request parameters
   * @return the query string or null if no parameters
   */
  private String buildQueryString(Map<String, String> parameters) {
    if (parameters.isEmpty()) {
      return null;
    }

    StringJoiner joiner = new StringJoiner("&");
    for (Map.Entry<String, String> param : parameters.entrySet()) {
      joiner.add(param.getKey() + "=" + urlEncode(param.getValue()));
    }

    return joiner.toString();
  }

  /**
   * URL encodes a parameter value.
   *
   * @param value the value to encode
   * @return the encoded value
   */
  private String urlEncode(String value) {
    return value.replace(" ", "%20").replace("&", "%26").replace("=", "%3D").replace("#", "%23")
        .replace("?", "%3F");
  }

  /**
   * Parses the HTTP response from the input stream.
   *
   * @param in the input stream from the socket
   * @return the parsed HTTP response
   * @throws IOException if an I/O error occurs
   */
  @SuppressWarnings({"PMD.AvoidBranchingStatementAsLastInLoop", "PMD.EmptyControlStatement"})
  private HttpResponse parseResponse(InputStream in) throws IOException {
    // Read status line
    String statusLine = readLine(in);
    int statusCode = parseStatusCode(statusLine);

    // Skip response headers until empty line
    String line;
    while ((line = readLine(in)) != null && !line.isEmpty()) {
      // Loop condition handles header skipping - body intentionally empty
    }

    // Read response body
    StringBuilder body = new StringBuilder();
    while ((line = readLine(in)) != null) {
      body.append(line).append("\n");
    }

    String responseBody = body.toString().trim();

    return new HttpResponse(statusCode, responseBody);
  }

  /**
   * Reads a line from the input stream until CRLF or LF.
   *
   * @param in the input stream
   * @return the line without line terminator, or null if EOF
   * @throws IOException if an I/O error occurs
   */
  private String readLine(InputStream in) throws IOException {
    StringBuilder sb = new StringBuilder();
    int b;

    while ((b = in.read()) != -1) {
      if (b == '\r') {
        in.read(); // read \n
        break;
      }
      if (b == '\n') {
        break;
      }
      sb.append((char) b);
    }

    if (sb.length() == 0 && b == -1) {
      return null;
    }

    return sb.toString();
  }

  /**
   * Parses the HTTP status code from the status line.
   *
   * @param statusLine the status line (e.g., "HTTP/1.1 200 OK")
   * @return the status code or 0 if parsing fails
   */
  private int parseStatusCode(String statusLine) {
    if (statusLine == null) {
      return 0;
    }

    String[] parts = statusLine.split(" ");
    if (parts.length >= 2) {
      try {
        return Integer.parseInt(parts[1]);
      } catch (NumberFormatException e) {
        return 0;
      }
    }

    return 0;
  }
}

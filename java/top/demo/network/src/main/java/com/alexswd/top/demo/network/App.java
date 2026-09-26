package com.alexswd.top.demo.network;

import com.alexswd.top.network.http.HttpClient;
import com.alexswd.top.network.http.HttpRequest;
import com.alexswd.top.network.http.HttpResponse;
import com.alexswd.top.network.tcp.PlainSocket;
import com.alexswd.top.network.tcp.TorSocket;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** HTTP client demo application. */
public final class App {

  private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

  private App() {}

  /**
   * Main entry point. Executes HTTP GET request to the URL provided as the first argument.
   *
   * <p>
   * Usage: java -cp ... App <url>
   *
   * @param args command line arguments (first argument should be the URL to request)
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      LOGGER.error("Usage: App <url>");
      System.exit(1);
    }

    String url = args[0];

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Testing URL on PlainSocket:");
    }
    testUrlOnPlainSocket(url);
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Testing URL on PlainSocket:");

      LOGGER.info("Testing URL on TorSocket:");
    }
    testUrlOnTorSocket(url);
  }

  /**
   * Tests a URL using PlainSocket connection.
   *
   * @param url the URL to test
   */
  public static void testUrlOnPlainSocket(String url) {
    try {
      PlainSocket socket = new PlainSocket();
      HttpClient client = new HttpClient(socket);
      HttpRequest request = new HttpRequest.Builder(url).build();
      HttpResponse response = client.get(request);

      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("PlainSocket - Status: {}", response.getStatus());
        LOGGER.info("PlainSocket - Body: {}", response.getBody());
      }
    } catch (IOException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("PlainSocket Error: {}", e.getMessage(), e);
      }
    }
  }

  /**
   * Tests a URL using TorSocket connection.
   *
   * @param url the URL to test
   */
  public static void testUrlOnTorSocket(String url) {
    try {
      TorSocket socket = new TorSocket();
      HttpClient client = new HttpClient(socket);
      HttpRequest request = new HttpRequest.Builder(url).build();
      HttpResponse response = client.get(request);

      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("TorSocket - Status: {}", response.getStatus());
        LOGGER.info("TorSocket - Body: {}", response.getBody());
      }
    } catch (IOException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("TorSocket Error: {}", e.getMessage(), e);
      }
    }
  }
}

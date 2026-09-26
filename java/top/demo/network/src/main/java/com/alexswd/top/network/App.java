package com.alexswd.top.network;

import com.alexswd.top.network.http.HttpClient;
import com.alexswd.top.network.http.HttpRequest;
import com.alexswd.top.network.http.HttpResponse;
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

    try {
      HttpClient client = new HttpClient();
      HttpRequest request = new HttpRequest.Builder(url).build();
      HttpResponse response = client.get(request);

      if (LOGGER.isInfoEnabled()) {
        LOGGER.info("Status: {}", response.getStatus());
        LOGGER.info("Body: {}", response.getBody());
      }
    } catch (IOException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Error: {}", e.getMessage(), e);
      }
      System.exit(1);
    }
  }
}

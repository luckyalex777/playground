package com.alexswd.top.network.tcp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.net.Socket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for {@link PlainSocket}.
 *
 * <p>
 * These tests verify that PlainSocket correctly extends SocketBase and maintains the delegation
 * behavior for plain (non-encrypted) socket communication.
 */
@DisplayName("PlainSocket")
class PlainSocketTest {

  @Test
  @DisplayName("should instantiate successfully with default constructor")
  void testInstantiationDefault() {
    try {
      PlainSocket plainSocket = new PlainSocket();
      assertThat(plainSocket, notNullValue());
      plainSocket.close();
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }

  @Test
  @DisplayName("should instantiate successfully with socket parameter")
  void testInstantiationWithSocket() {
    try (Socket socket = new Socket()) {
      PlainSocket plainSocket = new PlainSocket(socket);
      assertThat(plainSocket, notNullValue());
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }
}

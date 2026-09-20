package com.alexswd.network.tcp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link SocketBase}.
 */
@DisplayName("SocketBase")
class SocketBaseTest {

  private SocketBase socket;

  @BeforeEach
  void setUp() {
    socket = new SocketBase();
  }

  @Test
  @DisplayName("should instantiate successfully")
  void testInstantiation() {
    assertThat(socket, notNullValue());
  }

  @Test
  @DisplayName("should return default values for getters")
  void testDefaultGetterValues() {
    assertThat(socket.getInetAddress(), nullValue());
    assertThat(socket.getLocalAddress(), nullValue());
    assertThat(socket.getPort(), is(0));
    assertThat(socket.getLocalPort(), is(0));
    assertThat(socket.getRemoteSocketAddress(), nullValue());
    assertThat(socket.getLocalSocketAddress(), nullValue());
  }

  @Test
  @DisplayName("should return false for closed state getters")
  void testClosedStateGetters() {
    assertThat(socket.isClosed(), is(false));
    assertThat(socket.isConnected(), is(false));
    assertThat(socket.isInputShutdown(), is(false));
    assertThat(socket.isOutputShutdown(), is(false));
  }

  @Test
  @DisplayName("should return null for channel")
  void testGetChannel() {
    assertThat(socket.getChannel(), nullValue());
  }

  @Test
  @DisplayName("should return empty set for supported options")
  void testSupportedOptions() {
    Set<?> options = socket.supportedOptions();
    assertThat(options, notNullValue());
    assertThat(options.isEmpty(), is(true));
  }

  @Test
  @DisplayName("should handle method calls without throwing exceptions")
  void testMethodCallsWithoutExceptions() throws IOException {
    // These should not throw exceptions (stub implementations)
    socket.close();
    socket.shutdownInput();
    socket.shutdownOutput();
  }
}

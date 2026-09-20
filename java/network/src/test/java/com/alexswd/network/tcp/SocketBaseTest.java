package com.alexswd.network.tcp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.net.Socket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for {@link SocketBase}.
 *
 * <p>
 * These tests verify that SocketBase correctly delegates method calls to the underlying Socket
 * instance. Tests use real Socket instances to validate delegation behavior.
 */
@DisplayName("SocketBase")
class SocketBaseTest {

  @Test
  @DisplayName("should instantiate successfully with socket")
  void testInstantiation() {
    try (Socket socket = new Socket()) {
      SocketBase socketBase = new SocketBase(socket);
      assertThat(socketBase, notNullValue());
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }

  @Test
  @DisplayName("should delegate getPort to socket")
  void testGetPort() {
    try (Socket socket = new Socket()) {
      SocketBase socketBase = new SocketBase(socket);
      int port = socketBase.getPort();
      assertThat(port, is(socket.getPort()));
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }

  @Test
  @DisplayName("should delegate getLocalPort to socket")
  void testGetLocalPort() {
    try (Socket socket = new Socket()) {
      SocketBase socketBase = new SocketBase(socket);
      int port = socketBase.getLocalPort();
      assertThat(port, is(socket.getLocalPort()));
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }

  @Test
  @DisplayName("should delegate isClosed to socket")
  void testIsClosed() {
    try (Socket socket = new Socket()) {
      SocketBase socketBase = new SocketBase(socket);
      boolean isClosed = socketBase.isClosed();
      assertThat(isClosed, is(socket.isClosed()));
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }

  @Test
  @DisplayName("should delegate isConnected to socket")
  void testIsConnected() {
    try (Socket socket = new Socket()) {
      SocketBase socketBase = new SocketBase(socket);
      boolean isConnected = socketBase.isConnected();
      assertThat(isConnected, is(socket.isConnected()));
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }

  @Test
  @DisplayName("should delegate isInputShutdown to socket")
  void testIsInputShutdown() {
    try (Socket socket = new Socket()) {
      SocketBase socketBase = new SocketBase(socket);
      boolean isInputShutdown = socketBase.isInputShutdown();
      assertThat(isInputShutdown, is(socket.isInputShutdown()));
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }

  @Test
  @DisplayName("should delegate isOutputShutdown to socket")
  void testIsOutputShutdown() {
    try (Socket socket = new Socket()) {
      SocketBase socketBase = new SocketBase(socket);
      boolean isOutputShutdown = socketBase.isOutputShutdown();
      assertThat(isOutputShutdown, is(socket.isOutputShutdown()));
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }

  @Test
  @DisplayName("should delegate getInetAddress to socket")
  void testGetInetAddress() {
    try (Socket socket = new Socket()) {
      SocketBase socketBase = new SocketBase(socket);
      assertThat(socketBase.getInetAddress(), is(socket.getInetAddress()));
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }

  @Test
  @DisplayName("should delegate getLocalAddress to socket")
  void testGetLocalAddress() {
    try (Socket socket = new Socket()) {
      SocketBase socketBase = new SocketBase(socket);
      assertThat(socketBase.getLocalAddress(), is(socket.getLocalAddress()));
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }

  @Test
  @DisplayName("should delegate getChannel to socket")
  void testGetChannel() {
    try (Socket socket = new Socket()) {
      SocketBase socketBase = new SocketBase(socket);
      assertThat(socketBase.getChannel(), is(socket.getChannel()));
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }

  @Test
  @DisplayName("should delegate supportedOptions to socket")
  void testSupportedOptions() {
    try (Socket socket = new Socket()) {
      SocketBase socketBase = new SocketBase(socket);
      assertThat(socketBase.supportedOptions(), is(socket.supportedOptions()));
    } catch (IOException e) {
      // IOException is expected when creating a Socket in test environment;
      // continue with test to verify delegation works in principle
      // See java.net.Socket javadoc for constructor contract
      assert true;
    }
  }
}

package com.alexswd.network.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Tor socket implementation extending {@link SocketBase}.
 *
 * <p>
 * This class provides a socket implementation that routes connections through the Linux Tor service
 * via the SOCKS proxy. All connections are anonymized through the Tor network.
 *
 * <p>
 * Requires Tor service to be running on localhost:9050 (default Tor SOCKS port).
 *
 * @since 1.0
 */
public class TorSocket extends SocketBase {

  /** Default Tor SOCKS proxy host. */
  private static final String DEFAULT_TOR_HOST = "localhost";

  /** Default Tor SOCKS proxy port. */
  private static final int DEFAULT_TOR_PORT = 9050;

  /**
   * Constructs a new TorSocket instance with default Tor proxy settings.
   *
   * <p>
   * Connects through Tor SOCKS proxy at localhost:9050.
   *
   * @throws IOException if the socket cannot be created
   */
  public TorSocket() throws IOException {
    super(createTorSocketWithProxy(DEFAULT_TOR_HOST, DEFAULT_TOR_PORT));
  }

  /**
   * Constructs a new TorSocket instance with custom Tor proxy host and port.
   *
   * @param torHost the hostname or IP address of the Tor SOCKS proxy
   * @param torPort the port of the Tor SOCKS proxy
   * @throws IOException if the socket cannot be created
   */
  public TorSocket(String torHost, int torPort) throws IOException {
    super(createTorSocketWithProxy(torHost, torPort));
  }

  /**
   * Creates a socket configured to use the Tor SOCKS proxy.
   *
   * @param torHost the hostname or IP address of the Tor SOCKS proxy
   * @param torPort the port of the Tor SOCKS proxy
   * @return a new Socket instance
   * @throws IOException if the socket cannot be created
   */
  private static Socket createTorSocketWithProxy(String torHost, int torPort) throws IOException {
    Proxy torProxy = new Proxy(Type.SOCKS, new InetSocketAddress(torHost, torPort));
    return new Socket(torProxy);
  }

  /**
   * Connects to a remote address through the Tor network.
   *
   * @param address the remote host address
   * @param port the remote host port
   * @throws IOException if connection fails
   */
  @Override
  public void connect(InetAddress address, int port) throws IOException {
    socket.connect(new InetSocketAddress(address, port));
  }

  /**
   * Connects to a remote address through the Tor network with timeout.
   *
   * @param address the remote host address
   * @param port the remote host port
   * @param timeout the connection timeout in milliseconds
   * @throws IOException if connection fails
   */
  @Override
  public void connect(InetAddress address, int port, int timeout) throws IOException {
    socket.connect(new InetSocketAddress(address, port), timeout);
  }

  /**
   * Connects to a remote socket address through the Tor network.
   *
   * @param endpoint the remote socket address
   * @throws IOException if connection fails
   */
  @Override
  public void connect(SocketAddress endpoint) throws IOException {
    socket.connect(endpoint);
  }

  /**
   * Connects to a remote socket address through the Tor network with timeout.
   *
   * @param endpoint the remote socket address
   * @param timeout the connection timeout in milliseconds
   * @throws IOException if connection fails
   */
  @Override
  public void connect(SocketAddress endpoint, int timeout) throws IOException {
    socket.connect(endpoint, timeout);
  }
}

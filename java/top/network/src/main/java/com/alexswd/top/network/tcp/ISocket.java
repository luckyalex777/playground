package com.alexswd.top.network.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * Abstraction of network socket functionality.
 *
 * <p>
 * This interface defines all public methods of {@code java.net.Socket}, providing a contract for
 * socket implementations. Implementations may wrap, extend, or provide alternative implementations
 * of socket behavior.
 *
 * @since 1.0
 */
public interface ISocket {

  /**
   * Connects this socket to the specified remote address and port.
   *
   * @param address the remote address
   * @param port the remote port
   * @throws IOException if an I/O error occurs
   * @throws SocketException if the socket is already connected or closed
   */
  void connect(InetAddress address, int port) throws IOException;

  /**
   * Connects this socket to the specified remote address and port with a timeout.
   *
   * @param address the remote address
   * @param port the remote port
   * @param timeout the timeout in milliseconds, or zero for no timeout
   * @throws IOException if an I/O error occurs
   * @throws SocketException if the socket is already connected or closed
   */
  void connect(InetAddress address, int port, int timeout) throws IOException;

  /**
   * Connects this socket to the specified remote endpoint.
   *
   * @param endpoint the remote endpoint
   * @throws IOException if an I/O error occurs
   * @throws SocketException if the socket is already connected or closed
   */
  void connect(SocketAddress endpoint) throws IOException;

  /**
   * Connects this socket to the specified remote endpoint with a timeout.
   *
   * @param endpoint the remote endpoint
   * @param timeout the timeout in milliseconds, or zero for no timeout
   * @throws IOException if an I/O error occurs
   * @throws SocketException if the socket is already connected or closed
   */
  void connect(SocketAddress endpoint, int timeout) throws IOException;

  /**
   * Binds this socket to the specified local address and port.
   *
   * @param address the local address
   * @throws IOException if an I/O error occurs
   * @throws SocketException if the socket is already bound or closed
   */
  void bind(InetAddress address, int port) throws IOException;

  /**
   * Binds this socket to the specified local endpoint.
   *
   * @param bindpoint the local endpoint
   * @throws IOException if an I/O error occurs
   * @throws SocketException if the socket is already bound or closed
   */
  void bind(SocketAddress bindpoint) throws IOException;

  /**
   * Returns the address to which this socket is connected.
   *
   * @return the remote address, or null if not connected
   */
  InetAddress getInetAddress();

  /**
   * Returns the local address to which this socket is bound.
   *
   * @return the local address, or null if not bound
   */
  InetAddress getLocalAddress();

  /**
   * Returns the remote port to which this socket is connected.
   *
   * @return the remote port, or 0 if not connected
   */
  int getPort();

  /**
   * Returns the local port to which this socket is bound.
   *
   * @return the local port, or 0 if not bound
   */
  int getLocalPort();

  /**
   * Returns the remote socket address.
   *
   * @return the remote socket address, or null if not connected
   */
  SocketAddress getRemoteSocketAddress();

  /**
   * Returns the local socket address.
   *
   * @return the local socket address, or null if not bound
   */
  SocketAddress getLocalSocketAddress();

  /**
   * Returns an input stream for this socket.
   *
   * @return the input stream
   * @throws IOException if an I/O error occurs
   */
  InputStream getInputStream() throws IOException;

  /**
   * Returns an output stream for this socket.
   *
   * @return the output stream
   * @throws IOException if an I/O error occurs
   */
  OutputStream getOutputStream() throws IOException;

  /**
   * Sets the TCP_NODELAY option.
   *
   * @param on true to enable TCP_NODELAY, false to disable
   * @throws SocketException if an error occurs
   */
  void setTcpNoDelay(boolean on) throws SocketException;

  /**
   * Gets the TCP_NODELAY option.
   *
   * @return true if TCP_NODELAY is enabled, false otherwise
   * @throws SocketException if an error occurs
   */
  boolean getTcpNoDelay() throws SocketException;

  /**
   * Sets the SO_LINGER option.
   *
   * @param on true to enable SO_LINGER, false to disable
   * @param linger the timeout in seconds when SO_LINGER is enabled
   * @throws SocketException if an error occurs
   */
  void setSoLinger(boolean on, int linger) throws SocketException;

  /**
   * Gets the SO_LINGER option.
   *
   * @return the timeout in seconds, or -1 if SO_LINGER is disabled
   * @throws SocketException if an error occurs
   */
  int getSoLinger() throws SocketException;

  /**
   * Sets the SO_TIMEOUT option.
   *
   * @param timeout the timeout in milliseconds, or zero for no timeout
   * @throws SocketException if an error occurs
   */
  void setSoTimeout(int timeout) throws SocketException;

  /**
   * Gets the SO_TIMEOUT option.
   *
   * @return the timeout in milliseconds, or zero if no timeout is set
   * @throws SocketException if an error occurs
   */
  int getSoTimeout() throws SocketException;

  /**
   * Sets the SO_SNDBUF option.
   *
   * @param size the buffer size in bytes
   * @throws SocketException if an error occurs
   */
  void setSendBufferSize(int size) throws SocketException;

  /**
   * Gets the SO_SNDBUF option.
   *
   * @return the buffer size in bytes
   * @throws SocketException if an error occurs
   */
  int getSendBufferSize() throws SocketException;

  /**
   * Sets the SO_RCVBUF option.
   *
   * @param size the buffer size in bytes
   * @throws SocketException if an error occurs
   */
  void setReceiveBufferSize(int size) throws SocketException;

  /**
   * Gets the SO_RCVBUF option.
   *
   * @return the buffer size in bytes
   * @throws SocketException if an error occurs
   */
  int getReceiveBufferSize() throws SocketException;

  /**
   * Sets the SO_REUSEADDR option.
   *
   * @param on true to enable SO_REUSEADDR, false to disable
   * @throws SocketException if an error occurs
   */
  void setReuseAddress(boolean on) throws SocketException;

  /**
   * Gets the SO_REUSEADDR option.
   *
   * @return true if SO_REUSEADDR is enabled, false otherwise
   * @throws SocketException if an error occurs
   */
  boolean getReuseAddress() throws SocketException;

  /**
   * Sets the SO_KEEPALIVE option.
   *
   * @param on true to enable SO_KEEPALIVE, false to disable
   * @throws SocketException if an error occurs
   */
  void setKeepAlive(boolean on) throws SocketException;

  /**
   * Gets the SO_KEEPALIVE option.
   *
   * @return true if SO_KEEPALIVE is enabled, false otherwise
   * @throws SocketException if an error occurs
   */
  boolean getKeepAlive() throws SocketException;

  /**
   * Sets the IPTOS_LOWDELAY option for traffic class.
   *
   * @param tc the traffic class value
   * @throws SocketException if an error occurs
   */
  void setTrafficClass(int tc) throws SocketException;

  /**
   * Gets the traffic class option.
   *
   * @return the traffic class value
   * @throws SocketException if an error occurs
   */
  int getTrafficClass() throws SocketException;

  /**
   * Disables the input stream.
   *
   * @throws IOException if an I/O error occurs
   */
  void shutdownInput() throws IOException;

  /**
   * Disables the output stream.
   *
   * @throws IOException if an I/O error occurs
   */
  void shutdownOutput() throws IOException;

  /**
   * Closes this socket.
   *
   * @throws IOException if an I/O error occurs
   */
  void close() throws IOException;

  /**
   * Returns whether this socket is closed.
   *
   * @return true if the socket is closed, false otherwise
   */
  boolean isClosed();

  /**
   * Returns whether this socket is connected.
   *
   * @return true if the socket is connected, false otherwise
   */
  boolean isConnected();

  /**
   * Returns whether the input stream is shut down.
   *
   * @return true if the input stream is shut down, false otherwise
   */
  boolean isInputShutdown();

  /**
   * Returns whether the output stream is shut down.
   *
   * @return true if the output stream is shut down, false otherwise
   */
  boolean isOutputShutdown();

  /**
   * Returns the associated SocketChannel.
   *
   * @return the SocketChannel, or null if none is associated
   */
  SocketChannel getChannel();

  /**
   * Gets the value of a socket option.
   *
   * @param <T> the type of the socket option value
   * @param name the socket option name
   * @return the socket option value
   * @throws IOException if an I/O error occurs
   */
  <T> T getOption(SocketOption<T> name) throws IOException;

  /**
   * Sets the value of a socket option.
   *
   * @param <T> the type of the socket option value
   * @param name the socket option name
   * @param value the socket option value
   * @throws IOException if an I/O error occurs
   */
  <T> void setOption(SocketOption<T> name, T value) throws IOException;

  /**
   * Returns the set of socket options supported by this socket.
   *
   * @return the set of supported socket options
   */
  Set<SocketOption<?>> supportedOptions();
}

package com.alexswd.top.network.tcp;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * Base implementation of the {@link ISocket} interface.
 *
 * <p>
 * This class provides a delegating implementation that wraps a {@link java.net.Socket}. All methods
 * are delegated to the underlying socket instance.
 *
 * @since 1.0
 */
public class SocketBase implements ISocket {

  @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
      justification = "Intentional delegation wrapper that stores reference to mutable Socket")
  protected final Socket socket;

  /**
   * Constructs a new SocketBase instance with a newly created socket.
   *
   * @throws IOException if the socket cannot be created
   */
  public SocketBase() throws IOException {
    this(new Socket());
  }

  /**
   * Constructs a new SocketBase instance that wraps the provided socket.
   *
   * @param socket the underlying socket to wrap and delegate to
   */
  public SocketBase(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void connect(InetAddress address, int port) throws IOException {
    socket.connect(new java.net.InetSocketAddress(address, port));
  }

  @Override
  public void connect(InetAddress address, int port, int timeout) throws IOException {
    socket.connect(new java.net.InetSocketAddress(address, port), timeout);
  }

  @Override
  public void connect(SocketAddress endpoint) throws IOException {
    socket.connect(endpoint);
  }

  @Override
  public void connect(SocketAddress endpoint, int timeout) throws IOException {
    socket.connect(endpoint, timeout);
  }

  @Override
  public void bind(InetAddress address, int port) throws IOException {
    socket.bind(new java.net.InetSocketAddress(address, port));
  }

  @Override
  public void bind(SocketAddress bindpoint) throws IOException {
    socket.bind(bindpoint);
  }

  @Override
  public InetAddress getInetAddress() {
    return socket.getInetAddress();
  }

  @Override
  public InetAddress getLocalAddress() {
    return socket.getLocalAddress();
  }

  @Override
  public int getPort() {
    return socket.getPort();
  }

  @Override
  public int getLocalPort() {
    return socket.getLocalPort();
  }

  @Override
  public SocketAddress getRemoteSocketAddress() {
    return socket.getRemoteSocketAddress();
  }

  @Override
  public SocketAddress getLocalSocketAddress() {
    return socket.getLocalSocketAddress();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return socket.getInputStream();
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    return socket.getOutputStream();
  }

  @Override
  public void setTcpNoDelay(boolean on) throws SocketException {
    socket.setTcpNoDelay(on);
  }

  @Override
  public boolean getTcpNoDelay() throws SocketException {
    return socket.getTcpNoDelay();
  }

  @Override
  public void setSoLinger(boolean on, int linger) throws SocketException {
    socket.setSoLinger(on, linger);
  }

  @Override
  public int getSoLinger() throws SocketException {
    return socket.getSoLinger();
  }

  @Override
  public void setSoTimeout(int timeout) throws SocketException {
    socket.setSoTimeout(timeout);
  }

  @Override
  public int getSoTimeout() throws SocketException {
    return socket.getSoTimeout();
  }

  @Override
  public void setSendBufferSize(int size) throws SocketException {
    socket.setSendBufferSize(size);
  }

  @Override
  public int getSendBufferSize() throws SocketException {
    return socket.getSendBufferSize();
  }

  @Override
  public void setReceiveBufferSize(int size) throws SocketException {
    socket.setReceiveBufferSize(size);
  }

  @Override
  public int getReceiveBufferSize() throws SocketException {
    return socket.getReceiveBufferSize();
  }

  @Override
  public void setReuseAddress(boolean on) throws SocketException {
    socket.setReuseAddress(on);
  }

  @Override
  public boolean getReuseAddress() throws SocketException {
    return socket.getReuseAddress();
  }

  @Override
  public void setKeepAlive(boolean on) throws SocketException {
    socket.setKeepAlive(on);
  }

  @Override
  public boolean getKeepAlive() throws SocketException {
    return socket.getKeepAlive();
  }

  @Override
  public void setTrafficClass(int tc) throws SocketException {
    socket.setTrafficClass(tc);
  }

  @Override
  public int getTrafficClass() throws SocketException {
    return socket.getTrafficClass();
  }

  @Override
  public void shutdownInput() throws IOException {
    socket.shutdownInput();
  }

  @Override
  public void shutdownOutput() throws IOException {
    socket.shutdownOutput();
  }

  @Override
  public void close() throws IOException {
    socket.close();
  }

  @Override
  public boolean isClosed() {
    return socket.isClosed();
  }

  @Override
  public boolean isConnected() {
    return socket.isConnected();
  }

  @Override
  public boolean isInputShutdown() {
    return socket.isInputShutdown();
  }

  @Override
  public boolean isOutputShutdown() {
    return socket.isOutputShutdown();
  }

  @Override
  public SocketChannel getChannel() {
    return socket.getChannel();
  }

  @Override
  public <T> T getOption(SocketOption<T> name) throws IOException {
    return socket.getOption(name);
  }

  @Override
  public <T> void setOption(SocketOption<T> name, T value) throws IOException {
    socket.setOption(name, value);
  }

  @Override
  public Set<SocketOption<?>> supportedOptions() {
    return socket.supportedOptions();
  }
}

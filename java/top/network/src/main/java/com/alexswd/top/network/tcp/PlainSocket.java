package com.alexswd.top.network.tcp;

import java.io.IOException;
import java.net.Socket;

/**
 * Plain socket implementation extending {@link SocketBase}.
 *
 * <p>
 * This class provides a concrete, non-encrypted socket implementation. It extends SocketBase and
 * can be used for unencrypted TCP socket communication.
 *
 * @since 1.0
 */
public class PlainSocket extends SocketBase {

  /**
   * Constructs a new PlainSocket instance with a newly created socket.
   *
   * @throws IOException if the socket cannot be created
   */
  public PlainSocket() throws IOException {
    super();
  }

  /**
   * Constructs a new PlainSocket instance that wraps the provided socket.
   *
   * @param socket the underlying socket to wrap and delegate to
   */
  public PlainSocket(Socket socket) {
    super(socket);
  }
}

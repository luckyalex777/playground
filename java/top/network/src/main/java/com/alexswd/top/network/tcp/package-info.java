/**
 * Network socket abstraction and implementations.
 *
 * <p>
 * This package provides socket implementations for TCP network communication:
 *
 * <ul>
 * <li>{@link com.alexswd.top.network.tcp.ISocket} - Interface defining socket abstraction
 * <li>{@link com.alexswd.top.network.tcp.SocketBase} - Base implementation wrapping
 * {@code java.net.Socket}
 * <li>{@link com.alexswd.top.network.tcp.PlainSocket} - Plain unencrypted socket
 * <li>{@link com.alexswd.top.network.tcp.TorSocket} - Socket routed through Tor SOCKS proxy
 * </ul>
 *
 * <p>
 * The {@link com.alexswd.top.network.tcp.ISocket} interface abstracts socket operations, allowing
 * implementations to provide different connection strategies.
 * {@link com.alexswd.top.network.tcp.PlainSocket} provides standard TCP connections, while
 * {@link com.alexswd.top.network.tcp.TorSocket} routes connections through the Tor network for
 * anonymized communication.
 *
 * @since 1.0
 */
package com.alexswd.top.network.tcp;

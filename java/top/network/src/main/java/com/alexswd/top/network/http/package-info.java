/**
 * HTTP client implementation for GET requests.
 *
 * <p>
 * This package provides a lightweight HTTP client for executing GET requests:
 *
 * <ul>
 * <li>{@link com.alexswd.top.network.http.HttpClient} - HTTP client supporting GET requests
 * <li>{@link com.alexswd.top.network.http.HttpRequest} - HTTP request representation
 * <li>{@link com.alexswd.top.network.http.HttpResponse} - HTTP response with status and body
 * </ul>
 *
 * <p>
 * The {@link com.alexswd.top.network.http.HttpClient} uses pluggable socket implementations (via
 * {@link com.alexswd.top.network.tcp.ISocket}) to support different network communication
 * strategies. Requests are constructed using
 * {@link com.alexswd.top.network.http.HttpRequest.Builder} and responses provide status codes and
 * response bodies.
 *
 * @since 1.0
 */
package com.alexswd.top.network.http;

/**
 * Torrent file parser package.
 *
 * <p>
 * This package provides utilities for parsing and processing torrent files. Torrent files are
 * bencoded dictionaries that contain metadata about files to be downloaded via peer-to-peer
 * networks.
 *
 * <h2>Main Components</h2>
 *
 * <dl>
 * <dt>{@link TorrentParser}</dt>
 * <dd>The main entry point for parsing torrent files. This utility class provides static methods to
 * read a torrent file, decode its bencode-encoded content, extract all metadata, and construct a
 * structured {@link TorrentInfo} object.</dd>
 *
 * <dt>{@link TorrentInfo}</dt>
 * <dd>A data model representing the complete metadata of a torrent. Contains information such as:
 * <ul>
 * <li>Announce URLs and tracker information</li>
 * <li>Torrent name and file listing</li>
 * <li>Piece information (size and SHA-1 hashes)</li>
 * <li>Computed info hash (SHA-1)</li>
 * <li>Optional metadata (comments, creation date, creator)</li>
 * <li>File size and multi-file status</li>
 * </ul>
 * </dd>
 *
 * <dt>{@link TorrentFile}</dt>
 * <dd>Represents metadata for a single file within a torrent. Contains the file path, size, and
 * optional MD5 checksum.</dd>
 * </dl>
 *
 * <h2>Typical Usage</h2>
 *
 * <p>
 * Parse a torrent file and inspect its contents:
 *
 * <pre>{@code
 * try {
 *   TorrentInfo torrent = TorrentParser.parse("/path/to/file.torrent");
 *   System.out.println("Name: " + torrent.getName());
 *   System.out.println("Info Hash: " + torrent.getInfoHash());
 *   System.out.println("Total Size: " + torrent.getTotalLength() + " bytes");
 *   System.out.println("Number of Pieces: " + torrent.getPieceCount());
 *   for (TorrentFile file : torrent.getFiles()) {
 *     System.out.println("  File: " + file.getPath() + " (" + file.getLength() + " bytes)");
 *   }
 * } catch (IOException e) {
 *   System.err.println("Failed to parse torrent: " + e.getMessage());
 * }
 * }</pre>
 *
 * <h2>Torrent File Format</h2>
 *
 * <p>
 * Torrent files are bencoded dictionaries with the following structure:
 *
 * <pre>
 * announce          (required) Announce URL for the tracker
 * announce-list     (optional) List of alternate trackers
 * comment           (optional) Arbitrary comment
 * created by        (optional) Creator application/version
 * creation date     (optional) Creation timestamp (Unix epoch seconds)
 * encoding          (optional) Character encoding used
 * info              (required) Dictionary containing torrent metadata
 *   ├── name        The name of the root directory or file
 *   ├── piece length Length of each piece (typically 16KB to 16MB)
 *   ├── pieces      Concatenated SHA-1 hashes for each piece
 *   ├── private     (optional) If set to 1, torrent is private
 *   └── files       (multi-file only) List of file dictionaries
 *       ├── length  File size in bytes
 *       ├── path    List of path components
 *       └── md5sum  (optional) MD5 checksum
 * </pre>
 *
 * <h2>Info Hash</h2>
 *
 * <p>
 * The info hash is computed as the SHA-1 hash of the bencode-encoded "info" dictionary. This hash
 * is used to:
 * <ul>
 * <li>Uniquely identify torrents</li>
 * <li>Communicate with trackers</li>
 * <li>Participate in peer exchange (PEX) and DHT</li>
 * <li>Verify torrent integrity</li>
 * </ul>
 *
 * <h2>Thread Safety</h2>
 *
 * <p>
 * The {@link TorrentInfo} and {@link TorrentFile} classes are mutable and not thread-safe by
 * default. If instances are shared across threads, caller-provided synchronization is required.
 * {@link TorrentParser} is thread-safe as it only uses static methods without shared state.
 *
 * @since 1.0
 */
package com.alexswd.top.torrent.parser;

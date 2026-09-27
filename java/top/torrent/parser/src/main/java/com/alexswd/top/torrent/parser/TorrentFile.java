package com.alexswd.top.torrent.parser;

/**
 * Represents a single file within a torrent.
 *
 * <p>
 * This class encapsulates metadata about a file in a torrent, including its path, size, and
 * optional MD5 checksum. For single-file torrents, a TorrentFile represents the single file. For
 * multi-file torrents, a TorrentInfo contains multiple TorrentFile objects.
 *
 * <p>
 * Instances are mutable and not thread-safe by default.
 *
 * @since 1.0
 */
public class TorrentFile {
  private String path;
  private long length;
  private String md5sum;

  /**
   * Gets the file path.
   *
   * <p>
   * For single-file torrents, this is the file name. For multi-file torrents, this is the path
   * relative to the torrent root, with path components separated by forward slashes.
   *
   * @return the file path, or null if not set
   */
  public String getPath() {
    return path;
  }

  /**
   * Sets the file path.
   *
   * @param path the file path to set
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * Gets the size of the file in bytes.
   *
   * @return the file size in bytes
   */
  public long getLength() {
    return length;
  }

  /**
   * Sets the size of the file in bytes.
   *
   * @param length the file size in bytes
   */
  public void setLength(long length) {
    this.length = length;
  }

  /**
   * Gets the MD5 checksum of the file.
   *
   * <p>
   * This field is optional and may be null or empty. When present, it provides a quick verification
   * of file integrity.
   *
   * @return the MD5 checksum as a hexadecimal string, or null if not set
   */
  public String getMd5sum() {
    return md5sum;
  }

  /**
   * Sets the MD5 checksum of the file.
   *
   * @param md5sum the MD5 checksum as a hexadecimal string, or null to clear
   */
  public void setMd5sum(String md5sum) {
    this.md5sum = md5sum;
  }

  /**
   * Constructs a TorrentFile with the given path, length, and MD5 checksum.
   *
   * @param path the file path
   * @param length the file size in bytes
   * @param md5sum the MD5 checksum, or null if not provided
   */
  public TorrentFile(String path, long length, String md5sum) {
    this.path = path;
    this.length = length;
    this.md5sum = md5sum;
  }

  /**
   * Constructs a TorrentFile as a copy of another TorrentFile.
   *
   * @param source the TorrentFile to copy
   */
  public TorrentFile(TorrentFile source) {
    this.path = source.path;
    this.length = source.length;
    this.md5sum = source.md5sum;
  }
}

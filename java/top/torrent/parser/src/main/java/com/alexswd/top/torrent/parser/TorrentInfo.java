package com.alexswd.top.torrent.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HexFormat;
import java.util.List;

/**
 * Data model representing the metadata of a torrent file.
 *
 * <p>
 * This class encapsulates all parsed information from a torrent file, including:
 * <ul>
 * <li>Announce URLs and tracker information</li>
 * <li>Piece information (length, hashes)</li>
 * <li>File listing (single or multi-file torrents)</li>
 * <li>Metadata (name, creation date, encoding, comments)</li>
 * <li>The info hash (unique identifier)</li>
 * </ul>
 *
 * <p>
 * TorrentInfo instances are typically created by {@link TorrentParser} after parsing a torrent
 * file. The class provides getters and setters for all fields, as well as utility methods like
 * {@link #getPieceCount()}, {@link #getInfoHashBytes()}, and a detailed {@link #toString()}
 * representation.
 *
 * <p>
 * Thread safety: Instances are not thread-safe by default. Callers should synchronize access if
 * shared across multiple threads.
 *
 * @since 1.0
 */
public class TorrentInfo {
  private String filePath;
  private String announce;
  private List<String> announceList;
  private String comment;
  private String createdBy;
  private long creationDate;
  private String encoding;
  private String name;
  private long pieceLength;
  private List<String> pieceHashes;
  private boolean isPrivate;
  private boolean isMultiFile;
  private long totalLength;
  private List<TorrentFile> files;
  private String infoHash;

  /**
   * Returns the info hash as a byte array.
   *
   * <p>
   * Parses the hexadecimal info hash string and returns it as a 20-byte array (SHA-1 hash).
   *
   * @return the info hash as a byte array
   */
  public byte[] getInfoHashBytes() {
    return HexFormat.of().parseHex(this.infoHash);
  }


  /**
   * Gets the file path of the torrent file.
   *
   * @return the file path, or null if not set
   */
  public String getFilePath() {
    return filePath;
  }

  /**
   * Sets the file path of the torrent file.
   *
   * @param filePath the file path to set
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Gets the primary announce URL of the torrent.
   *
   * @return the announce URL, or empty string if not set
   */
  public String getAnnounce() {
    return announce;
  }

  /**
   * Sets the primary announce URL of the torrent.
   *
   * @param announce the announce URL to set
   */
  public void setAnnounce(String announce) {
    this.announce = announce;
  }

  /**
   * Gets the list of announce URLs from the announce-list tier.
   *
   * <p>
   * Returns an unmodifiable list. Returns an empty list if no announce-list is set.
   *
   * @return an unmodifiable list of announce URLs
   */
  public List<String> getAnnounceList() {
    if (announceList == null) {
      return new ArrayList<>();
    }
    return Collections.unmodifiableList(announceList);
  }

  /**
   * Sets the list of announce URLs from the announce-list tier.
   *
   * <p>
   * Creates a defensive copy of the input list.
   *
   * @param announceList the announce URL list to set, or null to clear
   */
  public void setAnnounceList(List<String> announceList) {
    if (announceList == null) {
      this.announceList = null;
    } else {
      this.announceList = new ArrayList<>(announceList);
    }
  }

  /**
   * Gets the comment field of the torrent.
   *
   * @return the comment, or empty string if not set
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets the comment field of the torrent.
   *
   * @param comment the comment to set
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * Gets the creator/application string of the torrent.
   *
   * @return the creator string, or empty string if not set
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * Sets the creator/application string of the torrent.
   *
   * @param createdBy the creator string to set
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Gets the creation date of the torrent (Unix timestamp in seconds).
   *
   * @return the creation date as a Unix timestamp, or 0 if not set
   */
  public long getCreationDate() {
    return creationDate;
  }

  /**
   * Sets the creation date of the torrent (Unix timestamp in seconds).
   *
   * @param creationDate the creation date as a Unix timestamp
   */
  public void setCreationDate(long creationDate) {
    this.creationDate = creationDate;
  }

  /**
   * Gets the encoding used in the torrent file.
   *
   * @return the encoding, or empty string if not set
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * Sets the encoding used in the torrent file.
   *
   * @param encoding the encoding to set
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * Gets the name of the torrent (torrent root name or file name).
   *
   * @return the name, or empty string if not set
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the torrent (torrent root name or file name).
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the length of each piece in bytes.
   *
   * @return the piece length in bytes
   */
  public long getPieceLength() {
    return pieceLength;
  }

  /**
   * Sets the length of each piece in bytes.
   *
   * @param pieceLength the piece length in bytes
   */
  public void setPieceLength(long pieceLength) {
    this.pieceLength = pieceLength;
  }

  /**
   * Gets the list of piece SHA-1 hashes.
   *
   * <p>
   * Returns an unmodifiable list of hexadecimal SHA-1 hashes, one per piece. Returns an empty list
   * if no hashes are set.
   *
   * @return an unmodifiable list of piece hashes
   */
  public List<String> getPieceHashes() {
    if (pieceHashes == null) {
      return new ArrayList<>();
    }
    return Collections.unmodifiableList(pieceHashes);
  }

  /**
   * Sets the list of piece SHA-1 hashes.
   *
   * <p>
   * Creates a defensive copy of the input list.
   *
   * @param pieceHashes the list of hashes to set, or null to clear
   */
  public void setPieceHashes(List<String> pieceHashes) {
    if (pieceHashes == null) {
      this.pieceHashes = new ArrayList<>();
    } else {
      this.pieceHashes = new ArrayList<>(pieceHashes);
    }
  }

  /**
   * Indicates whether this torrent is marked as private.
   *
   * <p>
   * Private torrents should not be announced to the DHT or peer exchange mechanisms.
   *
   * @return true if the torrent is private, false otherwise
   */
  public boolean isPrivate() {
    return isPrivate;
  }

  /**
   * Sets whether this torrent is marked as private.
   *
   * @param isPrivate true to mark as private, false otherwise
   */
  public void setPrivate(boolean isPrivate) {
    this.isPrivate = isPrivate;
  }

  /**
   * Indicates whether this is a multi-file torrent.
   *
   * @return true if the torrent contains multiple files, false for single-file torrents
   */
  public boolean isMultiFile() {
    return isMultiFile;
  }

  /**
   * Sets whether this is a multi-file torrent.
   *
   * @param isMultiFile true for multi-file torrents, false for single-file
   */
  public void setMultiFile(boolean isMultiFile) {
    this.isMultiFile = isMultiFile;
  }

  /**
   * Gets the total size of all files in the torrent.
   *
   * @return the total length in bytes
   */
  public long getTotalLength() {
    return totalLength;
  }

  /**
   * Sets the total size of all files in the torrent.
   *
   * @param totalLength the total length in bytes
   */
  public void setTotalLength(long totalLength) {
    this.totalLength = totalLength;
  }

  /**
   * Gets the list of files in the torrent.
   *
   * <p>
   * Returns an unmodifiable list of TorrentFile objects. For single-file torrents, this list
   * contains one entry. Returns an empty list if no files are set.
   *
   * @return an unmodifiable list of TorrentFile objects
   */
  public List<TorrentFile> getFiles() {
    if (files == null) {
      return new ArrayList<>();
    }
    return Collections.unmodifiableList(files);
  }

  /**
   * Sets the list of files in the torrent.
   *
   * <p>
   * Creates a defensive copy of the input list.
   *
   * @param files the list of files to set, or null to clear
   */
  public void setFiles(List<TorrentFile> files) {
    if (files == null) {
      this.files = null;
    } else {
      this.files = new ArrayList<>(files);
    }
  }

  /**
   * Gets the hexadecimal representation of the info hash.
   *
   * <p>
   * The info hash is the SHA-1 hash of the bencode-encoded info dictionary. It uniquely identifies
   * the torrent.
   *
   * @return the info hash as a hexadecimal string
   */
  public String getInfoHash() {
    return infoHash;
  }

  /**
   * Sets the hexadecimal representation of the info hash.
   *
   * @param infoHash the info hash as a hexadecimal string
   */
  public void setInfoHash(String infoHash) {
    this.infoHash = infoHash;
  }

  /**
   * Constructs a TorrentInfo with all metadata fields.
   *
   * <p>
   * Creates defensive copies of the input collections to prevent external modification.
   *
   * @param filePath the path to the torrent file
   * @param announce the primary announce URL
   * @param announceList the list of announce URLs from announce-list
   * @param comment the torrent comment
   * @param createdBy the creator/application string
   * @param creationDate the creation date as Unix timestamp (seconds)
   * @param encoding the character encoding used
   * @param name the torrent name
   * @param pieceLength the length of each piece in bytes
   * @param pieceHashes the list of SHA-1 hashes (hexadecimal) for each piece
   * @param isPrivate whether this is a private torrent
   * @param isMultiFile whether this is a multi-file torrent
   * @param totalLength the total size of all files in bytes
   * @param files the list of TorrentFile objects
   * @param infoHash the hexadecimal representation of the SHA-1 info hash
   */
  public TorrentInfo(String filePath, String announce, List<String> announceList, String comment,
      String createdBy, long creationDate, String encoding, String name, long pieceLength,
      List<String> pieceHashes, boolean isPrivate, boolean isMultiFile, long totalLength,
      List<TorrentFile> files, String infoHash) {
    this.filePath = filePath;
    this.announce = announce;
    this.announceList = new ArrayList<>(announceList);
    this.comment = comment;
    this.createdBy = createdBy;
    this.creationDate = creationDate;
    this.encoding = encoding;
    this.name = name;
    this.pieceLength = pieceLength;
    this.pieceHashes = new ArrayList<>(pieceHashes);
    this.isPrivate = isPrivate;
    this.isMultiFile = isMultiFile;
    this.totalLength = totalLength;
    this.files = new ArrayList<>(files);
    this.infoHash = infoHash;
  }

  /**
   * Constructs a TorrentInfo as a deep copy of another TorrentInfo instance.
   *
   * <p>
   * All fields are copied, including deep copies of mutable collections (announceList, pieceHashes,
   * files). If the source is null, the constructor completes without initializing fields.
   *
   * @param source the TorrentInfo instance to copy, or null
   */
  public TorrentInfo(TorrentInfo source) {
    if (source == null) {
      return;
    }

    this.filePath = source.filePath;
    this.announce = source.announce;

    // Deep copy of announceList
    if (source.announceList != null) {
      this.announceList = new ArrayList<>(source.announceList);
    } else {
      this.announceList = null;
    }

    this.comment = source.comment;
    this.createdBy = source.createdBy;
    this.creationDate = source.creationDate;
    this.encoding = source.encoding;
    this.name = source.name;
    this.pieceLength = source.pieceLength;

    // Deep copy of pieceHashes
    if (source.pieceHashes != null) {
      this.pieceHashes = new ArrayList<>(source.pieceHashes);
    } else {
      this.pieceHashes = null;
    }

    this.isPrivate = source.isPrivate;
    this.isMultiFile = source.isMultiFile;
    this.totalLength = source.totalLength;

    // Deep copy of files (assuming TorrentFile has its own copy constructor)
    if (source.files != null) {
      this.files = new ArrayList<>();
      for (TorrentFile file : source.files) {
        if (file != null) {
          // Assuming TorrentFile has a copy constructor
          this.files.add(new TorrentFile(file));
        } else {
          this.files.add(null);
        }
      }
    } else {
      this.files = null;
    }

    this.infoHash = source.infoHash;
  }

  /**
   * Calculates the number of pieces in the torrent.
   *
   * <p>
   * Uses ceiling division to account for partial final pieces if the total length is not evenly
   * divisible by the piece length.
   *
   * @return the number of pieces (rounds up if necessary)
   */
  public int getPieceCount() {
    var count = totalLength / pieceLength;
    if (totalLength % pieceLength != 0) {
      ++count;
    }
    return (int) count;
  }

  /**
   * Returns a formatted string representation of the torrent metadata.
   *
   * <p>
   * Includes a detailed summary of all torrent information including name, info hash, sizes, piece
   * information, file listings, and tracker URLs. The output is formatted with box-drawing
   * characters for visual clarity.
   *
   * @return a formatted string representation of this torrent
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("╔══════════════════════════════════════════════════════════╗\n");
    sb.append("║                    TORRENT METADATA                      ║\n");
    sb.append("╠══════════════════════════════════════════════════════════╣\n");
    sb.append(String.format("  Name         : %s%n", this.name));
    sb.append(String.format("  Info Hash    : %s%n", this.infoHash));
    sb.append(String.format("  Total Size   : %s (%,d bytes)%n", humanSize(this.totalLength),
        this.totalLength));
    sb.append(String.format("  Piece Length : %s (%,d bytes)%n", humanSize(this.pieceLength),
        this.pieceLength));
    sb.append(String.format("  Pieces       : %d%n", this.pieceHashes.size()));
    sb.append(String.format("  Private      : %s%n", this.isPrivate ? "yes" : "no"));
    sb.append(String.format("  Multi-file   : %s%n", this.isMultiFile ? "yes" : "no"));

    if (this.announce != null) {
      sb.append(String.format("  Announce     : %s%n", this.announce));
    }
    if (!this.announceList.isEmpty()) {
      sb.append("  Trackers     :\n");
      this.announceList.stream().distinct()
          .forEach(u -> sb.append("    • ").append(u).append("\n"));
    }
    if (this.comment != null) {
      sb.append(String.format("  Comment      : %s%n", this.comment));
    }
    if (this.createdBy != null) {
      sb.append(String.format("  Created by   : %s%n", this.createdBy));
    }
    if (this.creationDate != 0) {
      sb.append(
          String.format("  Created at   : %s%n", new java.util.Date(this.creationDate * 1000L)));
    }
    if (this.encoding != null) {
      sb.append(String.format("  Encoding     : %s%n", this.encoding));
    }

    sb.append(String.format("%n  Files (%d):%n", this.files.size()));
    this.files.forEach(f -> sb.append(f).append("\n"));

    sb.append("\n  First 5 piece SHA1 hashes:\n");
    this.pieceHashes.stream().limit(5).forEach(h -> sb.append("    ").append(h).append("\n"));
    if (this.pieceHashes.size() > 5) {
      sb.append("    ... (").append(this.pieceHashes.size() - 5).append(" more)\n");
    }

    sb.append("╚══════════════════════════════════════════════════════════╝\n");
    return sb.toString();
  }

  /**
   * Converts a byte count into a human-readable string representation.
   *
   * <p>
   * Returns the size with appropriate units (B, KB, MB, GB) as a formatted string with two decimal
   * places for fractional units.
   *
   * @param bytes the number of bytes to convert
   * @return a human-readable string representation of the size
   */
  private static String humanSize(long bytes) {
    if (bytes < 1024) {
      return bytes + " B";
    }
    if (bytes < 1024 * 1024) {
      return String.format("%.2f KB", bytes / 1024.0);
    }
    if (bytes < 1024 * 1024 * 1024) {
      return String.format("%.2f MB", bytes / (1024.0 * 1024));
    }
    return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
  }

}

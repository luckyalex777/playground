package com.alexswd.top.torrent.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HexFormat;
import java.util.List;

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

  public byte[] getInfoHashBytes() {
    return HexFormat.of().parseHex(this.infoHash);
  }


  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getAnnounce() {
    return announce;
  }

  public void setAnnounce(String announce) {
    this.announce = announce;
  }

  public List<String> getAnnounceList() {
    if (announceList == null) {
      return new ArrayList<>();
    }
    return Collections.unmodifiableList(announceList);
  }

  public void setAnnounceList(List<String> announceList) {
    if (announceList == null) {
      this.announceList = null;
    } else {
      this.announceList = new ArrayList<>(announceList);
    }
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public long getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(long creationDate) {
    this.creationDate = creationDate;
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getPieceLength() {
    return pieceLength;
  }

  public void setPieceLength(long pieceLength) {
    this.pieceLength = pieceLength;
  }

  public List<String> getPieceHashes() {
    if (pieceHashes == null) {
      return new ArrayList<>();
    }
    return Collections.unmodifiableList(pieceHashes);
  }

  public void setPieceHashes(List<String> pieceHashes) {
    if (pieceHashes == null) {
      this.pieceHashes = new ArrayList<>();
    } else {
      this.pieceHashes = new ArrayList<>(pieceHashes);
    }
  }

  public boolean isPrivate() {
    return isPrivate;
  }

  public void setPrivate(boolean isPrivate) {
    this.isPrivate = isPrivate;
  }

  public boolean isMultiFile() {
    return isMultiFile;
  }

  public void setMultiFile(boolean isMultiFile) {
    this.isMultiFile = isMultiFile;
  }

  public long getTotalLength() {
    return totalLength;
  }

  public void setTotalLength(long totalLength) {
    this.totalLength = totalLength;
  }

  public List<TorrentFile> getFiles() {
    if (files == null) {
      return new ArrayList<>();
    }
    return Collections.unmodifiableList(files);
  }

  public void setFiles(List<TorrentFile> files) {
    if (files == null) {
      this.files = null;
    } else {
      this.files = new ArrayList<>(files);
    }
  }

  public String getInfoHash() {
    return infoHash;
  }

  public void setInfoHash(String infoHash) {
    this.infoHash = infoHash;
  }

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

  public int getPieceCount() {
    var count = totalLength / pieceLength;
    if (totalLength % pieceLength != 0) {
      ++count;
    }
    return (int) count;
  }

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

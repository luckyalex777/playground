package com.alexswd.top.torrent.parser;

public class TorrentFile {
  private String path;
  private long length;
  private String md5sum;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public long getLength() {
    return length;
  }

  public void setLength(long length) {
    this.length = length;
  }

  public String getMd5sum() {
    return md5sum;
  }

  public void setMd5sum(String md5sum) {
    this.md5sum = md5sum;
  }

  public TorrentFile(String path, long length, String md5sum) {
    this.path = path;
    this.length = length;
    this.md5sum = md5sum;
  }

  public TorrentFile(TorrentFile source) {
    this.path = source.path;
    this.length = source.length;
    this.md5sum = source.md5sum;
  }
}

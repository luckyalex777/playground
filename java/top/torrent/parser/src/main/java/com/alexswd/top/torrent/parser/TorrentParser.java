package com.alexswd.top.torrent.parser;

import com.alexswd.top.bencode.Decoder;
import com.alexswd.top.bencode.Encoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public final class TorrentParser {
  private TorrentParser() {}

  public static TorrentInfo parse(String filePath) throws IOException {
    try {
      byte[] data = Files.readAllBytes(Paths.get(filePath));
      Object decoded = Decoder.bdecode(data);
      return buildTorrentInfo(decoded, filePath);
    } catch (Exception ex) {
      throw new IOException("Method parse failed", ex);
    }
  }

  private static TorrentInfo buildTorrentInfo(Object decoded, String filePath) throws IOException {
    if (!(decoded instanceof Map root)) {
      throw new IllegalArgumentException("Root bencode element must be a dictionary.");
    }
    String announce = root.containsKey("announce") ? str(root.get("announce")) : "";

    List<String> announceList = new ArrayList<>();
    if (root.containsKey("announce-list")) {
      var announceListObject = root.get("announce-list");
      if (announceListObject instanceof List tiers) {
        for (var tier : tiers) {
          if (tier instanceof List urls) {
            for (var url : urls) {
              announceList.add(str(url));
            }
          }
        }
      }
    }

    String comment = root.containsKey("comment") ? str(root.get("comment")) : "";
    String createdBy = root.containsKey("created by") ? str(root.get("created by")) : "";
    long creationDate =
        root.containsKey("creation date") ? (Long) root.get("creation date") : Long.valueOf(0);
    String encoding = root.containsKey("encoding") ? str(root.get("encoding")) : "";

    // Info dictionary
    Map<String, Object> info = null;
    var infoObject = root.get("info");
    if (infoObject instanceof Map<?, ?> infoMap) {
      info = safeCast(infoMap);
    }
    if (info == null) {
      throw new IllegalArgumentException("Missing 'info' dictionary");
    }
    String name = info.containsKey("name") ? str(info.get("name")) : "";
    long pieceLength = info.containsKey("name") ? (Long) info.get("piece length") : Long.valueOf(0);

    List<String> pieceHashList = new ArrayList<>();
    byte[] piecesRaw = (byte[]) info.get("pieces");
    if (piecesRaw != null) {
      int count = piecesRaw.length / 20;
      for (int i = 0; i < count; i++) {
        pieceHashList.add(bytesToHex(Arrays.copyOfRange(piecesRaw, i * 20, i * 20 + 20)));
      }
    }

    boolean isPrivate = false;
    if (info.containsKey("private")) {
      isPrivate = (Long) info.get("private") == 1L;
    }
    boolean isMultiFiles = info.containsKey("files");
    List<TorrentFile> torrentFiles = new ArrayList<>();
    long totalLength = 0;
    if (isMultiFiles) {
      var filesObject = info.get("files");
      if (filesObject instanceof List<?> files) {
        for (Object f : files) {
          if (f instanceof Map<?, ?> m) {
            var fm = safeCast(m);
            long length = (Long) fm.get("length");
            totalLength += length;

            if (fm.containsKey("path")) {
              var pathObject = fm.get("path");
              if (pathObject instanceof List<?> pathParts) {
                StringBuilder path = new StringBuilder();
                for (Object p : pathParts) {
                  if (!path.isEmpty()) {
                    path.append("/");
                  }
                  path.append(str(p));
                }
                String md5 = fm.containsKey("md5sum") ? str(fm.get("md5sum")) : null;
                var tf = new TorrentFile(path.toString(), length, md5);
                torrentFiles.add(tf);
              }
            }
          }
        }
      }
    } else {
      totalLength = (Long) info.get("length");
      String md5 = info.containsKey("md5sum") ? str(info.get("md5sum")) : null;
      var tf = new TorrentFile(name, totalLength, md5);
      torrentFiles.add(tf);
    }

    return new TorrentInfo(filePath, announce, announceList, comment, createdBy, creationDate,
        encoding, name, pieceLength, pieceHashList, isPrivate, isMultiFiles, totalLength,
        torrentFiles, computeInfoHash(root));
  }

  private static Map<String, Object> safeCast(Map<?, ?> map) {
    Map<String, Object> result = new HashMap<>();
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      if (entry.getKey() instanceof String && entry.getValue() != null) {
        result.put((String) entry.getKey(), entry.getValue());
      } else {
        throw new ClassCastException("Invalid types in map");
      }
    }
    return result;
  }

  private static String str(Object o) {
    if (o == null) {
      return null;
    }
    if (o instanceof byte[]) {
      return new String((byte[]) o, StandardCharsets.UTF_8);
    }
    return o.toString();
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  private static String computeInfoHash(Map<String, Object> root) throws IOException {
    if (!root.containsKey("info")) {
      throw new IOException("Info dictionary not found");
    }
    try {
      var infoBytes = Encoder.encode(root.get("info"));
      MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
      return bytesToHex(sha1.digest(infoBytes));
    } catch (NoSuchAlgorithmException ex) {
      throw new IOException("Method computeInfoHash failure", ex);
    }
  }
}

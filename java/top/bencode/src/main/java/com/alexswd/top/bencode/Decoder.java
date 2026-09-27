package com.alexswd.top.bencode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Decodes bencode-encoded data into Java objects.
 *
 * <p>
 * Bencode is a simple encoding format used in peer-to-peer systems such as BitTorrent. This decoder
 * supports:
 * <ul>
 * <li>Strings: length-prefixed byte strings (e.g., "4:spam")
 * <li>Integers: wrapped in 'i' and 'e' delimiters (e.g., "i3e")
 * <li>Lists: ordered collections wrapped in 'l' and 'e' delimiters
 * <li>Dictionaries: key-value maps wrapped in 'd' and 'e' delimiters
 * </ul>
 */
public final class Decoder {
  private static byte[] data;
  private static int pos;
  private static final Logger logger = LoggerFactory.getLogger(Decoder.class);

  private Decoder() {}

  /**
   * Decodes bencode-encoded data into a Java object.
   *
   * @param encodedData the bencode-encoded byte array
   * @return the decoded object (Long, byte[], List, or Map)
   * @throws IOException if the data is malformed or incomplete
   */
  public static Object bdecode(byte[] encodedData) throws IOException {
    data = new byte[encodedData.length];
    System.arraycopy(encodedData, 0, data, 0, encodedData.length);
    pos = 0;
    return bdecode();
  }

  /**
   * Recursively decodes the next bencode value from the current position.
   *
   * @return the decoded object (Long, byte[], List, or Map)
   * @throws IOException if the data is malformed, incomplete, or contains unsupported types
   */
  private static Object bdecode() throws IOException {
    if (pos >= data.length) {
      throw new IOException("Unexpected end of data");
    }
    char ch = (char) data[pos];
    if (ch == 'd') {
      return bdecodeDictionary();
    }
    if (ch == 'l') {
      return bdecodeList();
    }
    if (ch == 'i') {
      return bdecodeInteger();
    }
    if (Character.isDigit(ch)) {
      return bdecodeString();
    }

    throw new IOException("Unsupported bencode type '" + ch + "' at pos " + pos);
  }

  /**
   * Decodes a bencode integer from the current position.
   *
   * <p>
   * Bencode integers are in the format: i&lt;number&gt;e
   *
   * @return the decoded long integer value
   */
  private static long bdecodeInteger() {
    pos++; // skip 'i'
    int end = indexOf('e', pos);
    long value = Long.parseLong(new String(data, pos, end - pos, StandardCharsets.UTF_8));
    pos = end + 1;
    return value;
  }

  /**
   * Decodes a bencode string from the current position.
   *
   * <p>
   * Bencode strings are in the format: &lt;length&gt;:&lt;bytes&gt;
   *
   * @return the decoded byte array
   * @throws NumberFormatException if the length prefix is not a valid integer
   */
  private static byte[] bdecodeString() {
    int colon = 0;
    int len = 0;
    try {
      colon = indexOf(':', pos);
      len = Integer.parseInt(new String(data, pos, colon - pos, StandardCharsets.UTF_8));
      pos = colon + 1;
      byte[] bytes = Arrays.copyOfRange(data, pos, pos + len);
      pos += len;
      return bytes;
    } catch (NumberFormatException ex) {
      if (logger.isErrorEnabled()) {
        logger.error("Decoding string failure: pos={}, len={}, colon={}", pos, len, colon, ex);
      }
      throw ex;
    }
  }

  /**
   * Decodes a bencode list from the current position.
   *
   * <p>
   * Bencode lists are in the format: l&lt;items&gt;e
   *
   * @return a list containing decoded objects
   * @throws IOException if the data is malformed or incomplete
   */
  private static List<Object> bdecodeList() throws IOException {
    pos++; // skip 'l'
    List<Object> list = new ArrayList<>();
    while ((char) data[pos] != 'e') {
      list.add(bdecode());
    }
    pos++; // skip 'e'
    return list;
  }

  /**
   * Decodes a bencode dictionary from the current position.
   *
   * <p>
   * Bencode dictionaries are in the format: d&lt;key&gt;&lt;value&gt;...e Keys are strings and must
   * be in lexicographical order.
   *
   * @return a map with string keys and decoded object values
   * @throws IOException if the data is malformed or incomplete
   */
  private static Map<String, Object> bdecodeDictionary() throws IOException {
    pos++; // skip 'd'
    Map<String, Object> map = new LinkedHashMap<>();
    while ((char) data[pos] != 'e') {
      byte[] keyBytes = bdecodeString();
      String key = new String(keyBytes, StandardCharsets.UTF_8);
      Object value = bdecode();
      map.put(key, value);
    }
    pos++; // skip 'e'
    return map;
  }

  /**
   * Finds the index of the first occurrence of a target character from a given position.
   *
   * @param target the character to search for
   * @param from the starting position (inclusive)
   * @return the index of the target character
   * @throws NoSuchElementException if the target character is not found
   */
  private static int indexOf(char target, int from) {
    for (int i = from; i < data.length; i++) {
      if ((char) data[i] == target) {
        return i;
      }
    }
    throw new NoSuchElementException("'" + target + "' not found from pos " + from);
  }
}

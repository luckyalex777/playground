package com.alexswd.top.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Encodes Java objects into bencode format.
 *
 * <p>
 * Bencode is a simple encoding format used in peer-to-peer systems such as BitTorrent. This encoder
 * supports:
 * <ul>
 * <li>Strings: encoded as length-prefixed byte strings (e.g., "4:spam")
 * <li>Numbers: encoded as integers wrapped in 'i' and 'e' delimiters (e.g., "i3e")
 * <li>Lists: encoded as ordered collections wrapped in 'l' and 'e' delimiters
 * <li>Maps: encoded as dictionaries with lexicographically sorted string keys
 * </ul>
 */
public final class Encoder {
  private Encoder() {}

  /**
   * Encodes an object to bencode format.
   *
   * <p>
   * Supported types include:
   * <ul>
   * <li>String - encoded as bencoded string
   * <li>byte[] - encoded as bencoded byte string
   * <li>Number (Long, Integer, etc.) - encoded as bencoded integer
   * <li>List - encoded as bencoded list (recursively encodes elements)
   * <li>Map - encoded as bencoded dictionary (recursively encodes values)
   * </ul>
   *
   * @param obj the object to encode
   * @return the bencoded byte array
   * @throws IOException if an I/O error occurs during encoding
   * @throws IllegalArgumentException if the object type is not supported or contains null values
   */
  public static byte[] encode(Object obj) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    encodeObject(obj, baos);
    return baos.toByteArray();
  }

  /**
   * Encodes a single object to bencode format and writes it to the given output stream.
   *
   * <p>
   * This method dispatches to the appropriate encoding method based on the object type.
   *
   * @param obj the object to encode
   * @param out the output stream to write the encoded data to
   * @throws IOException if an I/O error occurs during encoding
   * @throws IllegalArgumentException if the object type is not supported or is null
   */
  private static void encodeObject(Object obj, ByteArrayOutputStream out) throws IOException {
    if (obj instanceof String) {
      encodeString((String) obj, out);
    } else if (obj instanceof byte[]) {
      encodeBytes((byte[]) obj, out);
    } else if (obj instanceof Number) {
      encodeNumber((Number) obj, out);
    } else if (obj instanceof List) {
      encodeList((List<?>) obj, out);
    } else if (obj instanceof Map) {
      encodeMap((Map<?, ?>) obj, out);
    } else if (obj == null) {
      throw new IllegalArgumentException("Cannot encode null value");
    } else {
      throw new IllegalArgumentException("Unsupported type: " + obj.getClass());
    }
  }

  /**
   * Encodes a string to bencode format.
   *
   * <p>
   * Format: &lt;length&gt;:&lt;bytes&gt; (UTF-8 encoded)
   *
   * @param str the string to encode
   * @param out the output stream to write to
   * @throws IOException if an I/O error occurs during encoding
   */
  private static void encodeString(String str, ByteArrayOutputStream out) throws IOException {
    byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
    out.write(Integer.toString(bytes.length).getBytes(StandardCharsets.US_ASCII));
    out.write(':');
    out.write(bytes);
  }

  /**
   * Encodes a byte array to bencode format.
   *
   * <p>
   * Format: &lt;length&gt;:&lt;bytes&gt;
   *
   * @param bytes the byte array to encode
   * @param out the output stream to write to
   * @throws IOException if an I/O error occurs during encoding
   */
  private static void encodeBytes(byte[] bytes, ByteArrayOutputStream out) throws IOException {
    out.write(Integer.toString(bytes.length).getBytes(StandardCharsets.US_ASCII));
    out.write(':');
    out.write(bytes);
  }

  /**
   * Encodes a number to bencode format.
   *
   * <p>
   * Format: i&lt;number&gt;e
   *
   * @param num the number to encode
   * @param out the output stream to write to
   * @throws IOException if an I/O error occurs during encoding
   */
  private static void encodeNumber(Number num, ByteArrayOutputStream out) throws IOException {
    out.write('i');
    out.write(num.toString().getBytes(StandardCharsets.US_ASCII));
    out.write('e');
  }

  /**
   * Encodes a list to bencode format.
   *
   * <p>
   * Format: l&lt;items&gt;e Each item is recursively encoded using
   * {@link #encodeObject(Object, ByteArrayOutputStream)}.
   *
   * @param list the list to encode
   * @param out the output stream to write to
   * @throws IOException if an I/O error occurs during encoding
   * @throws IllegalArgumentException if any element has an unsupported type
   */
  private static void encodeList(List<?> list, ByteArrayOutputStream out) throws IOException {
    out.write('l');
    for (Object item : list) {
      encodeObject(item, out);
    }
    out.write('e');
  }

  /**
   * Encodes a map to bencode dictionary format.
   *
   * <p>
   * Format: d&lt;key&gt;&lt;value&gt;...e Keys must be strings and are sorted lexicographically as
   * required by the bencode specification. Both keys and values are recursively encoded.
   *
   * @param map the map to encode
   * @param out the output stream to write to
   * @throws IOException if an I/O error occurs during encoding
   * @throws IllegalArgumentException if any key is not a String or byte[], or if any value has an
   *         unsupported type
   */
  private static void encodeMap(Map<?, ?> map, ByteArrayOutputStream out) throws IOException {
    // Bencode dictionaries MUST have keys in lexicographical order
    // Using TreeMap to automatically sort keys
    var sortedMap = new TreeMap<String, Object>();

    for (Map.Entry<?, ?> entry : map.entrySet()) {
      Object key = entry.getKey();
      String keyStr;

      if (key instanceof String) {
        keyStr = (String) key;
      } else if (key instanceof byte[]) {
        // Convert byte array key to string for sorting (bencode uses byte strings for keys)
        keyStr = new String((byte[]) key, StandardCharsets.ISO_8859_1);
      } else {
        throw new IllegalArgumentException(
            "Map keys must be String or byte[], got: " + key.getClass());
      }

      sortedMap.put(keyStr, entry.getValue());
    }

    out.write('d');
    for (Map.Entry<String, Object> entry : sortedMap.entrySet()) {
      // Encode key as bencoded string
      encodeString(entry.getKey(), out);
      // Encode value
      encodeObject(entry.getValue(), out);
    }
    out.write('e');
  }

  /**
   * Encodes an object to bencode format and returns it as a string.
   *
   * <p>
   * This is a convenience method for debugging purposes. The bencode output is decoded using
   * ISO-8859-1 character set to preserve byte values.
   *
   * @param obj the object to encode
   * @return the bencode output as an ISO-8859-1 encoded string
   * @throws IOException if an I/O error occurs during encoding
   * @throws IllegalArgumentException if the object type is not supported
   */
  public static String encodeToString(Object obj) throws IOException {
    byte[] encoded = encode(obj);
    return new String(encoded, StandardCharsets.ISO_8859_1);
  }
}

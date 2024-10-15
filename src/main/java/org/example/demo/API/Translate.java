package org.example.demo.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.example.demo.Models.Language;

public class Translate {

  /**
   * translate a text from srcLanguage to dstLanguage.
   *
   * @param text        text need to translate.
   * @param srcLanguage source Language
   * @param dstLanguage destination Language
   * @return translation of text
   */
  public static String translate(String text, Language srcLanguage, Language dstLanguage) {
    try {
      String link = "https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl="
          + srcLanguage.getCode()
          + "&tl="
          + dstLanguage.getCode()
          + "&dt=t&q="
          + URLEncoder.encode(text, "UTF-8");
      URL url = new URL(link);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestProperty("User-Agent",
          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(connection.getInputStream());
        if (jsonNode.isArray()) {
          for (JsonNode node : jsonNode) {
            if (node.isArray()) {
              return node.get(0).asText();
            }
            return node.asText();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return text;
  }

  public static void main(String[] args) {
    System.out.println(translate("Consider a 256kb 4-way set associative cache with 256 byte cache\n"
            + "lines for a processor that uses 64-bit data words and 48-bit byte\n"
            + "addresses. Assume the variable x, of type uint64, is stored in memory\n"
            + "at location 0x4A85_B413_A518. Assume that x is present in the cache,\n"
            + "and char* ptr = 0x4A85 B400 0000. Determine if the following\n"
            + "accesses will cause a cache miss or hit or say if it can’t be determined.\n", Language.ENGLISH, Language.VIETNAMESE));
  }
}

package org.example.demo.API;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.example.demo.Models.Language;

public class TextToSpeech {

<<<<<<< HEAD
=======
  private AdvancedPlayer player;
  private Thread playThread; // Thread to control playback

  public void speak(String text, Language lang) {
    try {
      if (playThread != null && playThread.isAlive()) {
        stop();
      }
      String encodeText = URLEncoder.encode(text, "UTF-8");

      String languageCode = lang.getCode();
      String link =
          "https://clients5.google.com/translate_tts?ie=UTF-8&client=tw-ob&tl=" + languageCode
              + "&q=" + encodeText;
      URL url = new URL(link);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("User-Agent", "Mozilla/5.0");

      // Check if the request was successful
      if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
        InputStream inputStream = new BufferedInputStream(conn.getInputStream());

        player = new AdvancedPlayer(inputStream);

        playThread = new Thread(() -> {
          try {
            player.play();  // Play the audio
          } catch (JavaLayerException e) {
            e.printStackTrace();
          } finally {
            // Close the InputStream after the playback is done
            try {
              player.close();  // Close the player
              player = null;
              inputStream.close();  // Close the input stream
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });

        playThread.start();

      } else {
        System.out.println(
            "Error: Unable to fetch TTS audio. Response code: " + conn.getResponseCode());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void stop() {
    if (player != null) {
      player.close();
      player = null;
      if (playThread != null) {
        if (playThread.isAlive()) {
          playThread.interrupt();
        }
      }
      System.out.println("Sound stopped.");
    }
  }
>>>>>>> 8fb21401abf863368ac0c8304533cc4cb9aaba72
}
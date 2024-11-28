package org.example.demo.API;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.example.demo.Models.Language;

public class TextToSpeech {

  private AdvancedPlayer player;
  private Thread playThread; // Thread to control playback

  /**
   * oke.
   */
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

  /**
   * oke.
   */
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

  private ArrayList<AdvancedPlayer> listVoices;
  private Thread threadSpeakPassage;

  /**
   * oke.
   */
  public void SpeakPassage(String passage, Language language) {
    stopSpeak();
    if (listVoices != null) {
      while (!listVoices.isEmpty()) {
        listVoices.getFirst().close();
        listVoices.removeFirst();
      }
    }
    threadSpeakPassage = new Thread(() -> {
      boolean startSpeak = false;
      listVoices = new ArrayList<>();

      String[] ps = passage.split("\n");

      ArrayList<String> texts = new ArrayList<>();
      for (String s : ps) {
        if (threadSpeakPassage.isInterrupted()) {
          if (listVoices != null) {
            while (!listVoices.isEmpty()) {
              listVoices.getFirst().close();
              listVoices.removeFirst();
            }
          }
          return;
        }
        for (String tmp : s.split(" ")) {
          if (threadSpeakPassage.isInterrupted()) {
            if (listVoices != null) {
              while (!listVoices.isEmpty()) {
                listVoices.getFirst().close();
                listVoices.removeFirst();
              }
            }
            return;
          }
          if (texts.isEmpty()) {
            texts.add(tmp);
          } else if (texts.getLast().length() + tmp.length() < 198) {
            texts.set(texts.size() - 1, texts.getLast() + " " + tmp);
          } else {
            texts.add(tmp);
          }
        }
        texts.set(texts.size() - 1, texts.getLast() + "\n");
      }

      AdvancedPlayer firstPlayer = createPlayer(texts.getFirst(), language);
      if (threadSpeakPassage.isInterrupted()) {
        if (listVoices != null) {
          while (!listVoices.isEmpty()) {
            listVoices.getFirst().close();
            listVoices.removeFirst();
          }
        }
        return;
      }
      while (!listVoices.isEmpty()) {
        listVoices.getFirst().close();
        listVoices.removeFirst();
      }
      listVoices.add(firstPlayer);
      Thread miniThread = new Thread(() -> {
        for (int i = 1; i < texts.size(); i++) {
          AdvancedPlayer newPlayer = createPlayer(texts.get(i), language);
          if (threadSpeakPassage.isInterrupted()) {
            if (listVoices != null) {
              while (!listVoices.isEmpty()) {
                listVoices.getFirst().close();
                listVoices.removeFirst();
              }
            }
            return;
          }
          listVoices.add(newPlayer);
        }
      });
      miniThread.setDaemon(true);
      miniThread.start();

      while (!listVoices.isEmpty()) {
        if (threadSpeakPassage.isInterrupted()) {
          if (listVoices != null) {
            while (!listVoices.isEmpty()) {
              listVoices.getFirst().close();
              listVoices.removeFirst();
            }
          }
          return;
        }
        try {
          listVoices.getFirst().play();
          listVoices.getFirst().close();
          listVoices.removeFirst();
        } catch (Exception e) {
          System.out.println(e.toString());
        }
      }
      stopSpeak();
    });
    threadSpeakPassage.start();
  }

  /**
   * oke.
   */
  public AdvancedPlayer createPlayer(String text, Language language) {
    try {
      String encodeText = URLEncoder.encode(text, "UTF-8");

      String languageCode = language.getCode();
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
        return new AdvancedPlayer(inputStream);
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return null;
  }

  /**
   * oke.
   */
  public void stopSpeak() {
    if (listVoices != null) {
      while (!listVoices.isEmpty()) {
        listVoices.getFirst().close();
        listVoices.removeFirst();
      }
    }
    if (threadSpeakPassage != null) {
      if (threadSpeakPassage.isAlive()) {
        threadSpeakPassage.interrupt();
      }
    }
  }

}
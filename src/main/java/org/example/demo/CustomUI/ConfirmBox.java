package org.example.demo.CustomUI;

import com.jfoenix.controls.JFXButton;
import java.util.HashMap;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.example.demo.API.Translate;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.Language;

public class ConfirmBox extends HBox implements MainInfo {

  private Label topicLabel;
  private Label contentLabel;
  private JFXButton yesButton;
  private JFXButton noButton;
  private HashMap<Object, String> enLang;
  private HashMap<Object, String> viLang;

  /**
   * constructor with topic, content, action when click yes or no.
   */
  public ConfirmBox(String topic, String content, Runnable yes, Runnable no) {

    initTopicLabel(topic);
    initContentLabel(content);
    initButton(yes, no);

    HBox buttonBox = new HBox(yesButton, noButton);
    buttonBox.setAlignment(Pos.CENTER_RIGHT);

    VBox box = new VBox(topicLabel, contentLabel, buttonBox);
    box.setMaxHeight(Region.USE_PREF_SIZE);
    box.setId("box");

    this.setPrefHeight(540);
    this.setPrefWidth(900);
    this.getChildren().add(box);
    this.setAlignment(Pos.CENTER);
    this.setId("FadedPane");
    this.getStylesheets()
        .add(getClass().getResource("/org/example/demo/CSS/ConfirmBox.css").toExternalForm());

    enLang = new HashMap<>();
    viLang = new HashMap<>();
    setUpLanguage(viLang, enLang);

  }

  /**
   * init topic.
   */
  private void initTopicLabel(String topic) {
    topicLabel = new Label(topic);
    topicLabel.setId("topic");
    topicLabel.setWrapText(true);
    topicLabel.setPrefWidth(400);
  }

  /**
   * init content.
   */
  private void initContentLabel(String content) {
    contentLabel = new Label(content);
    contentLabel.setId("content");
    contentLabel.setWrapText(true);
    contentLabel.setPrefWidth(400);
  }

  /**
   * init buttons.
   */
  private void initButton(Runnable yes, Runnable no) {
    yesButton = new JFXButton("Đồng ý");
    yesButton.setId("Button");
    yesButton.setOnAction(e -> {
      yes.run();
    });
    yesButton.setMinWidth(50);
    yesButton.setWrapText(true);

    noButton = new JFXButton("Hủy");
    noButton.setId("Button");
    noButton.setOnAction(e -> {
      no.run();
    });
    noButton.setMinWidth(50);
    noButton.setWrapText(true);
  }

  /**
   * set dark/light mode.
   */
  @Override
  public void applyDarkMode(boolean isDark) {

  }

  /**
   * translate en/vi language for some text.
   */
  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {
    if (isTranslate) {
      if (this.enLang.containsKey(topicLabel)) {
        topicLabel.setText(this.enLang.get(topicLabel));
      } else {
        PauseTransition transition = new PauseTransition(Duration.millis(500));
        transition.setOnFinished(e -> {
          if (this.enLang.containsKey(topicLabel)) {
            topicLabel.setText(this.enLang.get(topicLabel));
          }
        });
        transition.play();
      }

      if (this.enLang.containsKey(contentLabel)) {
        contentLabel.setText(this.enLang.get(contentLabel));
      } else {
        PauseTransition transition = new PauseTransition(Duration.millis(500));
        transition.setOnFinished(e -> {
          if (this.enLang.containsKey(contentLabel)) {
            contentLabel.setText(this.enLang.get(contentLabel));
          }
        });
        transition.play();
      }
      yesButton.setText("Agree");
      noButton.setText("Cancel");
    } else {
      topicLabel.setText(this.viLang.get(topicLabel));
      contentLabel.setText(this.viLang.get(contentLabel));
      noButton.setText("Hủy");
      yesButton.setText("Đồng ý");
    }
  }

  /**
   * set up en/vi language.
   */
  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {
    Thread thread = new Thread(() -> {
      viLang.put(topicLabel, topicLabel.getText());
      viLang.put(contentLabel, contentLabel.getText());

      enLang.put(topicLabel,
          Translate.translate(topicLabel.getText(), Language.VIETNAMESE, Language.ENGLISH));
      enLang.put(contentLabel,
          Translate.translate(contentLabel.getText(), Language.VIETNAMESE, Language.ENGLISH));
    });
    thread.start();

  }
}

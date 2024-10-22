package org.example.demo.CustomUI;

import com.jfoenix.controls.JFXButton;
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

public class ConfirmBox extends HBox {
  public ConfirmBox(String topic,String content,Runnable yes,Runnable no){

    Label topicLabel=new Label(topic);
    topicLabel.setId("topic");
    topicLabel.setWrapText(true);
    topicLabel.setPrefWidth(400);

    Label contentLabel=new Label(content);
    contentLabel.setId("content");
    contentLabel.setWrapText(true);
    contentLabel.setPrefWidth(400);

    JFXButton yesButton=new JFXButton("Yes");
    yesButton.setId("Button");
    yesButton.setOnAction(e->{yes.run();});
    yesButton.setPrefWidth(50);

    JFXButton noButton=new JFXButton("No");
    noButton.setId("Button");
    noButton.setOnAction(e->{no.run();});
    noButton.setPrefWidth(50);

    HBox buttonBox=new HBox(yesButton,noButton);
    buttonBox.setAlignment(Pos.CENTER_RIGHT);

    VBox box=new VBox(topicLabel,contentLabel,buttonBox);
    box.setMaxHeight(Region.USE_PREF_SIZE);
    box.setId("box");

    this.setPrefHeight(540);
    this.setPrefWidth(900);
    this.getChildren().add(box);
    this.setAlignment(Pos.CENTER);
    this.setId("FadedPane");
    this.getStylesheets().add(getClass().getResource("/org/example/demo/CSS/ConfirmBox.css").toExternalForm());

  }

}

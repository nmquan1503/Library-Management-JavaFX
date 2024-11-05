package org.example.demo.CustomUI;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class Warning extends VBox {

  private Transition transition;

  public Warning(String topic, String content) {
    Label topicLabel=new Label(topic);
    topicLabel.setWrapText(true);
    topicLabel.setFont(Font.font("System",FontWeight.BOLD,24));

    Label contentLabel=new Label(content);
    contentLabel.setWrapText(true);
    contentLabel.setFont(Font.font(18));

    JFXButton button=new JFXButton("OK");
    button.setFont(Font.font(16));
    button.setStyle("-fx-border-radius:8px;"
        + "-fx-background-radius:8px;"
        + "-fx-background-color:#FA8072;");
    button.setPrefWidth(50);
    button.setOnAction(e->{removeWarning();});
    HBox buttonBox=new HBox(button);
    buttonBox.setAlignment(Pos.CENTER_RIGHT);

    this.getChildren().addAll(topicLabel,contentLabel,buttonBox);
    this.setPrefWidth(350);
    this.setStyle("-fx-background-color:#FFFFFF;"
        + "-fx-background-radius:8px;"
        + "-fx-border-radius:8px;"
        + "-fx-border-width: 2px;"
        + "-fx-border-color:#E464C0;"
        + "-fx-padding:10px;");
    this.setLayoutY(-100);
    this.setLayoutX(365);
    playTransition();
  }

  private void playTransition(){
    TranslateTransition translateTransition=new TranslateTransition(Duration.millis(200),this);
    translateTransition.setToY(120);
    PauseTransition pauseTransition=new PauseTransition(Duration.millis(4000));
    transition=new SequentialTransition(translateTransition,pauseTransition);
    transition.setOnFinished(e->{
      removeWarning();
    });
    transition.play();
  }

  private void removeWarning(){
    if(transition!=null){
      transition.stop();
      transition=null;
    }
    TranslateTransition translateTransition=new TranslateTransition(Duration.millis(200),this);
    translateTransition.setToY(-100);
    translateTransition.setOnFinished(e->{
      Node node=this.getParent();
      if(node!=null){
        if(node instanceof AnchorPane){
          ((AnchorPane) node).getChildren().remove(this);
        }
      }
    });
    translateTransition.play();
  }

}

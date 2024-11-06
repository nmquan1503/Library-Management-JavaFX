package org.example.demo.Controllers;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class WaitingController {
  @FXML private ImageView center;
  @FXML private ImageView circle;

  private Transition transition;

  @FXML
  private void initialize(){
    transition=new ParallelTransition(
        fadeTran(center),
        fadeTran(circle),
        rotateAndScaleTran(circle,-2000)
    );
    PauseTransition pauseTransition=new PauseTransition(Duration.millis(3000));
    pauseTransition.setOnFinished(e->{
      transition.play();
    });
    pauseTransition.play();
  }

  public void stopTransition(Runnable runnable){
    if(transition!=null){
      transition.stop();
      transition=null;
    }
    transition = new ParallelTransition(
        fadeTran(center),
        fadeTran(circle),
        rotateAndScaleTran(circle,2000)
    );
    transition.setOnFinished(e->{
      transition.stop();
      transition=null;
      center=null;
      circle=null;
      runnable.run();
    });
    transition.play();
  }

  private Transition fadeTran(Node node){
    FadeTransition fadeTransition=new FadeTransition(Duration.millis(1500),node);
    fadeTransition.setFromValue(node.getOpacity());
    fadeTransition.setToValue(1-node.getOpacity());
    return fadeTransition;
  }

  private Transition rotateAndScaleTran(Node node,int time){
    ScaleTransition scaleTransition=new ScaleTransition(Duration.millis(1500),node);
    scaleTransition.setAutoReverse(true);
    scaleTransition.setCycleCount(time/1500);
    scaleTransition.setFromX(1.0);
    scaleTransition.setToX(1.2);
    scaleTransition.setFromY(1.0);
    scaleTransition.setToY(1.2);

    RotateTransition rotateTransition=new RotateTransition(Duration.millis(2000),node);
    rotateTransition.setCycleCount(time/2000);
    rotateTransition.setByAngle(360);
    rotateTransition.setAutoReverse(false);

    return new ParallelTransition(scaleTransition,rotateTransition);
  }

}

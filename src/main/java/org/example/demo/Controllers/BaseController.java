package org.example.demo.Controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


public class BaseController {

  @FXML
  private Circle avatar;

  @FXML
  public void initialize() {

    if (avatar != null) {
      try {
        Image myImage = new Image(
            getClass().getResourceAsStream("/org/example/demo/Image/default_avatar.jpg"));
        avatar.setFill(new ImagePattern(myImage));
        System.out.println("Avatar image loaded successfully.");
      } catch (Exception e) {
        System.out.println("Image loading failed: " + e.getMessage());
      }
    }
  }

}

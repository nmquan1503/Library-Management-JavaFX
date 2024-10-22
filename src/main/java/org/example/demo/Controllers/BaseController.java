package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.effect.BlendMode;

public class BaseController {

  boolean isDark = false;

  @FXML
  private AnchorPane mainPane;

  @FXML
  private Circle avatar;

  @FXML
  private AnchorPane bigPane;

  @FXML
  private JFXButton checkMode;

  @FXML
  public void initialize() {
    if (!isDark) {
      checkMode.setText("☀");
    } else {
      checkMode.setText("🌙");
    }
    if (avatar != null) {
      try {
        Image myImage = new Image(
            getClass().getResourceAsStream("/org/example/demo/Assets/default_avatar.jpg"));
        avatar.setFill(new ImagePattern(myImage));
        System.out.println("Avatar image loaded successfully.");
      } catch (Exception e) {
        System.out.println("Image loading failed: " + e.getMessage());
      }
    }
  }

  @FXML
  public void darkMode() {
    if (!isDark) {
      bigPane.setBlendMode(BlendMode.DIFFERENCE);

      applyImageViewBlendMode(bigPane, BlendMode.DIFFERENCE);

      avatar.setBlendMode(BlendMode.DIFFERENCE);
      checkMode.setText("🌙");
    } else {
      bigPane.setBlendMode(BlendMode.SRC_OVER);

      applyImageViewBlendMode(bigPane, BlendMode.SRC_OVER);

      avatar.setBlendMode(BlendMode.SRC_OVER);
      checkMode.setText("☀");
    }
    isDark = !isDark;
  }

  private void applyImageViewBlendMode(Parent parent, BlendMode blendMode) {
    for (Node node : parent.getChildrenUnmodifiable()) {
      if (node instanceof ImageView) {
        node.setBlendMode(blendMode);
      }
      if (node instanceof Parent) {
        applyImageViewBlendMode((Parent) node, blendMode);
      }
    }
  }


  @FXML
  public void moveDashboard() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/Home.fxml"));
      AnchorPane anchorPane = fxmlLoader.load();
      bigPane.getChildren().remove(mainPane);
      bigPane.getChildren().add(anchorPane);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void moveBooks() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/Books.fxml"));
      AnchorPane anchorPane = fxmlLoader.load();
      bigPane.getChildren().remove(mainPane);
      bigPane.getChildren().add(anchorPane);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void moveUser() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/Users.fxml"));
      AnchorPane anchorPane = fxmlLoader.load();
      bigPane.getChildren().remove(mainPane);
      bigPane.getChildren().add(anchorPane);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void moveEdit() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/Edit.fxml"));
      AnchorPane anchorPane = fxmlLoader.load();
      bigPane.getChildren().remove(mainPane);
      bigPane.getChildren().add(anchorPane);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}

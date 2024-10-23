package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.effect.BlendMode;

public class BaseController {

  public static boolean isDark = false;

  @FXML
  private AnchorPane mainPane;

  @FXML
  private Circle avatar;

  @FXML
  private AnchorPane bigPane;

  @FXML
  private JFXButton checkMode;

  private DarkModeController currentController;

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
      avatar.setBlendMode(BlendMode.DIFFERENCE);
      checkMode.setText("🌙");
    } else {
      bigPane.setBlendMode(BlendMode.SRC_OVER);
      avatar.setBlendMode(BlendMode.SRC_OVER);
      checkMode.setText("☀");
    }

    if (currentController != null) {
      currentController.applyDarkMode(!isDark);
    }

    isDark = !isDark;
  }

  private void switchPane(String fxmlPath) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
      AnchorPane anchorPane = fxmlLoader.load();

      currentController = fxmlLoader.getController();

      bigPane.getChildren().remove(mainPane);
      bigPane.getChildren().add(anchorPane);
      mainPane = anchorPane;

      if (isDark && currentController != null) {
        currentController.applyDarkMode(isDark);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void moveDashboard() {
    switchPane("/org/example/demo/FXML/Home.fxml");
  }

  @FXML
  public void moveBooks() {
    switchPane("/org/example/demo/FXML/Books.fxml");
  }

  @FXML
  public void moveUser() {
    switchPane("/org/example/demo/FXML/Users.fxml");
  }

  @FXML
  public void moveEdit() {
    switchPane("/org/example/demo/FXML/Edit.fxml");
  }

}

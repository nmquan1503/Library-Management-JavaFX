package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

public class App extends Application {

  public static Stage primaryStage;

  public static Scene startScene;

  public static Scene baseScene;

  @Override
  public void start(Stage stage) throws Exception {
    primaryStage = stage;
    if (startScene == null) {
      Parent root1 = FXMLLoader.load(
          Objects.requireNonNull(getClass().getResource("/org/example/demo/FXML/Start.fxml")));
      startScene = new Scene(root1);
    }

    if (baseScene == null) {
      Parent root2 = FXMLLoader.load(
          Objects.requireNonNull(getClass().getResource("/org/example/demo/FXML/Base.fxml")));
      baseScene = new Scene(root2);
    }

    primaryStage.setTitle("Library Management");
    primaryStage.setResizable(false);
    primaryStage.setScene(startScene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

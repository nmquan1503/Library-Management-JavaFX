package org.example.demo;

import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;
import javafx.util.Duration;
import org.example.demo.API.Network;
import org.example.demo.Controllers.WaitingController;
import org.example.demo.Models.Library;

public class App extends Application {

  public static Stage primaryStage;

  public static Scene startScene;

  public static Scene baseScene;

  public static Scene accountEditScene;

  @Override
  public void start(Stage stage) throws Exception {
    Class.forName("org.example.demo.API.Network");
    Class.forName("org.example.demo.Models.Library");

    primaryStage = stage;

    AtomicReference<FXMLLoader> waitLoader = new AtomicReference<>(
        new FXMLLoader(getClass().getResource("/org/example/demo/FXML/Waiting.fxml")));
    AtomicReference<Scene> waitScene = new AtomicReference<>(new Scene(waitLoader.get().load()));

    primaryStage.setTitle("Library Management");
    primaryStage.setResizable(false);
    primaryStage.setScene(waitScene.get());
    primaryStage.show();

    Thread thread = new Thread(() -> {
      try {
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

        if (accountEditScene == null) {
          Parent root3 = FXMLLoader.load(
              Objects.requireNonNull(
                  getClass().getResource("/org/example/demo/FXML/AccountSetting.fxml")));
          accountEditScene = new Scene(root3);
        }
        WaitingController controller = waitLoader.get().getController();
        Platform.runLater(() -> {
          if (controller != null) {
            controller.stopTransition(() -> {
              primaryStage.setScene(startScene);
              waitScene.set(null);
              waitLoader.set(null);
            });
          }
        });

      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(7));
    pauseTransition.setOnFinished(e -> {
      thread.start();
    });
    pauseTransition.play();

    primaryStage.setOnCloseRequest(e -> {
      Network.close();
      Platform.exit();
      System.exit(0);
    });
  }

  public static void main(String[] args) {
    launch(args);
  }
}

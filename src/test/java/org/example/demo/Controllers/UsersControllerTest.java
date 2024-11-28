package org.example.demo.Controllers;

import javafx.animation.Animation.Status;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class UsersControllerTest extends ApplicationTest {

  private UsersController usersController;

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/FXML/Users.fxml"));
    AnchorPane root = loader.load();

    usersController = loader.getController();

    stage.setScene(new Scene(root));
    stage.show();
  }

  @BeforeEach
  void setUp() {
    assertNotNull(usersController, "Controller should be initialized before tests");
  }

  @Test
  void testControllerInitialization() {
    assertNotNull(usersController, "UsersController should be initialized");
  }

  @Test
  void testTranslate1() {
    Platform.runLater(() -> {
      usersController.applyTranslate(null, null, true);
      assertEquals(usersController.getNameTextField().getPromptText(), "Enter name");
    });
  }

  @Test
  void testTranslate2() {
    Platform.runLater(() -> {
      usersController.applyTranslate(null, null, false);
      assertEquals(usersController.getBanListLabel().getText(), "Danh sách người bị cấm");
    });
  }

  @Test
  void testSetPageUser1() {
    Platform.runLater(() -> {
      int maxPageNumber = (usersController.getListUser().size() - 1) / 20 + 1;
      usersController.setListUsers(maxPageNumber + 1);
      assertEquals(Integer.parseInt(usersController.getPageNumberTextField().getText()),
          maxPageNumber);
    });
  }

  @Test
  void testSetPageUser2() {
    Platform.runLater(() -> {
      usersController.setListUsers(-1);
      assertEquals(Integer.parseInt(usersController.getPageNumberTextField().getText()), 1);
    });
  }

  @Test
  void testVisibleNextPageButton() {
    int maxPageNumber = (usersController.getListUser().size() - 1) / 20 + 1;
    usersController.setListUsers(maxPageNumber);
    assertFalse(usersController.getNextPageButton().isVisible());
  }

  @Test
  void testVisiblePrevPageButton() {
    usersController.setListUsers(1);
    assertFalse(usersController.getPrevPageButton().isVisible());
  }

  @Test
  void testLoadingTransition1() {
    Platform.runLater(() -> {
      PauseTransition transition = new PauseTransition(Duration.millis(1000));
      transition.setOnFinished(e -> {
        usersController.loadUserList();
        assertTrue(usersController.getLoadingPane().isVisible());
      });
      transition.play();
    });
  }

  @Test
  void testLoadingTransition2() {
    Platform.runLater(() -> {
      PauseTransition transition = new PauseTransition(Duration.millis(1000));
      transition.setOnFinished(e -> {
        usersController.loadUserList();
        assertEquals(usersController.getLoadingTransition().getStatus(), Status.RUNNING);
      });
      transition.play();
    });
  }


}
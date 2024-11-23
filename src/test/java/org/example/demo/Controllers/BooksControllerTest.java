package org.example.demo.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class BooksControllerTest extends ApplicationTest {

  private BooksController booksController;

  @Override
  public void start(Stage stage) throws Exception {
    // Load the FXML file
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/FXML/Books.fxml"));
    AnchorPane root = loader.load();

    // Get the controller
    booksController = loader.getController();
    assertNotNull(booksController, "Controller should not be null");

    // Set up the scene and show the stage
    stage.setScene(new Scene(root));
    stage.show();
  }

  @BeforeEach
  void setUp() {
    // Any setup needed before tests
    assertNotNull(booksController, "Controller should be initialized before tests");
  }

  @Test
  void testControllerInitialization() {
    // Check if the controller is initialized
    assertNotNull(booksController, "BooksController should be initialized");
  }

  @Test
  void testDarkMode(){
    booksController.applyDarkMode(true);
    assertEquals(booksController.getAdvertisementPane().getBlendMode(), BlendMode.DIFFERENCE);
  }

}

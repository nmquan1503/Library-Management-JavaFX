package org.example.demo.Controllers;

import javafx.animation.Animation.Status;
import javafx.application.Platform;
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
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/FXML/Books.fxml"));
    AnchorPane root = loader.load();

    booksController = loader.getController();
    assertNotNull(booksController, "Controller should not be null");

    stage.setScene(new Scene(root));
    stage.show();
  }

  @BeforeEach
  void setUp() {
    assertNotNull(booksController, "Controller should be initialized before tests");
  }

  @Test
  void testControllerInitialization() {
    assertNotNull(booksController, "BooksController should be initialized");
  }

  @Test
  void testDarkMode() {
    booksController.applyDarkMode(true);
    assertEquals(booksController.getAdvertisementPane().getBlendMode(), BlendMode.DIFFERENCE);
  }

  @Test
  void testTranslate1() {
    Platform.runLater(() -> {
      booksController.applyTranslate(null, null, true);
      assertEquals(booksController.getCategoryTextField().getPromptText(), "Category");
    });
  }

  @Test
  void testTranslate2() {
    Platform.runLater(() -> {
      booksController.applyTranslate(null, null, false);
    });
    assertEquals(booksController.getTitleTextField().getPromptText(), "Tiêu đề");
  }

  @Test
  void testSetPageBook1() {
    Platform.runLater(() -> {
      int maxPageNumber = (booksController.getListSuggestions().size() - 1) / 20 + 1;
      booksController.setListBooks(maxPageNumber + 1);
      assertEquals(Integer.parseInt(booksController.getPageNumberTextField().getText()),
          maxPageNumber);
    });
  }

  @Test
  void testSetPageBook2() {
    Platform.runLater(() -> {
      booksController.setListBooks(-1);
      assertEquals(Integer.parseInt(booksController.getPageNumberTextField().getText()), 1);
    });
  }

  @Test
  void testVisibleNextPageButton() {
    int maxPageNumber = (booksController.getListSuggestions().size() - 1) / 20 + 1;
    booksController.setListBooks(maxPageNumber);
    assertFalse(booksController.getNextPageButton().isVisible());
  }

  @Test
  void testVisiblePrevPageButton() {
    booksController.setListBooks(1);
    assertFalse(booksController.getPrevPageButton().isVisible());
  }

  @Test
  void testLoadingTransition1() {
    booksController.Search();
    assertTrue(booksController.getLoadingPane().isVisible());
  }

  @Test
  void testLoadingTransition2() {
    booksController.Search();
    assertEquals(booksController.getLoadingTransition().getStatus(), Status.RUNNING);
  }


}

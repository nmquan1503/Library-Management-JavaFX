package org.example.demo.Controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.demo.CustomUI.EditBookView;
import org.example.demo.CustomUI.EditUserView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class EditControllerTest extends ApplicationTest {

  private EditController editController;

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/FXML/Edit.fxml"));
    AnchorPane root = loader.load();

    editController = loader.getController();

    stage.setScene(new Scene(root));
    stage.show();
  }

  @BeforeEach
  void setUp() {
    assertNotNull(editController, "Controller should be initialized before tests");
  }

  @Test
  void testControllerInitialization() {
    assertNotNull(editController, "UsersController should be initialized");
  }

  @Test
  void testTranslate1() {
    Platform.runLater(() -> {
      editController.applyTranslate(null, null, true);
      assertEquals(editController.getBookSuggestionsTextField().getPromptText(),
          "Add books from Google Books");
      assertEquals(editController.getAddBookButton().getText(), "Add new book");
    });
  }

  @Test
  void testTranslate2() {
    Platform.runLater(() -> {
      editController.applyTranslate(null, null, false);
      assertEquals(editController.getBookSuggestionsTextField().getPromptText(),
          "Thêm sách từ Google Books");
      assertEquals(editController.getAddUserButton().getText(), "Thêm user");
    });
  }

  @Test
  void testSetPageBook1() {
    Platform.runLater(() -> {
      int maxPageNumber = (editController.getListBooks().size() - 1) / 20 + 1;
      editController.setPageBook(maxPageNumber + 1);
      assertEquals(Integer.parseInt(editController.getPageBookNumberTextField().getText()),
          maxPageNumber);
    });
  }

  @Test
  void testSetPageBook2() {
    Platform.runLater(() -> {
      editController.setPageBook(-1);
      assertEquals(Integer.parseInt(editController.getPageBookNumberTextField().getText()), 1);
    });
  }

  @Test
  void testSetPageUser1() {
    Platform.runLater(() -> {
      int maxPageNumber = (editController.getListUsers().size() - 1) / 20 + 1;
      editController.setPageUser(maxPageNumber + 1);
      assertEquals(Integer.parseInt(editController.getPageUserNumberTextField().getText()),
          maxPageNumber);
    });
  }

  @Test
  void testSetPageUser2() {
    Platform.runLater(() -> {
      editController.setPageUser(-1);
      assertEquals(Integer.parseInt(editController.getPageUserNumberTextField().getText()), 1);
    });
  }

  @Test
  void testVisibleNextPageBookButton() {
    int maxPageNumber = (editController.getListBooks().size() - 1) / 20 + 1;
    editController.setPageBook(maxPageNumber);
    assertFalse(editController.getNextPageBookButton().isVisible());
  }

  @Test
  void testVisiblePrevPageBookButton() {
    editController.setPageBook(1);
    assertFalse(editController.getPrevPageBookButton().isVisible());
  }

  @Test
  void testVisibleNextPageUserButton() {
    int maxPageNumber = (editController.getListUsers().size() - 1) / 20 + 1;
    editController.setPageUser(maxPageNumber);
    assertFalse(editController.getNextPageUserButton().isVisible());
  }

  @Test
  void testVisiblePrevPageUserButton() {
    editController.setPageUser(1);
    assertFalse(editController.getPrevPageUserButton().isVisible());
  }

  @Test
  void testAddBook() {
    Platform.runLater(() -> {
      editController.AddBook();
      PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
      pauseTransition.setOnFinished(e -> {
        assertInstanceOf(EditBookView.class, editController.getMainPane().getChildren().getLast());
      });
      pauseTransition.play();
    });
  }

  @Test
  void testAddUser() {
    Platform.runLater(() -> {
      editController.openAddUserView();
      PauseTransition pauseTransition = new PauseTransition(Duration.millis(500));
      pauseTransition.setOnFinished(e -> {
        assertInstanceOf(EditUserView.class, editController.getMainPane().getChildren().getLast());
      });
      pauseTransition.play();
    });
  }


}

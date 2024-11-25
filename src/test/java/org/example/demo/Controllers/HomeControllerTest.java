package org.example.demo.Controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

class HomeControllerTest extends ApplicationTest {

  private HomeController homeController;

  @Override
  public void start(Stage stage) throws Exception {
    // Load the FXML file
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/FXML/Home.fxml"));
    AnchorPane root = loader.load();

    // Get the controller
    homeController = loader.getController();
    assertNotNull(homeController, "Controller should not be null");

    // Set up the scene and show the stage
    stage.setScene(new Scene(root));
    stage.show();
  }

  @BeforeEach
  void setUp() {
    // Any setup needed before tests
    assertNotNull(homeController, "Controller should be initialized before tests");
  }

  @Test
  void testApplyTranslate() {
    // Mock language data
    HashMap<Object, String> viLang = new HashMap<>();
    HashMap<Object, String> enLang = new HashMap<>();

    // Populate mock data
    viLang.put(homeController.getHelloTxt(), "Xin chào");
    viLang.put(homeController.getTableLibTxt(), "Bảng thư viện");
    enLang.put(homeController.getHelloTxt(), "Hello");
    enLang.put(homeController.getTableLibTxt(), "Library Table");

    // Initialize UI elements with default text
    Platform.runLater(() -> {
      homeController.getHelloTxt().setText("Default Text");
      homeController.getNumBookTxt().setText("Default Books");
      homeController.getNumStuTxt().setText("Default Students");
      homeController.getOverDueTxt().setText("Default Overdue");
      homeController.getBanStuTxt().setText("Default Banned");
      homeController.getTableLibTxt().setText("Default Table");
      homeController.getBorrowRateTxt().setText("Default Rate");
    });

    // Call applyTranslate with isTranslate = true (English translation)
    Platform.runLater(() -> homeController.applyTranslate(viLang, enLang, true));

    // Verify the translations
    Platform.runLater(() -> {
      assertEquals("Hello", homeController.getHelloTxt().getText(),
          "helloTxt should be translated to English");
      assertEquals("Books", homeController.getNumBookTxt().getText(),
          "numBookTxt should display 'Books'");
      assertEquals("Students", homeController.getNumStuTxt().getText(),
          "numStuTxt should display 'Students'");
      assertEquals("Overdue books", homeController.getOverDueTxt().getText(),
          "overDueTxt should display 'Overdue books'");
      assertEquals("Banned students", homeController.getBanStuTxt().getText(),
          "banStuTxt should display 'Banned students'");
      assertEquals("Library Table", homeController.getTableLibTxt().getText(),
          "tableLibTxt should be translated to English");
      assertEquals("Borrowing Rate", homeController.getBorrowRateTxt().getText(),
          "borrowRateTxt should display 'Borrowing Rate'");
    });

    // Call applyTranslate with isTranslate = false (Vietnamese translation)
    Platform.runLater(() -> homeController.applyTranslate(viLang, enLang, false));

    // Verify the translations
    Platform.runLater(() -> {
      assertEquals("Xin chào", homeController.getHelloTxt().getText(),
          "helloTxt should be translated to Vietnamese");
      assertEquals("Số sách", homeController.getNumBookTxt().getText(),
          "numBookTxt should display 'Số sách'");
      assertEquals("Số sinh viên", homeController.getNumStuTxt().getText(),
          "numStuTxt should display 'Số sinh viên'");
      assertEquals("Số sách quá hạn", homeController.getOverDueTxt().getText(),
          "overDueTxt should display 'Số sách quá hạn'");
      assertEquals("Số sinh viên bị cấm", homeController.getBanStuTxt().getText(),
          "banStuTxt should display 'Số sinh viên bị cấm'");
      assertEquals("Bảng thư viện", homeController.getTableLibTxt().getText(),
          "tableLibTxt should be translated to Vietnamese");
      assertEquals("Tỉ lệ sách đã mượn", homeController.getBorrowRateTxt().getText(),
          "borrowRateTxt should display 'Tỉ lệ sách đã mượn'");
    });
  }


}
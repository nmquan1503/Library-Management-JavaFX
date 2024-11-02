package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.effect.BlendMode;
import javafx.stage.Stage;
import org.example.demo.API.Translate;
import org.example.demo.App;
import org.example.demo.Models.Language;

public class BaseController {

  HashMap<Object, String> viLang = new HashMap<>();
  HashMap<Object, String> enLang = new HashMap<>();

  public static boolean isDark = false;

  // isTranslate = false ứng với lang = "vi", ngược lại = "en"
  public static boolean isTranslate = false;

  @FXML
  private Circle avatar;

  @FXML
  private AnchorPane bigPane;

  @FXML
  private JFXButton checkMode;

  @FXML
  private ContextMenu avatarMenu;

  @FXML
  private TextField searchBase;

  @FXML
  private Tooltip notiText;

  @FXML
  private Tooltip darkText;

  @FXML
  private Tooltip avtText;

  @FXML
  private static AnchorPane mainPane;

  @FXML
  private static AnchorPane bookPane;

  @FXML
  private static AnchorPane editPane;

  @FXML
  private static AnchorPane userPane;

  @FXML
  private static AnchorPane borrowPane;

  @FXML
  private static AnchorPane returnPane;

  private HomeController homeController;

  private BooksController booksController;

  private EditController editController;

  private UsersController usersController;

  private BorrowBookController borrowBookController;

  private ReturnBookController returnBookController;

  public static AnchorPane getMainPane() {
    return mainPane;
  }

  public static AnchorPane getBookPane() {
    return bookPane;
  }

  public static AnchorPane getEditPane() {
    return editPane;
  }

  public static AnchorPane getUserPane() {
    return userPane;
  }

  public static AnchorPane getBorrowPane() {
    return borrowPane;
  }

  public static AnchorPane getReturnPane() {
    return returnPane;
  }

  @FXML
  public void initialize() {
    Thread loadMainThread = new Thread(new LoadMainTask());
    Thread loadBookThread = new Thread(new LoadBookTask());
    Thread loadEditThread = new Thread(new LoadEditTask());
    Thread loadUserThread = new Thread(new LoadUserTask());
    Thread loadBorrowThread = new Thread(new LoadBorrowBookTask());
    Thread loadReturnThread = new Thread(new LoadReturnBookTask());

    loadMainThread.start();
    loadBookThread.start();
    loadEditThread.start();
    loadUserThread.start();
    loadBorrowThread.start();
    loadReturnThread.start();

    try {
      loadMainThread.join();
      loadBookThread.join();
      loadEditThread.join();
      loadUserThread.join();
      loadBorrowThread.join();
      loadReturnThread.join();
    } catch (InterruptedException e) {
      System.out.println("Thread interrupted: " + e.getMessage());
    }

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
      } catch (Exception e) {
        System.out.println("Image loading failed: " + e.getMessage());
      }
    }
    avtMenuSetup();
    setUpLang();
    mainPane.setVisible(true);
    bookPane.setVisible(false);
    editPane.setVisible(false);
    userPane.setVisible(false);
    borrowPane.setVisible(false);
    returnPane.setVisible(false);
  }

  private class LoadMainTask extends Task<Void> {

    @Override
    protected Void call() {
      loadMain();
      return null;
    }

    @Override
    protected void succeeded() {
      Platform.runLater(() -> bigPane.getChildren().add(mainPane));
    }
  }

  private class LoadBookTask extends Task<Void> {

    @Override
    protected Void call() {
      loadBook();
      return null;
    }

    @Override
    protected void succeeded() {
      Platform.runLater(() -> bigPane.getChildren().add(bookPane));
    }
  }

  private class LoadEditTask extends Task<Void> {

    @Override
    protected Void call() {
      loadEdit();
      return null;
    }

    @Override
    protected void succeeded() {
      Platform.runLater(() -> bigPane.getChildren().add(editPane));
    }
  }

  private class LoadUserTask extends Task<Void> {

    @Override
    protected Void call() {
      loadUser();
      return null;
    }

    @Override
    protected void succeeded() {
      Platform.runLater(() -> bigPane.getChildren().add(userPane));
    }
  }

  private class LoadBorrowBookTask extends Task<Void> {

    @Override
    protected Void call() {
      loadBorrow();
      return null;
    }

    @Override
    protected void succeeded() {
      Platform.runLater(() -> bigPane.getChildren().add(borrowPane));
    }
  }

  private class LoadReturnBookTask extends Task<Void> {

    @Override
    protected Void call() {
      loadReturn();
      return null;
    }

    @Override
    protected void succeeded() {
      Platform.runLater(() -> bigPane.getChildren().add(returnPane));
    }
  }

  private void loadMain() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/Home.fxml"));
      mainPane = fxmlLoader.load();

      homeController = fxmlLoader.getController();
      homeController.setUpLanguage(viLang, enLang);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadBook() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/Books.fxml"));
      bookPane = fxmlLoader.load();

      booksController = fxmlLoader.getController();
      booksController.setUpLanguage(viLang, enLang);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadEdit() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/Edit.fxml"));
      editPane = fxmlLoader.load();

      editController = fxmlLoader.getController();
      editController.setUpLanguage(viLang, enLang);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadUser() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/Users.fxml"));
      userPane = fxmlLoader.load();

      usersController = fxmlLoader.getController();
      usersController.setUpLanguage(viLang, enLang);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadBorrow() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/BorrowBook.fxml"));
      borrowPane = fxmlLoader.load();

      borrowBookController = fxmlLoader.getController();
      borrowBookController.setUpLanguage(viLang, enLang);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadReturn() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/ReturnBook.fxml"));
      returnPane = fxmlLoader.load();

      returnBookController = fxmlLoader.getController();
      returnBookController.setUpLanguage(viLang, enLang);
    } catch (Exception e) {
      e.printStackTrace();
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

    if (homeController != null) {
      homeController.applyDarkMode(!isDark);
    }

    if (booksController != null) {
      booksController.applyDarkMode(!isDark);
    }

    if (usersController != null) {
      usersController.applyDarkMode(!isDark);
    }

    if (editController != null) {
      editController.applyDarkMode(!isDark);
    }

    if (returnBookController != null) {
      returnBookController.applyDarkMode(!isDark);
    }

    if (borrowBookController != null) {
      borrowBookController.applyDarkMode(!isDark);
    }

    isDark = !isDark;
  }

  private void avtMenuSetup() {
    avatarMenu.getItems().clear();

    MenuItem changeInfo = new MenuItem(" Cài đặt tài khoản");
    if (isTranslate) {
      changeInfo.setText(" Setting");
    }
    changeInfo.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.WRENCH));

    MenuItem translate = new MenuItem(" Dịch");
    if (isTranslate) {
      changeInfo.setText(" Translate");
    }
    translate.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.LANGUAGE));

    MenuItem logOut = new MenuItem(" Đăng xuất");
    if (isTranslate) {
      changeInfo.setText(" Log out");
    }
    logOut.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SIGN_OUT));

    changeInfo.setOnAction(e -> handleChangeAccountInfo());
    translate.setOnAction(e -> handleTranslate());
    logOut.setOnAction(e -> handleLogout());

    avatarMenu.getItems().addAll(changeInfo, translate, logOut);
  }

  private void handleChangeAccountInfo() {

  }

  private void handleLogout() {
    App.primaryStage.setScene(App.startScene);
    App.primaryStage.show();
  }

  private void setUpLang() {

    for (MenuItem item : avatarMenu.getItems()) {
      viLang.put(item, item.getText());
      if (item.getText().equals(" Dịch")) {
        enLang.put(item, " Translate");
      } else {
        enLang.put(item,
            Translate.translate(item.getText(), Language.VIETNAMESE, Language.ENGLISH));
      }
    }

    viLang.put(searchBase, searchBase.getPromptText());
    viLang.put(darkText, darkText.getText());
    viLang.put(notiText, notiText.getText());
    viLang.put(avtText, avtText.getText());

    enLang.put(searchBase,
        Translate.translate(searchBase.getPromptText(), Language.VIETNAMESE, Language.ENGLISH));
    enLang.put(darkText,
        "Light-Dark Mode");
    enLang.put(notiText,
        Translate.translate(notiText.getText(), Language.VIETNAMESE, Language.ENGLISH));
    enLang.put(avtText,
        Translate.translate(avtText.getText(), Language.VIETNAMESE, Language.ENGLISH));

  }

  private void handleTranslate() {
    if (isTranslate) {
      searchBase.setPromptText(viLang.get(searchBase));
      darkText.setText(viLang.get(darkText));
      notiText.setText(viLang.get(notiText));
      avtText.setText(viLang.get(avtText));

      for (MenuItem item : avatarMenu.getItems()) {
        item.setText(viLang.get(item));
      }

    } else {
      searchBase.setPromptText(enLang.get(searchBase));
      darkText.setText(enLang.get(darkText));
      notiText.setText(enLang.get(notiText));
      avtText.setText(enLang.get(avtText));

      for (MenuItem item : avatarMenu.getItems()) {
        item.setText(enLang.get(item));
      }
    }

    if (homeController != null) {
      homeController.applyTranslate(viLang, enLang, !isTranslate);
    }
    if (booksController != null) {
      booksController.applyTranslate(viLang, enLang, !isTranslate);
    }
    if (editController != null) {
      editController.applyTranslate(viLang, enLang, !isTranslate);
    }
    if (usersController != null) {
      usersController.applyTranslate(viLang, enLang, !isTranslate);
    }
    if (borrowBookController != null) {
      borrowBookController.applyTranslate(viLang, enLang, !isTranslate);
    }
    if (returnBookController != null) {
      returnBookController.applyTranslate(viLang, enLang, !isTranslate);
    }
    isTranslate = !isTranslate;
  }

  @FXML
  public void avatarClicked() {
    for (MenuItem item : avatarMenu.getItems()) {
      if (item.getGraphic() instanceof FontAwesomeIconView icon) {
        if (isDark) {
          icon.setFill(javafx.scene.paint.Color.WHITE);
        } else {
          icon.setFill(javafx.scene.paint.Color.BLACK);
        }
      }
    }
    if (isDark) {
      avatarMenu.getStyleClass().remove("dropdown-menu");
      avatarMenu.getStyleClass().add("dropdown-menu-dark");
      avatarMenu.setStyle("-fx-background-color: #000000; " +
          "-fx-border-color: #165B33; " +
          "-fx-border-radius: 8px; " +
          "-fx-padding: 5px;");
    } else {
      avatarMenu.getStyleClass().remove("dropdown-menu-dark");
      avatarMenu.getStyleClass().add("dropdown-menu");
      avatarMenu.setStyle("-fx-background-color: #f0f0f0; " +
          "-fx-border-color: #E464C0; " +
          "-fx-border-radius: 8px; " +
          "-fx-padding: 5px;");
    }
    Bounds avatarBounds = avatar.localToScreen(avatar.getBoundsInLocal());

    double menuX = avatarBounds.getMinX();
    double menuY = avatarBounds.getMaxY();

    avatarMenu.show(avatar, menuX - 143, menuY + 5);
  }

  @FXML
  public void moveDashboard() {
    mainPane.setVisible(true);
    homeController.refresh();
    bookPane.setVisible(false);
    editPane.setVisible(false);
    userPane.setVisible(false);
    borrowPane.setVisible(false);
    returnPane.setVisible(false);
  }

  @FXML
  public void moveBooks() {
    homeController.clearTimeline();
    mainPane.setVisible(false);
    bookPane.setVisible(true);
    editPane.setVisible(false);
    userPane.setVisible(false);
    borrowPane.setVisible(false);
    returnPane.setVisible(false);
  }

  @FXML
  public void moveUser() {
    homeController.clearTimeline();
    mainPane.setVisible(false);
    bookPane.setVisible(false);
    editPane.setVisible(false);
    userPane.setVisible(true);
    borrowPane.setVisible(false);
    returnPane.setVisible(false);
  }

  @FXML
  public void moveEdit() {
    homeController.clearTimeline();
    mainPane.setVisible(false);
    bookPane.setVisible(false);
    editPane.setVisible(true);
    userPane.setVisible(false);
    borrowPane.setVisible(false);
    returnPane.setVisible(false);
  }

  @FXML
  public void moveBorrowBook() {
    homeController.clearTimeline();
    mainPane.setVisible(false);
    bookPane.setVisible(false);
    editPane.setVisible(false);
    userPane.setVisible(false);
    borrowPane.setVisible(true);
    returnPane.setVisible(false);
  }

  @FXML
  public void moveReturnBook() {
    homeController.clearTimeline();
    mainPane.setVisible(false);
    bookPane.setVisible(false);
    editPane.setVisible(false);
    userPane.setVisible(false);
    borrowPane.setVisible(false);
    returnPane.setVisible(true);
  }

}

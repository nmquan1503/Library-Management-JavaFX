package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.effect.BlendMode;
import javafx.stage.Popup;
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

  @FXML
  private Button searchBtn;

  @FXML
  private Button deleteSearchBtn;

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
  private JFXListView<String> suggestionListView;

  private List<String> allSuggestions = List.of("Dashboard", "User", "Sách", "Borrowing", "Trả",
      "Tùy chỉnh", "DarkMode", "LightMode", "Log out", "Chỉnh sửa thông tin", "Thông báo", "Dịch",
      "Màn hình chính", "Học sinh", "Book", "Mượn", "Return", "Chỉnh sửa", "Chế độ tối",
      "Chế độ sáng", "Đăng xuất", "Account Setting", "Notification", "Translate", "Home",
      "Người mượn", "Copy", "Lending", "Hoàn trả sách", "Edit", "Tối", "Sáng", "Logout", "Setting",
      "Announcement", "Convert Language");

  @FXML
  public void initialize() {
    setupAutocomplete();
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

  public void styleListViewScrollBars(JFXListView<?> listView) {
    ScrollBar verticalScrollBar = (ScrollBar) listView.lookup(".scroll-bar:vertical");
    if (verticalScrollBar != null) {
      verticalScrollBar.setStyle("-fx-background-color: transparent;" +
          "-fx-opacity: 0");
    }
  }

  public void setupListViewWithStyledScrollBars(JFXListView<?> listView) {
    Platform.runLater(() -> styleListViewScrollBars(listView));
  }

  private void setupAutocomplete() {
    Popup suggestionPopup = new Popup();
    suggestionListView = new JFXListView<>();
    suggestionListView.setStyle("-fx-background-color: transparent;" +
        "-fx-border-color: Transparent");
    suggestionListView.setCellFactory(lv -> new ListCell<>() {
      private boolean isHovered = false;

      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
          setStyle("");
        } else {
          setText(item);
          updateStyle();
        }

        // focus effect
        selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
          updateStyle();
        });

        // Hover effect
        setOnMouseEntered(event -> {
          isHovered = true;
          updateStyle();
        });

        setOnMouseExited(event -> {
          isHovered = false;
          updateStyle();
        });
      }

      private void updateStyle() {
        if (isEmpty()) {
          return;
        }

        String style;
        if (isSelected()) {
          if (!isDark) {
            style = "-fx-text-fill: #A21D33; -fx-background-color: #FFC1E3;";
          } else {
            style = "-fx-text-fill: #ffdd4a; -fx-background-color: #2c5f2d;";
          }
        } else if (isHovered) {
          if (!isDark) {
            style = "-fx-text-fill: BLACK; -fx-background-color: LIGHTGRAY;";
          } else {
            style = "-fx-text-fill: WHITE; -fx-background-color: #1C1C1C;";
          }
        } else {
          if (!isDark) {
            style = "-fx-text-fill: BLACK; -fx-background-color: #f2f2f2;";
          } else {
            style = "-fx-text-fill: WHITE; -fx-background-color: BLACK;";
          }
        }

        style += "-fx-cursor: hand; -fx-padding: 5px;";
        setStyle(style);
      }
    });
    suggestionPopup.getContent().add(suggestionListView);
    suggestionListView.setPrefWidth(300);
    suggestionListView.setMaxHeight(100);
    suggestionPopup.setAutoHide(true);

    searchBase.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
      String input = searchBase.getText().trim();
      if (input.isEmpty()) {
        suggestionPopup.hide();
      } else {
        List<String> filteredSuggestions = allSuggestions.stream()
            .filter(item -> !item.trim().isEmpty())
            .filter(item -> item.toLowerCase().startsWith(input.toLowerCase()))
            .collect(Collectors.toList());

        if (!filteredSuggestions.isEmpty()) {
          suggestionListView.setItems(FXCollections.observableArrayList(filteredSuggestions));
          setupListViewWithStyledScrollBars(suggestionListView);
          if (!suggestionPopup.isShowing()) {
            suggestionPopup.show(searchBase,
                searchBase.localToScreen(0, searchBase.getHeight()).getX(),
                searchBase.localToScreen(0, searchBase.getHeight()).getY());
          }
        } else {
          suggestionPopup.hide();
        }
      }
    });

    suggestionListView.setOnMouseClicked(event -> {
      if (!suggestionListView.getSelectionModel().isEmpty()) {
        searchBase.setText(suggestionListView.getSelectionModel().getSelectedItem());
        performActionStr(suggestionListView.getSelectionModel().getSelectedItem());
        suggestionPopup.hide();
      }
    });

    suggestionListView.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
        suggestionListView.requestFocus();
      } else if (event.getCode() == KeyCode.ENTER) {
        searchBase.setText(suggestionListView.getSelectionModel().getSelectedItem());
        suggestionPopup.hide();
        String enterTxt = searchBase.getText().trim();
        int matchIndex = -1;
        for (int i = 0; i < allSuggestions.size(); i++) {
          if (allSuggestions.get(i).equalsIgnoreCase(enterTxt)) {
            matchIndex = i;
            break;
          }
        }

        if (matchIndex != -1) {
          performAction(matchIndex);
        }
      }
    });

    searchBase.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        String enterTxt = searchBase.getText().trim();
        int matchIndex = -1;
        for (int i = 0; i < allSuggestions.size(); i++) {
          if (allSuggestions.get(i).equalsIgnoreCase(enterTxt)) {
            matchIndex = i;
            break;
          }
        }

        if (matchIndex != -1) {
          performAction(matchIndex);
        }
      }
    });

    searchBtn.setOnAction(event -> {
      performActionStr(searchBase.getText().trim());
    });

    deleteSearchBtn.setOnAction(event -> {
      searchBase.setText("");
    });

  }

  private int getSuggestionIdx(String text) {
    for (int i = 0; i < allSuggestions.size(); i++) {
      if (text.equalsIgnoreCase(allSuggestions.get(i))) {
        return i;
      }
    }
    return -1;
  }

  private void performActionStr(String txt) {
    int idx = getSuggestionIdx(txt);
    if (idx != -1) {
      performAction(idx);
    }
  }

  private void performAction(int idx) {
    idx %= 12;
    if (idx == 0) {
      moveDashboard();
    } else if (idx == 1) {
      moveUser();
    } else if (idx == 2) {
      moveBooks();
    } else if (idx == 3) {
      moveBorrowBook();
    } else if (idx == 4) {
      moveReturnBook();
    } else if (idx == 5) {
      moveEdit();
    } else if (idx == 6) {
      if (!isDark) {
        darkMode();
      }
    } else if (idx == 7) {
      if (isDark) {
        darkMode();
      }
    } else if (idx == 8) {
      handleLogout();
    } else if (idx == 9) {
      handleChangeAccountInfo();
    } else if (idx == 10) {
      System.out.println("Setup notification later");
    } else if (idx == 11) {
      handleTranslate();
    }
    searchBase.setText("");
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

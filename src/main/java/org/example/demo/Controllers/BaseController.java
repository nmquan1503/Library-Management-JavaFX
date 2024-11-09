package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.effect.BlendMode;
import javafx.stage.Popup;
import org.example.demo.API.Network;
import org.example.demo.API.Translate;
import org.example.demo.App;
import org.example.demo.CustomUI.NotificationView;
import org.example.demo.CustomUI.Warning;
import org.example.demo.Database.JDBC;
import org.example.demo.Models.Borrowing.Borrowing;
import org.example.demo.Models.Language;
import org.example.demo.Models.Library;
import org.example.demo.Models.Users.Date;

public class BaseController {

  private static final IntegerProperty checkState = new SimpleIntegerProperty(1);

  public static int getBaseState() {
    return checkState.get();
  }

  public static void setBaseState(int val) {
    checkState.set(val);
  }

  private static final IntegerProperty libId = new SimpleIntegerProperty(-1);

  public static int getLibId() {
    return libId.get();
  }

  public static void setLibId(int val) {
    libId.set(val);
  }

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

  @FXML
  private JFXButton bell;

  private HomeController homeController;

  private BooksController booksController;

  private EditController editController;

  private UsersController usersController;

  private BorrowBookController borrowBookController;

  private ReturnBookController returnBookController;

  private Popup popup;

  private final ArrayList<NotificationView> notificationList = new ArrayList<>();

  private static final IntegerProperty isBorrowingChanged = new SimpleIntegerProperty(0);

  public static void setIsBorrowingChanged(int val) {
    isBorrowingChanged.set(val);
  }

  public static int getIsBorrowingChanged() {
    return isBorrowingChanged.get();
  }

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

  private boolean isTranSetUp = false;

  @FXML
  private JFXListView<String> suggestionListView;

  private final List<String> allSuggestions = List.of("Dashboard", "User", "Sách", "Borrowing",
      "Trả",
      "Tùy chỉnh", "DarkMode", "LightMode", "Log out", "Chỉnh sửa thông tin", "Thông báo", "Dịch",
      "Màn hình chính", "Học sinh", "Book", "Mượn", "Return", "Chỉnh sửa", "Chế độ tối",
      "Chế độ sáng", "Đăng xuất", "Account Setting", "Notification", "Translate", "Home",
      "Người mượn", "Copy", "Lending", "Hoàn trả sách", "Edit", "Tối", "Sáng", "Logout", "Setting",
      "Announcement", "Convert Language");

  @FXML
  public void initialize() {
    libId.addListener((observable, oldValue, newValue) -> {
      loadLibrarianInfo();
    });
    checkState.addListener((observable, oldValue, newValue) -> {
      loadLibrarianInfo();
    });
    isBorrowingChanged.addListener((observable, oldValue, newValue) -> {
      refreshNotification();
    });
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

    avtMenuSetup();
    initNotificationList();
    mainPane.setVisible(true);
    bookPane.setVisible(false);
    editPane.setVisible(false);
    userPane.setVisible(false);
    borrowPane.setVisible(false);
    returnPane.setVisible(false);
    editController.setBooksController(booksController);
    editController.setUsersController(usersController);
  }

  private void loadLibrarianInfo() {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    try {
      conn = JDBC.getConnection();

      String sql = "SELECT name_librarian, avatar FROM librarian WHERE id_librarian ="
          + BaseController.getLibId();
      assert conn != null;
      preparedStatement = conn.prepareStatement(sql);

      rs = preparedStatement.executeQuery();

      if (rs.next()) {
        // set librarian name
        String name = rs.getString("name_librarian");
        homeController.setLibName(name);

        // set avatar for account
        Blob avatarBlob = rs.getBlob("avatar");
        if (avatarBlob != null) {
          try (InputStream avatarStream = avatarBlob.getBinaryStream()) {
            Image avatarImage = new Image(avatarStream);
            avatar.setFill(new ImagePattern(avatarImage));
          } catch (Exception e) {
            System.out.println("Failed to load avatar image: " + e.getMessage());
            loadDefaultAvatar();
          }
        } else {
          loadDefaultAvatar();
        }
      }

      JDBC.closeConnection(conn);
    } catch (Exception se) {
      se.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }

  private void loadDefaultAvatar() {
    try {
      Image defaultImage = new Image(
          Objects.requireNonNull(
              getClass().getResourceAsStream("/org/example/demo/Assets/default_avatar.jpg"))
      );
      avatar.setFill(new ImagePattern(defaultImage));
    } catch (Exception e) {
      System.out.println("Default image loading failed: " + e.getMessage());
    }
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
    suggestionListView.setPrefWidth(280);
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
    } catch (Exception e) {
      System.out.println("loadMain error");
    }
  }

  private void loadBook() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/Books.fxml"));
      bookPane = fxmlLoader.load();

      booksController = fxmlLoader.getController();
    } catch (Exception e) {
      System.out.println("loadBook error");
    }
  }

  private void loadEdit() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/Edit.fxml"));
      editPane = fxmlLoader.load();

      editController = fxmlLoader.getController();
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
    setBaseState(0);
    AccountSettingController.setAccState(1);
    App.primaryStage.setScene(App.accountEditScene);
    App.primaryStage.show();
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

    homeController.setUpLanguage(viLang, enLang);
    booksController.setUpLanguage(viLang, enLang);
    borrowBookController.setUpLanguage(viLang, enLang);
    editController.setUpLanguage(viLang, enLang);
    usersController.setUpLanguage(viLang, enLang);
    returnBookController.setUpLanguage(viLang, enLang);

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
    if (!Network.isConnected()) {
      bigPane.getChildren().add(new Warning("Lỗi mạng", "Vui lòng kiểm tra kết nối"));
      return;
    }
    if (!isTranSetUp) {
      setUpLang();
      isTranSetUp = true;
    }
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

  private void initNotificationList() {
    ArrayList<Borrowing> history = Library.getInstance().getListBorrowingNearingDeadline();
    for (Borrowing borrowing : history) {
      notificationList.add(new NotificationView(borrowing, 330, 70));
    }
  }

  private void refreshNotification() {
    ArrayList<Borrowing> history = Library.getInstance().getListBorrowingNearingDeadline();
    ArrayList<Integer> ls = new ArrayList<>();
    ArrayList<NotificationView> del = new ArrayList<>();
    for (NotificationView notificationView : notificationList) {
      ls.add(notificationView.getBorrowing().getIdBorrowing());
    }
    int i = 0;
    for (Borrowing borrowing : history) {
      if (!ls.contains(borrowing.getIdBorrowing())) {
        notificationList.add(new NotificationView(borrowing, 330, 70));
      } else {
        if (notificationList.get(i).isSeen()) {
          del.add(notificationList.get(i));
        }
      }
      i++;
    }
    if (notificationList.size() > 20 && !del.isEmpty()) {
      for (NotificationView notificationView : del) {
        notificationList.remove(notificationView);
      }
    }
  }

  @FXML
  private void notificationClick() {

    if (popup == null) {
      popup = new Popup();
      popup.setAutoHide(true);
    }

    if (popup.isShowing()) {
      popup.hide();
      popup.getContent().clear();
      return;
    }

    popup.getContent().clear();

    JFXListView<NotificationView> listView = new JFXListView<>();
    setupListViewWithStyledScrollBars(listView);
    listView.setStyle("-fx-background-color: transparent;" +
        "-fx-border-color: Transparent");
    listView.getItems().addAll(notificationList);
    listView.setPrefWidth(370);
    listView.setMaxWidth(370);
    listView.setPrefHeight(270);
    listView.setCellFactory(param -> new ListCell<>() {
      private boolean isHovered = false;

      @Override
      protected void updateItem(NotificationView item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setGraphic(null);
        } else {
          if (item.isSeen()) {
            item.getImage().setBlendMode(BlendMode.SRC_OVER);
            item.markSeen();
          }
          setGraphic(item);
          updateStyle();
        }

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

        setOnMouseClicked(event -> {
          assert item != null;
          item.getImage().setBlendMode(BlendMode.SRC_OVER);
          item.markSeen();
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Notification Details");

          if (item != null) {
            if (BaseController.isTranslate) {
              alert.setHeaderText("Details for Notification");
            } else {
              alert.setHeaderText("Chi tiết thông báo");
            }
            String nameUser = Library.getInstance().getUser(item.getBorrowing().getIdUser())
                .getName();
            String nameBook = Library.getInstance().getBook(item.getBorrowing().getIdBook())
                .getTitle();
            Date borrowDate = item.getBorrowing().getBorrowedDate();
            Date dueDate = item.getBorrowing().getDueDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedBorrowDate = borrowDate.toLocalDate().format(formatter);
            String formattedDueDate = dueDate.toLocalDate().format(formatter);
            if (!isTranslate) {
              if (dueDate.toLocalDate().isBefore(LocalDate.now())) {
                alert.setContentText(
                    nameUser + " mượn cuốn " + nameBook + " vào ngày " + formattedBorrowDate
                        + " và đáng ra phải trả vào ngày " + formattedDueDate);
              } else {
                alert.setContentText(
                    nameUser + " mượn cuốn " + nameBook + " vào ngày " + formattedBorrowDate
                        + " và sẽ phải trả vào ngày " + formattedDueDate);
              }
            } else {
              if (dueDate.toLocalDate().isBefore(LocalDate.now())) {
                alert.setContentText(
                    nameUser + " borrowed the book " + nameBook + " on " + formattedBorrowDate
                        + " and was supposed to return it on " + formattedDueDate);
              } else {
                alert.setContentText(
                    nameUser + " borrowed the book " + nameBook + " on " + formattedBorrowDate
                        + " and is expected to return it on " + formattedDueDate);
              }
            }

            alert.showAndWait();
          }
        });
      }

      private void updateStyle() {
        if (isEmpty()) {
          return;
        }

        String style = "";
        if (isSelected()) {
          if (!isDark) {
            style += "-fx-background-color: #FFC1E3;";
          } else {
            style += "-fx-background-color: #2c5f2d;";
          }
        } else if (isHovered) {
          if (!isDark) {
            style += "-fx-background-color: #ffcce9;";
          } else {
            style += "-fx-background-color: #1C1C1C;";
          }
        } else {
          if (!isDark) {
            style += "-fx-background-color: #ffe6f4;";
          } else {
            style += "-fx-background-color: BLACK;";
          }
        }

        if (!isDark) {
          style += "-fx-border-color: #e05269;";
        } else {
          style += "-fx-border-color: #50af52;";
        }

        style += "-fx-border-insets: 1.8px; -fx-background-insets: 1.8px; -fx-border-width: 0.7px; -fx-cursor: hand; -fx-padding: 5px; -fx-border-radius: 10px; -fx-background-radius: 10px;";
        setStyle(style);
      }

    });

    popup = new Popup();
    popup.getContent().add(listView);
    popup.setAutoHide(true);

    Bounds bellBounds = bell.localToScreen(bell.getBoundsInLocal());
    double popupX = bellBounds.getMinX() - 280;
    double popupY = bellBounds.getMaxY() - 10;

    popup.show(bell, popupX, popupY);
  }

}

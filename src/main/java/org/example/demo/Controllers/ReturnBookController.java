package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.example.demo.CustomUI.SuggestionView;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.BookShelf;
import org.example.demo.Models.Borrowing.Borrowing;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Users.User;
import org.example.demo.Models.Users.UserList;

public class ReturnBookController implements MainInfo {

  private ObservableList<returnTableData> dataList = FXCollections.observableArrayList();
  private UserList userList = new UserList();
  private BookShelf bookList = new BookShelf();
  @FXML
  private TableColumn<returnTableData, String> userColumn;

  @FXML
  private TableColumn<returnTableData, String> bookColumn;

  @FXML
  private TableColumn<returnTableData, String> borrowedDateColumn;

  @FXML
  private TableColumn<returnTableData, String> dueDateColumn;

  @FXML
  private Button left;

  @FXML
  private Button right;

  @FXML
  private TableView tableView;

  @FXML
  private Label pageNumber;
  private int pageNow = 1;

  @FXML
  private ComboBox sortBox;
  @FXML
  private JFXListView<SuggestionView> suggestionUser;
  private ObservableList<SuggestionView> suggestions;

  @FXML
  private TextField userIdBox;

  @FXML
  private TextField userSearchBox;

  @FXML
  private Button searchButton;

  @FXML
  private Pane Pane1;

  @FXML
  private Label wrongNotification;
  @FXML
  private VBox VBox1;

  @FXML
  private Pane returnPane;

  @FXML
  private AnchorPane mainPane;

  @FXML
  private JFXButton button1;

  @FXML
  private JFXButton button2;

  @FXML
  private JFXButton button3;

  @FXML
  private JFXButton button4;

  @FXML
  private JFXButton button5;

  @FXML
  private Label nameUserLabel;

  @FXML
  private Label idUserLabel;

  @FXML
  private Label nameBookLabel;

  @FXML
  private Label idBookLabel;

  @FXML
  private ImageView userAvatar;

  @FXML
  private ImageView bookAvatar;

  @FXML
  private Label borrowedDate;

  @FXML
  private DatePicker datePicker;

  private void addBox() {
    sortBox.getItems().addAll(
            "Tìm Kiếm Theo Người Mượn",
            "Tìm Kiếm Theo Sách"
    );
    sortBox.setValue("Tìm Kiếm Theo Người Mượn");
  }

  @FXML
  private void rightController() {
    pageNow++;
    left.setDisable(false);
    pageNumber.setText(String.valueOf(pageNow));
    int x = dataList.size();
    if (x <= 5 * pageNow) {
      right.setDisable(true);
      right.setStyle(""); // Khôi phục màu mặc định nếu không đủ điều kiện
    } else {
      x = 5 * pageNow;
    }
    tableView.setItems(
            FXCollections.observableArrayList(dataList.subList(5 * (pageNow - 1), x)));
  }

  @FXML
  private void leftController() {
    pageNow--;
    pageNumber.setText(String.valueOf(pageNow));
    right.setDisable(false);
    int x = 0;
    if (pageNow == 1) {
      left.setDisable(true);
      left.setStyle(""); // Khôi phục màu mặc định nếu không đủ điều kiện
    }
    x = 5 * pageNow;
    tableView.setItems(
            FXCollections.observableArrayList(dataList.subList(5 * (pageNow - 1), x)));
  }

  public void updateHistory(int userID) {

    pageNow = 1;
    if ( userID != -2 ) {
      pageNumber.setText(String.valueOf(pageNow));
    }
    left.setDisable(true);
    dataList.clear();
    ArrayList<Borrowing> allBorrowing;

    if ( userID == -1) allBorrowing = Library.getInstance().getListBorrowingFromUserName("");
    else if ( userID == -2 ) {
      String prefixName = userSearchBox.getText();
      allBorrowing = Library.getInstance().getListBorrowingFromUserName(prefixName);

    }
    else allBorrowing = Library.getInstance().getListBorrowingFromUser(userID);
    for (Borrowing x : allBorrowing) {
      String action = "";
      if (x.getReturnedDate() == null) {
        action = "Mượn";
      } else {
        action = "Trả";
      }
      String user = userList.getUser(x.getIdUser()).getName();
      String nameBook = bookList.getBook(x.getIdBook()).getTitle();
      LocalDate now;
      now = x.getBorrowedDate().toLocalDate();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
      String Today = now.format(formatter);
      LocalDate now1 = x.getDueDate().toLocalDate();
      String Today1 = now1.format(formatter);
      returnTableData y = new returnTableData(user, nameBook, Today, Today1);
      y.setIdUser(userList.getUser(x.getIdUser()).getId());
      y.setIdBook(bookList.getBook(x.getIdBook()).getId());
      y.setDue(now1);
      dataList.add(y);
    }

    for (returnTableData x : dataList) {
      String s = x.getBook();
      if (s.length() >= 20) {
        String t = "";
        boolean check = false;
        for (int i = 0; i < s.length(); i++) {
          t = t + s.charAt(i);
          if (i >= 20 && s.charAt(i) == ' ' && check == false) {
            check = true;
            t = t + "\n";
          }
        }
        x.setBook(t);
      }
    }
    if (dataList.size() > pageNow * 5) {
      right.setDisable(false);
    }
    int x = Math.min(dataList.size(), pageNow * 5);
    tableView.setItems(
            FXCollections.observableArrayList(dataList.subList(5 * (pageNow - 1), x)));
    if (dataList.size() > 5) {
      right.setDisable(false);
    } else {
      right.setDisable(true);
    }
  }

  private void createErrorText(String content) {
    if (wrongNotification.isVisible()) {
      return;
    }
    wrongNotification.setVisible(true);
    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2),
            wrongNotification);
    wrongNotification.setText(content);
    scaleTransition.setFromX(0);  // Bắt đầu từ kích thước 0 (nhỏ tí)
    scaleTransition.setFromY(0);
    scaleTransition.setToX(1);    // Kết thúc ở kích thước gốc
    scaleTransition.setToY(1);
    PauseTransition pause1 = new PauseTransition(Duration.seconds(1.5));
    ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2),
            wrongNotification);
    scaleDown.setFromX(1);  // Bắt đầu từ kích thước gốc
    scaleDown.setFromY(1);
    scaleDown.setToX(0);    // Thu nhỏ lại về kích thước 0
    scaleDown.setToY(0);

    // Tạo SequentialTransition để nối hai animation lại với nhau
    SequentialTransition sequentialTransition = new SequentialTransition(scaleTransition,
            pause1, scaleDown);
    sequentialTransition.setOnFinished(event -> wrongNotification.setVisible(false));
    sequentialTransition.play();
  }

  @FXML
  private void searchButtonController() {
    if (userIdBox.getText().isEmpty() ) return;
    int id = Integer.parseInt(userIdBox.getText());
    User user = userList.getUser(id);
    if( user == null ) {
      userSearchBox.setText("");
      createErrorText("User ID không tồn tại");
      return;
    }
    userSearchBox.setText(user.getName());
    updateHistory(user.getId());
  }

  private void searchButtonController1() {
    updateHistory(-2);
  }

  private void createPaneTransition() {
    if (returnPane.isVisible()) {
      return;
    }
    ColorAdjust darkenEffect = new ColorAdjust();
    darkenEffect.setBrightness(-0.4);
    mainPane.setEffect(darkenEffect);
    mainPane.getChildren().forEach(node -> {
      node.setDisable(true);
    });
    returnPane.setVisible(true);
    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5),
            returnPane);
    scaleTransition.setFromX(0);
    scaleTransition.setFromY(0);
    scaleTransition.setToX(1);
    scaleTransition.setToY(1);

    // Tạo SequentialTransition để nối hai animation lại với nhau
    SequentialTransition sequentialTransition = new SequentialTransition(scaleTransition);
    sequentialTransition.setOnFinished(event -> returnPane.setDisable(false));
    sequentialTransition.play();
  }

  @FXML
  private void returnAction(ActionEvent actionEvent) {
    createPaneTransition();
    JFXButton button = (JFXButton) actionEvent.getSource();
    returnTableData x;
    if ( button == button1 ){
      x = (returnTableData) tableView.getItems().get(0);
    }
    else if ( button == button2 ){
      x = (returnTableData) tableView.getItems().get(1);
    }
    else if ( button == button3 ) {
      x = (returnTableData) tableView.getItems().get(2);
    }
    else if ( button == button4 ) {
      x = (returnTableData) tableView.getItems().get(3);
    }
    else {
      x = (returnTableData) tableView.getItems().get(4);
    }
    nameUserLabel.setText(x.getUser().replace("\n"," "));
    idUserLabel.setText(""+x.getIdUser());
    nameBookLabel.setText(x.getBook().replace("\n"," "));
    idBookLabel.setText(""+x.getIdBook());
    Image image = userList.getUser(x.getIdUser()).getAvatar();
    if ( image != null ) userAvatar.setImage(image);
    else userAvatar.setImage(new Image(getClass().getResource("/org/example/demo/Assets/default_avt_user.jpg").toExternalForm()));
    String s = bookList.getBook(x.getIdBook()).getImageLink();
    if ( s == null || s.isEmpty() ) bookAvatar.setImage(new Image(getClass().getResource("/org/example/demo/Assets/basic.jpg").toExternalForm()));
    else bookAvatar.setImage(new Image(getClass().getResource(s).toExternalForm()));
    borrowedDate.setText(x.getBorrowedDate());
    datePicker.setValue(x.getDue());
  }

  @FXML
  private void closeAction() {
    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5),
            returnPane);
    scaleTransition.setFromX(1);  // Bắt đầu từ kích thước 0 (nhỏ tí)
    scaleTransition.setFromY(1);
    scaleTransition.setToX(0);    // Kết thúc ở kích thước gốc
    scaleTransition.setToY(0);
    SequentialTransition sequentialTransition = new SequentialTransition(scaleTransition);
    sequentialTransition.setOnFinished(event -> {
      mainPane.getChildren().forEach(node -> {
        node.setDisable(false);
      });
      if ( pageNow == 1 ) {
        left.setDisable(true);
      }
      if ( pageNow*5>=dataList.size()) {
        right.setDisable(true);
      }
      mainPane.setEffect(null);
      returnPane.setVisible(false);
      returnPane.setDisable(true);
    });
    sequentialTransition.play();
  }

  private Timer timer;
  @FXML
  private void initialize() {
    addBox();
    datePicker.setConverter(new StringConverter<LocalDate>() {
      String pattern = "dd-MM-yyyy";
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

      @Override
      public String toString(LocalDate date) {
        if (date != null) {
          return dateFormatter.format(date);
        } else {
          return "";
        }
      }

      @Override
      public LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
          return LocalDate.parse(string, dateFormatter);
        } else {
          return null;
        }
      }
    });


    returnPane.setVisible(false);
    returnPane.setDisable(true);
    left.setDisable(true);
    suggestionUser.setMinHeight(0);
    suggestionUser.setMaxHeight(0);

    suggestions = FXCollections.observableArrayList();
    suggestionUser.setItems(suggestions);

    userSearchBox.focusedProperty().addListener(new ChangeListener<Boolean>() {

      @Override
      public void changed(ObservableValue<? extends Boolean> observableValue,
              Boolean oldValue, Boolean newValue) {
        if (!newValue) {
          suggestionUser.setVisible(true);
          suggestionUser.getItems().clear();
          suggestionUser.setMinHeight(0);
          suggestionUser.setMaxHeight(0);
          VBox1.setMaxHeight(35);
          VBox1.setMinHeight(35);
          if (Pane1.getStyleClass().contains("newShape")) {
            Pane1.getStyleClass().remove("newShape");
          }

        } else {
          suggestionUser.setVisible(true);
          CreateUserSuggestions();
          if (!Pane1.getStyleClass().contains("newShape") && suggestionUser.getHeight() > 0) {
            Pane1.getStyleClass().add("newShape");
          }
        }
      }
    });
    userSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
      if (userSearchBox.isFocused()) {
        CreateUserSuggestions();
        if (timer != null) {
          timer.cancel();
        }

        timer = new Timer();

        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            Task<Void> searchTask = new Task<Void>() {
              @Override
              protected Void call() throws Exception {

                searchButtonController1();
                return null;
              }

              @Override
              protected void succeeded() {
                super.succeeded();
                Platform.runLater(() -> {
                  pageNumber.setText(String.valueOf(1));
                });
              }

              @Override
              protected void failed() {
                super.failed();
              }
            };

            // Chạy Task trong một thread riêng biệt
            Thread taskThread = new Thread(searchTask);
            taskThread.setDaemon(true);  // Đảm bảo thread không chặn việc thoát ứng dụng
            taskThread.start();
          }
        }, 100);
      }
    });
    suggestionUser.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> {
              if (suggestionUser.getItems().isEmpty()) {
                return;
              }
              if (newValue != null) {

                Task<Void> updateTask = new Task<Void>() {
                  @Override
                  protected Void call() {
                    Platform.runLater(() -> {
                      Pane1.requestFocus();
                      userSearchBox.setText(
                              newValue.getContent()); // Đặt giá trị của TextField thành gợi ý đã chọn

                      userIdBox.setText(""+newValue.getID());
                      updateHistory(newValue.getID());
                      userSearchBox.positionCaret(userSearchBox.getText().length());
                      if (!suggestionUser.getItems().isEmpty()) {
                        suggestionUser.getItems().clear();
                        suggestionUser.setVisible(false);
                        suggestionUser.setMinHeight(0);
                        suggestionUser.setMaxHeight(0);

                        // Xóa class "newShape" khỏi Pane1 nếu tồn tại
                        if (Pane1.getStyleClass().contains("newShape")) {
                          Pane1.getStyleClass().remove("newShape");
                        }
                      }
                    });
                    return null;
                  }
              };
                new Thread(updateTask).start();
              }
            });

    borrowedDateColumn.setReorderable(false);
    userColumn.setReorderable(false);
    bookColumn.setReorderable(false);
    borrowedDateColumn.setCellValueFactory(new PropertyValueFactory<returnTableData, String>("borrowedDate"));
    userColumn.setCellValueFactory(new PropertyValueFactory<returnTableData, String>("user"));
    bookColumn.setCellValueFactory(new PropertyValueFactory<returnTableData, String>("book"));
    dueDateColumn.setCellValueFactory(new PropertyValueFactory<returnTableData, String>("dueDate"));

    bookColumn.setCellFactory(tc -> {
      return new javafx.scene.control.TableCell<returnTableData, String>() {
        private final Text text = new Text();

        @Override
        protected void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);
          if (empty || item == null) {
            setGraphic(null);
          } else {
            text.setText(item);
            text.wrappingWidthProperty()
                    .bind(getTableColumn().widthProperty()); // Đặt wrappingWidth để tự động xuống dòng
            text.setStyle(
                    "-fx-font-family: 'HYWenHei-85W'; -fx-fill: #8e8e8e;-fx-font-size: 17px;");
            text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            setAlignment(javafx.geometry.Pos.CENTER); // Căn giữa nội dung trong ô
            setGraphic(text);
          }
        }
      };
    });
    userColumn.setCellFactory(tc -> {
              return new javafx.scene.control.TableCell<returnTableData, String>() {
                private final Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty) {
                  super.updateItem(item, empty);
                  if (empty || item == null) {
                    setGraphic(null);
                  } else {
                    text.setText(item);
                    text.wrappingWidthProperty()
                            .bind(getTableColumn().widthProperty()); // Đặt wrappingWidth để tự động xuống dòng
                    text.setStyle(
                            "-fx-font-family: 'HYWenHei-85W'; -fx-fill: #8e8e8e;-fx-font-size: 17px;");
                    text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
                    setAlignment(javafx.geometry.Pos.CENTER); // Căn giữa nội dung trong ô
                    setGraphic(text);
                  }
                }
              };
            });
    updateHistory(-1);
    tableView.setPrefHeight(5 * 55 + 54);
    tableView.setItems(
            FXCollections.observableArrayList(dataList.subList(0, Math.min(5, dataList.size()))));

  }

  private void CreateUserSuggestions() {
    String prefixName = userSearchBox.getText();
    if (prefixName.isEmpty()) {
      suggestionUser.getItems().clear();
      suggestionUser.setVisible(false);
      if (Pane1.getStyleClass().contains("newShape")) {
        Pane1.getStyleClass().remove("newShape");
      }
      suggestionUser.setMaxHeight(0);
      suggestionUser.setMinHeight(0);

      return;
    }
    Thread thread = new Thread(() -> {
      ArrayList<Suggestion> listSuggestions = Library.getInstance()
              .getUserSuggestions(prefixName);
      ObservableList<SuggestionView> observableList = FXCollections.observableArrayList();

      Platform.runLater(() -> {
        suggestionUser.setItems(observableList);
        for (Suggestion suggestion : listSuggestions) {
          if (observableList.size() >= 5) {
            break;
          }
          observableList.add(new SuggestionView(suggestion, 35, 230));
        }
        int lastIndex = suggestionUser.getItems().size() - 1;

        suggestionUser.setVisible(true);

        int heightOfListView = Math.min(suggestionUser.getItems().size(), 5) * 55;
        suggestionUser.setMinHeight(heightOfListView);
        suggestionUser.setMaxHeight(heightOfListView);
        if (heightOfListView == 0 && Pane1.getStyleClass().contains("newShape")) {
          Pane1.getStyleClass().remove("newShape");
        }
        if (heightOfListView > 0 && !Pane1.getStyleClass().contains("newShape")) {
          Pane1.getStyleClass().add("newShape");
        }

        VBox1.setMinHeight(35 + heightOfListView);
        VBox1.setMaxHeight(35 + heightOfListView);
      });
    });
    thread.start();
  }

  public void confirmButtonAction(ActionEvent event) {
  }

  public void DeclineButtonAction(ActionEvent event) {
  }

  public void backButtonAction(ActionEvent event) {
  }

  public void refresh() {
    sortBox.setValue("Tìm Kiếm Theo Người Mượn");

    userIdBox.setText("");
    userSearchBox.setText("");
    updateHistory(-1);

  }
  @Override
  public void applyDarkMode(boolean isDark) {

  }

  // Không gọi setUpLanguage ở đây
  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {

  }

  // viLang lưu nội dung tiếng Việt gắn với Object, enLang lưu tiếng Anh
  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }


}

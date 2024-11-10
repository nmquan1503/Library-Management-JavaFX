package org.example.demo.Controllers;

import com.jfoenix.controls.JFXListView;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.demo.CustomUI.SuggestionView;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.BookShelf;
import org.example.demo.Models.Borrowing.Borrowing;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
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
  private VBox VBox1;
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

  public void updateHistory() {

    while (pageNow > 1) {
      leftController();
    }
    dataList.clear();
    ArrayList<Borrowing> allBorrowing;

    allBorrowing = Library.getInstance().getAllBorrowing();

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
      dataList.add(new returnTableData(user, nameBook, Today, Today1));
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

  @FXML
  private void searchButtonController() {

  }

  @FXML
  private void initialize() {
    addBox();
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
          suggestionUser.setVisible(false);
          if (Pane1.getStyleClass().contains("newShape")) {
            Pane1.getStyleClass().remove("newShape");
          }
        } else {
          suggestionUser.setVisible(true);
          if (!Pane1.getStyleClass().contains("newShape") && suggestionUser.getHeight() > 0) {
            Pane1.getStyleClass().add("newShape");
          }
        }
      }
    });
    userSearchBox.textProperty().addListener((observable) -> {
      if (userSearchBox.isFocused()) {
        CreateUserSuggestions();
      }
    });
    suggestionUser.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> {
              if (suggestionUser.getItems().isEmpty()) {
                return;
              }
              if (newValue != null) {
                Pane1.requestFocus();
                userSearchBox.setText(
                        newValue.getContent()); // Đặt giá trị của TextField thành gợi ý đã chọn

                userIdBox.setText(""+newValue.getID());
                userSearchBox.positionCaret(userSearchBox.getText().length());
                Platform.runLater(() -> {
                  suggestionUser.getItems().clear();
                  suggestionUser.setVisible(false);
                  suggestionUser.setMinHeight(0);
                  suggestionUser.setMaxHeight(0);

                  // Xóa class "newShape" khỏi Pane1 nếu tồn tại
                  if (Pane1.getStyleClass().contains("newShape")) {
                    Pane1.getStyleClass().remove("newShape");
                  }
                });
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
    updateHistory();
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

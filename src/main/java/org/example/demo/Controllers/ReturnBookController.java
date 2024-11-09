package org.example.demo.Controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.BookShelf;
import org.example.demo.Models.Borrowing.Borrowing;
import org.example.demo.Models.Library;
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

  private void updateHistory() {

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
  private void initialize() {
    left.setDisable(true);
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

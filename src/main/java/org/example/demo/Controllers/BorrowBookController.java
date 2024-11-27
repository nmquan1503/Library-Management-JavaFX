package org.example.demo.Controllers;

import com.jfoenix.controls.JFXListView;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.concurrent.Task;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlendMode;
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
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.BookShelf.BookShelf;
import org.example.demo.Models.Borrowing.Borrowing;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Users.User;
import org.example.demo.Models.Users.UserList;
import org.example.demo.Models.Users.Date;


public class BorrowBookController implements MainInfo {

  private ReturnBookController returnBookController = new ReturnBookController();
  private ObservableList<TableData> dataList = FXCollections.observableArrayList();
  private int pageNow;

  @FXML
  private Pane Pane1;

  @FXML
  private VBox VBox1;

  @FXML
  private AnchorPane mainPane;

  @FXML
  private ComboBox sortBox;

  @FXML
  private TableView<TableData> tableView; // Gán ID của TableView từ Scene Builder

  @FXML
  private TableColumn<TableData, String> typeColumn;

  @FXML
  private TableColumn<TableData, String> userColumn;

  @FXML
  private TableColumn<TableData, String> bookColumn;

  @FXML
  private TableColumn<TableData, String> timeColumn;

  @FXML
  private Button right;

  @FXML
  private Button left;

  @FXML
  private Label pageNumber;

  @FXML
  private AnchorPane secondPane;

  @FXML
  private JFXListView<SuggestionView> suggestionUser;

  @FXML
  private TextField userSearchBox;

  @FXML
  private TextField userIdBox;

  @FXML
  private Button searchButton;

  private UserList userList;
  private BookShelf bookList;
  @FXML
  private Label BirthdayLabel;

  @FXML
  private Label PhoneLabel;

  @FXML
  private Label EmailLabel;

  @FXML
  private Label AddressLabel;

  @FXML
  private Label isBanLabel;

  @FXML
  private TextField bookIdBox;

  @FXML
  private Pane Pane2;

  @FXML
  private VBox VBox2;

  @FXML
  private TextField bookSearchBox;

  @FXML
  private JFXListView<SuggestionView> suggestionBook;

  @FXML
  private Label wrongNotification;

  @FXML
  private Label PublisherLabel;

  @FXML
  private Label PublishedDateLabel;

  @FXML
  private Label BorrowedDateLabel;

  @FXML
  private DatePicker DueDatePicker;

  @FXML
  private Label QuantityLeftLabel;

  @FXML
  private Button searchButton1;

  @FXML
  private Button CancelButton;

  @FXML
  private Button CreateButton;

  @FXML
  private VBox alert;

  @FXML
  private Label confirmTitle;

  @FXML
  private Label confirmMessage;

  @FXML
  private Button confirmButton;

  @FXML
  private Button declineButton;

  @FXML
  private Button closeButton;

  private User npc;

  private Book book;

  @FXML
  private Pane successPane;

  @FXML
  private ImageView gifView;

  @FXML
  private Button backButton;

  @FXML
  private ImageView starImage;

  @FXML
  private ImageView starImage1;
  @FXML
  private Label borrowHistoryLabel;
  @FXML
  private Label borrowHistoryLabel1;
  @FXML
  private Label sortByLabel;
  @FXML
  private Button borrowButton;
  @FXML
  private Label birthdayLabel;
  @FXML
  private Label phoneLabel;

  @FXML
  private Label locateLabel;
  @FXML
  private Label banLabel;
  @FXML
  private Label publishedDateLabel;
  @FXML
  private Label borrowedDateLabel;
  @FXML
  private Label dueDateLabel;
  @FXML
  private Label leftLabel;
  @FXML
  private Label publisherLabel;
  @FXML
  private Label successLabel;
  @FXML
  private Label sucessMessageLabel;


  public static BooleanProperty listenUpdate = new SimpleBooleanProperty(false);

  @FXML
  private ObservableList<SuggestionView> suggestions;
  private ObservableList<SuggestionView> suggestions1;

  public void setReturnBookController(
          ReturnBookController returnBookController) {
    this.returnBookController = returnBookController;
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

  @FXML
  private void muonSachController() {
    secondPane.setDisable(false);
    secondPane.setVisible(true);
    mainPane.setVisible(false);
    mainPane.setDisable(true);
  }

  private void addBox() {
    if (!sortBox.getItems().isEmpty()) return;
     sortBox.getItems().addAll(
        "Sách Chưa Trả",
        "Sách Đã Trả",
        "Toàn Bộ Lịch Sử"
    );
    sortBox.setValue("Toàn Bộ Lịch Sử");
  }

  private void resetUserSearch() {
    userIdBox.setText("");
    userSearchBox.setText(""); // Đặt giá trị của TextField thành gợi ý đã chọn
    BirthdayLabel.setText("");
    PhoneLabel.setText("");
    EmailLabel.setText("");
    AddressLabel.setText("");
    isBanLabel.setText("");
  }

  private void resetBookSearch() {
    bookIdBox.setText("");
    bookSearchBox.setText("");
    PublishedDateLabel.setText("");
    BorrowedDateLabel.setText("");
    DueDatePicker.setValue(null);
    QuantityLeftLabel.setText("");
    PublisherLabel.setText("");
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

    String s = userIdBox.getText();
    if (s.isEmpty()) {
      return;
    }
    int x = Integer.parseInt(s);
    npc = userList.getUser(x);
    if (npc == null) {
      if (wrongNotification.isVisible()) {
        return;
      }
      if ( borrowHistoryLabel1.getText().equals("Yêu Cầu Mượn Sách")) {
        createErrorText("User ID không tồn tại !!!");
      }
      else {
        createErrorText("User ID is not available !!!");
      }
      resetUserSearch();

    } else {
      userSearchBox.setText(npc.getName()); // Đặt giá trị của TextField thành gợi ý đã chọn

      userIdBox.setText(String.valueOf(x));
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      String dateString = formatter.format(npc.getBirthday());
      BirthdayLabel.setText(dateString);

      PhoneLabel.setText(npc.getPhoneNumber());
      EmailLabel.setText(npc.getEmail());
      AddressLabel.setText(npc.getAddress());
      if (!npc.isBan()) {
        isBanLabel.setText("No");
      } else {
        isBanLabel.setText("Yes");
      }
    }
  }

  @FXML
  private void searchButtonController1() {
    String s = bookIdBox.getText();
    if (s.isEmpty()) {
      return;
    }
    int x = Integer.parseInt(s);
    book = bookList.getBook(x);
    if (book == null) {
      if (wrongNotification.isVisible()) {
        return;
      }
      if ( borrowHistoryLabel1.getText().equals("Yêu Cầu Mượn Sách")) {
        createErrorText("Book ID không tồn tại !!!");
      }
      else {
        createErrorText("Book ID is not available !!!");
      }
      resetBookSearch();

    } else {
      bookSearchBox.setText(book.getTitle()); // Đặt giá trị của TextField thành gợi ý đã chọn

      //userIdBox.setText(String.valueOf(x));
      Date today = new Date(new java.sql.Date(System.currentTimeMillis()));

      BorrowedDateLabel.setText(today.toString());
      LocalDate localDatePlus10Days = today.add(10).toLocalDate();
      DueDatePicker.setValue(localDatePlus10Days);
      QuantityLeftLabel.setText("" + book.getQuantity() + " quyển");
      PublisherLabel.setText(book.getPublisher());
      PublishedDateLabel.setText("" + book.getPublishedDate());
    }
  }

  @FXML
  private void CancelAction() {
    alert.setVisible(true);
    ColorAdjust darkenEffect = new ColorAdjust();
    darkenEffect.setBrightness(-0.4);
    secondPane.setEffect(darkenEffect);
    secondPane.getChildren().forEach(node -> {
      node.setDisable(true);
    });
    
    alert.setDisable(false);

    if ( !userIdBox.getPromptText().equals("User ID")) {
      confirmTitle.setText("Hủy Yêu Cầu Mượn Sách");
      confirmMessage.setText("Xác Nhận Hủy Chứ?");
    }
    else {
      confirmTitle.setText("Cancel Borrowing Request");
      confirmMessage.setText("Confirm Cancel?");
    }
    confirmButton.setText("Yes");
    declineButton.setText(" No");
    closeButton.setVisible(true);
    closeButton.setDisable(false);
  }

  @FXML
  private void DeclineButtonAction() {
    secondPane.getChildren().forEach(node -> {
      node.setDisable(false);
    });
    secondPane.setEffect(null);
    alert.setVisible(false);
    alert.setDisable(true);
    closeButton.setVisible(false);
    closeButton.setDisable(true);

  }

  @FXML
  private void confirmButtonAction() {
    if (!confirmTitle.getText().equals("Thêm Yêu Cầu Mượn Sách") && !confirmTitle.getText().equals("Add Borrow Request")) {
      secondPane.setDisable(true);
      secondPane.setVisible(false);
      mainPane.setVisible(true);
      mainPane.setDisable(false);
      successPane.setDisable(true);
      successPane.setVisible(false);
      resetUserSearch();
      resetBookSearch();
      book = null;
      npc = null;
      DeclineButtonAction();
    } else {
      Date today = new Date(new java.sql.Date(System.currentTimeMillis()));
      LocalDate localDate = today.toLocalDate();
      if (npc == null) {
        if (borrowHistoryLabel1.getText().equals("Yêu Cầu Mượn Sách")) {
          createErrorText("Thiếu thông tin người mượn!!!");
        } else {
          createErrorText("Missing borrower information!!!");
        }
      } else if (book == null) {
        if (borrowHistoryLabel1.getText().equals("Yêu Cầu Mượn Sách")) {
          createErrorText("Thiếu thông tin sách!!!");
        } else {
          createErrorText("Missing book information!!!");
        }
      } else if (Integer.parseInt(userIdBox.getText()) != npc.getId() || !userSearchBox.getText()
              .equals(npc.getName())) {
        if (borrowHistoryLabel1.getText().equals("Yêu Cầu Mượn Sách")) {
          createErrorText("Thông tin người mượn không chính xác!!!");
        } else {
          createErrorText("Borrower information is incorrect!!!");
        }
      } else if (Integer.parseInt(bookIdBox.getText()) != book.getId() || !bookSearchBox.getText()
              .equals(book.getTitle())) {
        if (borrowHistoryLabel1.getText().equals("Yêu Cầu Mượn Sách")) {
          createErrorText("Thông tin sách không chính xác!!!");
        } else {
          createErrorText("Book information is incorrect!!!");
        }
      } else if (isBanLabel.getText().equals("Yes")) {
        if (borrowHistoryLabel1.getText().equals("Yêu Cầu Mượn Sách")) {
          createErrorText("Người mượn đang bị cấm!!!");
        } else {
          createErrorText("The borrower is banned!!!");
        }
      } else if (book.getQuantity() == 0) {
        if (borrowHistoryLabel1.getText().equals("Yêu Cầu Mượn Sách")) {
          createErrorText("Số lượng sách không đủ!!!");
        } else {
          createErrorText("Not enough book quantity!!!");
        }
      } else if (DueDatePicker.getValue().isBefore(localDate)) {
        if (borrowHistoryLabel1.getText().equals("Yêu Cầu Mượn Sách")) {
          createErrorText("Ngày trả không được sớm hơn ngày mượn!!!");
        } else {
          createErrorText("Return date cannot be earlier than borrow date!!!");
        }
      } else {
        successPane.setDisable(false);
        successPane.setVisible(true);
        alert.setVisible(false);
        alert.setDisable(true);
        closeButton.setDisable(true);
        closeButton.setVisible(false);
        gifView.setVisible(true);
        backButton.setDisable(true);
        backButton.setStyle("-fx-background-color: #d5e6f9;");
        gifView.setImage(new Image(getClass().getResource("/images/success.gif").toExternalForm()));
        PauseTransition delay = new PauseTransition(
            Duration.seconds(2)); // Thời gian delay bằng với thời lượng GIF
        delay.setOnFinished(e -> {
          backButton.setDisable(false);
          gifView.setImage(
              new Image(getClass().getResource("/images/sucessimage.png").toExternalForm()));
          backButton.setStyle("-fx-background-color: #4899f7;-fx-font-size:13px;");

        });
        delay.play();
        Task<Void> borrowTask = new Task<>() {
          @Override
          protected Void call() throws Exception {
            Date today = new Date(new java.sql.Date(System.currentTimeMillis()));
            LocalDate x = DueDatePicker.getValue();
            Date due = new Date(x.getYear(), x.getMonthValue(), x.getDayOfMonth());
            Library.getInstance().borrowBook(book, npc, today, due);
            return null;
          }

          @Override
          protected void succeeded() {
            super.succeeded();
            updateHistory("" + sortBox.getValue());
            returnBookController.updateHistory(-1);
            returnBookController.setPageNumber(1);
          }

          @Override
          protected void failed() {
          }
        };

        // Chạy Task trên một luồng riêng biệt
        Thread taskThread = new Thread(borrowTask);
        taskThread.setDaemon(true); // Đảm bảo luồng không chặn việc thoát ứng dụng
        taskThread.start();
        BaseController.setIsBorrowingChanged(1 - BaseController.getIsBorrowingChanged());
        return;
      }
      DeclineButtonAction();
    }
  }

  @FXML
  private void backButtonAction() {
    secondPane.setDisable(true);
    secondPane.setVisible(false);
    mainPane.setVisible(true);
    mainPane.setDisable(false);
    successPane.setVisible(false);
    successPane.setDisable(true);
    resetUserSearch();
    resetBookSearch();
    book = null;
    npc = null;
    DeclineButtonAction();
  }

  @FXML
  private void CreateAction() {
    alert.setVisible(true);
    ColorAdjust darkenEffect = new ColorAdjust();
    darkenEffect.setBrightness(-0.4);
    secondPane.setEffect(darkenEffect);
    secondPane.getChildren().forEach(node -> {
      node.setDisable(true);
    });
    alert.setDisable(false);
    if ( userIdBox.getPromptText().equals("User ID")) {
      confirmTitle.setText("Add Borrow Request");
      confirmMessage.setText("Confirm Addition?");
    }
    else {
      confirmTitle.setText("Thêm Yêu Cầu Mượn Sách");
      confirmMessage.setText("Xác Nhận Thêm Chứ?");
    }
    confirmButton.setText("Yes");
    declineButton.setText(" No");
    closeButton.setVisible(true);
    closeButton.setDisable(false);
  }

  public void updateHistory(String type) {
    pageNow = 1;
    left.setDisable(true);
    pageNumber.setText("" + 1);
    dataList.clear();
    sortBox.setValue("" + type);

    // Tạo một Task để xử lý công việc nặng
    Task<ObservableList<TableData>> loadHistoryTask = new Task<>() {
      @Override
      protected ObservableList<TableData> call() {
        ArrayList<Borrowing> allBorrowing;
        if (type.equals("Toàn Bộ Lịch Sử") || type.equals("Full History")) {
          allBorrowing = Library.getInstance().getAllHistory();
        } else if (type.equals("Sách Chưa Trả") || type.equals("Unreturned Books")) {
          allBorrowing = Library.getInstance().getAllBorrowing();
        } else {
          allBorrowing = Library.getInstance().getAllReturning();
        }

        ObservableList<TableData> tempDataList = FXCollections.observableArrayList();

        for (Borrowing x : allBorrowing) {
          String action = (x.getReturnedDate() == null) ? "Mượn" : "Trả";
          if ( x.getReturnedDate() == null ) {
            if ( sortByLabel.getText().equals("Sort By") ) {
              action = "Borrow";
            }
          }
          else if ( sortByLabel.getText().equals("Sort By")) {
            action = "Return";
          }
          String user = userList.getUser(x.getIdUser()).getName();
          String nameBook = bookList.getBook(x.getIdBook()).getTitle();
          LocalDate now = (x.getReturnedDate() == null) ? x.getBorrowedDate().toLocalDate() : x.getReturnedDate().toLocalDate();
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
          String today = now.format(formatter);

          TableData tableData = new TableData(action, user, nameBook, today);

          // Cắt chuỗi nếu cần
          if (tableData.getBook().length() >= 20) {
            String s = tableData.getBook();
            StringBuilder t = new StringBuilder();
            boolean check = false;
            for (int i = 0; i < s.length(); i++) {
              t.append(s.charAt(i));
              if (i >= 20 && s.charAt(i) == ' ' && !check) {
                check = true;
                t.append("\n");
              }
            }
            tableData.setBook(t.toString());
          }

          tempDataList.add(tableData);
        }
        return tempDataList;
      }

      @Override
      protected void succeeded() {
        super.succeeded();
        ObservableList<TableData> result = getValue();

        // Cập nhật giao diện trên UI thread
        dataList.setAll(result);

        if (dataList.size() > pageNow * 5) {
          right.setDisable(false);
        }
        int x = Math.min(dataList.size(), pageNow * 5);
        tableView.setItems(
                FXCollections.observableArrayList(dataList.subList(5 * (pageNow - 1), x))
        );

        right.setDisable(dataList.size() <= 5);
      }

      @Override
      protected void failed() {
        super.failed();

      }
    };

    // Chạy Task trong một thread riêng biệt
    Thread backgroundThread = new Thread(loadHistoryTask);
    backgroundThread.setDaemon(true);
    backgroundThread.start();
  }


  @FXML
  private void initialize() {
    addBox();
    setupTooltip();
    alert.setVisible(false);
    alert.setDisable(true);
    closeButton.setVisible(false);
    closeButton.setDisable(true);
    userList = new UserList();
    bookList = new BookShelf();
    suggestionUser.setMinHeight(0);
    suggestionUser.setMaxHeight(0);
    suggestionBook.setMinHeight(0);
    suggestionBook.setMaxHeight(0);
    suggestions = FXCollections.observableArrayList();
    suggestions1 = FXCollections.observableArrayList();
    suggestionUser.setItems(suggestions);
    suggestionBook.setItems(suggestions1);
    successPane.setVisible(false);
    successPane.setDisable(true);

    sortBox.valueProperty().addListener((observable, oldValue, newValue) -> {
      updateHistory("" + newValue);
      // Xử lý logic khi giá trị thay đổi
    });

    DueDatePicker.setConverter(new StringConverter<LocalDate>() {
      String pattern = "dd/MM/yyyy";
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

    userSearchBox.focusedProperty().addListener(new ChangeListener<Boolean>() {

      @Override
      public void changed(ObservableValue<? extends Boolean> observableValue,
          Boolean oldValue, Boolean newValue) {
        if (!newValue) {
          suggestionUser.setVisible(false);
          suggestionUser.setMaxHeight(0);
          suggestionUser.setMinHeight(0);
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
    bookSearchBox.focusedProperty().addListener(new ChangeListener<Boolean>() {

      @Override
      public void changed(ObservableValue<? extends Boolean> observableValue,
          Boolean oldValue, Boolean newValue) {
        if (!newValue) {
          suggestionBook.setVisible(false);
          suggestionBook.getItems().clear();
          suggestionBook.setMinHeight(0);
          suggestionBook.setMaxHeight(0);
          VBox2.setMaxHeight(35);
          VBox2.setMinHeight(35);
          if (Pane2.getStyleClass().contains("newShape")) {
            Pane2.getStyleClass().remove("newShape");
          }
        } else {
          suggestionBook.setVisible(true);
          CreateBookSuggestions();
          if (!Pane2.getStyleClass().contains("newShape") && suggestionBook.getHeight() > 0) {
            Pane2.getStyleClass().add("newShape");
          }
        }
      }
    });
    userSearchBox.textProperty().addListener((observable) -> {
      if (userSearchBox.isFocused()) {
        CreateUserSuggestions();
      }
    });
    bookSearchBox.textProperty().addListener((observable) -> {
      if (bookSearchBox.isFocused()) {
        CreateBookSuggestions();
      }

    });
    // Lắng nghe sự kiện khi người dùng chọn gợi ý
    suggestionUser.getSelectionModel().selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (suggestionUser.getItems().isEmpty()) {
            return;
          }
          if (newValue != null) {
            Pane1.requestFocus();
            userSearchBox.setText(
                newValue.getContent()); // Đặt giá trị của TextField thành gợi ý đã chọn

            userIdBox.setText(String.valueOf(newValue.getID()));
            npc = userList.getUser(newValue.getID());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = formatter.format(npc.getBirthday());
            BirthdayLabel.setText(dateString);

            PhoneLabel.setText(npc.getPhoneNumber());
            EmailLabel.setText(npc.getEmail());
            AddressLabel.setText(npc.getAddress());
            if (!npc.isBan()) {
              isBanLabel.setText("No");
            } else {
              isBanLabel.setText("Yes");
            }
            userSearchBox.positionCaret(userSearchBox.getText().length());
            Platform.runLater(() -> {
              suggestionUser.getItems().clear();
              suggestionUser.setVisible(false);
              suggestionUser.setMinHeight(0);
              suggestionUser.setMaxHeight(0);
              VBox1.setMinHeight(35);
              VBox1.setMaxHeight(35);
              // Xóa class "newShape" khỏi Pane1 nếu tồn tại
              if (Pane1.getStyleClass().contains("newShape")) {
                Pane1.getStyleClass().remove("newShape");
              }
            });
          }
        });
    suggestionBook.getSelectionModel().selectedItemProperty()
            .addListener((observable, oldValue, newValue) -> {
              if (newValue == null || suggestionBook.getItems().isEmpty()) {
                return;
              }

              Task<Void> updateTask = new Task<Void>() {
                @Override
                protected Void call() {
                  // Cập nhật dữ liệu khi một mục mới được chọn
                  Platform.runLater(() -> {
                    Pane2.requestFocus();
                    bookSearchBox.setText(newValue.getContent());
                    bookIdBox.setText(String.valueOf(newValue.getID()));

                    // Lấy thông tin về sách
                    book = bookList.getBook(newValue.getID());
                    if (book != null) {
                      bookSearchBox.positionCaret(bookSearchBox.getText().length());
                      PublisherLabel.setText(book.getPublisher());
                      PublishedDateLabel.setText(String.valueOf(book.getPublishedDate()));

                      // Xử lý ngày hiện tại và ngày trả sách
                      Date today = new Date(new java.sql.Date(System.currentTimeMillis()));
                      BorrowedDateLabel.setText(today.toString());

                      LocalDate localDatePlus10Days = today.add(10).toLocalDate();
                      DueDatePicker.setValue(localDatePlus10Days);

                      QuantityLeftLabel.setText(book.getQuantity() + " quyển");
                    }
                    Pane2.requestFocus();

                    // Đảm bảo các thao tác xoá và ẩn danh sách gợi ý diễn ra an toàn
                    if (!suggestionBook.getItems().isEmpty()) {
                      suggestionBook.getItems().clear();
                      suggestionBook.setVisible(false);
                      suggestionBook.setMinHeight(0);
                      suggestionBook.setMaxHeight(0);
                      VBox2.setMinHeight(35);
                      VBox2.setMaxHeight(35);
                      // Xóa class "newShape" khỏi Pane2 nếu tồn tại
                      if (Pane2.getStyleClass().contains("newShape")) {
                        Pane2.getStyleClass().remove("newShape");
                      }
                    }
                  });
                  return null;
                }
              };

              // Chạy Task trên luồng nền
              new Thread(updateTask).start();
            });
    secondPane.setDisable(true);
    secondPane.setVisible(false);
    tableView.setSelectionModel(null);
    pageNow = 1;
    left.setDisable(true);
    updateHistory("" + sortBox.getValue());

    if (dataList.size() <= 5) {
      right.setDisable(true);
    }
    typeColumn.setReorderable(false);
    userColumn.setReorderable(false);
    bookColumn.setReorderable(false);
    typeColumn.setCellValueFactory(new PropertyValueFactory<TableData, String>("action"));
    userColumn.setCellValueFactory(new PropertyValueFactory<TableData, String>("user"));
    bookColumn.setCellValueFactory(new PropertyValueFactory<TableData, String>("book"));
    timeColumn.setCellValueFactory(new PropertyValueFactory<TableData, String>("date"));

    bookColumn.setCellFactory(tc -> {
      return new javafx.scene.control.TableCell<TableData, String>() {
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
                "-fx-fill: #8e8e8e;-fx-font-size: 17px;");
            text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            setAlignment(javafx.geometry.Pos.CENTER); // Căn giữa nội dung trong ô
            setGraphic(text);
          }
        }
      };
    });
    userColumn.setCellFactory(tc -> {
      return new javafx.scene.control.TableCell<TableData, String>() {
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
                "-fx-fill: #8e8e8e;-fx-font-size: 17px;");
            text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
            setAlignment(javafx.geometry.Pos.CENTER); // Căn giữa nội dung trong ô
            setGraphic(text);
          }
        }
      };
    });
    tableView.setPrefHeight(5 * 55 + 51);
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
      VBox1.setMinHeight(35);
      VBox1.setMaxHeight(35);
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

  private void CreateBookSuggestions() {
    String prefixname = bookSearchBox.getText();
    if (Pane2.isFocused()) {
      prefixname = prefixname + "@";
    }
    String prefixName = prefixname;
    if (prefixName.isEmpty()) {
      suggestionBook.getItems().clear();
      suggestionBook.setVisible(false);
      if (Pane2.getStyleClass().contains("newShape")) {
        Pane2.getStyleClass().remove("newShape");
      }
      suggestionBook.setMaxHeight(0);
      suggestionBook.setMinHeight(0);
      VBox2.setMinHeight(35);
      VBox2.setMaxHeight(35);
      return;
    }
    Thread thread = new Thread(() -> {
      ArrayList<Suggestion> listSuggestions = Library.getInstance()
          .getBookSuggestions(prefixName);
      ObservableList<SuggestionView> observableList = FXCollections.observableArrayList();

      Platform.runLater(() -> {
        suggestionBook.setItems(observableList);
        for (Suggestion suggestion : listSuggestions) {
          if (observableList.size() >= 5) {
            break;
          }
          observableList.add(new SuggestionView(suggestion, 35, 230));
        }
        int lastIndex = suggestionBook.getItems().size() - 1;

        suggestionBook.setVisible(true);

        int heightOfListView = Math.min(suggestionBook.getItems().size(), 5) * 55;
        suggestionBook.setMinHeight(heightOfListView);
        suggestionBook.setMaxHeight(heightOfListView);
        if (heightOfListView == 0 && Pane2.getStyleClass().contains("newShape")) {
          Pane2.getStyleClass().remove("newShape");
        }
        if (heightOfListView > 0 && !Pane2.getStyleClass().contains("newShape")
            && suggestionBook.isVisible()) {
          Pane2.getStyleClass().add("newShape");
        }

        VBox2.setMinHeight(35 + heightOfListView);
        VBox2.setMaxHeight(35 + heightOfListView);
      });
    });
    thread.start();
  }

  private void setupTooltip() {
    Tooltip tooltip = new Tooltip("Search");

    // Đặt thời gian delay cho Tooltip khi hover (1 giây)
    tooltip.setShowDelay(Duration.seconds(1));

    // Gắn Tooltip vào FontAwesomeIconView searchIcon
    Tooltip.install(searchButton, tooltip);
    Tooltip.install(searchButton1, tooltip);
  }

  public void refresh() {
    resetBookSearch();
    resetUserSearch();
    secondPane.setVisible(false);
    secondPane.setDisable(true);
    mainPane.setVisible(true);
    mainPane.setDisable(false);
    alert.setDisable(true);
    alert.setVisible(false);
    successPane.setDisable(true);
    successPane.setVisible(false);
    closeButton.setDisable(true);
    closeButton.setVisible(false);
    mainPane.getChildren().forEach(node -> {
      node.setDisable(false);
    });

    mainPane.setEffect(null);
    secondPane.getChildren().forEach(node -> {
      node.setDisable(false);
    });
    secondPane.setEffect(null);
    secondPane.setDisable(true);

    updateHistory("Toàn Bộ Lịch Sử");
    if (sortByLabel.getText().equals("Sort By")){
      sortBox.setValue("Full History");
    }
    else {
      sortBox.setValue("Toàn Bộ Lịch Sử");
    }
    book = null;
    npc = null;
  }

  @Override
  public void applyDarkMode(boolean isDark) {
    if ( isDark ) {
      starImage1.setBlendMode(BlendMode.DIFFERENCE);
      starImage.setBlendMode(BlendMode.DIFFERENCE);
    }
    else {
      starImage1.setBlendMode(BlendMode.SRC_OVER);
      starImage.setBlendMode(BlendMode.SRC_OVER);
    }
    for (SuggestionView suggestionView : suggestionUser.getItems()) {
      suggestionView.applyDarkMode(isDark);
    }
    for (SuggestionView suggestionView : suggestionBook.getItems()) {
      suggestionView.applyDarkMode(isDark);
    }
  }

  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {
    if ( isTranslate) {
      borrowHistoryLabel.setText("Book Borrowing History");
      sortByLabel.setText("Sort By");
      sortBox.getItems().set(0, "Unreturned Books");
      sortBox.getItems().set(1, "Returned Books");
      sortBox.getItems().set(2, "Full History");
      if (sortBox.getValue().equals("Sách Chưa Trả") ) {
        sortBox.setValue("Unreturned Books");
      }
      else if (sortBox.getValue().equals("Sách Đã Trả"))
      {
        sortBox.setValue("Returned Books");
      }
      else {
        sortBox.setValue("Full History");
      }
      typeColumn.setText("Action");
      userColumn.setText("User");
      bookColumn.setText("Book");
      timeColumn.setText("Time");
      borrowButton.setText("Create A Request");
      borrowHistoryLabel1.setText("Book Borrowing Request");
      userIdBox.setPromptText("User ID");
      userSearchBox.setPromptText("User Name");
      bookIdBox.setPromptText("Book ID");
      bookSearchBox.setPromptText("Book Title");
      birthdayLabel.setText("BirthDay");
      phoneLabel.setText("Phone Number");
      locateLabel.setText("Location");
      banLabel.setText("Is Banned?");
      publisherLabel.setText("Publisher");
      publishedDateLabel.setText("Published Date");
      borrowedDateLabel.setText("Borrowed Date");
      dueDateLabel.setText("Due Date");
      leftLabel.setText("Remain");
      CancelButton.setText("Cancel");
      CreateButton.setText("Create");
      if ( confirmTitle.getText().equals("Thêm Yêu Cầu Mượn Sách")) {
        confirmTitle.setText("Add Borrow Request");
        confirmMessage.setText("Confirm Addition?");
      }
      else {
        confirmTitle.setText("Cancel Borrowing Request");
        confirmMessage.setText("Confirm Cancel?");
      }
      successLabel.setText("Success!");
      sucessMessageLabel.setText("Transaction performed successfully");
      backButton.setText("Go Back");
    }
    else {
      borrowHistoryLabel.setText("Lịch Sử Mượn Sách");
      sortByLabel.setText("Sắp Xếp Theo");
      sortBox.getItems().set(0, "Sách Chưa Trả");
      sortBox.getItems().set(1, "Sách Đã Trả");
      sortBox.getItems().set(2, "Toàn Bộ Lịch Sử");
      if (sortBox.getValue().equals("Unreturned Books") ) {
        sortBox.setValue("Sách Chưa Trả");
      }
      else if (sortBox.getValue().equals("Returned Books"))
      {
        sortBox.setValue("Sách Đã Trả");
      }
      else {
        sortBox.setValue("Toàn Bộ Lịch Sử");
      }
      typeColumn.setText("Hành Động");
      userColumn.setText("Người Dùng");
      bookColumn.setText("Sách");
      timeColumn.setText("Thời Gian");
      borrowButton.setText("Tạo Yêu Cầu");
      userIdBox.setPromptText("ID Người");
      userSearchBox.setPromptText("Tên Người Dùng");
      borrowHistoryLabel1.setText("Yêu Cầu Mượn Sách");
      bookIdBox.setPromptText("ID Book");
      bookSearchBox.setPromptText("Tên Sách");
      birthdayLabel.setText("Sinh Nhật");
      phoneLabel.setText("Số Điện Thoại");
      locateLabel.setText("Địa Chỉ");
      banLabel.setText("Bị Cấm?");
      publisherLabel.setText("Nhà Xuất Bản");
      publishedDateLabel.setText("Ngày Xuất Bản");
      borrowedDateLabel.setText("Ngày Mượn");
      dueDateLabel.setText("Hạn Trả");
      leftLabel.setText("Còn Lại");
      CancelButton.setText("Hủy");
      CreateButton.setText("Tạo");
      if ( confirmTitle.getText().equals("Add Borrow Request")) {
        confirmTitle.setText("Thêm Yêu Cầu Mượn Sách");
        confirmMessage.setText("Xác Nhận Thêm Chứ?");
      }
      else {
        confirmTitle.setText("Hủy Yêu Cầu Mượn Sách");
        confirmMessage.setText("Xác Nhận Hủy Chứ?");
      }
      successLabel.setText("Thành công!");
      sucessMessageLabel.setText("Giao dịch thực hiện thành công");
      backButton.setText("Quay Về");
    }
  }

  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }
}

package org.example.demo.Controllers;

import com.jfoenix.controls.JFXListView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.collections.ObservableListBase;
import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.example.demo.CustomUI.SuggestionView;
import org.example.demo.Database.JDBC;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.BookShelf.BookShelf;
import org.example.demo.Models.Borrowing.BorrowHistory;
import org.example.demo.Models.Borrowing.Borrowing;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Trie.Trie;
import org.example.demo.Models.Users.User;
import org.example.demo.Models.Users.UserList;
import org.example.demo.Models.Users.Date;
import javafx.scene.layout.HBox;


public class BorrowBookController implements MainInfo {

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
    private ObservableList<SuggestionView> suggestions;
    private ObservableList<SuggestionView> suggestions1;

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
        sortBox.getItems().addAll(
                "Sách Chưa Trả",
                "Sách Đã Trả",
                "Toàn Bộ Lịch Sử"
        );
        sortBox.setValue("Sách Chưa Trả");
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
        if ( wrongNotification.isVisible() ) return;
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
        if (s.isEmpty()) return;
        int x = Integer.parseInt(s);
        npc = userList.getUser(x);
        if (npc == null) {
            if ( wrongNotification.isVisible() ) {
                return;
            }
            createErrorText("User ID không tồn tại !!!");
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
        if ( s.isEmpty() ) return;
        int x = Integer.parseInt(s);
        book = bookList.getBook(x);
        if (book == null) {
            if ( wrongNotification.isVisible() ) {
                return;
            }
            createErrorText("Book ID không tồn tại !!!");
            resetBookSearch();

        } else {
            bookSearchBox.setText(book.getTitle()); // Đặt giá trị của TextField thành gợi ý đã chọn

            //userIdBox.setText(String.valueOf(x));
            Date today = new Date(new java.sql.Date(System.currentTimeMillis()));

            BorrowedDateLabel.setText(today.toString());
            LocalDate localDatePlus10Days = today.add(10).toLocalDate();
            DueDatePicker.setValue(localDatePlus10Days);
            QuantityLeftLabel.setText(""+book.getQuantity()+ " quyển" );
            PublisherLabel.setText(book.getPublisher());
            PublishedDateLabel.setText(""+book.getPublishedDate());
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

        confirmTitle.setText("Hủy Yêu Cầu Mượn Sách");
        confirmMessage.setText("Xác Nhận Hủy Chứ?");
        confirmButton.setText("YES");
        declineButton.setText("Cancel");
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
        if ( declineButton.getText().equals("Cancel")) {
            secondPane.setDisable(true);
            secondPane.setVisible(false);
            mainPane.setVisible(true);
            mainPane.setDisable(false);
            successPane.setDisable(true);
            successPane.setVisible(false);
            resetUserSearch();
            resetBookSearch();
            DeclineButtonAction();
        }
        else {
            Date today = new Date(new java.sql.Date(System.currentTimeMillis()));
            LocalDate localDate = today.toLocalDate();
            if ( npc == null ) {
                createErrorText("Thiếu thông tin người mượn!!!");
            }
            else if ( book == null ) {
                createErrorText("Thiếu thông tin sách!!!");
            }
            else if ( Integer.parseInt(userIdBox.getText()) != npc.getId() || !userSearchBox.getText().equals(npc.getName())) {
                createErrorText("Thông tin người mượn không chính xác!!!");
            }
            else if ( Integer.parseInt(bookIdBox.getText()) != book.getId() || !bookSearchBox.getText().equals(book.getTitle())) {
                createErrorText("Thông tin sách không chính xác!!!");
            }
            else if ( isBanLabel.getText().equals("Yes") ) {
                createErrorText("Người mượn đang bị cấm!!!");
            }
            else if ( book.getQuantity() == 0 ) {
                createErrorText("Số lượng sách không đủ!!!");
            }
            else if ( DueDatePicker.getValue().isBefore(localDate) ) {
                createErrorText("Ngày trả không được sớm hơn ngày mượn!!!");
            }
            else {
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
                PauseTransition delay = new PauseTransition(Duration.seconds(2)); // Thời gian delay bằng với thời lượng GIF
                delay.setOnFinished(e -> {
                    backButton.setDisable(false);
                    gifView.setImage(new Image(getClass().getResource("/images/sucessimage.png").toExternalForm()));
                    backButton.setStyle("-fx-background-color: #4899f7;-fx-font-size:13px;");

                });
                delay.play();


            }

            DeclineButtonAction();

        }
    }

    @FXML
    private void backButtonAction() {
        Date today = new Date(new java.sql.Date(System.currentTimeMillis()));
        LocalDate x = DueDatePicker.getValue();
        Date due = new Date(x.getYear(),x.getMonthValue(),x.getDayOfMonth());
        Library.getInstance().borrowBook(book,npc,today,due);
        updateHistory(""+sortBox.getValue());
        secondPane.setDisable(true);
        secondPane.setVisible(false);
        mainPane.setVisible(true);
        mainPane.setDisable(false);
        successPane.setVisible(false);
        successPane.setDisable(true);
        resetUserSearch();
        resetBookSearch();
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

        confirmTitle.setText("Thêm Yêu Cầu Mượn Sách");
        confirmMessage.setText("Xác Nhận Thêm Chứ?");
        confirmButton.setText("YES");
        declineButton.setText("NO");
        closeButton.setVisible(true);
        closeButton.setDisable(false);
    }

    private void updateHistory(String type) {

        while ( pageNow > 1 ) {
            leftController();
        }
        dataList.clear();
        ArrayList<Borrowing> allBorrowing;
        if ( type.equals("Toàn Bộ Lịch Sử") ) allBorrowing= Library.getInstance().getAllHistory();
        else if ( type.equals("Sách Chưa Trả") ) allBorrowing = Library.getInstance().getAllBorrowing();
        else allBorrowing=Library.getInstance().getAllReturning();
        for ( Borrowing x : allBorrowing) {
            String action = "";
            if ( x.getReturnedDate() == null ) action = "Mượn";
            else action = "Trả";
            String user = userList.getUser(x.getIdUser()).getName();
            String nameBook = bookList.getBook(x.getIdBook()).getTitle();
            LocalDate now ;
            if (x.getReturnedDate() == null ) now= x.getBorrowedDate().toLocalDate();
            else now = x.getReturnedDate().toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String Today = now.format(formatter);
            dataList.add(new TableData(action,user,nameBook,Today));
        }

        for (TableData x : dataList) {
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
        if ( dataList.size() > pageNow*5 ) right.setDisable(false);
        int x = Math.min(dataList.size(),pageNow*5);
        tableView.setItems(
                FXCollections.observableArrayList(dataList.subList(5 * (pageNow - 1), x)));
        if ( dataList.size() > 5 ) right.setDisable(false);
        else right.setDisable(true);
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
            updateHistory(""+newValue);
            // Xử lý logic khi giá trị thay đổi
        });

        DueDatePicker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "dd/MM/yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        userSearchBox.focusedProperty().addListener( new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                    Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    suggestionUser.setVisible(false);
                    if ( Pane1.getStyleClass().contains("newShape") ) Pane1.getStyleClass().remove("newShape");
                }
                else {
                    suggestionUser.setVisible(true);
                    if ( !Pane1.getStyleClass().contains("newShape") && suggestionUser.getHeight() > 0 ) Pane1.getStyleClass().add("newShape");
                }
            }
        });
        bookSearchBox.focusedProperty().addListener( new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue,
                    Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    suggestionBook.setVisible(false);
                    suggestionBook.getItems().clear();
                    suggestionBook.setMinHeight(0);
                    suggestionBook.setMaxHeight(0);

                    if ( Pane2.getStyleClass().contains("newShape") ) Pane2.getStyleClass().remove("newShape");
                }
                else {
                    suggestionBook.setVisible(true);
                    if ( !Pane2.getStyleClass().contains("newShape") && suggestionBook.getHeight() > 0 ) Pane2.getStyleClass().add("newShape");
                }
            }
        });
        userSearchBox.textProperty().addListener((observable) -> {
            if (userSearchBox.isFocused()) {
                CreateUserSuggestions();
            }
        });
        bookSearchBox.textProperty().addListener( (observable) -> {
            if ( bookSearchBox.isFocused()) {
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
                        suggestionUser.getItems().clear();
                        suggestionUser.setVisible(false); // Ẩn danh sách gợi ý sau khi chọn
                        if (Pane1.getStyleClass().contains("newShape")) {
                            Pane1.getStyleClass().remove("newShape");
                        }

                        suggestionUser.setMinHeight(0);
                        suggestionUser.setMaxHeight(0);
                    }
                });
        suggestionBook.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (suggestionBook.getItems().isEmpty()) {
                        return;
                    }
                    if (newValue != null) {
                        Pane2.requestFocus();
                        bookSearchBox.setText(
                                newValue.getContent()); // Đặt giá trị của TextField thành gợi ý đã chọn

                        bookIdBox.setText(String.valueOf(newValue.getID()));
                        book = bookList.getBook(newValue.getID());
                        bookSearchBox.positionCaret(bookSearchBox.getText().length());
                        PublisherLabel.setText(book.getPublisher());

                        PublishedDateLabel.setText(""+book.getPublishedDate());
                        Date today = new Date(new java.sql.Date(System.currentTimeMillis()));

                        BorrowedDateLabel.setText(today.toString());

                        LocalDate localDatePlus10Days = today.add(10).toLocalDate();
                        DueDatePicker.setValue(localDatePlus10Days);
                        QuantityLeftLabel.setText(""+book.getQuantity()+" quyển");

                        CreateBookSuggestions();
                        suggestionBook.getItems().clear();
                        suggestionBook.setVisible(false);
                        Pane2.requestFocus();
                        if (Pane2.getStyleClass().contains("newShape")) {
                            Pane2.getStyleClass().remove("newShape");
                        }

                        suggestionBook.setMinHeight(0);
                        suggestionBook.setMaxHeight(0);
                    }
                });
        secondPane.setDisable(true);
        secondPane.setVisible(false);
        tableView.setSelectionModel(null);
        pageNow = 1;
        left.setDisable(true);
        updateHistory(""+sortBox.getValue());


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
                                "-fx-font-family: 'HYWenHei-85W'; -fx-fill: #8e8e8e;-fx-font-size: 17px;");
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
                                "-fx-font-family: 'HYWenHei-85W'; -fx-fill: #8e8e8e;-fx-font-size: 17px;");
                        text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
                        setAlignment(javafx.geometry.Pos.CENTER); // Căn giữa nội dung trong ô
                        setGraphic(text);
                    }
                }
            };
        });
        tableView.setPrefHeight(5 * 55 + 51);
        tableView.setItems(FXCollections.observableArrayList(dataList.subList(0, Math.min(5,dataList.size()))));

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
                    if ( observableList.size() >= 5 ) break;
                    observableList.add(new SuggestionView(suggestion, 35, 230,
                            secondPane.getParent().getBlendMode()));
                }
                int lastIndex = suggestionUser.getItems().size() - 1;

                suggestionUser.setVisible(true);

                int heightOfListView = Math.min(suggestionUser.getItems().size(), 5) * 55;
                suggestionUser.setMinHeight(heightOfListView);
                suggestionUser.setMaxHeight(heightOfListView);
                if ( heightOfListView == 0 && Pane1.getStyleClass().contains("newShape") ) {
                    Pane1.getStyleClass().remove("newShape");
                }
                if (heightOfListView > 0 && !Pane1.getStyleClass().contains("newShape") ) {
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
        if ( Pane2.isFocused() ) prefixname=prefixname+"@";
        String prefixName = prefixname;
        if (prefixName.isEmpty()) {
            suggestionBook.getItems().clear();
            suggestionBook.setVisible(false);
            if (Pane2.getStyleClass().contains("newShape")) {
                Pane2.getStyleClass().remove("newShape");
            }
            suggestionBook.setMaxHeight(0);
            suggestionBook.setMinHeight(0);
            return;
        }
        Thread thread = new Thread(() -> {
            ArrayList<Suggestion> listSuggestions = Library.getInstance()
                    .getBookSuggestions(prefixName);
            ObservableList<SuggestionView> observableList = FXCollections.observableArrayList();

            Platform.runLater(() -> {
                suggestionBook.setItems(observableList);
                for (Suggestion suggestion : listSuggestions) {
                    if ( observableList.size() >= 5 ) break;
                    observableList.add(new SuggestionView(suggestion, 35, 230,
                            secondPane.getParent().getBlendMode()));
                }
                int lastIndex = suggestionBook.getItems().size() - 1;

                suggestionBook.setVisible(true);

                int heightOfListView = Math.min(suggestionBook.getItems().size(), 5) * 55;
                suggestionBook.setMinHeight(heightOfListView);
                suggestionBook.setMaxHeight(heightOfListView);
                if ( heightOfListView == 0 && Pane2.getStyleClass().contains("newShape") ) {
                    Pane2.getStyleClass().remove("newShape");
                }
                if (heightOfListView > 0 && !Pane2.getStyleClass().contains("newShape") && suggestionBook.isVisible()) {
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

    @Override
    public void applyDarkMode(boolean isDark) {

    }

    @Override
    public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
            boolean isTranslate) {

    }

    @Override
    public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

    }
}

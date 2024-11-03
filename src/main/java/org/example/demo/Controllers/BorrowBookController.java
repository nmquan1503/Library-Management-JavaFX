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
import java.util.Date;
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

import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.demo.CustomUI.SuggestionView;
import org.example.demo.Database.JDBC;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.BookShelf.BookShelf;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Trie.Trie;
import org.example.demo.Models.Users.User;
import org.example.demo.Models.Users.UserList;


public class BorrowBookController implements MainInfo {

    private ObservableList<TableData> dataList;
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

    private Trie userNameTrie;

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
    public void rightController() {
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
    public void leftController() {
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

    private ObservableList<SuggestionView> suggestions;
    private ObservableList<SuggestionView> suggestions1;

    @FXML
    public void muonSachController() {
        secondPane.setDisable(false);
        secondPane.setVisible(true);
        mainPane.setVisible(false);
        mainPane.setDisable(true);
    }

    public void addBox() {
        sortBox.getItems().addAll(
                "Hành Động Mượn Sách",
                "Hành Động Trả Sách",
                "Toàn Bộ Lịch Sử"
        );
        sortBox.setValue("Hành Động Mượn Sách");
    }

    @FXML
    public void searchButtonController() {

        String s = userIdBox.getText();
        int x = Integer.parseInt(s);
        User npc = userList.getUser(x);
        if (npc == null) {
            if ( wrongNotification.isVisible() ) {
                return;
            }
            wrongNotification.setVisible(true);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2),
                    wrongNotification);
            scaleTransition.setFromX(0);  // Bắt đầu từ kích thước 0 (nhỏ tí)
            scaleTransition.setFromY(0);
            scaleTransition.setToX(1);    // Kết thúc ở kích thước gốc
            scaleTransition.setToY(1);
            PauseTransition pause1 = new PauseTransition(Duration.seconds(2.0));
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
            userSearchBox.setText(""); // Đặt giá trị của TextField thành gợi ý đã chọn
            BirthdayLabel.setText("");
            PhoneLabel.setText("");
            EmailLabel.setText("");
            AddressLabel.setText("");
            isBanLabel.setText("");

        } else {
            userSearchBox.setText(npc.getName()); // Đặt giá trị của TextField thành gợi ý đã chọn

            userIdBox.setText(String.valueOf(x));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
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
    public void initialize() {
        addBox();
        setupTooltip();
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
                        User x = userList.getUser(newValue.getID());
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = formatter.format(x.getBirthday());
                        BirthdayLabel.setText(dateString);

                        PhoneLabel.setText(x.getPhoneNumber());
                        EmailLabel.setText(x.getEmail());
                        AddressLabel.setText(x.getAddress());
                        if (!x.isBan()) {
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
                        Book x = bookList.getBook(newValue.getID());
                        bookSearchBox.positionCaret(bookSearchBox.getText().length());
                        PublisherLabel.setText(x.getPublisher());
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = formatter.format(x.getPublishedDate());
                        PublishedDateLabel.setText(dateString);
                        Date today = new Date(System.currentTimeMillis());
                        dateString = formatter.format(today);
                        BorrowedDateLabel.setText(dateString);
                        Date datePlus10Days = new Date(today.getTime() + 10L * 24 * 60 * 60 * 1000);
                        LocalDate localDatePlus10Days = datePlus10Days.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        DueDatePicker.setValue(localDatePlus10Days);
                        QuantityLeftLabel.setText(""+x.getQuantity());

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
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String Today = now.format(formatter);
        dataList = FXCollections.observableArrayList(
                new TableData("Mượn", "THAO", "Harry Potter và Phòng Chứa Bí Mật", Today),
                new TableData("Trả", "THAO", "Harry Potter và Bảo Bối Tử Thần", Today),
                new TableData("Mượn", "THAO", "Harry Potter và tên tù nhân ngục Azkaban", Today),
                new TableData("Mượn", "THAO", "Harry Potter Va Bao Boi Tu Than", Today),
                new TableData("Mượn", "THAO", "Harry Potter và bảo bối tử thần", Today),
                new TableData("Trả", "THAO", "Harry Potter và phòng chứa bí mật", Today),
                new TableData("Mượn", "THAO", "Harry Potter & hội phượng hoàng", Today),
                new TableData("Mượn", "THAO", "Harry Potter and the Deathly Hallows", Today),
                new TableData("Mượn", "THAO", "Harry Potter and the Chamber of Secrets", Today),
                new TableData("Trả", "THAO", "\u200Fهيرى پوٹر اور رازوں کا کمره :\u200F", Today),
                new TableData("Trả", "THAO", "Marsupilami", Today),
                new TableData("Mượn", "THAO", "Doraemon", Today)

        );
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

        tableView.setPrefHeight(5 * 55 + 51);
        tableView.setItems(FXCollections.observableArrayList(dataList.subList(0, 5)));

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

package org.example.demo.Controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class BorrowBookController {

    private ObservableList<TableData> dataList;
    private int pageNow;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private ComboBox sortBox;

    @FXML
    private TableView<TableData> tableView; // Gán ID của TableView từ Scene Builder

    @FXML
    private TableColumn<TableData,String> typeColumn;

    @FXML
    private TableColumn<TableData,String> userColumn;

    @FXML
    private TableColumn<TableData,String> bookColumn;

    @FXML
    private TableColumn<TableData,String> timeColumn;

    @FXML
    private Button right;

    @FXML
    private Button left;

    @FXML
    private Label pageNumber;

    @FXML
    private AnchorPane secondPane;

    @FXML
    public void rightController() {
        pageNow++;
        left.setDisable(false);
        pageNumber.setText(String.valueOf(pageNow));
        int x = dataList.size();
        if ( x <= 5*pageNow ) {
            right.setDisable(true);
            right.setStyle(""); // Khôi phục màu mặc định nếu không đủ điều kiện
        }
        else x = 5*pageNow;
        tableView.setItems(FXCollections.observableArrayList(dataList.subList(5*(pageNow-1),x)));
    }

    @FXML
    public void leftController() {
        pageNow--;
        pageNumber.setText(String.valueOf(pageNow));
        right.setDisable(false);
        int x = 0;
        if ( pageNow == 1 ) {
            left.setDisable(true);
            left.setStyle(""); // Khôi phục màu mặc định nếu không đủ điều kiện
        }
        x = 5*pageNow;
        tableView.setItems(FXCollections.observableArrayList(dataList.subList(5*(pageNow-1),x)));
    }

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
    public void initialize() {
        secondPane.setDisable(true);
        secondPane.setVisible(false);
        tableView.setSelectionModel(null);
        pageNow=1;
        left.setDisable(true);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String Today = now.format(formatter);
        dataList = FXCollections.observableArrayList(
            new TableData("Mượn", "THAO", "Harry Potter và Phòng Chứa Bí Mật", Today ),
                new TableData("Trả", "THAO", "Harry Potter và Bảo Bối Tử Thần", Today ),
                new TableData("Mượn", "THAO", "Harry Potter và tên tù nhân ngục Azkaban", Today ),
                new TableData("Mượn", "THAO", "Harry Potter Va Bao Boi Tu Than", Today ),
                new TableData("Mượn", "THAO", "Harry Potter và bảo bối tử thần", Today ),
                new TableData("Trả", "THAO", "Harry Potter và phòng chứa bí mật", Today ),
                new TableData("Mượn", "THAO", "Harry Potter & hội phượng hoàng", Today ),
                new TableData("Mượn", "THAO", "Harry Potter and the Deathly Hallows", Today ),
                new TableData("Mượn", "THAO", "Harry Potter and the Chamber of Secrets", Today ),
                new TableData("Trả", "THAO", "\u200Fهيرى پوٹر اور رازوں کا کمره :\u200F", Today ),
                new TableData("Trả", "THAO", "Marsupilami", Today ),
                new TableData("Mượn", "THAO", "Doraemon", Today )


        );
        for ( TableData x: dataList) {
            String s = x.getBook();
            if ( s.length() >= 20 ) {
                String t="";
                boolean check = false;
                for ( int i = 0 ; i < s.length(); i++) {
                    t=t+s.charAt(i);
                    if ( i >= 20 && s.charAt(i) == ' ' && check == false ){
                        check=true;
                        t=t+"\n";
                    }
                }
                x.setBook(t);
            }
        }
        if ( dataList.size() <= 5 ) {
            right.setDisable(true);
        }
        typeColumn.setReorderable(false);
        userColumn.setReorderable(false);
        bookColumn.setReorderable(false);
        typeColumn.setCellValueFactory(new PropertyValueFactory<TableData,String>("action"));
        userColumn.setCellValueFactory(new PropertyValueFactory<TableData,String>("user"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<TableData,String>("book"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<TableData,String>("date"));

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
                        text.wrappingWidthProperty().bind(getTableColumn().widthProperty()); // Đặt wrappingWidth để tự động xuống dòng
                        text.setStyle("-fx-font-family: 'HYWenHei-85W'; -fx-fill: #8e8e8e;-fx-font-size: 17px;");
                        text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
                        setAlignment(javafx.geometry.Pos.CENTER); // Căn giữa nội dung trong ô
                        setGraphic(text);
                    }
                }
            };
        });

        tableView.setPrefHeight(5 * 55 + 51);
        tableView.setItems(FXCollections.observableArrayList(dataList.subList(0,5)));

    }
}

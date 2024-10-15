package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.mysql.cj.log.Log;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.demo.Database.JDBC;

public class StartController {
    private Stage stage;
    private Parent root;
    private Scene scene;


    @FXML
    private AnchorPane BackgroundPane ;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField accountField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private JFXButton hidePassword;

    @FXML
    private TextField passwordText;

    @FXML
    private FontAwesomeIconView eyeIcon;

    @FXML
    private JFXButton deleteAccount;

    @FXML
    private JFXButton deletePassword;

    @FXML
    private JFXButton LogIn;

    @FXML
    private ImageView imageViewAnimation;

    @FXML
    private Label wrongNotification;

    private Image[] images;
    private int currentImageIndex = 0;
    private Timeline timeline;
    private boolean forward = true;
    private long lastUpdate = 0; // Thời gian cập nhật lần cuối
    private long spacetimeInNano = (long) (50_000_000); // Chuyển đổi spacetime sang nano giây
    public void BackgroundRequest() {
        BackgroundPane.requestFocus();
    }
    public void mainRequest() {
        mainPane.requestFocus();
    }
    @FXML
    public void togglePasswordVisibility() {
        if (passwordField.isVisible()) {
            String s = passwordField.getText();
            if ( s != null ) passwordField.setVisible(false);
            passwordText.setText(s); // Đặt nội dung của TextField
            passwordText.setVisible(true); // Hiển thị TextField
            passwordField.setVisible(false);
        } else {
            String textFieldText = passwordText.getText();
            if ( textFieldText != null ) passwordField.setText(textFieldText); // Đặt nội dung cho PasswordField
            passwordField.setVisible(true); // Hiển thị PasswordField
            passwordText.setVisible(false); // Ẩn TextField
        }
        if (eyeIcon.getGlyphName().equals("EYE_SLASH")) {
            eyeIcon.setIcon(FontAwesomeIcon.EYE); // Đổi sang mắt mở
            passwordText.requestFocus();
            passwordText.positionCaret(passwordText.getText().length());
        } else {
            eyeIcon.setIcon(FontAwesomeIcon.EYE_SLASH); // Đổi sang mắt nhắm
            passwordField.requestFocus();
            passwordField.positionCaret(passwordField.getText().length());

        }
    }

    public void removeAccount() {
        accountField.setText("");
        accountField.requestFocus();
    }

    public void removePassword() {
        passwordField.setText("");
        passwordText.setText("");
        if (eyeIcon.getGlyphName().equals("EYE") ) {
            passwordText.requestFocus();
        }
        else {
            passwordField.requestFocus();
        }
    }
    public void updateLogin() {
        if (!accountField.getText().isEmpty() && (!passwordField.getText().isEmpty()) && !passwordText.getText().isEmpty()) {
            LogIn.setStyle("-fx-background-color: rgba(250, 110, 150, 1);-fx-text-fill: white;"); // Đổi màu khi cả hai trường đều có chữ
            LogIn.setDisable(false);

        } else {

            LogIn.setRipplerFill(null);
            LogIn.setStyle(""); // Khôi phục màu mặc định nếu không đủ điều kiện
        }
    }
    private void updateImage() {
        images = new Image[]{
                new Image(getClass().getResource("/images/openeye.png").toExternalForm()),
                new Image(getClass().getResource("/images/hideeye1.png").toExternalForm()),
                new Image(getClass().getResource("/images/hideeye2.png").toExternalForm()),
                new Image(getClass().getResource("/images/hideeye3.png").toExternalForm()),
                new Image(getClass().getResource("/images/hideeye4.png").toExternalForm()),
                new Image(getClass().getResource("/images/hideeye5.png").toExternalForm())
        };
    }
    private void setdefaultImage() {
        images = new Image[]{
                new Image(getClass().getResource("/images/openeye.png").toExternalForm()),
                new Image(getClass().getResource("/images/closeeye.png").toExternalForm()),
                new Image(getClass().getResource("/images/halfeye.png").toExternalForm()),
                new Image(getClass().getResource("/images/openeye.png").toExternalForm())
        };
    }
    private void setTihiImage(boolean level) {
        if ( level == true ) {
            images = new Image[]{
                    new Image(getClass().getResource("/images/openeye.png").toExternalForm()),
                    new Image(getClass().getResource("/images/hideeye1.png").toExternalForm()),
                    new Image(getClass().getResource("/images/hideeye2.png").toExternalForm()),
                    new Image(getClass().getResource("/images/hideeye3.png").toExternalForm()),
                    new Image(getClass().getResource("/images/hideeye4.png").toExternalForm()),
                    new Image(getClass().getResource("/images/hideeye5.png").toExternalForm()),
                    new Image(getClass().getResource("/images/tihi1.png").toExternalForm()),
                    new Image(getClass().getResource("/images/tihi2.png").toExternalForm()),
                    new Image(getClass().getResource("/images/tihi3.png").toExternalForm()),
                    new Image(getClass().getResource("/images/tihi4.png").toExternalForm()),
            };
        }
        else {
            images = new Image[]{
                    new Image(getClass().getResource("/images/hideeye5.png").toExternalForm()),
                    new Image(getClass().getResource("/images/tihi1.png").toExternalForm()),
                    new Image(getClass().getResource("/images/tihi2.png").toExternalForm()),
                    new Image(getClass().getResource("/images/tihi3.png").toExternalForm()),
                    new Image(getClass().getResource("/images/tihi4.png").toExternalForm()),
            };
        }
    }

    public void LogInController(ActionEvent event) throws IOException {
        String tk = accountField.getText();
        String mk = "";
        if ( eyeIcon.getGlyphName().equals("EYE") ) {
            mk=passwordText.getText();
        }
        else mk=passwordField.getText();
        boolean check = false;
        String query = "SELECT * FROM librarian WHERE username_account = ? AND password_account = ?";
        try (Connection connection = JDBC.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set giá trị cho các tham số trong câu truy vấn
            preparedStatement.setString(1, tk);
            preparedStatement.setString(2, mk);

            // Thực thi truy vấn
            ResultSet resultSet = preparedStatement.executeQuery();

            // Nếu resultSet có dòng dữ liệu, nghĩa là tài khoản và mật khẩu đúng
            if (resultSet.next()) check=true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        if ( check == false ) {
            wrongNotification.setVisible(true);
        }
        else {
            wrongNotification.setVisible(false);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/FXML/Home.fxml"));
                root = loader.load();

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                scene = new Scene(root);

                stage.setScene(scene);
                stage.show();
            } catch (IOException e ) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    public void initialize() {
        spacetimeInNano=50_000_000;
        setdefaultImage();
        wrongNotification.setVisible(false);
        deletePassword.setVisible(false); // Ẩn icon ban đầu
        passwordText.setVisible(false);
        passwordField.setVisible(true);
        LogIn.setOnMouseEntered(event -> {
            if (!accountField.getText().isEmpty() && (!passwordField.getText().isEmpty()) && !passwordText.getText().isEmpty()) {
                LogIn.setCursor(Cursor.HAND);
                LogIn.setDisable(false);
            }
            else {
                LogIn.setDisable(true);
            }


        });

        // Thay đổi con trỏ khi nhấn vào button
        LogIn.setOnMousePressed(event -> {
            if (!accountField.getText().isEmpty() && (!passwordField.getText().isEmpty()) && !passwordText.getText().isEmpty()) {
                LogIn.setCursor(Cursor.HAND);
                LogIn.setDisable(false);
            }
            else {
                LogIn.setDisable(true);

            }
        });

        // Khi nhả chuột thì giữ lại hiệu ứng bàn tay khi hover
        LogIn.setOnMouseReleased(event -> {
            if (!accountField.getText().isEmpty() && (!passwordField.getText().isEmpty()) && !passwordText.getText().isEmpty()) {
                LogIn.setCursor(Cursor.HAND);
                LogIn.setDisable(false);
            }
            else {
                LogIn.setDisable(true);

            }
        });
        // Listener để theo dõi sự thay đổi trong passwordField
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            deletePassword.setVisible(!newValue.isEmpty()); // Hiện icon nếu có chữ
            passwordText.setText(passwordField.getText());
            updateLogin();

        });
        passwordText.textProperty().addListener((observable, oldValue, newValue) -> {
            deletePassword.setVisible(!newValue.isEmpty()); // Hiện icon nếu có chữ
            passwordField.setText(passwordText.getText());
            updateLogin();
        });

        deleteAccount.setVisible(false);
        accountField.textProperty().addListener((observable, oldValue, newValue) -> {
            deleteAccount.setVisible(!newValue.isEmpty()); // Hiện icon nếu có chữ
            updateLogin();
        });
        imageViewAnimation.setImage(images[0]);

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= spacetimeInNano) {
                    lastUpdate = now; // Cập nhật thời gian lần cuối

                    if (passwordField.isFocused() || passwordText.isFocused() || hidePassword.isPressed()) {
                        if ( (hidePassword.isPressed() || deletePassword.isPressed()) && images.length == 4 && eyeIcon.getGlyphName().equals("EYE_SLASH")) {
                            setTihiImage(true);
                            currentImageIndex=0;
                            spacetimeInNano = 25_000_000;
                        }
                        else if ( passwordText.isPressed() && images.length == 4 && eyeIcon.getGlyphName().equals("EYE")) {
                            setTihiImage(true);
                            currentImageIndex=0;
                            spacetimeInNano = 25_000_000;
                        }
                        else if ( hidePassword.isPressed() && images.length == 4) {
                            updateImage();
                            currentImageIndex=0;
                            spacetimeInNano = 25_000_000;
                            return;
                        }
                        else if ( hidePassword.isPressed() ) return;
                        if ( images.length == 10 ) {
                            if (currentImageIndex < images.length ) {

                                imageViewAnimation.setImage(images[currentImageIndex]);
                                currentImageIndex++;
                                return;
                            }
                        }

                        if ( hidePassword.isPressed() && images.length == 5 ) {
                            spacetimeInNano = 25_000_000;
                            if ( currentImageIndex > 0 ) currentImageIndex--;
                            imageViewAnimation.setImage(images[currentImageIndex]);
                            return;
                        }
                        if ( passwordField.isFocused() ) {
                            spacetimeInNano = 25_000_000;

                            if (images.length != 6) {

                                if (images.length == 5 ) {
                                    currentImageIndex=5;

                                }
                                else currentImageIndex=0;
                                updateImage();
                                imageViewAnimation.setImage(images[currentImageIndex]);
                                return;
                            }

                            if (currentImageIndex < images.length - 1) {
                                spacetimeInNano = 25_000_000;
                                currentImageIndex++;
                                imageViewAnimation.setImage(images[currentImageIndex]);
                                //System.out.println("YEs");
                            }
                        }
                        else {
                            spacetimeInNano = 25_000_000;
                            if (images.length != 5 ) {
                                if ( images.length == 10 ) currentImageIndex=4;
                                else currentImageIndex=0;
                                setTihiImage(images.length == 4);
                                imageViewAnimation.setImage(images[currentImageIndex]);
                                return;
                            }

                            if (currentImageIndex < images.length - 1) {
                                currentImageIndex++;
                                imageViewAnimation.setImage(images[currentImageIndex]);

                            }
                        }
                    } else {

                        if ( (images == null) || (images.length != 4 && currentImageIndex == 0)  ) {
                            setdefaultImage();
                            spacetimeInNano = 50_000_000;
                        }
                        else if ( images.length != 4 && currentImageIndex != 0 ) {
                            if ( images.length == 5 ) {
                                setTihiImage(true);
                                currentImageIndex=10;
                            }
                            currentImageIndex--;
                            imageViewAnimation.setImage(images[currentImageIndex]);
                            return;

                        }
                        if (currentImageIndex >= images.length - 1) {
                            currentImageIndex++;
                            if (currentImageIndex >= 50) currentImageIndex = 0;
                        } else {
                            currentImageIndex = (currentImageIndex + 1) % images.length;
                            imageViewAnimation.setImage(images[currentImageIndex]);
                        }
                    }
                }
            }
        };
        animationTimer.start(); // Bắt đầu animation
    }


}

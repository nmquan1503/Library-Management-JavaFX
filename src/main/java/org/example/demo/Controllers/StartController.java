package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.prefs.Preferences;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.StackPane;

import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.demo.App;
import org.example.demo.Database.JDBC;

public class StartController {

  private Stage stage;
  private Parent root;
  private Scene scene;
  private Scene originalScene; // Để lưu lại scene ban đầu

  @FXML
  private AnchorPane BackgroundPane;

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

  @FXML
  private JFXCheckBox remember;

  @FXML
  private StackPane loadingPane;

  @FXML
  private ProgressIndicator loadingIndicator;
  private Preferences prefs;

  private static int id;

  public StartController() {
    prefs = Preferences.userNodeForPackage(StartController.class);
  }

  private static final String PREF_ACCOUNT = "account";
  private static final String PREF_PASSWORD = "password";
  private static final String PREF_REMEMBER_ME = "rememberMe";

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
      if (s != null) {
        passwordField.setVisible(false);
      }
      passwordText.setText(s); // Đặt nội dung của TextField
      passwordText.setVisible(true); // Hiển thị TextField
      passwordField.setVisible(false);
    } else {
      String textFieldText = passwordText.getText();
      if (textFieldText != null) {
        passwordField.setText(textFieldText); // Đặt nội dung cho PasswordField
      }
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
    if (eyeIcon.getGlyphName().equals("EYE")) {
      passwordText.requestFocus();
    } else {
      passwordField.requestFocus();
    }
  }

  public void updateLogin() {
    if (!accountField.getText().isEmpty() && (!passwordField.getText().isEmpty())
        && !passwordText.getText().isEmpty()) {
      LogIn.setStyle(
          "-fx-background-color: rgba(250, 110, 150, 1);-fx-text-fill: white;"); // Đổi màu khi cả hai trường đều có chữ
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
    if (level == true) {
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
    } else {
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
    String mk =
        eyeIcon.getGlyphName().equals("EYE") ? passwordText.getText() : passwordField.getText();
    boolean check = false;

    // Tạo một loading indicator
    loadingPane.setVisible(true);

    loadingIndicator.setVisible(true);
    loadingIndicator.setProgress(-1.0); // Chỉ định trạng thái loading

    //loadingPane.getChildren().add(loadingIndicator);
    loadingPane.setAlignment(Pos.CENTER); // Căn giữa
    LogIn.setRipplerFill(null);
    LogIn.setDisable(true);
    LogIn.setStyle(""); // Khôi phục màu mặc định nếu không đủ điều kiện;
    Platform.runLater(() -> {
      BackgroundPane.requestFocus(); // Đảm bảo BackgroundPane nhận focus
    });
    long startTime = System.currentTimeMillis(); // Lưu thời gian bắt đầu
    Task<Boolean> loginTask = new Task<>() {
      @Override
      protected Boolean call() throws Exception {
        String query = "SELECT id_librarian FROM librarian WHERE username_account = ? AND password_account = ?";

        try (Connection connection = JDBC.getConnection()) {
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          preparedStatement.setString(1, tk);
          preparedStatement.setString(2, mk);

          // Thực thi truy vấn
          ResultSet resultSet = preparedStatement.executeQuery();

          // Kiểm tra tài khoản
          if (resultSet.next()) {
            id = resultSet.getInt("id_librarian");
            JDBC.closeConnection(connection);
            return true; // Tài khoản hợp lệ
          }
          JDBC.closeConnection(connection);
        } catch (Exception e) {

          e.printStackTrace();
        }

        return false; // Tài khoản không hợp lệ
      }

      @Override
      protected void succeeded() {
        super.succeeded();
        boolean check = getValue();
        LogIn.setRipplerFill(null);
        LogIn.setDisable(true);
        LogIn.setStyle(""); // Khôi phục màu mặc định nếu không đủ điều kiện;

        // Ẩn loading indicator
        long endTime = System.currentTimeMillis(); // Lưu thời gian kết thúc
        long elapsedTime = endTime - startTime; // Tính thời gian thực hiện
        long minLoadTime = 1300; // Thời gian tối thiểu (1 giây)

        // Dùng PauseTransition để đảm bảo thời gian chờ tối thiểu
        PauseTransition pause = new PauseTransition(
            Duration.millis(Math.max(elapsedTime, minLoadTime)));
        pause.setOnFinished(e -> {
          loadingPane.setVisible(false);
          updateLogin();
          if (!check) {
            wrongNotification.setVisible(true);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2),
                wrongNotification);
            scaleTransition.setFromX(0);  // Bắt đầu từ kích thước 0 (nhỏ tí)
            scaleTransition.setFromY(0);
            scaleTransition.setToX(1);    // Kết thúc ở kích thước gốc
            scaleTransition.setToY(1);
            PauseTransition pause1 = new PauseTransition(Duration.seconds(1.0));
            ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2),
                wrongNotification);
            scaleDown.setFromX(1);  // Bắt đầu từ kích thước gốc
            scaleDown.setFromY(1);
            scaleDown.setToX(0);    // Thu nhỏ lại về kích thước 0
            scaleDown.setToY(0);

            // Tạo SequentialTransition để nối hai animation lại với nhau
            SequentialTransition sequentialTransition = new SequentialTransition(scaleTransition,
                pause1, scaleDown);
            sequentialTransition.play();


          } else {
            wrongNotification.setVisible(false);
            // Lưu trữ tài khoản nếu có
            if (remember.isSelected()) {
              prefs.put(PREF_ACCOUNT, tk);
              prefs.put(PREF_PASSWORD, mk);
              prefs.putBoolean(PREF_REMEMBER_ME, true);
            } else {
              prefs.put(PREF_ACCOUNT, "");
              prefs.put(PREF_PASSWORD, "");
              prefs.putBoolean(PREF_REMEMBER_ME, false);
            }
            loadHomeScene(event);
          }

          //loadingPane.getChildren().clear();
        });
        pause.play();

      }
    };

    Thread loginThread = new Thread(loginTask);
    loginThread.setDaemon(true);
    loginThread.start();
  }

  private void loadHomeScene(ActionEvent event) {
//    try {
//      FXMLLoader loader = new FXMLLoader(
//          getClass().getResource("/org/example/demo/FXML/Base.fxml"));
//      root = loader.load();
//      Scene mainScene = new Scene(root);
//
//      ((Node) event.getSource()).getScene().getWindow().hide();
//      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//      stage.setScene(mainScene);
//      stage.show();
//    } catch (IOException e) {
//      e.printStackTrace();
//
//    }
    if (StartController.getID() != BaseController.getLibId()) {
      BaseController.setLibId(StartController.getID());
    }

    if (BaseController.getBookPane() != null) {
      BaseController.getBookPane().setVisible(false);
    }

    if (BaseController.getBorrowPane() != null) {
      BaseController.getBorrowPane().setVisible(false);
    }

    if (BaseController.getEditPane() != null) {
      BaseController.getEditPane().setVisible(false);
    }

    if (BaseController.getReturnPane() != null) {
      BaseController.getReturnPane().setVisible(false);
    }

    if (BaseController.getUserPane() != null) {
      BaseController.getUserPane().setVisible(false);
    }

    if (BaseController.getMainPane() != null) {
      BaseController.getMainPane().setVisible(true);
    }

    App.primaryStage.setScene(App.baseScene);
    App.primaryStage.show();
  }

  @FXML
  public void initialize() {

    spacetimeInNano = 50_000_000;
    setdefaultImage();
    loadingIndicator.setVisible(false);
    wrongNotification.setVisible(false);
    loadingPane.setVisible(false);
    deletePassword.setVisible(false); // Ẩn icon ban đầu
    passwordText.setVisible(false);
    passwordField.setVisible(true);
    deleteAccount.setVisible(false);
    boolean rememberMe = prefs.getBoolean(PREF_REMEMBER_ME, false);
    remember.setSelected(rememberMe);

    if (rememberMe) {
      String savedAccount = prefs.get(PREF_ACCOUNT, "");
      String savedPassword = prefs.get(PREF_PASSWORD, "");
      accountField.setText(savedAccount);
      passwordField.setText(savedPassword);
      passwordText.setText(savedPassword);
      deletePassword.setVisible(true);
      deleteAccount.setVisible(true);
      updateLogin();
    }
    Platform.runLater(() -> {
      BackgroundPane.requestFocus(); // Đảm bảo BackgroundPane nhận focus
    });

    LogIn.setOnMouseEntered(event -> {
      if (!LogIn.isDisable()) { // Kiểm tra nếu nút Log In không bị vô hiệu hóa
        if (!accountField.getText().isEmpty() && (!passwordField.getText().isEmpty())
            && !passwordText.getText().isEmpty()) {
          LogIn.setCursor(Cursor.HAND);
          LogIn.setDisable(false);
        } else {
          LogIn.setDisable(true);
        }
      }
    });

    LogIn.setOnMousePressed(event -> {
      if (!LogIn.isDisable()) { // Kiểm tra nếu nút Log In không bị vô hiệu hóa
        if (!accountField.getText().isEmpty() && (!passwordField.getText().isEmpty())
            && !passwordText.getText().isEmpty()) {
          LogIn.setCursor(Cursor.HAND);
          LogIn.setDisable(false);
        } else {
          LogIn.setDisable(true);
        }
      }
    });

    // Khi nhả chuột thì giữ lại hiệu ứng bàn tay khi hover
    LogIn.setOnMouseReleased(event -> {
      if (!LogIn.isDisable()) {
        if (!accountField.getText().isEmpty() && (!passwordField.getText().isEmpty())
            && !passwordText.getText().isEmpty()) {
          LogIn.setCursor(Cursor.HAND);
          LogIn.setDisable(false);
        } else {
          LogIn.setDisable(true);

        }
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
            if ((hidePassword.isPressed() || deletePassword.isPressed()) && images.length == 4
                && eyeIcon.getGlyphName().equals("EYE_SLASH")) {
              setTihiImage(true);
              currentImageIndex = 0;
              spacetimeInNano = 25_000_000;
            } else if (passwordText.isPressed() && images.length == 4 && eyeIcon.getGlyphName()
                .equals("EYE")) {
              setTihiImage(true);
              currentImageIndex = 0;
              spacetimeInNano = 25_000_000;
            } else if (hidePassword.isPressed() && images.length == 4) {
              updateImage();
              currentImageIndex = 0;
              spacetimeInNano = 25_000_000;
              return;
            } else if (hidePassword.isPressed()) {
              return;
            }
            if (images.length == 10) {
              if (currentImageIndex < images.length) {

                imageViewAnimation.setImage(images[currentImageIndex]);
                currentImageIndex++;
                return;
              }
            }

            if (hidePassword.isPressed() && images.length == 5) {
              spacetimeInNano = 25_000_000;
              if (currentImageIndex > 0) {
                currentImageIndex--;
              }
              imageViewAnimation.setImage(images[currentImageIndex]);
              return;
            }
            if (passwordField.isFocused()) {
              spacetimeInNano = 25_000_000;

              if (images.length != 6) {

                if (images.length == 5) {
                  currentImageIndex = 5;

                } else {
                  currentImageIndex = 0;
                }
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
            } else {
              spacetimeInNano = 25_000_000;
              if (images.length != 5) {
                if (images.length == 10) {
                  currentImageIndex = 4;
                } else {
                  currentImageIndex = 0;
                }
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

            if ((images == null) || (images.length != 4 && currentImageIndex == 0)) {
              setdefaultImage();
              spacetimeInNano = 50_000_000;
            } else if (images.length != 4 && currentImageIndex != 0) {
              if (images.length == 5) {
                setTihiImage(true);
                currentImageIndex = 10;
              }
              currentImageIndex--;
              imageViewAnimation.setImage(images[currentImageIndex]);
              return;

            }
            if (currentImageIndex >= images.length - 1) {
              currentImageIndex++;
              if (currentImageIndex >= 50) {
                currentImageIndex = 0;
              }
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

  public static int getID() {     // lấy id người quản lý
    return id;
  }
}

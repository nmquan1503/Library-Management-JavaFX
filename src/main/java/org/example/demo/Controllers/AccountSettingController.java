package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.demo.App;
import org.example.demo.Database.JDBC;

public class AccountSettingController {

  /**
   * oke.
   */
  private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

  /**
   * oke.
   */
  private static final IntegerProperty checkState = new SimpleIntegerProperty(0);

  /**
   * oke.
   */
  public static int getAccState() {
    return checkState.get();
  }

  /**
   * oke.
   */
  public static void setAccState(int val) {
    checkState.set(val);
  }

  @FXML
  private Circle avatarCircle;

  @FXML
  private FontAwesomeIconView cameraIcon;

  @FXML
  private Arc chooseAvtFileBtn;

  @FXML
  private TextField promptName;

  @FXML
  private DatePicker promptBirth;

  @FXML
  private TextField promptPhone;

  @FXML
  private TextField promptEmail;

  @FXML
  private ComboBox<String> promptAddress;

  @FXML
  private PasswordField promptPassword;

  @FXML
  private TextField textField;

  @FXML
  private FontAwesomeIconView eyeIcon;

  @FXML
  private HBox passwordContainer;

  @FXML
  private AnchorPane succeedPane;

  @FXML
  private AnchorPane errorPane;

  private Timeline fadeOutTimeline;

  /**
   * oke.
   */
  public void initialize() {
    loadInfo();
    loadAddress();
    setCameraHoverEffect();
    chooseAvtFileBtn.setOnMouseClicked(this::handleChooseAvatarClick);
    cameraIcon.setOnMouseClicked(this::handleChooseAvatarClick);
    checkState.addListener((observable, oldValue, newValue) -> {
      loadInfo();
    });
    promptEmail.textProperty().addListener((observableValue, oldValue, newValue) -> {
      if (!newValue.matches(EMAIL_REGEX) && !newValue.isEmpty()) {
        promptEmail.setStyle("-fx-border-color: red;");
      } else {
        promptEmail.setStyle("");
      }
    });

    promptPassword.textProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue.contains(" ")) {
        passwordContainer.setStyle("-fx-border-color: red;");
      } else {
        passwordContainer.setStyle("");
      }
    });

    promptEmail.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue && promptEmail.getText().isEmpty()) {
        promptEmail.setStyle("");
      }
    });

    promptPassword.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue && promptPassword.getText().isEmpty()) {
        passwordContainer.setStyle("");
      }
    });
  }

  /**
   * oke.
   */
  public void updateAvatar(String path, int id) {
    try (Connection conn = JDBC.getConnection()) {
      String sql = "UPDATE librarian SET avatar = ? WHERE id_librarian = ?";
      assert conn != null;
      try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

        FileInputStream avt = new FileInputStream(path);
        preparedStatement.setBinaryStream(1, avt, avt.available());
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();

        JDBC.closeConnection(conn);
      }
    } catch (Exception e) {
      System.err.println(
          "Some error occurred in updateAvatar function in AccountSettingController class!");
    }
  }

  /**
   * oke.
   */
  private void loadDefaultAvatar() {
    try {
      Image defaultImage = new Image(
          Objects.requireNonNull(
              getClass().getResourceAsStream(
                  "/org/example/demo/Assets/default_avatar.jpg"))
      );
      avatarCircle.setFill(new ImagePattern(defaultImage));
    } catch (Exception e) {
      System.out.println("Default image loading failed: " + e.getMessage());
    }
  }

  /**
   * oke.
   */
  private void loadInfo() {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    try {
      conn = JDBC.getConnection();

      String sql =
          "SELECT li.avatar, li.birthday, li.phone_number_librarian, li.email_librarian, a.name_address "
              +
              "FROM librarian li " +
              "LEFT JOIN address a ON li.id_address = a.id_address " +
              "WHERE li.id_librarian = ?";
      assert conn != null;
      preparedStatement = conn.prepareStatement(sql);
      preparedStatement.setInt(1, BaseController.getLibId());

      rs = preparedStatement.executeQuery();

      if (rs.next()) {
        Blob avatarBlob = rs.getBlob("avatar");
        if (avatarBlob != null) {
          try (InputStream avatarStream = avatarBlob.getBinaryStream()) {
            Image avatarImage = new Image(avatarStream);
            avatarCircle.setFill(new ImagePattern(avatarImage));
          } catch (Exception e) {
            System.out.println("Failed to load avatar image: " + e.getMessage());
            loadDefaultAvatar();
          }
        } else {
          loadDefaultAvatar();
        }

        promptName.setText("");
        promptPassword.setText("");
        promptEmail.setText("");

        LocalDate date = rs.getDate("birthday").toLocalDate();
        promptBirth.setValue(date);

        String phoneNum = rs.getString("phone_number_librarian");
        promptPhone.setText(phoneNum);

        if (rs.getString("email_librarian") != null) {
          promptEmail.setPromptText(rs.getString("email_librarian"));
        } else {
          promptEmail.setPromptText("e.g: example@domain.com");
        }

        if (rs.getString("name_address") != null) {
          promptAddress.setValue(rs.getString("name_address"));
        } else {
          promptAddress.setValue("");
          promptAddress.setPromptText("Choose an address");
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

  /**
   * oke.
   */
  private void handleChooseAvatarClick(MouseEvent event) {

    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Avatar Image");
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png",
        "*.jpg");
    fileChooser.getExtensionFilters().add(extFilter);

    Stage stage = (Stage) chooseAvtFileBtn.getScene().getWindow();
    File selectedFile = fileChooser.showOpenDialog(stage);

    if (selectedFile != null) {
      updateAvatar(selectedFile.getAbsolutePath(), BaseController.getLibId());
      loadInfo();
      succeedNotification();
    }
  }

  /**
   * oke.
   */
  public void resetDefaultAvt() {
    updateAvatar(Objects.requireNonNull(
            getClass().getResource("/org/example/demo/Assets/default_avatar.jpg")).getPath(),
        BaseController.getLibId());
    loadDefaultAvatar();
    succeedNotification();
  }

  /**
   * oke.
   */
  private void setCameraHoverEffect() {
    cameraIcon.setOnMouseEntered(event -> {
      onHoverArc(true);
    });

    cameraIcon.setOnMouseExited(event -> {
      onHoverArc(false);
    });

    chooseAvtFileBtn.setOnMouseEntered(event -> {
      onHoverArc(true);
    });

    chooseAvtFileBtn.setOnMouseExited(event -> {
      onHoverArc(false);
    });
  }

  /**
   * oke.
   */
  private void onHoverArc(boolean isHovered) {
    if (isHovered) {
      chooseAvtFileBtn.setFill(Color.rgb(105, 105, 105, 0.7));
      cameraIcon.setOpacity(0.7);
    } else {
      chooseAvtFileBtn.setFill(Color.rgb(128, 128, 128, 0.6));
      cameraIcon.setOpacity(0.6);
    }
  }

  /**
   * oke.
   */
  @FXML
  private void saveName() {
    String newName = promptName.getText().trim();
    if (!newName.isEmpty()) {
      try (Connection conn = JDBC.getConnection()) {
        String sql = "UPDATE librarian SET name_librarian = ? WHERE id_librarian = ?";
        assert conn != null;
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
          preparedStatement.setString(1, newName);
          preparedStatement.setInt(2, BaseController.getLibId());
          int rowsAffected = preparedStatement.executeUpdate();

          if (rowsAffected > 0) {
            succeedNotification();
            promptName.clear();
          }
          succeedNotification();
          JDBC.closeConnection(conn);
        }
      } catch (Exception e) {
        System.err.println(
            "Some error occurred in saveName function in AccountSettingController class!");
      }
    } else {
      errorNotification();
    }
  }

  /**
   * oke.
   */
  @FXML
  private void saveBirthday() {
    LocalDate birthday = promptBirth.getValue();
    if (birthday != null) {
      try (Connection conn = JDBC.getConnection()) {
        String sql = "UPDATE librarian SET birthday = ? WHERE id_librarian = ?";
        assert conn != null;
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
          preparedStatement.setDate(1, Date.valueOf(birthday));
          preparedStatement.setInt(2, BaseController.getLibId());
          int rowsAffected = preparedStatement.executeUpdate();

          if (rowsAffected > 0) {
            succeedNotification();
            promptBirth.setValue(birthday);
          }
          succeedNotification();
          JDBC.closeConnection(conn);
        }
      } catch (Exception e) {
        System.err.println(
            "Some error occurred in saveBirthday function in AccountSettingController class!");
      }
    } else {
      errorNotification();
    }
  }

  /**
   * oke.
   */
  @FXML
  private void saveEmail() {
    String email = promptEmail.getText().trim();
    if (!email.isEmpty() && email.matches(EMAIL_REGEX)) {
      try (Connection conn = JDBC.getConnection()) {
        String sql = "UPDATE librarian SET email_librarian = ? WHERE id_librarian = ?";
        assert conn != null;
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
          preparedStatement.setString(1, email);
          preparedStatement.setInt(2, BaseController.getLibId());
          int rowsAffected = preparedStatement.executeUpdate();

          if (rowsAffected > 0) {
            promptEmail.setPromptText(email);
            succeedNotification();
          }
          succeedNotification();
          JDBC.closeConnection(conn);
        }
      } catch (Exception e) {
        System.err.println(
            "Some error occurred in saveName function in AccountSettingController class!");
      }
    } else {
      errorNotification();
    }
  }

  /**
   * oke.
   */
  @FXML
  private void saveAddress() {
    String selectedAddress = promptAddress.getValue();

    if (selectedAddress != null) {
      try (Connection conn = JDBC.getConnection()) {
        assert conn != null;

        String sql = "UPDATE librarian SET id_address = (SELECT id_address FROM address WHERE name_address = ?) WHERE id_librarian = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
          preparedStatement.setString(1, selectedAddress);
          preparedStatement.setInt(2,
              BaseController.getLibId());

          int rowsAffected = preparedStatement.executeUpdate();

          if (rowsAffected > 0) {
            promptAddress.setPromptText(selectedAddress);
            succeedNotification();
          } else {
            errorNotification();
          }
        }
      } catch (Exception e) {
        System.err.println("Error saving address: " + e.getMessage());
      }
    } else {
      errorNotification();
    }
  }

  /**
   * oke.
   */
  @FXML
  private void eyeSee() {
    if (eyeIcon.getGlyphName().equals("EYE")) {
      promptPassword.setVisible(false);
      textField.setText(promptPassword.getText());
      textField.setVisible(true);
      eyeIcon.setGlyphName("EYE_SLASH");
    } else {
      textField.setVisible(false);
      promptPassword.setText(textField.getText());
      promptPassword.setVisible(true);
      eyeIcon.setGlyphName("EYE");
    }
  }

  /**
   * oke.
   */
  @FXML
  private void savePassword() {
    Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Xác nhận đổi mật khẩu");
    confirmationAlert.setHeaderText("Bạn chắc chứ");
    confirmationAlert.setContentText("Điều này sẽ thay đổi mật khẩu của bạn trong hệ thống.");

    Optional<ButtonType> result = confirmationAlert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
      try {
        String newPassword = promptPassword.getText();
        if (!textField.getText().trim().isEmpty()) {
          newPassword = textField.getText();
        }
        if (!newPassword.isEmpty() && !newPassword.contains(" ")) {
          try (Connection conn = JDBC.getConnection()) {
            String sql = "UPDATE librarian SET password_account = ? WHERE id_librarian = ?";
            assert conn != null;
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
              preparedStatement.setString(1, newPassword);
              preparedStatement.setInt(2, BaseController.getLibId());
              preparedStatement.executeUpdate();
              textField.clear();
              promptPassword.clear();
              succeedNotification();
            }
          }
        } else {
          errorNotification();
        }
      } catch (Exception e) {
        System.err.println("Error saving password: " + e.getMessage());
      }
    } else {
      System.out.println("Password save action was canceled by the user.");
    }
  }

  @FXML
  private JFXButton btnName;

  @FXML
  private JFXButton btnBirth;

  @FXML
  private JFXButton btnEmail;

  @FXML
  private JFXButton btnAddr;

  @FXML
  private JFXButton btnPass;

  @FXML
  private JFXButton btnReset;

  /**
   * oke.
   */
  private void btnDisable() {
    btnName.setDisable(true);
    btnBirth.setDisable(true);
    btnEmail.setDisable(true);
    btnAddr.setDisable(true);
    btnPass.setDisable(true);
    btnReset.setDisable(true);
  }

  /**
   * oke.
   */
  private void btnEnable() {
    btnName.setDisable(false);
    btnBirth.setDisable(false);
    btnEmail.setDisable(false);
    btnAddr.setDisable(false);
    btnPass.setDisable(false);
    btnReset.setDisable(false);
  }

  /**
   * oke.
   */
  private void succeedNotification() {
    succeedPane.setVisible(true);
    succeedPane.setOpacity(1.0);

    btnDisable();

    if (fadeOutTimeline != null && fadeOutTimeline.getStatus() == Timeline.Status.RUNNING) {
      fadeOutTimeline.stop();
    }

    fadeOutTimeline = new Timeline(
        new KeyFrame(Duration.seconds(1),
            new KeyValue(succeedPane.opacityProperty(), 1.0)
        ),
        new KeyFrame(Duration.seconds(2),
            new KeyValue(succeedPane.opacityProperty(), 0)
        )
    );

    fadeOutTimeline.setOnFinished(event -> {
      succeedPane.setVisible(false);
      fadeOutTimeline.stop();
      btnEnable();
    });

    fadeOutTimeline.play();
  }

  /**
   * oke.
   */
  private void errorNotification() {
    errorPane.setVisible(true);
    errorPane.setOpacity(1.0);

    btnDisable();

    if (fadeOutTimeline != null && fadeOutTimeline.getStatus() == Timeline.Status.RUNNING) {
      fadeOutTimeline.stop();
    }

    fadeOutTimeline = new Timeline(
        new KeyFrame(Duration.seconds(1),
            new KeyValue(errorPane.opacityProperty(), 1.0)
        ),
        new KeyFrame(Duration.seconds(2),
            new KeyValue(errorPane.opacityProperty(), 0)
        )
    );

    fadeOutTimeline.setOnFinished(event -> {
      errorPane.setVisible(false);
      fadeOutTimeline.stop();
      btnEnable();
    });

    fadeOutTimeline.play();
  }

  /**
   * oke.
   */
  private void loadAddress() {
    List<String> addresses = new ArrayList<>();

    try (Connection conn = JDBC.getConnection()) {
      String sql = "SELECT name_address FROM address";
      assert conn != null;
      try (PreparedStatement preparedStatement = conn.prepareStatement(sql);
          ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {
          addresses.add(resultSet.getString("name_address"));
        }
      }
    } catch (Exception e) {
      System.err.println("Error loading addresses: " + e.getMessage());
    }

    promptAddress.getItems().clear();
    promptAddress.getItems().addAll(addresses);
  }

  /**
   * oke.
   */
  @FXML
  private void moveToBase() {
    BaseController.setBaseState(1);
    setAccState(0);
    App.primaryStage.setScene(App.baseScene);
    App.primaryStage.show();
  }

}

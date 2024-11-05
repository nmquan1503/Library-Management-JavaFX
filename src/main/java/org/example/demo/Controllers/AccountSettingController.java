package org.example.demo.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.demo.App;
import org.example.demo.Database.JDBC;


public class AccountSettingController {

  private static final IntegerProperty checkState = new SimpleIntegerProperty(0);

  public static int getAccState() {
    return checkState.get();
  }

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
  private AnchorPane succeedPane;

  public void initialize() {
    loadAvtImg();
    setCameraHoverEffect();
    chooseAvtFileBtn.setOnMouseClicked(this::handleChooseAvatarClick);
    cameraIcon.setOnMouseClicked(this::handleChooseAvatarClick);
    checkState.addListener((observable, oldValue, newValue) -> {
      loadAvtImg();
    });
  }

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

  private void loadAvtImg() {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    try {
      conn = JDBC.getConnection();

      String sql = "SELECT avatar FROM librarian WHERE id_librarian ="
          + BaseController.getLibId();
      assert conn != null;
      preparedStatement = conn.prepareStatement(sql);

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
      loadAvtImg();
    }
  }

  public void resetDefaultAvt() {
    updateAvatar(Objects.requireNonNull(
            getClass().getResource("/org/example/demo/Assets/default_avatar.jpg")).getPath(),
        BaseController.getLibId());
    loadDefaultAvatar();
  }

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

  private void onHoverArc(boolean isHovered) {
    if (isHovered) {
      chooseAvtFileBtn.setFill(Color.rgb(105, 105, 105, 0.7));
      cameraIcon.setOpacity(0.7);
    } else {
      chooseAvtFileBtn.setFill(Color.rgb(128, 128, 128, 0.6));
      cameraIcon.setOpacity(0.6);
    }
  }

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
          preparedStatement.executeUpdate();
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

  private void succeedNotification() {

  }

  private void errorNotification() {

  }

  @FXML
  private void moveToBase() {
    BaseController.setBaseState(1);
    setAccState(0);
    App.primaryStage.setScene(App.baseScene);
    App.primaryStage.show();
  }

}

package org.example.demo.CustomUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.util.Duration;
import org.example.demo.API.Network;
import org.example.demo.Controllers.BaseController;
import org.example.demo.Controllers.EditController;
import org.example.demo.Controllers.UsersController;
import org.example.demo.Database.JDBC;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Users.Date;
import org.example.demo.Models.Users.User;

public class EditUserView extends ScrollPane implements MainInfo {

  @FXML
  private AnchorPane viewPane;

  @FXML
  private VBox wrapper;
  @FXML
  private ImageView imageUser;

  @FXML
  private TextField nameTextField;
  @FXML
  private Label idLabel;

  @FXML
  private Label birthdayTag;
  @FXML
  private TextField birthdayTextField;

  @FXML
  private Label addressTag;
  @FXML
  private JFXComboBox<String> addressComboBox;

  @FXML
  private Label phoneNumberTag;
  @FXML
  private TextField phoneNumberTextField;

  @FXML
  private Label emailTag;
  @FXML
  private TextField emailTextField;

  @FXML
  private Label endBanDateTag;
  @FXML
  private TextField endBanDateTextField;

  @FXML
  private JFXButton saveButton;

  @FXML
  private Pane loadingPane;
  private Transition loadingTransition;

  private User oldUser;

  private HashMap<Object, String> viLang;
  private HashMap<Object, String> enLang;

  private EditController editController;

  /**
   * constructor.
   */
  public EditUserView(EditController editController) {
    initView();
    initLoadingTransition();
    this.editController = editController;
  }

  /**
   * set up all with user's info.
   */
  public void setUser(User user) {
    if (user == null) {
      initDefaultImage();
      initDefaultId();
      initDefaultAddress();
    } else {
      initImage(user);
      initName(user);
      initId(user);
      initBirthday(user);
      initAddress(user);
      initPhoneNumber(user);
      initEmail(user);
      initEndBanDate(user);
    }
    this.oldUser = user;
  }

  /**
   * setup after open edit user view.
   */
  public void completeSetup() {
    setupPhoneNumberTextField();
    setupDateTextField(endBanDateTextField);
    setupDateTextField(birthdayTextField);
    viLang = new HashMap<>();
    enLang = new HashMap<>();
    setUpLanguage(viLang, enLang);
    if (BaseController.isTranslate) {
      applyTranslate(null, null, true);
    }
    viewPane.getChildren().remove(loadingPane);
    loadingPane = null;
    loadingTransition.stop();
    loadingTransition = null;
  }

  /**
   * set end ban date of user.
   */
  private void initEndBanDate(User user) {
    setupDateTextField(endBanDateTextField);
    if (user != null) {
      if (user.getBanEndTime() != null) {
        Date date = new Date(user.getBanEndTime());
        endBanDateTextField.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());
      }
    }
  }

  /**
   * set mail of user.
   */
  private void initEmail(User user) {
    if (user != null) {
      if (user.getEmail() != null) {
        emailTextField.setText(user.getEmail());
      }
    }
  }

  /**
   * set phone of user.
   */
  private void initPhoneNumber(User user) {
    if (user.getPhoneNumber() != null) {
      phoneNumberTextField.setText(user.getPhoneNumber());
    }
  }

  /**
   * ser up text field only accept number.
   */
  private void setupPhoneNumberTextField() {
    phoneNumberTextField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        if (!newValue.matches("\\d*")) {
          phoneNumberTextField.setText(newValue.replaceAll("[^\\d]", ""));
        }
      }
    });
  }

  /**
   * set address of user.
   */
  private void initAddress(User user) {
    initDefaultAddress();
    if (user.getAddress() != null) {
      addressComboBox.setValue(user.getAddress());
    }
  }

  /**
   * set default address.
   */
  private void initDefaultAddress() {
    Connection connection = JDBC.getConnection();
    try {
      String query = "select name_address from address";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        String address = resultSet.getString(1);
        addressComboBox.getItems().add(address);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    JDBC.closeConnection(connection);
  }

  /**
   * set birthday of user.
   */
  private void initBirthday(User user) {
    setupDateTextField(birthdayTextField);
    if (user.getBirthday() != null) {
      Date date = new Date(user.getBirthday());
      birthdayTextField.setText(date.getDay() + "/" + date.getMonth() + "/" + date.getYear());
    }

  }

  /**
   * set name of user.
   */
  private void initName(User user) {
    if (user.getName() != null) {
      nameTextField.setText(user.getName());
    }
  }

  /**
   * set id of user.
   */
  private void initId(User user) {
    if (user.getId() == -1) {
      initDefaultId();
    } else {
      idLabel.setText("#" + user.getId());
    }
  }

  /**
   * set up default id.
   */
  private void initDefaultId() {
    int id = -1;
    Connection connection = JDBC.getConnection();
    try {
      String query = "select max(id_user) as id " +
          "from user ";
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        id = resultSet.getInt("id") + 1;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    JDBC.closeConnection(connection);
    idLabel.setText("#" + id);
  }


  /**
   * set up image of user.
   */
  private void initImage(User user) {
    if (user.getAvatar() == null || !Network.isConnected()) {
      initDefaultImage();
      return;
    }
    imageUser.setImage(user.getAvatar());

    if (BaseController.isDark) {
      wrapper.setBlendMode(BlendMode.DIFFERENCE);
      wrapper.setId("wrapper_dark");
    } else {
      wrapper.setBlendMode(BlendMode.SRC_OVER);
    }

  }

  /**
   * set default avatar.
   */
  private void initDefaultImage() {
    imageUser.setImage(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/org/example/demo/Assets/default_avatar.jpg"))));
    if (BaseController.isDark) {
      wrapper.setBlendMode(BlendMode.DIFFERENCE);
      wrapper.setId("wrapper_dark");
    } else {
      wrapper.setBlendMode(BlendMode.SRC_OVER);
    }
  }

  /**
   * init view.
   */
  private void initView() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/EditUserView.fxml"));
      fxmlLoader.setController(this);
      AnchorPane anchorPane = fxmlLoader.load();
      this.setContent(anchorPane);
    } catch (Exception e) {
      e.printStackTrace();
    }

    this.setPrefHeight(540);
    this.setPrefWidth(900);
    this.getStylesheets()
        .add(getClass().getResource("/org/example/demo/CSS/BookView.css").toExternalForm());
    this.setId("FadedScrollPane");
  }

  /**
   * set up regex of date text field.
   */
  private void setupDateTextField(TextField textField) {
    textField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        if (!newValue.matches("\\d{0,2}/?\\d{0,2}/?\\d{0,4}")) {
          textField.setText(oldValue);
        }
      }
    });
  }


  /**
   * exit view. open confirm box (save or not save).
   */
  @FXML
  private void ExitView() {
    AnchorPane mainPane = (AnchorPane) this.getParent();
    ConfirmBox confirmBox = new ConfirmBox(
        "Xác nhận hủy sự thay đổi?",
        "Nếu bạn chọn \"Hủy\", bạn sẽ được tiếp tục thay đổi thông tin người mượn.",
        () -> {
          mainPane.getChildren().removeLast();
          mainPane.getChildren().removeLast();
        },
        () -> {
          mainPane.getChildren().removeLast();
        }
    );
    mainPane.getChildren().add(confirmBox);
  }

  /**
   * save user.
   */
  @FXML
  private void SaveUser() {
    if (nameTextField.getText().isEmpty()) {
      nameTextField.requestFocus();
      Node parent = this;
      while (parent.getParent() != null) {
        parent = parent.getParent();
      }
      if (parent instanceof AnchorPane) {
        ((AnchorPane) parent).getChildren().add(new Warning(
            "Lỗi thông tin!",
            "Tên không được để trống."
        ));
      }
      return;
    }
    if (createDateFromString(birthdayTextField.getText()) == null) {
      birthdayTextField.requestFocus();
      Node parent = this;
      while (parent.getParent() != null) {
        parent = parent.getParent();
      }
      if (parent instanceof AnchorPane) {
        ((AnchorPane) parent).getChildren().add(new Warning(
            "Lỗi thông tin!",
            "Hãy nhập lại ngày sinh."
        ));
      }
      return;
    }
    if (phoneNumberTextField.getText().isEmpty()) {
      phoneNumberTextField.requestFocus();
      Node parent = this;
      while (parent.getParent() != null) {
        parent = parent.getParent();
      }
      if (parent instanceof AnchorPane) {
        ((AnchorPane) parent).getChildren().add(new Warning(
            "Lỗi thông tin!",
            "Số điện thoại không được để trống."
        ));
      }
      return;
    }
    if (addressComboBox.getValue() == null) {
      addressComboBox.requestFocus();
      Node parent = this;
      while (parent.getParent() != null) {
        parent = parent.getParent();
      }
      if (parent instanceof AnchorPane) {
        ((AnchorPane) parent).getChildren().add(new Warning(
            "Lỗi thông tin!",
            "Địa chỉ không được để trống."
        ));
      }
      return;
    }
    if (createDateFromString(endBanDateTextField.getText()) == null
        && !endBanDateTextField.getText().isEmpty()) {
      endBanDateTextField.requestFocus();
      Node parent = this;
      while (parent.getParent() != null) {
        parent = parent.getParent();
      }
      if (parent instanceof AnchorPane) {
        ((AnchorPane) parent).getChildren().add(new Warning(
            "Lỗi thông tin!",
            "Hãy nhập lại ngày hết hạn cấm."
        ));
      }
      return;
    }

    AnchorPane mainPane = (AnchorPane) this.getParent();
    ConfirmBox confirmBox = new ConfirmBox(
        "Xác nhận Lưu?",
        "Nếu bạn chọn \"Hủy\", bạn sẽ được tiếp tục thay đổi thông tin người mượn.",
        () -> {
          Node parent = mainPane.getParent();
          if (parent != null) {
            if (parent instanceof AnchorPane) {
              if (oldUser != null) {
                ((AnchorPane) parent).getChildren().add(new Warning(
                    "Thành công!",
                    "Đã thay đổi thông tin người dùng."
                ));
              } else {
                ((AnchorPane) parent).getChildren().add(new Warning(
                    "Thành công!",
                    "Người dùng đã được thêm thành công."
                ));
              }
            }
          }
          mainPane.getChildren().removeLast();
          Thread thread = new Thread(() -> {
            if (oldUser != null) {
              Library.getInstance().deleteUser(oldUser);
            }
            User newUser = createNewUser();
            if (newUser != null) {
              Library.getInstance().insertUserWithID(newUser, newUser.getId());

              if (oldUser == null) {
                Platform.runLater(() -> {
                  editController.addUserSuggestion(new Suggestion(newUser));
                });
              } else {
                Platform.runLater(() -> {
                  editController.fixUserSuggestion(new Suggestion(newUser));
                });
              }
            }

          });
          thread.start();
          mainPane.getChildren().removeLast();
        },
        () -> {
          mainPane.getChildren().removeLast();
        }
    );
    mainPane.getChildren().add(confirmBox);
  }

  /**
   * create user from all entered info.
   */
  private User createNewUser() {
    Image image = imageUser.getImage();

    String name = nameTextField.getText();

    int id = Integer.parseInt(idLabel.getText().substring(1));

    Date birthDay = createDateFromString(birthdayTextField.getText());
    if (birthDay == null && !birthdayTextField.getText().isEmpty()) {
      return null;
    }

    String address = addressComboBox.getValue();

    String phoneNumber = phoneNumberTextField.getText();

    String email = emailTextField.getText();

    Date endBanDate = createDateFromString(endBanDateTextField.getText());
    if (endBanDate == null && !endBanDateTextField.getText().isEmpty()) {
      return null;
    }

    return new User(id, name, birthDay, address, email, image, phoneNumber, endBanDate);
  }

  /**
   * create date from date text field.
   */
  private Date createDateFromString(String s) {
    int index1 = s.indexOf('/');
    if (index1 == -1) {
      return null;
    }
    int index2 = s.substring(index1 + 1).indexOf('/') + index1 + 1;
    if (index2 == index1) {
      return null;
    }
    String stringDay = s.substring(0, index1);
    String stringMonth = s.substring(index1 + 1, index2);
    String stringYear = s.substring(index2 + 1);
    if (stringDay.isEmpty() || stringMonth.isEmpty() || stringYear.isEmpty()) {
      return null;
    }
    int day = Integer.parseInt(s.substring(0, index1));
    int month = Integer.parseInt(s.substring(index1 + 1, index2));
    int year = Integer.parseInt(s.substring(index2 + 1));
    Date newDate = new Date(year, month, day);
    if (newDate.getYear() != year || newDate.getMonth() != month || newDate.getDay() != day) {
      return null;
    }
    return newDate;
  }

  /**
   * start transition.
   */
  private void initLoadingTransition() {
    Arc arc1 = (Arc) loadingPane.getChildren().getFirst();
    Arc arc2 = (Arc) loadingPane.getChildren().get(1);
    Arc arc3 = (Arc) loadingPane.getChildren().get(2);

    RotateTransition transition1 = new RotateTransition(Duration.millis(1000), arc1);
    transition1.setByAngle(360);
    transition1.setCycleCount(Transition.INDEFINITE);
    transition1.setAutoReverse(false);

    RotateTransition transition2 = new RotateTransition(Duration.millis(700), arc2);
    transition2.setByAngle(360);
    transition2.setCycleCount(Transition.INDEFINITE);
    transition2.setAutoReverse(false);

    RotateTransition transition3 = new RotateTransition(Duration.millis(400), arc3);
    transition3.setByAngle(360);
    transition3.setCycleCount(Transition.INDEFINITE);
    transition3.setAutoReverse(false);

    loadingTransition = new ParallelTransition(transition1, transition2, transition3);
    loadingTransition.play();
  }


  /**
   * set dark/light mode.
   */
  @Override
  public void applyDarkMode(boolean isDark) {
    if (isDark) {
      this.wrapper.setBlendMode(BlendMode.DIFFERENCE);
      this.wrapper.setId("wrapper_dark");
    } else {
      this.wrapper.setBlendMode(BlendMode.SRC_OVER);
      this.wrapper.setId("wrapper_light");
    }
  }

  /**
   * translate en/vi language for some text.
   */
  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {
    if (isTranslate) {

      nameTextField.setPromptText("Name");

      birthdayTag.setText("Birthday: ");
      birthdayTextField.setPromptText("Birthday");

      addressTag.setText("Address: ");
      addressComboBox.setPromptText(" Address");

      phoneNumberTag.setText("Phone number: ");
      phoneNumberTextField.setPromptText("Phone number");

      endBanDateTag.setText("Banned until: ");
      endBanDateTextField.setPromptText("End ban date");

      saveButton.setText("Save");
    } else {
      nameTextField.setPromptText("Tên");

      birthdayTag.setText("Ngày sinh: ");
      birthdayTextField.setPromptText("Ngày sinh");

      addressTag.setText("Địa chỉ: ");
      addressComboBox.setPromptText(" Địa chỉ");

      phoneNumberTag.setText("Số điện thoại: ");
      phoneNumberTextField.setPromptText("Số điện thoại");

      endBanDateTag.setText("Bị cấm tới ngày: ");
      endBanDateTextField.setPromptText("Ngày hết hạn cấm");

      saveButton.setText("Lưu");
    }
  }

  /**
   * set up en/vi language.
   */
  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }

}

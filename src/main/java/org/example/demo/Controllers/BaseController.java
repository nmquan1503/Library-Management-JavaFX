package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.effect.BlendMode;
import org.example.demo.API.Translate;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.Language;

public class BaseController {

  HashMap<Object, String> viLang = new HashMap<>();
  HashMap<Object, String> enLang = new HashMap<>();

  public static boolean isDark = false;

  // isTranslate = false ứng với lang = "vi", ngược lại = "en"
  public static boolean isTranslate = false;

  @FXML
  private AnchorPane mainPane;

  @FXML
  private Circle avatar;

  @FXML
  private AnchorPane bigPane;

  @FXML
  private JFXButton checkMode;

  @FXML
  private ContextMenu avatarMenu;

  @FXML
  private TextField searchBase;

  @FXML
  private Tooltip notiText;

  @FXML
  private Tooltip darkText;

  @FXML
  private Tooltip avtText;

  private MainInfo currentController;

  @FXML
  public void initialize() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/Home.fxml"));
      AnchorPane anchorPane = fxmlLoader.load();

      currentController = fxmlLoader.getController();

      bigPane.getChildren().remove(mainPane);
      bigPane.getChildren().add(anchorPane);
      mainPane = anchorPane;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!isDark) {
      checkMode.setText("☀");
    } else {
      checkMode.setText("🌙");
    }

    if (avatar != null) {
      try {
        Image myImage = new Image(
            getClass().getResourceAsStream("/org/example/demo/Assets/default_avatar.jpg"));
        avatar.setFill(new ImagePattern(myImage));
      } catch (Exception e) {
        System.out.println("Image loading failed: " + e.getMessage());
      }
    }
    avtMenuSetup();
    setUpLang();

  }

  @FXML
  public void darkMode() {
    if (!isDark) {
      bigPane.setBlendMode(BlendMode.DIFFERENCE);
      avatar.setBlendMode(BlendMode.DIFFERENCE);
      checkMode.setText("🌙");
    } else {
      bigPane.setBlendMode(BlendMode.SRC_OVER);
      avatar.setBlendMode(BlendMode.SRC_OVER);
      checkMode.setText("☀");
    }

    if (currentController != null) {
      currentController.applyDarkMode(!isDark);
    }

    isDark = !isDark;
  }

  private void avtMenuSetup() {
    avatarMenu.getItems().clear();

    MenuItem changeInfo = new MenuItem(" Cài đặt tài khoản");
    if (isTranslate) {
      changeInfo.setText(" Setting");
    }
    changeInfo.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.WRENCH));

    MenuItem translate = new MenuItem(" Dịch");
    if (isTranslate) {
      changeInfo.setText(" Translate");
    }
    translate.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.LANGUAGE));

    MenuItem logOut = new MenuItem(" Đăng xuất");
    if (isTranslate) {
      changeInfo.setText(" Log out");
    }
    logOut.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SIGN_OUT));

    changeInfo.setOnAction(e -> handleChangeAccountInfo());
    translate.setOnAction(e -> handleTranslate());
    logOut.setOnAction(e -> handleLogout());

    avatarMenu.getItems().addAll(changeInfo, translate, logOut);
  }

  private void handleChangeAccountInfo() {

  }

  private void handleLogout() {

  }

  private void setUpLang() {
    HomeController.setUpLanguage(viLang, enLang);
    BooksController.setUpLanguage(viLang, enLang);
    EditController.setUpLanguage(viLang, enLang);
    UsersController.setUpLanguage(viLang, enLang);
    for (MenuItem item : avatarMenu.getItems()) {
      viLang.put(item, item.getText());
      if (item.getText().equals(" Dịch")) {
        enLang.put(item, " Translate");
      } else {
        enLang.put(item,
            Translate.translate(item.getText(), Language.VIETNAMESE, Language.ENGLISH));
      }
    }

    viLang.put(searchBase, searchBase.getPromptText());
    viLang.put(darkText, darkText.getText());
    viLang.put(notiText, notiText.getText());
    viLang.put(avtText, avtText.getText());

    enLang.put(searchBase,
        Translate.translate(searchBase.getPromptText(), Language.VIETNAMESE, Language.ENGLISH));
    enLang.put(darkText,
        "Light-Dark Mode");
    enLang.put(notiText,
        Translate.translate(notiText.getText(), Language.VIETNAMESE, Language.ENGLISH));
    enLang.put(avtText,
        Translate.translate(avtText.getText(), Language.VIETNAMESE, Language.ENGLISH));

  }

  private void handleTranslate() {
    if (isTranslate) {
      searchBase.setPromptText(viLang.get(searchBase));
      darkText.setText(viLang.get(darkText));
      notiText.setText(viLang.get(notiText));
      avtText.setText(viLang.get(avtText));

      for (MenuItem item : avatarMenu.getItems()) {
        item.setText(viLang.get(item));
      }

    } else {
      searchBase.setPromptText(enLang.get(searchBase));
      darkText.setText(enLang.get(darkText));
      notiText.setText(enLang.get(notiText));
      avtText.setText(enLang.get(avtText));

      for (MenuItem item : avatarMenu.getItems()) {
        item.setText(enLang.get(item));
      }
    }

    // translate for main pane
    if (currentController != null) {
      currentController.applyTranslate(viLang, enLang, !isTranslate);
    }
    isTranslate = !isTranslate;
  }

  @FXML
  public void avatarClicked() {
    for (MenuItem item : avatarMenu.getItems()) {
      if (item.getGraphic() instanceof FontAwesomeIconView) {
        FontAwesomeIconView icon = (FontAwesomeIconView) item.getGraphic();
        if (isDark) {
          icon.setFill(javafx.scene.paint.Color.WHITE);
        } else {
          icon.setFill(javafx.scene.paint.Color.BLACK);
        }
      }
    }
    if (isDark) {
      avatarMenu.getStyleClass().remove("dropdown-menu");
      avatarMenu.getStyleClass().add("dropdown-menu-dark");
      avatarMenu.setStyle("-fx-background-color: #000000; " +
          "-fx-border-color: #165B33; " +
          "-fx-border-radius: 8px; " +
          "-fx-padding: 5px;");
    } else {
      avatarMenu.getStyleClass().remove("dropdown-menu-dark");
      avatarMenu.getStyleClass().add("dropdown-menu");
      avatarMenu.setStyle("-fx-background-color: #f0f0f0; " +
          "-fx-border-color: #E464C0; " +
          "-fx-border-radius: 8px; " +
          "-fx-padding: 5px;");
    }
    Bounds avatarBounds = avatar.localToScreen(avatar.getBoundsInLocal());

    double menuX = avatarBounds.getMinX();
    double menuY = avatarBounds.getMaxY();

    avatarMenu.show(avatar, menuX - 143, menuY + 5);
  }

  private void switchPane(String fxmlPath) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
      AnchorPane anchorPane = fxmlLoader.load();

      currentController = fxmlLoader.getController();

      bigPane.getChildren().remove(mainPane);
      bigPane.getChildren().add(anchorPane);
      mainPane = anchorPane;

      if (isDark && currentController != null) {
        currentController.applyDarkMode(isDark);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void moveDashboard() {
    switchPane("/org/example/demo/FXML/Home.fxml");
  }

  @FXML
  public void moveBooks() {
    switchPane("/org/example/demo/FXML/Books.fxml");
  }

  @FXML
  public void moveUser() {
    switchPane("/org/example/demo/FXML/Users.fxml");
  }

  @FXML
  public void moveEdit() {
    switchPane("/org/example/demo/FXML/Edit.fxml");
  }

}

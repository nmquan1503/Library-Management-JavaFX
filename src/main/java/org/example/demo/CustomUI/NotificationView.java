
package org.example.demo.CustomUI;

import java.util.HashMap;
import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.demo.API.Network;
import org.example.demo.Controllers.BaseController;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Borrowing.Borrowing;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Users.Date;
import org.example.demo.Models.Users.User;

public class NotificationView extends HBox implements MainInfo {

  private boolean isSeen;

  private StackPane wrapper;
  private ImageView image;
  private Label content;

  public NotificationView(Borrowing borrowing, int width, int height) {
    isSeen = false;

    User user = Library.getInstance().getUser(borrowing.getIdUser());
    Book book = Library.getInstance().getBook(borrowing.getIdBook());

    initImage(user, height, width);
    initContent(user, book, borrowing.getDueDate(), height, width);

    this.getChildren().add(wrapper);
    this.getChildren().add(content);

    this.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/org/example/demo/CSS/Pagination.css"))
            .toExternalForm());
    this.setPrefHeight(height);
    this.setPrefWidth(width);
    this.setAlignment(Pos.CENTER);
    this.setSpacing(3);

  }

  public void markSeen() {
    isSeen = true;
    content.setId(null);
    content.setFont(Font.font("System", FontWeight.NORMAL, 14));
  }

  public boolean isSeen() {
    return isSeen;
  }

  private void initImage(User user, int height, int width) {
    wrapper = new StackPane();
    wrapper.setPrefHeight(height);
    wrapper.setPrefWidth(height / 1.5);
    wrapper.setAlignment(Pos.CENTER);

    image = new ImageView();
    if (user.getAvatar() != null) {
      image.setImage(user.getAvatar());
    } else {
      image.setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/default_avt_user.png"))));
    }
    image.setPreserveRatio(true);
    image.setFitHeight(height - 30);

    wrapper.getChildren().add(image);
    if (BaseController.isDark) {
      wrapper.setBlendMode(BlendMode.DIFFERENCE);
    } else {
      wrapper.setBlendMode(BlendMode.SRC_OVER);
    }
  }

  private void initContent(User user, Book book, Date dueDate, int height, int width) {

    content = new Label();

    if (dueDate.isAfter(Date.today())) {
      content.setText(user.getName() + " đã quá hạn trả sách " + book.getTitle() + " " + Math.abs(
          dueDate.datediff(Date.today())) + " ngày!");
    } else {
      content.setText(user.getName() + " còn " + Math.abs(dueDate.datediff(Date.today()))
          + " ngày nữa phải trả sách " + book.getTitle());
    }
    content.setPrefWidth(width - height / 1.5 - 5);
    content.setPrefHeight(height - 7);
    content.setWrapText(true);
    content.setAlignment(Pos.CENTER);
    content.setId("notification-suggestion");
  }

  @Override
  public void applyDarkMode(boolean isDark) {
    if (isDark) {
      wrapper.setBlendMode(BlendMode.DIFFERENCE);
    } else {
      wrapper.setBlendMode(BlendMode.SRC_OVER);
    }
  }

  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {

  }

  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }
}

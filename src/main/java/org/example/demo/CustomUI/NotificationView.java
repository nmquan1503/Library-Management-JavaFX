package org.example.demo.CustomUI;

import java.util.HashMap;
import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.example.demo.Controllers.BaseController;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Borrowing.Borrowing;
import org.example.demo.Models.Library;
import org.example.demo.Models.Users.Date;
import org.example.demo.Models.Users.User;

public class NotificationView extends HBox implements MainInfo {

  private boolean isSeen;

  private ImageView image;
  private Label content;

  private final Borrowing borrowing;

  public Borrowing getBorrowing() {
    return borrowing;
  }

  public ImageView getImage() {
    return image;
  }

  public NotificationView(Borrowing borrowing, int width, int height) {
    isSeen = false;

    this.borrowing = borrowing;
    User user = Library.getInstance().getUser(borrowing.getIdUser());
    Book book = Library.getInstance().getBook(borrowing.getIdBook());

    initImage(user, height, width);
    initContent(user, book, borrowing.getDueDate(), height, width);

    this.getChildren().add(image);
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
    if (!BaseController.isDark) {
      content.setId("notification-suggestion-marked");
    } else {
      content.setId("notification-suggestion-marked-dark");
    }
  }

  public boolean isSeen() {
    return isSeen;
  }

  private void initImage(User user, int height, int width) {

    image = new ImageView();
    if (user.getAvatar() != null) {
      image.setImage(user.getAvatar());
    } else {
      image.setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/default_avt_user.jpg"))));
    }
    image.setPreserveRatio(true);
    image.setFitHeight(height - 30);

    if (BaseController.isDark) {
      image.setBlendMode(BlendMode.DIFFERENCE);
    } else {
      image.setBlendMode(BlendMode.SRC_OVER);
    }
  }

  private void initContent(User user, Book book, Date dueDate, int height, int width) {

    content = new Label();
    String bookTitle = book.getTitle();
    int daysDiff = (int) Math.abs(dueDate.datediff(Date.today()));

    if (!dueDate.isAfter(Date.today())) {
      if (!BaseController.isTranslate) {
        content.setText(
            user.getName() + " đã quá hạn trả sách " + bookTitle + " " + daysDiff + " ngày!");
      } else {
        content.setText(
            user.getName() + " is " + daysDiff + " days overdue for returning the book " + bookTitle
                + "!");
      }
    } else {
      if (!BaseController.isTranslate) {
        content.setText(user.getName() + " còn " + daysDiff
            + " ngày nữa phải trả sách " + bookTitle);
      } else {
        content.setText(user.getName() + " has " + daysDiff
            + " days left to return the book " + bookTitle);
      }
    }

    content.setPrefWidth(width - height / 1.5 - 5);
    content.setPrefHeight(height - 7);
    content.setWrapText(true);
    content.setAlignment(Pos.CENTER);
    if (BaseController.isDark) {
      content.setId("notification-suggestion-dark");
    } else {
      content.setId("notification-suggestion");
    }
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

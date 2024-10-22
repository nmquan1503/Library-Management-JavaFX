package org.example.demo.Models.Suggestion;

import java.util.Objects;
import javafx.scene.image.Image;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Users.User;

public class Suggestion {

  private int id;
  private Image icon;
  private String content;

  public Suggestion(Book book) {
    id = book.getId();
    if (book.getImageLink() != null) {
      icon = new Image(book.getImageLink());
    } else {
      icon = new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg")));
    }
    content = book.getTitle();
  }

  public Suggestion(User user) {
    id = user.getId();
    if (user.getAvatar() == null) {
      icon = new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg")));
    } else {
      icon = user.getAvatar();
    }
    content = user.getName();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Image getIcon() {
    return icon;
  }

  public void setIcon(Image icon) {
    this.icon = icon;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}

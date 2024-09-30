package org.example.demo.Models.Users;

import java.sql.Date;
import java.util.ArrayList;
import javafx.scene.image.Image;

public class Librarian extends Person {

  ArrayList<Notification> notificationsList;

  public Librarian() {
    super();
    notificationsList = new ArrayList<>();
  }

  public Librarian(ArrayList<Notification> notificationsList) {
    this.notificationsList = notificationsList;
  }

  public Librarian(int id, String name, Date birthday, String address, String email,
      String phoneNumber, Image avatar,
      ArrayList<Notification> notificationsList) {
    super(id, name, birthday, address, email, phoneNumber, avatar);
    this.notificationsList = notificationsList;
  }

  @Override
  public int SaveInfo() {
    // add to db:
    return this.getId();
  }
}

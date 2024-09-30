package org.example.demo.Models.Users;

import java.sql.Date;

public class Notification {

  private String title;
  private String content;
  private Date time;
  private int id_user;
  private boolean isSeen;

  public Notification(String title, String content, Date time, int id_user, boolean isSeen) {
    this.title = title;
    this.content = content;
    this.time = time;
    this.id_user = id_user;
    this.isSeen = isSeen;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public int getId_user() {
    return id_user;
  }

  public void setId_user(int id_user) {
    this.id_user = id_user;
  }

  public boolean isSeen() {
    return isSeen;
  }

  public void setSeen(boolean seen) {
    isSeen = seen;
  }

  public int SaveInfo() {
    // add to db:

    return this.getId_user();
  }
}

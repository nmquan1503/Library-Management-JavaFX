package org.example.demo.Controllers;

public class TableData {

  private String action;
  private String user;
  private String book;
  private String date;

  public TableData(String action, String user, String book, String date) {
    this.action = action;
    this.user = user;
    this.book = book;
    this.date = date;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getBook() {
    return book;
  }

  public void setBook(String book) {
    this.book = book;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}

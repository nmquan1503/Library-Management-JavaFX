package org.example.demo.Models.Users;

public class Date extends java.sql.Date {

  public Date(int year, int month, int day) {
    super(year - 1900, month - 1, day);
  }
}

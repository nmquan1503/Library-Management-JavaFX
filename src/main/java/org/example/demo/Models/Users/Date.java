package org.example.demo.Models.Users;

import java.util.Calendar;

public class Date extends java.sql.Date {

  public Date(int year, int month, int day) {
    super(year - 1900, month - 1, day);
  }

  public Date add(int days) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);
    calendar.add(Calendar.DAY_OF_MONTH, days);
    return new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH));
  }

  public static void main(String[] args) {
    Date myDate = new Date(2024, 10, 3); // Create a date
    Date newDate = myDate.add(31); // Add 31 days
    System.out.println("New Date: " + newDate);
  }
}

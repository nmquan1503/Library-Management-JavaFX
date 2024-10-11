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

  @Override
  public int getYear() {
    return super.getYear() + 1900;
  }

  @Override
  public int getMonth() {
    return super.getMonth() + 1;
  }

  @Override
  public int getDay() {
    return super.getDay();
  }

  public boolean isAfter(Date date) {
    if (this.getYear() > date.getYear()) {
      return true;
    }
    if (this.getMonth() > date.getMonth()) {
      return true;
    }
    return this.getDay() > date.getDay();
  }

  public boolean isEqual(Date date) {
    return this.getYear() == date.getYear() &&
        this.getMonth() == date.getMonth() &&
        this.getDay() == date.getDay();
  }

  public boolean isBefore(Date date) {
    if (this.getYear() < date.getYear()) {
      return true;
    }
    if (this.getMonth() < date.getMonth()) {
      return true;
    }
    return this.getDay() < date.getDay();
  }
}
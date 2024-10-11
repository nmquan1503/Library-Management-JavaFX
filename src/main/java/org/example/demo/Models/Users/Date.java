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
  public void setYear(int year) {
    super.setYear(year - 1900);
  }

  @Override
  public int getMonth() {
    return super.getMonth() + 1;
  }

  @Override
  public void setMonth(int month) {
    super.setMonth(month - 1);
  }

  @Override
  public int getDate() {
    return super.getDate();
  }

  @Override
  public void setDate(int date) {
    super.setDate(date);
  }

  /**
   * @param other
   * @return 1 if this > other, 0 if this = other and -1 otherwise
   */
  int compare(Date other) {
    if (this.before(other)) {
      return -1;
    } else if (this.after(other)) {
      return 1;
    } else {
      return 0;
    }
  }
}

package org.example.demo.Models.Users;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;

public class Date extends java.sql.Date {

  /**
   * oke.
   */
  public Date(int year, int month, int day) {
    super(year - 1900, month - 1, day);
  }

  /**
   * oke.
   */
  public Date(java.sql.Date date) {
    super(date.getTime());
  }

  /**
   * oke.
   */
  public Date add(int days) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);
    calendar.add(Calendar.DAY_OF_MONTH, days);
    return new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * oke.
   */
  @Override
  public int getYear() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);  // Set thời gian cho Calendar dựa trên đối tượng java.sql.Date
    return calendar.get(Calendar.YEAR);
  }

  /**
   * oke.
   */
  @Override
  public int getMonth() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);
    return calendar.get(Calendar.MONTH) + 1;
  }

  /**
   * oke.
   */
  @Override
  public int getDay() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);
    return calendar.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * oke.
   */
  @Override
  public String toString() {
    return this.getDay() + "/ " + this.getMonth() + "/ " + this.getYear();
  }


  /**
   * oke.
   */
  public boolean isAfter(Date date) {
    if (this.getYear() > date.getYear()) {
      return true;
    } else if (this.getYear() < date.getYear()) {
      return false;
    }
    if (this.getMonth() > date.getMonth()) {
      return true;
    } else if (this.getMonth() < date.getMonth()) {
      return false;
    }
    return this.getDay() > date.getDay();
  }

  /**
   * oke.
   */
  public boolean isEqual(Date date) {
    return this.getYear() == date.getYear() &&
        this.getMonth() == date.getMonth() &&
        this.getDay() == date.getDay();
  }

  /**
   * oke.
   */
  public boolean isBefore(Date date) {
    if (this.getYear() < date.getYear()) {
      return true;
    } else if (this.getYear() > date.getYear()) {
      return false;
    }
    if (this.getMonth() < date.getMonth()) {
      return true;
    } else if (this.getMonth() > date.getMonth()) {
      return false;
    }
    return this.getDay() < date.getDay();
  }

  /**
   * oke.
   */
  @Override
  public LocalDate toLocalDate() {
    LocalDate res = LocalDate.of(this.getYear(), this.getMonth(), this.getDay());
    return res;
  }

  /**
   * oke.
   */
  public static Date today() {
    return new Date(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now()
        .getDayOfMonth());
  }

  /**
   * oke.
   */
  public long datediff(Date other) {
    return ChronoUnit.DAYS.between(other.toLocalDate(), this.toLocalDate());
  }

  /**
   * oke.
   */
  public static void main(String[] args) {
    Date myDate = new Date(2024, 10, 1); // Create a date
  }
}

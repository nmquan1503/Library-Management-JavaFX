package org.example.demo.Models.Users;

import java.sql.Date;
import javafx.scene.image.Image;

public abstract class Person {

  protected int id;
  protected String name;
  protected Date birthday;
  protected String address;
  protected String email;
  protected String phoneNumber;
  protected Image avatar;

  public Person() {
  }

  public Person(int id, String name, Date birthday, String address, String email,
      String phoneNumber,
      Image avatar) {
    this.id = id;
    this.name = name;
    this.birthday = birthday;
    this.address = address;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.avatar = avatar;
  }

  public Person(String name, Date birthday, String address, String email, String phoneNumber,
      Image avatar) {
    this.name = name;
    this.birthday = birthday;
    this.address = address;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.avatar = avatar;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Image getAvatar() {
    return avatar;
  }

  public void setAvatar(Image avatar) {
    this.avatar = avatar;
  }

  public abstract int SaveInfo();
}

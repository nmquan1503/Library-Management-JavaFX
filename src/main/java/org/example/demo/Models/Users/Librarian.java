package org.example.demo.Models.Users;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.image.Image;
import org.example.demo.Database.JDBC;

public class Librarian extends Person {

  private ArrayList<Notification> notificationsList;

  private String username_account;
  private String password_account;

  public Librarian() {
    super();
    notificationsList = new ArrayList<>();
  }

  public Librarian(ArrayList<Notification> notificationsList, String username_account,
      String password_account) {
    this.notificationsList = notificationsList;
    this.username_account = username_account;
    this.password_account = password_account;
  }

  public Librarian(String name, Date birthday, String address, String email, String phoneNumber,
      Image avatar, ArrayList<Notification> notificationsList, String username_account,
      String password_account) {
    super(name, birthday, address, email, phoneNumber, avatar);
    this.notificationsList = notificationsList;
    this.username_account = username_account;
    this.password_account = password_account;
  }

  public Librarian(ArrayList<Notification> notificationsList) {
    this.notificationsList = notificationsList;
  }

  public Librarian(String name, Date birthday, String address, String email,
      String phoneNumber, Image avatar,
      ArrayList<Notification> notificationsList) {
    super(name, birthday, address, email, phoneNumber, avatar);
    this.notificationsList = notificationsList;
  }

  public Librarian(String name, Date birthday, String address, String email, String phoneNumber,
      Image avatar, String username_account, String password_account) {
    super(name, birthday, address, email, phoneNumber, avatar);
    this.username_account = username_account;
    this.password_account = password_account;
  }

  public Librarian(String username_account, String password_account) {
    this.username_account = username_account;
    this.password_account = password_account;
  }

  public ArrayList<Notification> getNotificationsList() {
    return notificationsList;
  }

  public void setNotificationsList(
      ArrayList<Notification> notificationsList) {
    this.notificationsList = notificationsList;
  }

  public String getPassword_account() {
    return password_account;
  }

  public void setPassword_account(String password_account) {
    this.password_account = password_account;
  }

  public String getUsername_account() {
    return username_account;
  }

  public void setUsername_account(String username_account) {
    this.username_account = username_account;
  }

  public static void deleteFromDb(int id) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = JDBC.getConnection();
      String sql = "DELETE FROM librarian WHERE id_librarian = ?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, id);
      int rowsAffected = pstmt.executeUpdate();
      if (rowsAffected > 0) {
        System.out.println("Record deleted successfully.");
      } else {
        System.out.println("No record found with the specified ID.");
      }
      JDBC.closeConnection(conn);
    } catch (SQLException se) {
      se.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int SaveInfo() {
    // if phone number has already existed: return -1
    if (isPhoneExist(this.getPhoneNumber())) {
      return -1;
    }
    // add to db:
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = JDBC.getConnection();
      System.out.println("Insert into user table...");
      String sql =
          "INSERT INTO librarian (name_librarian, birthday, phone_number_librarian, email_librarian, id_address, username_account, password_account, avatar)"
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
      if (getAddressId(this.getAddress()) == -1) {
        System.out.println("Connection successful!");
        String q = "INSERT INTO address(name_address) "
            + "VALUES (?)";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1, this.getAddress());
        int rowsAffected = pstmt.executeUpdate();
        System.out.println("Insertion successful! Rows affected: " + rowsAffected);
      }
      pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

      pstmt.setString(1, this.getName());
      pstmt.setDate(2, this.getBirthday());
      pstmt.setString(3, this.getPhoneNumber());
      pstmt.setString(4, this.getEmail());
      pstmt.setInt(5, this.getAddressId(this.getAddress()));
      pstmt.setString(6, this.getUsername_account());
      pstmt.setString(7, this.getPassword_account());

      byte[] avatarBytes = convertImageToBytes(
          this.getAvatar());
      pstmt.setBytes(8, avatarBytes);

      // Execute the insert query
      int rowsAffected = pstmt.executeUpdate();
      System.out.println("Insertion successful! Rows affected: " + rowsAffected);

      ResultSet rs = pstmt.getGeneratedKeys();
      if (rs.next()) {
        this.setId(rs.getInt(1));
      }
      JDBC.closeConnection(conn);
    } catch (SQLException se) {
      se.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return this.getId();
  }
}
package org.example.demo.Models.Users;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.scene.image.Image;
import org.example.demo.Database.JDBC;

public class User extends Person {

  private Date banEndTime;

  /**
   * oke.
   */
  public User(String name, Date birthday, String address, String email,
      String phoneNumber, Date banEndTime) {
    super(name, birthday, address, email, phoneNumber);
    this.banEndTime = banEndTime;
  }

  /**
   * oke.
   */
  public User(String name, Date birthday, String address, String email,
      String phoneNumber, Image avatar, Date banEndTime) {
    super(name, birthday, address, email, phoneNumber, avatar);
    this.banEndTime = banEndTime;
  }

  /**
   * oke.
   */
  public User(int id, String name, java.sql.Date birthday, String address, String email,
      Image avatar,
      String phoneNumber, Date banEndTime) {
    super(id, name, birthday, address, email, avatar, phoneNumber);
    this.banEndTime = banEndTime;
  }

  /**
   * oke.
   */
  public Date getBanEndTime() {
    return banEndTime;
  }

  /**
   * oke.
   */
  public void setBanEndTime(Date banEndTime) {
    this.banEndTime = banEndTime;
  }

  /**
   * oke.
   */
  public boolean isBan() {
//    return LocalDate.now().isAfter(this.banEndTime.toLocalDate());
    if (this.banEndTime == null) {
      return false;
    }
    Date now = new Date(LocalDate.now().getYear(), LocalDate.now().getMonthValue(),
        LocalDate.now().getDayOfMonth());
    return banEndTime.isAfter(now);
  }

  /**
   * oke.
   */
  public static void deleteFromDb(int id) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = JDBC.getConnection();

      String sql = "DELETE FROM user WHERE id_user = ?";
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

  /**
   * oke.
   */
  @Override
  public int SaveInfo() {

    // if phone number has already existed: return -1
    if (isPhoneExist(this.getPhoneNumber())) {
      return -1;
    }

    // add in db:
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    try {
      connection = JDBC.getConnection();
      System.out.println("Connection successful!");

      String query =
          "INSERT INTO user(name_user, birthday, phone_number_user, email_user, id_address, ban_date, avatar) "
              + "VALUES ( ?, ?, ?, ?, ?, ?, ?)";

      if (getAddressId(this.getAddress()) == -1) {
        System.out.println("Connection successful!");
        String q = "INSERT INTO address(name_address) "
            + "VALUES (?)";
        preparedStatement = connection.prepareStatement(q);
        preparedStatement.setString(1, this.getAddress());
        int rowsAffected = preparedStatement.executeUpdate();
        System.out.println("Insertion successful! Rows affected: " + rowsAffected);
      }

      preparedStatement = connection.prepareStatement(query,
          PreparedStatement.RETURN_GENERATED_KEYS);

      preparedStatement.setString(1, this.getName());
      preparedStatement.setDate(2, this.getBirthday());
      preparedStatement.setString(3, this.getPhoneNumber());
      preparedStatement.setString(4, this.getEmail());
      preparedStatement.setInt(5, getAddressId(this.getAddress()));
      preparedStatement.setDate(6, this.getBanEndTime());

      preparedStatement.setBytes(7, convertImageToBytes(this.getAvatar()));

      int rowsAffected = preparedStatement.executeUpdate();
      System.out.println("Insertion successful! Rows affected: " + rowsAffected);

      ResultSet rs = preparedStatement.getGeneratedKeys();
      if (rs.next()) {
        this.setId(rs.getInt(1));
      }
      JDBC.closeConnection(connection);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return this.getId();
  }

  /**
   * oke.
   */
  public void removeInfo() {
    Connection connection = JDBC.getConnection();

    try {
      String query = "delete from user " +
          "where id_user=(?)";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, id);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }

    JDBC.closeConnection(connection);
  }
}
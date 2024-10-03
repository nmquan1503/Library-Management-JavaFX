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

public class User extends Person {

  private Date banEndTime;

  public User(String name, Date birthday, String address, String email,
      String phoneNumber, Date banEndTime) {
    super(name, birthday, address, email, phoneNumber);
    this.banEndTime = banEndTime;
  }

  public User(String name, Date birthday, String address, String email,
      String phoneNumber, Image avatar, Date banEndTime) {
    super(name, birthday, address, email, phoneNumber, avatar);
    this.banEndTime = banEndTime;
  }

  public Date getBanEndTime() {
    return banEndTime;
  }

  public void setBanEndTime(Date banEndTime) {
    this.banEndTime = banEndTime;
  }

  // change with the UML:isUnBan->isBan
  public boolean isBan() {
    return LocalDate.now().isAfter(this.banEndTime.toLocalDate());
  }

  // delete from database
  public static void deleteFromDb(int id) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    String dbURL = "jdbc:mysql://localhost:3306/library";
    String user = "root";
    String password = "encoding1105";
    try {
      conn = DriverManager.getConnection(dbURL, user, password);

      String sql = "DELETE FROM user WHERE id_user = ?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, id);
      int rowsAffected = pstmt.executeUpdate();
      if (rowsAffected > 0) {
        System.out.println("Record deleted successfully.");
      } else {
        System.out.println("No record found with the specified ID.");
      }
    } catch (SQLException se) {
      se.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (pstmt != null) {
          conn.close();
        }
      } catch (SQLException se) {
      }
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }

  @Override
  public int SaveInfo() {

    // if phone number has already existed: return -1
    if (isPhoneExist(this.getPhoneNumber())) {
      return -1;
    }

    // add in db:
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    String dbURL = "jdbc:mysql://localhost:3306/library";
    String user = "root";
    String password = "encoding1105";

    try {
      connection = DriverManager.getConnection(dbURL, user, password);
      System.out.println("Connection successful!");

      String query =
          "INSERT INTO user(name_user, birthday, phone_number_user, email_user, id_address, ban_date, avatar) "
              + "VALUES ( ?, ?, ?, ?, ?, ?, ?)";

      if (getAddressId(this.getAddress()) == -1) {
        connection = DriverManager.getConnection(dbURL, user, password);
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

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return this.getId();
  }
}
package org.example.demo.Models.Users;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Notification {

  private int id;
  private String title;
  private String content;
  private Date time;
  private int id_user;
  private boolean isSeen;

  public Notification(String title, String content, Date time, int id_user) {
    this.title = title;
    this.content = content;
    this.time = time;
    this.id_user = id_user;
    this.isSeen = false;
  }

  public Notification(String title, String content, Date time, int id_user,
      boolean isSeen) {
    this.title = title;
    this.content = content;
    this.time = time;
    this.id_user = id_user;
    this.isSeen = isSeen;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public int getId_user() {
    return id_user;
  }

  public void setId_user(int id_user) {
    this.id_user = id_user;
  }

  public boolean isSeen() {
    return isSeen;
  }

  public void setSeen(boolean seen) {
    isSeen = seen;
  }

  public void setSeen() {
    isSeen = true;
  }

  public int SaveInfo() {
    // add to db:
    Connection conn = null;
    PreparedStatement pstmt = null;
    String dbURL = "jdbc:mysql://localhost:3306/library";
    String user = "root";
    String password = "encoding1105";
    try {
      conn = DriverManager.getConnection(dbURL, user, password);
      System.out.println("Insert into user table...");
      String sql =
          "INSERT INTO notifications (title, content, id_user, is_seen) "
              + "VALUES (?, ?, ?, ?)";
      pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

      pstmt.setString(1, this.getTitle());
      pstmt.setString(2, this.getContent());
      pstmt.setInt(3, this.getId_user());
      pstmt.setBoolean(4, this.isSeen());

      // Execute the insert query
      int rowsAffected = pstmt.executeUpdate();
      System.out.println("Insert successfully! Rows affected: " + rowsAffected);

      ResultSet rs = pstmt.getGeneratedKeys();
      if (rs.next()) {
        this.setId(rs.getInt(1));
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
    return this.getId_user();
  }
}
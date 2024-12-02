package org.example.demo.Models.Users;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.demo.Database.JDBC;

public class Notification {

  /**
   * oke.
   */
  private int id;
  private String title;
  private String content;
  private Date time;
  /**
   * oke.
   */
  private int id_user;
  /**
   * oke.
   */
  private boolean isSeen;

  /**
   * oke.
   */
  public Notification(String title, String content, Date time, int id_user) {
    this.title = title;
    this.content = content;
    this.time = time;
    this.id_user = id_user;
    this.isSeen = false;
  }

  /**
   * oke.
   */
  public Notification(String title, String content, Date time, int id_user,
      boolean isSeen) {
    this.title = title;
    this.content = content;
    this.time = time;
    this.id_user = id_user;
    this.isSeen = isSeen;
  }

  /**
   * oke.
   */
  public int getId() {
    return this.id;
  }

  /**
   * oke.
   */
  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  /**
   * oke.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  /**
   * oke.
   */
  public void setContent(String content) {
    this.content = content;
  }

  public Date getTime() {
    return time;
  }

  /**
   * oke.
   */
  public void setTime(Date time) {
    this.time = time;
  }

  /**
   * oke.
   */
  public int getId_user() {
    return id_user;
  }

  /**
   * oke.
   */
  public void setId_user(int id_user) {
    this.id_user = id_user;
  }

  /**
   * oke.
   */
  public boolean isSeen() {
    return isSeen;
  }

  /**
   * oke.
   */
  public void setSeen(boolean seen) {
    isSeen = seen;
  }

  /**
   * oke.
   */
  public void setSeen() {
    isSeen = true;
  }

  /**
   * oke.
   */
  public int SaveInfo() {
    // add to db:
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = JDBC.getConnection();
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
      JDBC.closeConnection(conn);
    } catch (SQLException se) {
      se.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return this.getId_user();
  }
}

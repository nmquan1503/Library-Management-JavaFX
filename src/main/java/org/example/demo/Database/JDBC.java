package org.example.demo.Database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBC {

  public static Connection getConnection() {
    try {
      Properties properties = new Properties();
      FileInputStream fis = new FileInputStream("src/main/resources/database.properties");
      properties.load(fis);
      String JDBC_URL = properties.getProperty("jdbc.url");
      String JDBC_USERNAME = properties.getProperty("jdbc.username");
      String JDBC_PASSWORD = properties.getProperty("jdbc.password");
      return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void closeConnection(Connection connection) {
    if (connection != null) {
      try {
        connection.close(); // Đóng kết nối
      } catch (SQLException e) {
        System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
      }
    }
  }
}

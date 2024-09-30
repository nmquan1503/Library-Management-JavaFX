package org.example.demo.Models.Users;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

public class User extends Person {

  private Date banEndTime;

  public User(int id, String name, Date birthday, String address, Date banEndTime, String email,
      String phoneNumber,
      Image avatar) {
    super(id, name, birthday, address, email, phoneNumber, avatar);
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
  public void deleteFromDb() {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
//      conn = ....getConnection();
      String sql = "DELETE FROM user WHERE id_user = ?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, this.getId());
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

  // update from database
  public void updateFromDb(String columnName, Object newVal) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      // Assuming you have a method to get your connection
      // conn = ....getConnection();

      // Validate the column name to avoid SQL injection
      ArrayList<String> validColumns = (ArrayList<String>) Arrays.asList("name_user", "birthday",
          "phone_number_user",
          "email_user", "id_address", "unban_date", "avatar");
      if (!validColumns.contains(columnName)) {
        throw new IllegalArgumentException("Invalid column name: " + columnName);
      }

      String sql = "UPDATE user SET " + columnName + " = ? WHERE id_user = ?";

      // Create PreparedStatement
      pstmt = conn.prepareStatement(sql);

      // Set the correct data type based on newVal's type
      if (newVal instanceof String) {
        pstmt.setString(1, (String) newVal); // For VARCHAR and similar
      } else if (newVal instanceof LocalDate) {
        pstmt.setDate(1, Date.valueOf((LocalDate) newVal)); // For DATE type
      } else if (newVal instanceof Integer) {
        pstmt.setInt(1, (Integer) newVal); // For INT or other numeric types
      } else if (newVal instanceof InputStream) {
        pstmt.setBlob(1, (InputStream) newVal); // For BLOB type
      } else if (newVal == null) {
        pstmt.setNull(1, java.sql.Types.NULL); // Handle NULL values
      } else {
        throw new IllegalArgumentException("Unsupported data type for newVal");
      }

      pstmt.setInt(2, this.getId());

      int rowsAffected = pstmt.executeUpdate();

      // Check the result
      if (rowsAffected > 0) {
        System.out.println("Record updated successfully.");
      } else {
        System.out.println("No record found with the specified ID.");
      }
    } catch (SQLException se) {
      se.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // Clean up JDBC resources (close connection, statement, etc.)
      try {
        if (pstmt != null) {
          pstmt.close();
        }
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
    // add in db:
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      System.out.println("Insert into user table...");
      String sql =
          "INSERT INTO user "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
      pstmt = conn.prepareStatement(sql);

      pstmt.setInt(1, this.getId());
      pstmt.setString(2, this.getName());
      pstmt.setDate(3, this.getBirthday());
      pstmt.setString(4, this.getPhoneNumber());
      pstmt.setString(5, this.getEmail());
      pstmt.setInt(6, this.getAddressId(this.getAddress()));
      pstmt.setDate(7, this.getBanEndTime());

      byte[] avatarBytes = convertImageToBytes(
          this.getAvatar());
      pstmt.setBytes(8, avatarBytes);

      // Execute the insert query
      int rowsAffected = pstmt.executeUpdate();
      System.out.println("Insert successfully!");
      ;

      System.out.println("Insert successfully!");

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
    return this.getId();
  }

  private int getAddressId(String address) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      // Get the connection from your DatabaseConnection class
//      conn = DatabaseConnection.getConnection();

      String sql = "SELECT id_address FROM address WHERE name_address = ?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, address); // Set the parameter to the address

      rs = pstmt.executeQuery();

      if (rs.next()) {
        return rs.getInt("id_address");
      }

    } catch (SQLException se) {
      se.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (pstmt != null) {
          pstmt.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }

    return -1;
  }

  private byte[] convertImageToBytes(Image fxImage) {
    if (fxImage == null) {
      return null;
    }

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      // Create a writable image to extract pixel data
      WritableImage writableImage = new WritableImage(
          (int) fxImage.getWidth(),
          (int) fxImage.getHeight()
      );

      PixelReader pixelReader = fxImage.getPixelReader();
      writableImage.getPixelWriter().setPixels(
          0, 0,
          (int) fxImage.getWidth(),
          (int) fxImage.getHeight(),
          pixelReader,
          0, 0
      );

      BufferedImage bImage = new BufferedImage(
          (int) fxImage.getWidth(),
          (int) fxImage.getHeight(),
          BufferedImage.TYPE_INT_ARGB
      );

      for (int y = 0; y < fxImage.getHeight(); y++) {
        for (int x = 0; x < fxImage.getWidth(); x++) {
          bImage.setRGB(x, y, pixelReader.getArgb(x, y));
        }
      }

      // Write the BufferedImage to ByteArrayOutputStream as PNG (or any format you want)
      ImageIO.write(bImage, "png", baos);

      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
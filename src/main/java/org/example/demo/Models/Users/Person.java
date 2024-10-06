package org.example.demo.Models.Users;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import org.example.demo.Database.JDBC;

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

  public Person(String name, Date birthday, String address, String email,
      String phoneNumber) {
    this.name = name;
    this.birthday = birthday;
    this.address = address;
    this.email = email;
    this.phoneNumber = phoneNumber;
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

  public static void deleteFromDb(int id) {
  }

  ;

  protected static byte[] convertImageToBytes(Image fxImage) {
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

  protected static byte[] downloadImageFromURL(String urlString) throws Exception {
    URL url = new URL(urlString);
    try (InputStream inputStream = url.openStream(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
      return outputStream.toByteArray();
    }
  }

  protected static void retrieveImageFromDB(String outputImagePath)
      throws SQLException, IOException {
    Connection connection = JDBC.getConnection();

    String query = "SELECT image FROM test LIMIT 1";  // Lấy dòng đầu tiên trong bảng
    PreparedStatement preparedStatement = connection.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();

    if (resultSet.next()) {
      // Lấy dữ liệu BLOB dưới dạng byte array
      byte[] imageBytes = resultSet.getBytes("image");

      // Ghi dữ liệu byte ra file ảnh
      try (OutputStream outputStream = new FileOutputStream(outputImagePath)) {
        outputStream.write(imageBytes);
        System.out.println("Image has been written to " + outputImagePath);
      }
    } else {
      System.out.println("No image found in the table.");
    }

    JDBC.closeConnection(connection);
  }

  protected static int getAddressId(String address) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      conn = JDBC.getConnection();

      String sql = "SELECT id_address FROM address WHERE name_address = ?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, address); // Set the parameter to the address

      rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("id_address");
      }

    } catch (SQLException | IOException se) {
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

  protected static boolean isPhoneExist(String phone) {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
      connection = JDBC.getConnection();
      System.out.println("Connection successful!");

      String query = "SELECT phone_number_user FROM user";

      preparedStatement = connection.prepareStatement(query);

      // Execute the query and get the result
      resultSet = preparedStatement.executeQuery();

      // Process the result
      while (resultSet.next()) {
        // Retrieve the value of the "phone_number_user" column
        String phoneNumber = resultSet.getString("phone_number_user");
        if (phone.equals(phoneNumber)) {
          return true;
        }
      }

      String query2 = "SELECT phone_number_librarian FROM librarian";
      preparedStatement = connection.prepareStatement(query2);
      resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        String phoneNumber = resultSet.getString("phone_number_librarian");
        if (phone.equals(phoneNumber)) {
          return true;
        }
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
    return false;
  }
}

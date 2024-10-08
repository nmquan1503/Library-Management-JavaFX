package org.example.demo.Models.BookShelf;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import org.example.demo.Database.JDBC;

public class Book {


  private int idBook;
  private String title;
  private ArrayList<String> authors;
  private String publisher;
  private int publishedDate;
  private String description;
  private int pageCount;
  private ArrayList<String> categories;
  private double averageRating;
  private int ratingsCount;
  private String imageLink;
  private int quantity;


  public Book(int idBook, String title, ArrayList<String> authors, String publisher,
      int publishedDate,
      String description, int pageCount, ArrayList<String> categories, int ratingsCount,
      double averageRating, String imageLink,int quantity) {
    this.idBook = idBook;
    this.title = title;
    this.authors = authors;
    this.publisher = publisher;
    this.publishedDate = publishedDate;
    this.description = description;
    this.pageCount = pageCount;
    this.categories = categories;
    this.ratingsCount = ratingsCount;
    this.averageRating = averageRating;
    this.imageLink = imageLink;
    this.quantity=quantity;
  }

  public Book() {
    authors = new ArrayList<>();
    categories = new ArrayList<>();
  }

  public int getId() {
    return idBook;
  }

  public void setId(int id) {
    this.idBook = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ArrayList<String> getAuthors() {
    return authors;
  }

  public void setAuthors(ArrayList<String> authors) {
    this.authors = authors;
  }

  public int getPublishedDate() {
    return publishedDate;
  }

  public void setPublishedDate(int publishedDate) {
    this.publishedDate = publishedDate;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getPageCount() {
    return pageCount;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  public ArrayList<String> getCategories() {
    return categories;
  }

  public void setCategories(ArrayList<String> categories) {
    this.categories = categories;
  }

  public double getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(double averageRating) {
    this.averageRating = averageRating;
  }

  public int getRatingsCount() {
    return ratingsCount;
  }

  public void setRatingsCount(int ratingsCount) {
    this.ratingsCount = ratingsCount;
  }

  public String getImageLink() {
    return imageLink;
  }

  public void setImageLink(String imageLink) {
    this.imageLink = imageLink;
  }

  public int getQuantity(){return quantity;}

  public void setQuantity(int quantity){this.quantity=quantity;}

  public int SaveInfo() {
    try (Connection connection = JDBC.getConnection()) {
      // Tạo câu lệnh SQL với placeholders
      String sql = "INSERT INTO books (title, description, publisher, published_date, page_count, count_rating, average_rating, link_image, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
      // Sử dụng PreparedStatement để chèn dữ liệu
      PreparedStatement statement = connection.prepareStatement(sql,
          Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, title);
      statement.setString(2, description);
      statement.setString(3, publisher);
      statement.setInt(4, publishedDate);
      statement.setInt(5, pageCount);
      statement.setInt(6, ratingsCount);
      statement.setDouble(7, averageRating);
      statement.setString(8, imageLink);
      statement.setInt(9,quantity);
      try {
        statement.executeUpdate();
        ResultSet generatedKeys1 = statement.getGeneratedKeys();
        if (generatedKeys1.next()) {
          idBook = generatedKeys1.getInt(1);
        }
      } catch (SQLException e) {
        String selectQuery = "SELECT id_book FROM books WHERE title = ? AND link_image = ? ";
        PreparedStatement pstmt = connection.prepareStatement(selectQuery);
        pstmt.setString(1, title);
        pstmt.setString(2, imageLink);
        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
          idBook = resultSet.getInt("id_book");
        }
        return idBook;
      }
      if(authors!=null) {
        String sql1 = "INSERT INTO authors (name_author) VALUES (?)";
        String selectQuery = "SELECT id_author FROM authors WHERE name_author = ?";
        ArrayList<Integer> idAuthor = new ArrayList<>();
        PreparedStatement statement1 = connection.prepareStatement(sql1,
            Statement.RETURN_GENERATED_KEYS);
        for (String s : authors) {
          try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
            pstmt.setString(1, s);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
              idAuthor.add((Integer) rs.getInt("id_author"));
            } else {
              statement1.setString(1, s);
              statement1.executeUpdate();
              ResultSet generatedKeys = statement1.getGeneratedKeys();
              if (generatedKeys.next()) {
                idAuthor.add((Integer) generatedKeys.getInt(1));
              }
            }
          }
        }
        String sql2 = "INSERT INTO book_author (id_book, id_author) VALUES (?,?)";
        PreparedStatement statement2 = connection.prepareStatement(sql2);

        for (Integer x : idAuthor) {
          statement2.setInt(1, idBook);
          statement2.setInt(2, x.intValue());
          statement2.addBatch();
        }
        statement2.executeBatch();
      }
      if(categories!=null) {
        ArrayList<Integer> idCate = new ArrayList<>();
        String sql3 = "INSERT INTO categories (name_category) VALUES (?)";
        String selectQuery = "SELECT id_category FROM categories WHERE name_category = ?";
        PreparedStatement statement3 = connection.prepareStatement(sql3,
            Statement.RETURN_GENERATED_KEYS);
        for (String s : categories) {

          try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
            pstmt.setString(1, s);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
              idCate.add((Integer) rs.getInt("id_category"));
            } else {
              statement3.setString(1, s);
              statement3.executeUpdate();
              ResultSet generatedKeys = statement3.getGeneratedKeys();
              if (generatedKeys.next()) {
                idCate.add((Integer) generatedKeys.getInt(1));
              }
            }
          }
        }

        String sql4 = "INSERT INTO book_category (id_book,id_category) VALUES (?,?)";
        PreparedStatement statement4 = connection.prepareStatement(sql4);
        for (Integer x : idCate) {
          statement4.setInt(1, idBook);
          statement4.setInt(2, x.intValue());
          statement4.addBatch();
        }
        statement4.executeBatch();
      }
      JDBC.closeConnection(connection);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return idBook;
  }

  public void removeInfo() {
    Connection connection = JDBC.getConnection();

    try {
      String query = "delete from books " +
          "where id_book=(?)";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, idBook);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }

    JDBC.closeConnection(connection);
  }


}

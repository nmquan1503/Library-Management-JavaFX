package org.example.demo.Models.BookShelf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.example.demo.Database.JDBC;
import org.example.demo.Models.Trie.Trie;

public class BookShelf {

  private Trie books;

  /**
   * create trie of books table.
   */
  public BookShelf() {
    books = new Trie();
    Connection connection = JDBC.getConnection();
    try {
      String query = "select id_book, title " +
          "from books";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int id = resultSet.getInt("id_book");
        String title = resultSet.getString("title");
        books.insertNode(title, id);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    JDBC.closeConnection(connection);
  }

  /**
   * create a book that have a given id.
   *
   * @param id id of book.
   * @return book have this id.
   */
  public Book getBook(int id) {
    Book book = null;
    Connection connection = JDBC.getConnection();
    try {
      String query = "select title, description, publisher, published_date,page_count,count_rating,average_rating,link_image, quantity from books where id_book=(?)";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.isBeforeFirst()) {
        return null;
      }
      String title = null;
      ArrayList<String> authors = null;
      String publisher = null;
      int publishedDate = -1;
      String description = null;
      int pageCount = -1;
      ArrayList<String> categories = null;
      double averageRating = 0;
      int ratingsCount = 0;
      String imageLink = null;
      int quantity=0;
      while (resultSet.next()) {
        title = resultSet.getString("title");
        description = resultSet.getString("description");
        publisher = resultSet.getString("publisher");
        publishedDate = resultSet.getInt("published_date");
        pageCount = resultSet.getInt("page_count");
        ratingsCount = resultSet.getInt("count_rating");
        averageRating = resultSet.getDouble("average_rating");
        imageLink = resultSet.getString("link_image");
        quantity=resultSet.getInt("quantity");
      }
      query = "select authors.name_author as name_author " +
          "from authors " +
          "join book_author on authors.id_author=book_author.id_author " +
          "where book_author.id_book=(?)";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.isBeforeFirst()) {
        authors = new ArrayList<>();
        while (resultSet.next()) {
          authors.add(resultSet.getString("name_author"));
        }
      }
      query = "select categories.name_category as name_category " +
          "from categories " +
          "join book_category on categories.id_category=book_category.id_category " +
          "where book_category.id_book = (?)";
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.isBeforeFirst()) {
        categories = new ArrayList<>();
        while (resultSet.next()) {
          categories.add(resultSet.getString("name_category"));
        }
      }
      book = new Book(id, title, authors, publisher, publishedDate, description, pageCount,
          categories, ratingsCount, averageRating, imageLink,quantity);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBC.closeConnection(connection);
    }
    return book;
  }

  /**
   * insert book to trie and database.
   *
   * @param book book need to insert.
   * @return id of book.
   */
  public int insertBook(Book book) {
    int id = book.SaveInfo();
    books.insertNode(book.getTitle(), id);
    return id;
  }

  public int insertBookWithID(Book book, int idBook){
    int id=book.SaveInfo();
      Connection connection=JDBC.getConnection();
      try{
        String query="update books set id_book=(?) where id_book=(?)";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setInt(1,idBook);
        statement.setInt(2,id);
        statement.executeUpdate();
        book.setId(idBook);
      }catch (Exception e){
        e.printStackTrace();
      }
      JDBC.closeConnection(connection);
    books.insertNode(book.getTitle(),book.getId());
    return idBook;
  }

  /**
   * delete book from trie and database.
   *
   * @param book book need to delete.
   */
  public void deleteBook(Book book) {
    books.deleteNode(book.getTitle(), book.getId());
    book.removeInfo();
  }

  /**
   * create all book have name start with prefix.
   *
   * @param prefix word that books start with.
   * @return list book.
   */
  public ArrayList<Book> getListBook(String prefix) {
    ArrayList<Book> listBook = new ArrayList<>();
    ArrayList<Integer> listId = books.getListIdStartWith(prefix);
    for (int i : listId) {
      listBook.add(getBook(i));
    }
    return listBook;
  }

}

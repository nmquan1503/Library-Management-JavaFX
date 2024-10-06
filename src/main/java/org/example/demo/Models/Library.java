package org.example.demo.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.example.demo.Database.JDBC;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.BookShelf.BookShelf;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Users.Date;
import org.example.demo.Models.Users.User;
import org.example.demo.Models.Users.UserList;

public class Library {

  private BookShelf bookShelf;
  private UserList userList;

  public Library() {
    this.bookShelf = new BookShelf();
    this.userList = new UserList();
  }

  /**
   * save history of borrowing in database.
   * @param book a book borrowed by user.
   * @param user user borrow book.
   * @param borrowedDate day that user borrow book.
   * @return id_borrowing after add information in database.
   */
  public int borrowBook(Book book, User user, Date borrowedDate) {
    Connection connection=JDBC.getConnection();
    try {
      String query = "insert into borrowing(id_book,id_user,borrowed_date,due_date,returned_date) values (?,?,?,?,?)";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, book.getId());
      preparedStatement.setInt(2, user.getId());
      preparedStatement.setDate(3, borrowedDate);
      preparedStatement.setDate(4, borrowedDate.add(10));
      preparedStatement.setDate(5, null);
      preparedStatement.executeUpdate();
      ResultSet key = preparedStatement.getGeneratedKeys();
      if (key.next()) {
        return key.getInt(1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      JDBC.closeConnection(connection);
    }
    return -1;
  }

  /**
   * save the returnDate in database.
   * @param id_borrowing id of borrowing in database.
   * @param returnDate day that user return book to library.
   */
  public void returnBook(int id_borrowing, Date returnDate) {
    Connection connection=JDBC.getConnection();
    try {
      String query = "update borrowing set returned_date = (?) where id_borrowing = (?)";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setDate(1, returnDate);
      preparedStatement.setInt(2, id_borrowing);
      preparedStatement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      JDBC.closeConnection(connection);
    }
  }

  /**
   * @param prefix a word given to create suggestions of all book have prefix of title like that word.
   * @return list of suggestions.
   */
  public ArrayList<Suggestion> getUserSuggestions(String prefix) {
    ArrayList<Book> listBook = bookShelf.getListBook(prefix);
    ArrayList<Suggestion> listSuggestions = new ArrayList<>();
    for (Book book : listBook) {
      listSuggestions.add(new Suggestion(book));
    }
    return listSuggestions;
  }

  /**
   *
   * @param prefix a word given to create suggestions of all user have prefix of name like that word.
   * @return list of suggestions.
   */
  public ArrayList<Suggestion> getBookSuggestions(String prefix) {
    ArrayList<User> listUser = userList.getListUser(prefix);
    ArrayList<Suggestion> listSuggestions = new ArrayList<>();
    for (User user : listUser) {
      listSuggestions.add(new Suggestion(user));
    }
    return listSuggestions;
  }

}

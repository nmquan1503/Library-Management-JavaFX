package org.example.demo.Models;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import org.example.demo.Database.JDBC;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.BookShelf.BookShelf;
import org.example.demo.Models.Borrowing.BorrowHistory;
import org.example.demo.Models.Borrowing.Borrowing;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Users.Date;
import org.example.demo.Models.Users.User;
import org.example.demo.Models.Users.UserList;

public class Library {

  private static Library instance;
  static {
    instance=new Library();
  }

  private BookShelf bookShelf;
  private UserList userList;
  private BorrowHistory borrowHistory;

  private Library() {
    this.bookShelf = new BookShelf();
    this.userList = new UserList();
    borrowHistory = new BorrowHistory();
  }

  public static Library getInstance(){
    if(instance==null){
      instance=new Library();
    }
    return instance;
  }


  /**
   * save history of borrowing in database.
   *
   * @param book         a book borrowed by user.
   * @param user         user borrow book.
   * @param borrowedDate day that user borrow book.
   * @return id_borrowing after add information in database.
   */
  public int borrowBook(Book book, User user, Date borrowedDate) {
    return borrowHistory.addBorrowing(

        new Borrowing(-1, book.getId(), user.getId(), borrowedDate, borrowedDate.add(10), null));

  }
  public int borrowBook(Book book, User user, Date borrowedDate, Date dueDate) {
    return borrowHistory.addBorrowing(

            new Borrowing(-1, book.getId(), user.getId(), borrowedDate, dueDate, null));

  }


  /**
   * save the returnDate in database.
   *
   * @param id_borrowing id of borrowing in database.
   * @param returnDate   day that user return book to library.
   */
  public void returnBook(int id_borrowing, Date returnDate) {
    borrowHistory.updateReturnedDateOfBorrowing(id_borrowing, returnDate);
  }

  /**
   * @param prefix a word given to create suggestions of all book have prefix of title like that
   *               word.
   * @return list of suggestions.
   */
  public ArrayList<Suggestion> getBookSuggestions(String prefix) {
    ArrayList<Book> listBook = bookShelf.getListBook(prefix);
    ArrayList<Suggestion> listSuggestions = new ArrayList<>();
    for (Book book : listBook) {
      listSuggestions.add(new Suggestion(book));
    }
    return listSuggestions;
  }

  /**
   * @param prefix a word given to create suggestions of all user have prefix of name like that
   *               word.
   * @return list of suggestions.
   */
  public ArrayList<Suggestion> getUserSuggestions(String prefix) {
    ArrayList<User> listUser = userList.getListUser(prefix);
    ArrayList<Suggestion> listSuggestions = new ArrayList<>();
    for (User user : listUser) {
      if (user != null) {
        listSuggestions.add(new Suggestion(user));
      }
    }
    return listSuggestions;
  }

  public ArrayList<Suggestion> getBannedUserSuggestions(String prefix) {
    ArrayList<User> listUser = userList.getListUser(prefix);
    ArrayList<Suggestion> listSuggestions = new ArrayList<>();
    for (User user : listUser) {
      if (user.isBan()) {
        listSuggestions.add(new Suggestion(user));
      }
    }
    return listSuggestions;
  }

  public Book getBook(int idBook) {
    return bookShelf.getBook(idBook);
  }

  public User getUser(int idUser) {
    return userList.getUser(idUser);
  }

  public ArrayList<Borrowing> getAllHistory() {
    return borrowHistory.getAllHistory();
  }
  public ArrayList<Borrowing> getAllReturning() { return borrowHistory.getAllReturning(); }
  public ArrayList<Borrowing> getAllBorrowing() { return borrowHistory.getAllBorrowing(); }
  public ArrayList<Borrowing> getListBorrowingFromBook(int idBook) {
    return borrowHistory.getListBorrowingFromBook(idBook);
  }

  public ArrayList<Borrowing> getListBorrowingFromUser(int idUser) {
    return borrowHistory.getListBorrowingFromUser(idUser);
  }

  public Borrowing getBorrowing(int idBorrowing) {
    return borrowHistory.getBorrowing(idBorrowing);
  }

  public int insertUser(User user) {
    return userList.insertUser(user);
  }

  public int insertBook(Book book) {
    return bookShelf.insertBook(book);
  }

  public void deleteBook(Book book) {
    bookShelf.deleteBook(book);
  }

  public void deleteUser(User user) {
    userList.deleteUser(user);
  }

  public int insertBookWithID(Book book, int idBook) {
    return bookShelf.insertBookWithID(book, idBook);
  }

  public int insertUserWithID(User user, int idUser) {
    return userList.insertUserWithID(user, idUser);
  }




  public static void main(String[] args) {
    Library library = new Library();
    Image image = new Image(Objects.requireNonNull(
        Library.class.getResourceAsStream("/org/example/demo/Assets/basic.jpg")));
    User user = new User("Nguyễn Minh Quân",
        new Date(2005, 3, 15),
        "Thái Bình",
        "minhquan15032005@gmail.com",
        "0346399421",
        image,
        new Date(2000, 0, 0));
    library.insertUser(user);

  }



}

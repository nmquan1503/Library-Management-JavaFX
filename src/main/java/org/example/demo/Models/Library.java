package org.example.demo.Models;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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

  private BookShelf bookShelf;
  private UserList userList;
  private BorrowHistory borrowHistory;

  public Library() {
    this.bookShelf = new BookShelf();
    this.userList = new UserList();
    borrowHistory=new BorrowHistory();
  }

  /**
   * save history of borrowing in database.
   * @param book a book borrowed by user.
   * @param user user borrow book.
   * @param borrowedDate day that user borrow book.
   * @return id_borrowing after add information in database.
   */
  public int borrowBook(Book book, User user, Date borrowedDate) {
    return borrowHistory.addBorrowing(new Borrowing(-1,book.getId(),user.getId(),borrowedDate,borrowedDate.add(10),null));
  }

  /**
   * save the returnDate in database.
   * @param id_borrowing id of borrowing in database.
   * @param returnDate day that user return book to library.
   */
  public void returnBook(int id_borrowing, Date returnDate) {
    borrowHistory.updateReturnedDateOfBorrowing(id_borrowing,returnDate);
  }

  /**
   * @param prefix a word given to create suggestions of all book have prefix of title like that word.
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
   *
   * @param prefix a word given to create suggestions of all user have prefix of name like that word.
   * @return list of suggestions.
   */
  public ArrayList<Suggestion> getUserSuggestions(String prefix) {
    ArrayList<User> listUser = userList.getListUser(prefix);
    ArrayList<Suggestion> listSuggestions = new ArrayList<>();
    for (User user : listUser) {
      listSuggestions.add(new Suggestion(user));
    }
    return listSuggestions;
  }

  public Book getBook(int idBook){
    return bookShelf.getBook(idBook);
  }

  public User getUser(int idUser){
    return userList.getUser(idUser);
  }

  public ArrayList<Borrowing> getAllBorrowing(){
    return borrowHistory.getAllBorrowing();
  }
  
  public ArrayList<Borrowing> getListBorrowingFromBook(int idBook){
    return borrowHistory.getListBorrowingFromBook(idBook);
  }

  public ArrayList<Borrowing> getListBorrowingFromUser(int idUser){
    return borrowHistory.getListBorrowingFromUser(idUser);
  }

  public Borrowing getBorrowing(int idBorrowing){
    return borrowHistory.getBorrowing(idBorrowing);
  }


}

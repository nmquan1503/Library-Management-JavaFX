package org.example.demo.Models.Borrowing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.example.demo.Database.JDBC;
import org.example.demo.Models.Users.Date;

public class BorrowHistory {
  public BorrowHistory(){
  }

  /**
   * nếu chưa trả thì đứng đầu list, xếp theo dueDate tăng dần, borrowDate giảm dần.
   * nếu đã trả sort theo return giảm dần, borrow giảm dần
   */
  public ArrayList<Borrowing> getAllBorrowing(){
    ArrayList<Borrowing> history=new ArrayList<>();
    Connection connection= JDBC.getConnection();
    try {
      String query = "select id_borrowing, " +
          "id_book, " +
          "id_user, " +
          "borrowed_date, " +
          "due_date, " +
          "from borrowing " +
          "where returned_date is null " +
          "order by due_date asc, borrowed_date desc";
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        int idBorrowing=resultSet.getInt("id_borrowing");
        int idBook = resultSet.getInt("id_book");
        int idUser = resultSet.getInt("id_user");
        Date borrowedDate = (Date) resultSet.getDate("borrowed_date");
        Date dueDate = (Date) resultSet.getDate("due_date");
        history.add(new Borrowing(idBorrowing,idBook, idUser, borrowedDate, dueDate, null));
      }

      query = "select id_borrowing, "+
          "id_book, " +
          "id_user, " +
          "borrowed_date, " +
          "due_date, " +
          "returned_date, " +
          "from borrowing " +
          "where returned_date is not null" +
          "order by returned_date desc,borrowed_date desc";
      statement=connection.prepareStatement(query);
      resultSet=statement.executeQuery();
      while(resultSet.next()){
        int idBorrowing=resultSet.getInt("id_borrowing");
        int idBook = resultSet.getInt("id_book");
        int idUser = resultSet.getInt("id_user");
        Date borrowedDate = (Date) resultSet.getDate("borrowed_date");
        Date dueDate = (Date) resultSet.getDate("due_date");
        Date returnedDate = (Date) resultSet.getDate("returned_date");
        history.add(new Borrowing(idBorrowing,idBook, idUser, borrowedDate, dueDate, returnedDate));
      }
    }
    catch (Exception e){
      e.printStackTrace();
    }
    JDBC.closeConnection(connection);
    return history;
  }

  public int addBorrowing(Borrowing borrowing){
    Connection connection=JDBC.getConnection();
    try {

      String query="select quantity "+
          "from books "+
          "where id_book=(?)";
      PreparedStatement preparedStatement=connection.prepareStatement(query);
      preparedStatement.setInt(1,borrowing.getIdBook());
      ResultSet resultSet=preparedStatement.executeQuery();
      int quantityBook=0;
      if(resultSet.next()){
        quantityBook=resultSet.getInt("quantity");
        if(quantityBook==0)return -1;
      }
      query= "update books "+
          "set quantity = (?) "+
          "where id_book = (?)";
      preparedStatement=connection.prepareStatement(query);
      preparedStatement.setInt(1,quantityBook-1);
      preparedStatement.setInt(2,borrowing.getIdBook());
      preparedStatement.executeUpdate();

      query = "insert into borrowing(id_book,id_user,borrowed_date,due_date,returned_date) values (?,?,?,?,?)";
      preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, borrowing.getIdBook());
      preparedStatement.setInt(2, borrowing.getIdUser());
      preparedStatement.setDate(3, borrowing.getBorrowedDate());
      preparedStatement.setDate(4, borrowing.getDueDate());
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

  public void updateReturnedDateOfBorrowing(int idBorrowing, Date returnedDate){
    Connection connection=JDBC.getConnection();
    try {
      String query = "update borrowing set returned_date = (?) where id_borrowing = (?)";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setDate(1, returnedDate);
      preparedStatement.setInt(2, idBorrowing);
      preparedStatement.executeUpdate();

      query=  "select borrowing.id_book as id_book"+
          "books.quantity as quantity"+
          "from borrowing "+
          "join books on borrowing.id_book=books.id_book "+
          "where borrowing.id_borrowing = (?)";
      preparedStatement=connection.prepareStatement(query);
      ResultSet resultSet=preparedStatement.executeQuery();
      if(resultSet.next()){
        int id_book=resultSet.getInt("id_book");
        int quantity=resultSet.getInt("quantity");
        query="update books "+
            "set quantity = (?) "+
            "where id_book = (?)";
        preparedStatement=connection.prepareStatement(query);
        preparedStatement.setInt(1,quantity+1);
        preparedStatement.setInt(2,id_book);
        preparedStatement.executeUpdate();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      JDBC.closeConnection(connection);
    }
  }

  public Borrowing getBorrowing(int idBorrowing){
    Connection connection=JDBC.getConnection();
    try {
      String query= "select id_book, "+
                            "id_user, "+
                            "borrowed_date, "+
                            "due_date, "+
                            "returned_date "+
                            "from borrowing "+
                            "where id_borrowing = (?)";
      PreparedStatement preparedStatement=connection.prepareStatement(query);
      preparedStatement.setInt(1,idBorrowing);
      ResultSet resultSet=preparedStatement.executeQuery();
      if(resultSet.next()){
        return new Borrowing(
            idBorrowing,
            resultSet.getInt("id_book"),
            resultSet.getInt("id_user"),
            (Date) resultSet.getDate("borrowed_date"),
            (Date) resultSet.getDate("due_date"),
            (Date) resultSet.getDate("returned_date")
        );
      }
    }
    catch (Exception e){
      e.printStackTrace();
    }
    JDBC.closeConnection(connection);
    return null;
  }

  public ArrayList<Borrowing> getListBorrowingFromBook(int idBook){
    Connection connection=JDBC.getConnection();
    ArrayList<Borrowing> history=new ArrayList<>();
    try {
      String query = "select id_borrowing, " +
          "id_user, " +
          "borrowed_date, " +
          "due_date, " +
          "from borrowing " +
          "where returned_date is null and id_book = (?) " +
          "order by due_date asc, borrowed_date desc";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1,idBook);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        int idBorrowing=resultSet.getInt("id_borrowing");
        int idUser = resultSet.getInt("id_user");
        Date borrowedDate = (Date) resultSet.getDate("borrowed_date");
        Date dueDate = (Date) resultSet.getDate("due_date");
        history.add(new Borrowing(idBorrowing,idBook, idUser, borrowedDate, dueDate, null));
      }

      query = "select id_borrowing, "+
          "id_user, " +
          "borrowed_date, " +
          "due_date, " +
          "returned_date, " +
          "from borrowing " +
          "where returned_date is not null and id_book = (?)" +
          "order by returned_date desc,borrowed_date desc";
      statement=connection.prepareStatement(query);
      statement.setInt(1,idBook);
      resultSet=statement.executeQuery();
      while(resultSet.next()){
        int idBorrowing=resultSet.getInt("id_borrowing");
        int idUser = resultSet.getInt("id_user");
        Date borrowedDate = (Date) resultSet.getDate("borrowed_date");
        Date dueDate = (Date) resultSet.getDate("due_date");
        Date returnedDate = (Date) resultSet.getDate("returned_date");
        history.add(new Borrowing(idBorrowing,idBook, idUser, borrowedDate, dueDate, returnedDate));
      }
    }
    catch (Exception e){
      e.printStackTrace();
    }
    JDBC.closeConnection(connection);
    return history;
  }

  public ArrayList<Borrowing> getListBorrowingFromUser(int idUser){
    ArrayList<Borrowing> history=new ArrayList<>();
    Connection connection=JDBC.getConnection();
    try {
      String query = "select id_borrowing, " +
          "id_book, " +
          "borrowed_date, " +
          "due_date, " +
          "from borrowing " +
          "where returned_date is null and id_user = (?) " +
          "order by due_date asc, borrowed_date desc";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1,idUser);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        int idBorrowing=resultSet.getInt("id_borrowing");
        int idBook = resultSet.getInt("id_book");
        Date borrowedDate = (Date) resultSet.getDate("borrowed_date");
        Date dueDate = (Date) resultSet.getDate("due_date");
        history.add(new Borrowing(idBorrowing,idBook, idUser, borrowedDate, dueDate, null));
      }

      query = "select id_borrowing, "+
          "id_book, " +
          "borrowed_date, " +
          "due_date, " +
          "returned_date, " +
          "from borrowing " +
          "where returned_date is not null and id_user = (?)" +
          "order by returned_date desc,borrowed_date desc";
      statement=connection.prepareStatement(query);
      statement.setInt(1,idUser);
      resultSet=statement.executeQuery();
      while(resultSet.next()){
        int idBorrowing=resultSet.getInt("id_borrowing");
        int idBook = resultSet.getInt("id_book");
        Date borrowedDate = (Date) resultSet.getDate("borrowed_date");
        Date dueDate = (Date) resultSet.getDate("due_date");
        Date returnedDate = (Date) resultSet.getDate("returned_date");
        history.add(new Borrowing(idBorrowing,idBook, idUser, borrowedDate, dueDate, returnedDate));
      }
    }
    catch (Exception e){
      e.printStackTrace();
    }
    JDBC.closeConnection(connection);
    return history;
  }

  public ArrayList<Borrowing> getListBorrowingNearingDeadline(){
    ArrayList<Borrowing> list=new ArrayList<>();

    Connection connection=JDBC.getConnection();
    try {
      String query ="select id_borrowing,"
          + "id_book,"
          + "id_user,"
          + "borrowed_date,"
          + "due_date,"
          + "returned_date "
          + "from borrowing "
          + "where returned_date is null and datediff(now(),due_date)<=1 "
          + "order by due_date";
      PreparedStatement preparedStatement=connection.prepareStatement(query);
      ResultSet resultSet=preparedStatement.executeQuery();
      while (resultSet.next()){
        int id_borrowing=resultSet.getInt("id_borrowing");
        int id_book=resultSet.getInt("id_book");
        int id_user=resultSet.getInt("id_user");
        Date due_date=new Date(resultSet.getDate("due_date"));
        Date returned_date=new Date(resultSet.getDate("returned_date"));
        Date borrowed_date=new Date(resultSet.getDate("borrowed_date"));
        list.add(new Borrowing(id_borrowing,id_book,id_user,borrowed_date,due_date,returned_date));
      }
    }
    catch (Exception e){
      e.printStackTrace();
    }
    finally {
      JDBC.closeConnection(connection);
    }
    return list;
  }

}

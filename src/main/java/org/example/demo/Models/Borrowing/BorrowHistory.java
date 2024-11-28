package org.example.demo.Models.Borrowing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.example.demo.Database.JDBC;
import org.example.demo.Models.Users.Date;

public class BorrowHistory {

    public BorrowHistory() {
    }

    /**
     * nếu chưa trả thì đứng đầu list, xếp theo dueDate tăng dần, borrowDate giảm dần. nếu đã trả
     * sort theo return giảm dần, borrow giảm dần
     */
    public ArrayList<Borrowing> getAllHistory() {
        ArrayList<Borrowing> history = new ArrayList<>();
        Connection connection = JDBC.getConnection();
        try {
            String query = "SELECT id_borrowing, " +
                    "id_book, " +
                    "id_user, " +
                    "borrowed_date, " +
                    "due_date, " +
                    "returned_date " +
                    "FROM borrowing " +
                    "ORDER BY id_borrowing DESC;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idBorrowing = resultSet.getInt("id_borrowing");
                int idBook = resultSet.getInt("id_book");
                int idUser = resultSet.getInt("id_user");
                Date borrowedDate = new Date(resultSet.getDate("borrowed_date"));
                Date dueDate = new Date(resultSet.getDate("due_date"));
                Date test;
                if (resultSet.getDate("returned_date") != null) {
                    test = new Date(resultSet.getDate("returned_date"));
                } else {
                    test = null;
                }
                history.add(
                        new Borrowing(idBorrowing, idBook, idUser, borrowedDate, dueDate, test));
            }

//      query = "select id_borrowing, "+
//          "id_book, " +
//          "id_user, " +
//          "borrowed_date, " +
//          "due_date, " +
//          "returned_date " +
//          "from borrowing " +
//          "where returned_date is not null " +
//          "order by returned_date desc,borrowed_date desc";
//      statement=connection.prepareStatement(query);
//      resultSet=statement.executeQuery();
//      while(resultSet.next()){
//        int idBorrowing=resultSet.getInt("id_borrowing");
//        int idBook = resultSet.getInt("id_book");
//        int idUser = resultSet.getInt("id_user");
//        Date borrowedDate = (Date) resultSet.getDate("borrowed_date");
//        Date dueDate = (Date) resultSet.getDate("due_date");
//        Date returnedDate = (Date) resultSet.getDate("returned_date");
//        history.add(new Borrowing(idBorrowing,idBook, idUser, borrowedDate, dueDate, returnedDate));
//      }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBC.closeConnection(connection);
        return history;
    }

    public ArrayList<Borrowing> getAllReturning() {
        ArrayList<Borrowing> history = new ArrayList<>();
        Connection connection = JDBC.getConnection();
        try {
            String query;
            PreparedStatement statement;
            ResultSet resultSet;
            query = "select id_borrowing, " +
                    "id_book, " +
                    "id_user, " +
                    "borrowed_date, " +
                    "due_date, " +
                    "returned_date " +
                    "from borrowing " +
                    "where returned_date is not null " +
                    "order by id_borrowing desc";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idBorrowing = resultSet.getInt("id_borrowing");
                int idBook = resultSet.getInt("id_book");
                int idUser = resultSet.getInt("id_user");
                Date borrowedDate = new Date(resultSet.getDate("borrowed_date"));
                Date dueDate = new Date(resultSet.getDate("due_date"));
                Date returnedDate = new Date(resultSet.getDate("returned_date"));
                history.add(
                        new Borrowing(idBorrowing, idBook, idUser, borrowedDate, dueDate,
                                returnedDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBC.closeConnection(connection);
        return history;
    }

    public ArrayList<Borrowing> getAllBorrowing() {
        ArrayList<Borrowing> history = new ArrayList<>();
        Connection connection = JDBC.getConnection();
        try {
            String query;
            PreparedStatement statement;
            ResultSet resultSet;
            query = "select id_borrowing, " +
                    "id_book, " +
                    "id_user, " +
                    "borrowed_date, " +
                    "due_date, " +
                    "returned_date " +
                    "from borrowing " +
                    "where returned_date is null " +
                    "order by id_borrowing desc";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idBorrowing = resultSet.getInt("id_borrowing");
                int idBook = resultSet.getInt("id_book");
                int idUser = resultSet.getInt("id_user");
                Date borrowedDate = new Date(resultSet.getDate("borrowed_date"));
                Date dueDate = new Date(resultSet.getDate("due_date"));
                history.add(
                        new Borrowing(idBorrowing, idBook, idUser, borrowedDate, dueDate, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBC.closeConnection(connection);
        return history;
    }

    public int addBorrowing(Borrowing borrowing) {
        Connection connection = JDBC.getConnection();
        try {

            String query = "select quantity " +
                    "from books " +
                    "where id_book=(?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, borrowing.getIdBook());
            ResultSet resultSet = preparedStatement.executeQuery();
            int quantityBook = 0;
            if (resultSet.next()) {
                quantityBook = resultSet.getInt("quantity");
                if (quantityBook == 0) {
                    return -1;
                }
            }
            query = "update books " +
                    "set quantity = (?) " +
                    "where id_book = (?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, quantityBook - 1);
            preparedStatement.setInt(2, borrowing.getIdBook());
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
        } finally {
            JDBC.closeConnection(connection);
        }
        return -1;
    }


    public void updateReturnedDateOfBorrowing(int idBorrowing, Date returnedDate) {
        Connection connection = JDBC.getConnection();
        try {
            String query = "SELECT MAX(id_borrowing) + 1 FROM borrowing";
            PreparedStatement selectStatement = connection.prepareStatement(query);
            ResultSet resultSet = selectStatement.executeQuery();

            int newIdBorrowing = 0;
            if (resultSet.next()) {
                newIdBorrowing = resultSet.getInt(1); // Lấy giá trị MAX(id_borrowing) + 1
            }
            resultSet.close();
            selectStatement.close();

            String updateQuery = "UPDATE borrowing SET returned_date = ?, id_borrowing = ? WHERE id_borrowing = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDate(1, returnedDate);
            preparedStatement.setInt(2, newIdBorrowing); // Giá trị mới cho id_borrowing
            preparedStatement.setInt(3, idBorrowing); // id_borrowing hiện tại để tìm hàng cần cập nhật
            preparedStatement.executeUpdate();

            query = "select borrowing.id_book as id_book, " +
                    "books.quantity as quantity " +
                    "from borrowing " +
                    "join books on borrowing.id_book=books.id_book " +
                    "where borrowing.id_borrowing = (?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idBorrowing);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id_book = resultSet.getInt("id_book");
                int quantity = resultSet.getInt("quantity");
                query = "update books " +
                        "set quantity = (?) " +
                        "where id_book = (?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, quantity + 1);
                preparedStatement.setInt(2, id_book);
                preparedStatement.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.closeConnection(connection);
        }
    }

    public void updateDueDateOfBorrowing(int idBorrowing, Date dueDate ) {
        Connection connection = JDBC.getConnection();
        try {
            String query = "update borrowing set due_date = (?) where id_borrowing = (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, dueDate);
            preparedStatement.setInt(2, idBorrowing);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.closeConnection(connection);
        }
    }

    public Borrowing getBorrowing(int idBorrowing) {
        Connection connection = JDBC.getConnection();
        try {
            String query = "select id_book, " +
                    "id_user, " +
                    "borrowed_date, " +
                    "due_date, " +
                    "returned_date " +
                    "from borrowing " +
                    "where id_borrowing = (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idBorrowing);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Borrowing(
                        idBorrowing,
                        resultSet.getInt("id_book"),
                        resultSet.getInt("id_user"),
                        (Date) resultSet.getDate("borrowed_date"),
                        (Date) resultSet.getDate("due_date"),
                        (Date) resultSet.getDate("returned_date")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBC.closeConnection(connection);
        return null;
    }

    public ArrayList<Borrowing> getListBorrowingFromBook(int idBook) {
        Connection connection = JDBC.getConnection();
        ArrayList<Borrowing> history = new ArrayList<>();
        try {
            String query = "select id_borrowing, " +
                    "id_user, " +
                    "borrowed_date, " +
                    "due_date " +
                    "from borrowing " +
                    "where returned_date is null and id_book = (?) " +
                    "order by due_date asc, borrowed_date desc, id_borrowing desc";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idBook);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idBorrowing = resultSet.getInt("id_borrowing");
                int idUser = resultSet.getInt("id_user");
                Date borrowedDate = new Date(resultSet.getDate("borrowed_date"));
                Date dueDate =  new Date(resultSet.getDate("due_date"));
                history.add(
                        new Borrowing(idBorrowing, idBook, idUser, borrowedDate, dueDate, null));
            }

//            query = "select id_borrowing, " +
//                    "id_user, " +
//                    "borrowed_date, " +
//                    "due_date, " +
//                    "returned_date " +
//                    "from borrowing " +
//                    "where returned_date is not null and id_book = (?)" +
//                    "order by returned_date desc,borrowed_date desc, id_borrowing desc";
//            statement = connection.prepareStatement(query);
//            statement.setInt(1, idBook);
//            resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                int idBorrowing = resultSet.getInt("id_borrowing");
//                int idUser = resultSet.getInt("id_user");
//                Date borrowedDate = new Date(resultSet.getDate("borrowed_date"));
//                Date dueDate = new Date(resultSet.getDate("due_date"));
//                Date returnedDate = new Date(resultSet.getDate("returned_date"));
//                history.add(
//                        new Borrowing(idBorrowing, idBook, idUser, borrowedDate, dueDate,
//                                returnedDate));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBC.closeConnection(connection);
        return history;
    }

    public ArrayList<Borrowing> getListBorrowingFromUser(int idUser) {
        ArrayList<Borrowing> history = new ArrayList<>();
        Connection connection = JDBC.getConnection();
        try {
            String query = "select id_borrowing, " +
                    "id_book, " +
                    "borrowed_date, " +
                    "due_date " +
                    "from borrowing " +
                    "where returned_date is null and id_user = (?) " +
                    "order by due_date asc, borrowed_date desc, id_borrowing desc";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idUser);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idBorrowing = resultSet.getInt("id_borrowing");
                int idBook = resultSet.getInt("id_book");
                Date borrowedDate = new Date(resultSet.getDate("borrowed_date"));
                Date dueDate = new Date(resultSet.getDate("due_date"));
                history.add(
                        new Borrowing(idBorrowing, idBook, idUser, borrowedDate, dueDate, null));
            }

//      query = "select id_borrowing, " +
//          "id_book, " +
//          "borrowed_date, " +
//          "due_date, " +
//          "returned_date " +
//          "from borrowing " +
//          "where returned_date is not null and id_user = (?)" +
//          "order by returned_date desc,borrowed_date desc";
//      statement = connection.prepareStatement(query);
//      statement.setInt(1, idUser);
//      resultSet = statement.executeQuery();
//      while (resultSet.next()) {
//        int idBorrowing = resultSet.getInt("id_borrowing");
//        int idBook = resultSet.getInt("id_book");
//        Date borrowedDate = (Date) resultSet.getDate("borrowed_date");
//        Date dueDate = (Date) resultSet.getDate("due_date");
//        Date returnedDate = (Date) resultSet.getDate("returned_date");
//        history.add(
//            new Borrowing(idBorrowing, idBook, idUser, borrowedDate, dueDate, returnedDate));
//      }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBC.closeConnection(connection);
        return history;
    }

    public ArrayList<Borrowing> getListBorrowingFromUserName(String prefixName) {
        ArrayList<Borrowing> history = new ArrayList<>();
        Connection connection = JDBC.getConnection();
        try {
            String query = "SELECT b.id_borrowing, " +
                    "b.id_user, " +
                    "u.name_user, " +
                    "b.id_book, " +
                    "b.borrowed_date, " +
                    "b.due_date " +
                    "FROM borrowing b " +
                    "JOIN user u ON b.id_user = u.id_user " +
                    "WHERE b.returned_date IS NULL AND u.name_user LIKE ? " +
                    "ORDER BY b.due_date ASC, b.borrowed_date DESC, b.id_borrowing DESC";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, prefixName + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idBorrowing = resultSet.getInt("id_borrowing");
                int idBook = resultSet.getInt("id_book");
                Date borrowedDate = new Date(resultSet.getDate("borrowed_date"));
                Date dueDate = new Date(resultSet.getDate("due_date"));
                int idUser = resultSet.getInt("id_user");
                history.add(
                        new Borrowing(idBorrowing, idBook, idUser, borrowedDate, dueDate, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBC.closeConnection(connection);
        return history;
    }

    public ArrayList<Borrowing> getListBorrowingFromBookName(String prefixName) {
        ArrayList<Borrowing> history = new ArrayList<>();
        Connection connection = JDBC.getConnection();
        try {
            String query = "SELECT b.id_borrowing, " +
                    "b.id_user, " +
                    "u.title, " +
                    "b.id_book, " +
                    "b.borrowed_date, " +
                    "b.due_date " +
                    "FROM borrowing b " +
                    "JOIN books u ON b.id_book = u.id_book " +
                    "WHERE b.returned_date IS NULL AND u.title LIKE ? " +
                    "ORDER BY b.due_date ASC, b.borrowed_date DESC, b.id_borrowing DESC";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, prefixName + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idBorrowing = resultSet.getInt("id_borrowing");
                int idBook = resultSet.getInt("id_book");
                Date borrowedDate = new Date(resultSet.getDate("borrowed_date"));
                Date dueDate = new Date(resultSet.getDate("due_date"));
                int idUser = resultSet.getInt("id_user");
                history.add(
                        new Borrowing(idBorrowing, idBook, idUser, borrowedDate, dueDate, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDBC.closeConnection(connection);
        return history;
    }

    public ArrayList<Borrowing> getListBorrowingNearingDeadline() {
        ArrayList<Borrowing> list = new ArrayList<>();

        Connection connection = JDBC.getConnection();
        try {
            String query = "select id_borrowing,"
                    + "id_book,"
                    + "id_user,"
                    + "borrowed_date,"
                    + "due_date,"
                    + "returned_date "
                    + "from borrowing "
                    + "where returned_date is null and datediff(now(),due_date)>=1 "
                    + "order by due_date";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id_borrowing = resultSet.getInt("id_borrowing");
                int id_book = resultSet.getInt("id_book");
                int id_user = resultSet.getInt("id_user");
                Date due_date = null;
                if (resultSet.getDate("due_date") != null) {
                    due_date = new Date(resultSet.getDate("due_date"));
                }
                Date returned_date = null;
                if (resultSet.getDate("returned_date") != null) {
                    returned_date = new Date(resultSet.getDate("returned_date"));
                }
                Date borrowed_date = null;
                if (resultSet.getDate("borrowed_date") != null) {
                    borrowed_date = new Date(resultSet.getDate("borrowed_date"));
                }
                list.add(
                        new Borrowing(id_borrowing, id_book, id_user, borrowed_date, due_date,
                                returned_date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBC.closeConnection(connection);
        }
        return list;
    }
}

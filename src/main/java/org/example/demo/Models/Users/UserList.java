package org.example.demo.Models.Users;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.scene.image.Image;
import org.example.demo.Database.JDBC;
import org.example.demo.Models.Trie.Trie;

public class UserList {

  private Trie users;

  /**
   * create trie of user table
   */
  public UserList() {
    users = new Trie();
    Connection connection = JDBC.getConnection();

    try {
      String query = "select id_user, name_user " +
          "from user";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int id = resultSet.getInt("id_user");
        String name = resultSet.getString("name_user");
        users.insertNode(name, id);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    JDBC.closeConnection(connection);
  }

  /**
   * @param id id of user.
   * @return user have this id.
   */
  public User getUser(int id) {
    User user = null;
    Connection connection = JDBC.getConnection();
    try {
      String query = "select user.name_user as name_user, " +
          "user.birthday as birthday, " +
          "user.phone_number_user as phone_number, " +
          "user.email_user as email, " +
          "address.name_address as address, " +
          "user.ban_date as ban_date, " +
          "user.avatar as avatar " +
          "from user left join address on user.id_address=address.id_address " +
          "where user.id_user = (?)";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (!resultSet.isBeforeFirst()) {
        return null;
      }
      resultSet.next();
      String name_user = resultSet.getString("name_user");
      Date birthday = null;
      if (resultSet.getDate("birthday") != null) {
        birthday = new Date(resultSet.getDate("birthday"));
      }
      String phone_number = resultSet.getString("phone_number");
      String email_user = resultSet.getString("email");
      String address = resultSet.getString("address");
      Date ban_date = null;
      if (resultSet.getDate("ban_date") != null) {
        ban_date = new Date(resultSet.getDate("ban_date"));
      }
      Image avatar = createImageFromBlob(resultSet.getBinaryStream("avatar"));
      user = new User(id, name_user, birthday, address, email_user, avatar, phone_number, ban_date);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      JDBC.closeConnection(connection);
    }
    return user;
  }

  /**
   * create a Image from blob.
   *
   * @param inputStream blob created from a image.
   * @return Image.
   */
  private Image createImageFromBlob(InputStream inputStream) {
    if (inputStream == null) {
      return null;
    }
    return new Image(inputStream);
  }

  /**
   * insert user into trie and database.
   *
   * @param user user need to save info.
   * @return id of user.
   */
  public int insertUser(User user) {
    int id = user.SaveInfo();
    users.insertNode(user.getName(), id);
    return id;
  }

  /**
   * delete user from trie and database.
   *
   * @param user user need to delete.
   */
  public void deleteUser(User user) {
    user.removeInfo();
    users.deleteNode(user.getName(), user.getId());
  }

  /**
   * get all user have name start with prefixName.
   *
   * @param prefixName word given to search all user have name start with.
   * @return list user.
   */
  public ArrayList<User> getListUser(String prefixName) {
    ArrayList<User> listUser = new ArrayList<>();
    ArrayList<Integer> listId = users.getListIdStartWith(prefixName);
    for (int i : listId) {
      listUser.add(getUser(i));
    }
    return listUser;
  }

  public int insertUserWithID(User user, int idUser) {
    int id = user.SaveInfo();
    Connection connection = JDBC.getConnection();
    try {
      String query = "update user set id_user=(?) where id_user=(?)";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, idUser);
      statement.setInt(2, id);
      statement.executeUpdate();
      user.setId(idUser);
    } catch (Exception e) {
      e.printStackTrace();
    }
    JDBC.closeConnection(connection);
    users.insertNode(user.getName(), user.getId());
    return idUser;
  }

}

package org.example.demo.Models.Users;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.scene.image.Image;
import org.example.demo.Models.Trie.Trie;

public class UserList {
  private Trie users;
  public UserList(){
    users=new Trie();
  }

  public User getUser(int id){
    User user=null;
    try{
      Connection connection=JDBC.getConnection();
      String query="select user.name_user as name_user, user.birthday as birthday, user.phone_number_user as phone_number, user.email_user as email, address.name_address as address, user.ban_date as ban_date, user.avatar as avatar from user join address in user.id_address=address.id_address where user.id_user = (?)";
      PreparedStatement preparedStatement=connection.prepareStatement(query);
      preparedStatement.setInt(1,id);
      ResultSet resultSet=preparedStatement.executeQuery();
      if(!resultSet.isBeforeFirst())return null;
      resultSet.next();
      String name_user=resultSet.getString("name_user");
      Date birthday=resultSet.getDate("birthday");
      String phone_number=resultSet.getString("phone_number");
      String email_user=resultSet.getString("email");
      String address=resultSet.getString("address");
      Date ban_date=resultSet.getDate("ban_date");
      Image avatar=createImageFromBlob(resultSet.getBinaryStream("avatar"));
      user=new User(name_user,birthday,phone_number,email_user,address,ban_date,avatar);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    return user;
  }

  private Image createImageFromBlob(InputStream inputStream){
    if(inputStream==null)return null;
    return new Image(inputStream);
  }

  public int insertUser(User user){
    int id=user.saveInfo();
    users.insertNode(user.getName(),id);
    return id;
  }

  public void deleteUser(User user){
    user.removeInfo();
    users.deleteNode(user.getName(),user.getId());
  }

  public ArrayList<User> getListUser(String prefixName){
    ArrayList<User> listUser=null;
    ArrayList<Integer>listId=users.getListIdStartWith(prefixName);
    for(int i:listId){
      listUser.add(getUser(i));
    }
    return listUser;
  }

}

package org.example.demo.CustomUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.example.demo.Database.JDBC;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Users.Date;
import org.example.demo.Models.Users.User;

public class EditUserView extends ScrollPane {

  @FXML private ImageView imageUser;
  @FXML private TextField nameTextField;
  @FXML private Label idLabel;
  @FXML private TextField birthdayTextField;
  @FXML private TextField addressTextField;
  @FXML private TextField phoneNumberTextField;
  @FXML private TextField emailTextField;
  @FXML private TextField endBanDateTextField;

  private Library library;
  private ArrayList<Suggestion> listUsers;
  private Runnable laterAction;
  private User oldUser;

  public EditUserView(Library library, ArrayList<Suggestion> listUsers,Runnable laterAction){

    initView();
    initImage(null);
    initId(null);
    initBirthday(null);
    initPhoneNumber(null);
    initEndBanDate(null);
    this.library=library;
    this.listUsers=listUsers;
    this.laterAction=laterAction;
    this.oldUser=null;
  }

  public EditUserView(Library library,ArrayList<Suggestion> listUsers, User oldUser,Runnable laterAction){

    initView();
    initImage(oldUser);
    initName(oldUser);
    initId(oldUser);
    initBirthday(oldUser);
    initAddress(oldUser);
    initPhoneNumber(oldUser);
    initEmail(oldUser);
    initEndBanDate(oldUser);

    this.library=library;
    this.listUsers=listUsers;
    this.oldUser=oldUser;
    this.laterAction=laterAction;
  }

  private void initEndBanDate(User user){
    setupDateTextField(endBanDateTextField);
    if(user!=null){
      if(user.getBanEndTime()!=null){
        Date date=new Date(user.getBanEndTime());
        endBanDateTextField.setText(date.getDay()+"/"+date.getMonth()+"/"+date.getYear());
      }
    }
  }

  private void initEmail(User user){
    if(user!=null){
      if(user.getEmail()!=null){
        emailTextField.setText(user.getEmail());
      }
    }
  }

  private void initPhoneNumber(User user){
    phoneNumberTextField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
          phoneNumberTextField.setText(newValue.replaceAll("[^\\d]", ""));
        }
      }
    });
    if(user!=null){
      if(user.getPhoneNumber()!=null){
        phoneNumberTextField.setText(user.getPhoneNumber());
      }
    }
  }
  
  private void initAddress(User user){
    if(user!=null){
      if(user.getAddress()!=null){
        addressTextField.setText(user.getAddress());
      }
    }
  }

  private void initBirthday(User user){
    setupDateTextField(birthdayTextField);
    if(user!=null){
      if(user.getBirthday()!=null){
        Date date=new Date(user.getBirthday());
        birthdayTextField.setText(date.getDay()+"/"+date.getMonth()+"/"+date.getYear());
      }
    }

  }
  private void initName(User user){
    if(user!=null){
      if(user.getName()!=null) {
        nameTextField.setText(user.getName());
      }
    }
  }

  private void initId(User user){
    if(user==null){
      int id=-1;
      Connection connection= JDBC.getConnection();
      try{
        String query="select max(id_user) as id "+
            "from user ";
        PreparedStatement statement=connection.prepareStatement(query);
        ResultSet resultSet=statement.executeQuery();
        if(resultSet.next()){
          id=resultSet.getInt("id")+1;
        }
      }
      catch (Exception e){
        e.printStackTrace();
      }
      JDBC.closeConnection(connection);

      if(id!=-1)idLabel.setText("#"+id);
    }
    else {
      idLabel.setText("#"+user.getId());
    }
  }

  private void initImage(User user){
    if(user==null){
      imageUser.setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));
    }
    else if(user.getAvatar()==null){
      imageUser.setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));
    }
    else {
      imageUser.setImage(user.getAvatar());
    }
  }

  private void initView(){
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/EditUserView.fxml"));
      fxmlLoader.setController(this);
      AnchorPane anchorPane = fxmlLoader.load();
      this.setContent(anchorPane);
    }
    catch (Exception e){
      e.printStackTrace();
    }

    this.setPrefHeight(540);
    this.setPrefWidth(900);
    this.getStylesheets().add(getClass().getResource("/org/example/demo/CSS/BookView.css").toExternalForm());
    this.setId("FadedScrollPane");
  }

  private void setupDateTextField(TextField textField){
    textField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d{0,2}/?\\d{0,2}/?\\d{0,4}")) {
          textField.setText(oldValue);
        }
      }
    });
  }


  @FXML
  private void ExitView(){
    AnchorPane mainPane=(AnchorPane) this.getParent();
    ConfirmBox confirmBox=new ConfirmBox(
        "Xác nhận hủy sự thay đổi?",
        "Nếu bạn chọn \"No\", bạn sẽ được tiếp tục thay đổi thông tin người mượn.",
        ()->{
          mainPane.getChildren().removeLast();
          mainPane.getChildren().removeLast();
        },
        ()->{
          mainPane.getChildren().removeLast();
        }
    );
    mainPane.getChildren().add(confirmBox);
  }

  @FXML
  private void SaveUser(){
    if(nameTextField.getText().isEmpty()){
      nameTextField.requestFocus();
      return;
    }
    if(createDateFromString(birthdayTextField.getText())==null){
      birthdayTextField.requestFocus();
      return;
    }
    if(phoneNumberTextField.getText().isEmpty()){
      phoneNumberTextField.requestFocus();
      return;
    }
    if(addressTextField.getText().isEmpty()){
      addressTextField.requestFocus();
      return;
    }
    if(createDateFromString(endBanDateTextField.getText())==null && !endBanDateTextField.getText().isEmpty()){
      endBanDateTextField.requestFocus();
      return;
    }

    AnchorPane mainPane=(AnchorPane) this.getParent();
    ConfirmBox confirmBox=new ConfirmBox(
        "Xác nhận Lưu?",
        "Nếu bạn chọn \"No\", bạn sẽ được tiếp tục thay đổi thông tin người mượn.",
        ()->{
          mainPane.getChildren().removeLast();
          Thread thread=new Thread(()->{
            if(oldUser!=null)library.deleteUser(oldUser);
            User newUser=createNewUser();
            if(newUser!=null) {
              library.insertUserWithID(newUser, newUser.getId());

              if (oldUser == null) {
                listUsers.add(new Suggestion(newUser));
              } else {
                for (int i = 0; i < listUsers.size(); i++) {
                  if (listUsers.get(i).getId() == newUser.getId()) {
                    listUsers.set(i, new Suggestion(newUser));
                    break;
                  }
                }
              }
            }

            Platform.runLater(laterAction);

          });
          thread.start();
          mainPane.getChildren().removeLast();
        },
        ()->{
          mainPane.getChildren().removeLast();
        }
    );
    mainPane.getChildren().add(confirmBox);
  }

  private User createNewUser(){
    Image image = imageUser.getImage();

    String name=nameTextField.getText();

    int id = Integer.parseInt(idLabel.getText().substring(1));

    Date birthDay=createDateFromString(birthdayTextField.getText());
    if(birthDay==null && !birthdayTextField.getText().isEmpty())return null;

    String address=addressTextField.getText();

    String phoneNumber=phoneNumberTextField.getText();

    String email=emailTextField.getText();

    Date endBanDate=createDateFromString(endBanDateTextField.getText());
    if(endBanDate==null && !endBanDateTextField.getText().isEmpty())return null;

    return new User(id,name,birthDay,address,email,image,phoneNumber,endBanDate);
  }

  private Date createDateFromString(String s){
    int index1=s.indexOf('/');
    if(index1==-1)return null;
    int index2=s.substring(index1+1).indexOf('/')+index1+1;
    if(index2==-1)return null;
    String stringDay=s.substring(0,index1);
    String stringMonth=s.substring(index1+1,index2);
    String stringYear=s.substring(index2+1);
    if(stringDay.isEmpty() || stringMonth.isEmpty() || stringYear.isEmpty())return null;
    int day=Integer.parseInt(s.substring(0,index1));
    int month=Integer.parseInt(s.substring(index1+1,index2));
    int year=Integer.parseInt(s.substring(index2+1));
    Date newDate=new Date(year,month,day);
    if(newDate.getYear()!=year || newDate.getMonth()!=month || newDate.getDay()!=day)return null;
    return newDate;
  }




}

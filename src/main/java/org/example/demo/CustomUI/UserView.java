package org.example.demo.CustomUI;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.Users.Date;
import org.example.demo.Models.Users.User;

public class UserView extends ScrollPane implements MainInfo {

  @FXML private AnchorPane wrapper;
  @FXML private ImageView imageUser;

  @FXML private Label nameLabel;
  @FXML private Label idLabel;

  @FXML private VBox infoBox;

  @FXML private HBox birthdayBox;
  @FXML private Label birthdayLabel;

  @FXML private HBox addressBox;
  @FXML private Label addressLabel;

  @FXML private HBox phoneNumberBox;
  @FXML private Label phoneNumberLabel;

  @FXML private HBox emailBox;
  @FXML private Label emailLabel;

  @FXML private HBox endBanDateBox;
  @FXML private Label endBanDateLabel;

  public UserView(User user,BlendMode blendMode){
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/UserView.fxml"));
      fxmlLoader.setController(this);
      AnchorPane anchorPane=fxmlLoader.load();
      this.setContent(anchorPane);
    }
    catch (Exception e){
      e.printStackTrace();
    }

    this.setPrefHeight(540);
    this.setPrefWidth(900);
    this.getStylesheets().add(getClass().getResource("/org/example/demo/CSS/BookView.css").toExternalForm());
    this.setId("FadedScrollPane");

    setImage(user,blendMode);
    setName(user);
    setID(user);
    setBirthday(user);
    setAddress(user);
    setPhoneNumber(user);
    setEmail(user);
    setEndBanDate(user);
  }

  private void setImage(User user,BlendMode blendMode){
    if(user.getAvatar()==null){
      imageUser.setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));
    }
    else {
      imageUser.setImage(user.getAvatar());
    }
    if(blendMode==null){
      wrapper.setBlendMode(BlendMode.SRC_OVER);
    }
    else wrapper.setBlendMode(blendMode);
  }
  
  private void setName(User user){
    if(user.getName()!=null)nameLabel.setText(user.getName());
  }

  private void setID(User user){
    if(user.getId()>0){
      idLabel.setText("#"+user.getId());
    }
  }

  private void setBirthday(User user){
    if(user.getBirthday()!=null){
      birthdayLabel.setText(user.getBirthday().toString());
    }
    else {
      infoBox.getChildren().remove(birthdayBox);
    }
  }

  private void setAddress(User user){
    if(user.getAddress()!=null){
      addressLabel.setText(user.getAddress());
    }
    else {
      infoBox.getChildren().remove(addressBox);
    }
  }

  private void setPhoneNumber(User user){
    if(user.getPhoneNumber()!=null){
      phoneNumberLabel.setText(user.getPhoneNumber());
    }
    else {
      infoBox.getChildren().remove(phoneNumberBox);
    }
  }

  private void setEmail(User user){
    if(user.getEmail()!=null){
      emailLabel.setText(user.getEmail());
    }
    else {
      infoBox.getChildren().remove(emailBox);
    }
  }

  private void setEndBanDate(User user){
    if(user.isBan()){
      endBanDateLabel.setText(user.getBanEndTime().toString());
    }
    else infoBox.getChildren().remove(endBanDateBox);
  }

  @FXML
  private void ExitView(){
    AnchorPane mainPane=(AnchorPane) this.getParent();
    mainPane.getChildren().remove(this);
  }

  @FXML
  private void Speak(){

  }

  @Override
  public void applyDarkMode(boolean isDark) {
    if(isDark){
      this.wrapper.setBlendMode(BlendMode.DIFFERENCE);
    }
    else {
      this.wrapper.setBlendMode(BlendMode.SRC_OVER);
    }
  }

  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {

  }

  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }

  @Override
  public void removeLang(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }
}

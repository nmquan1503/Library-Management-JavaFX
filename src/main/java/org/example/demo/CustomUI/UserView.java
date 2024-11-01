package org.example.demo.CustomUI;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.util.Duration;
import org.example.demo.API.TextToSpeech;
import org.example.demo.Controllers.BaseController;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.Language;
import org.example.demo.Models.Users.Date;
import org.example.demo.Models.Users.User;

public class UserView extends ScrollPane implements MainInfo {

  @FXML private AnchorPane viewPane;

  @FXML private AnchorPane wrapper;
  @FXML private ImageView imageUser;

  @FXML private Label nameLabel;
  @FXML private Label idLabel;

  @FXML private VBox infoBox;

  @FXML private HBox birthdayBox;
  @FXML private Label birthdayTag;
  @FXML private Label birthdayLabel;

  @FXML private HBox addressBox;
  @FXML private Label addressTag;
  @FXML private Label addressLabel;

  @FXML private HBox phoneNumberBox;
  @FXML private Label phoneNumberTag;
  @FXML private Label phoneNumberLabel;

  @FXML private HBox emailBox;
  @FXML private Label emailTag;
  @FXML private Label emailLabel;

  @FXML private HBox endBanDateBox;
  @FXML private Label endBanDateTag;
  @FXML private Label endBanDateLabel;

  @FXML private Pane loadingPane;
  private Transition loadingTransition;

  private HashMap<Object,String > viLang;
  private HashMap<Object,String > enLang;

  private TextToSpeech tts;

  public UserView(){
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

    initLoadingTransition();
    tts=new TextToSpeech();
  }

  public void setUser(User user){
    setImage(user);
    setName(user);
    setID(user);
    setBirthday(user);
    setAddress(user);
    setPhoneNumber(user);
    setEmail(user);
    setEndBanDate(user);

    viLang=new HashMap<>();
    enLang=new HashMap<>();
    setUpLanguage(viLang,enLang);
    if(BaseController.isTranslate){
      applyTranslate(null,null,true);
    }

    viewPane.getChildren().remove(loadingPane);
    loadingPane=null;
    loadingTransition.stop();
    loadingTransition=null;

  }


  private void setImage(User user){
    if(user.getAvatar()==null){
      imageUser.setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));
    }
    else {
      imageUser.setImage(user.getAvatar());
    }
    if(BaseController.isDark){
      wrapper.setBlendMode(BlendMode.DIFFERENCE);
      wrapper.setId("wrapper_dark");
    }
    else wrapper.setBlendMode(BlendMode.SRC_OVER);
    wrapper.setPrefWidth(imageUser.getFitWidth()+6);
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
    tts.stopSpeak();
    ScaleTransition transition=new ScaleTransition(Duration.millis(200),this);
    transition.setToX(0);
    transition.setToY(0);
    transition.setOnFinished(e->{
      AnchorPane mainPane=(AnchorPane) this.getParent();
      mainPane.getChildren().remove(this);
    });
    transition.play();
  }

  @FXML
  private void Speak(){
    String oup="";
    if(nameLabel!=null){
      if(!nameLabel.getText().isEmpty()){
        if(BaseController.isTranslate)oup=oup.concat("Name user: "+nameLabel.getText()+"\n");
        else oup=oup.concat("Tên người mượn: "+nameLabel.getText()+"\n");
      }
    }

    if(birthdayLabel!=null){
      if(!birthdayLabel.getText().isEmpty()){
        oup=oup.concat(birthdayTag.getText()+birthdayLabel.getText()+"\n");
      }
    }

    if(addressLabel!=null){
      if(!addressLabel.getText().isEmpty()){
        oup=oup.concat(addressTag.getText()+addressLabel.getText()+"\n");
      }
    }

    if(phoneNumberLabel!=null){
      if(!phoneNumberLabel.getText().isEmpty()){
        oup=oup.concat(phoneNumberTag.getText()+phoneNumberLabel.getText()+"\n");
      }
    }

    if(emailLabel!=null){
      if(!emailLabel.getText().isEmpty()){
        oup=oup.concat(emailTag.getText()+emailLabel.getText()+"\n");
      }
    }

    if(endBanDateLabel!=null){
      if(!endBanDateLabel.getText().isEmpty()){
        oup=oup.concat(endBanDateTag.getText()+endBanDateLabel.getText()+"\n");
      }
    }

    tts.stopSpeak();
    tts=new TextToSpeech();
    if(BaseController.isTranslate){
      tts.SpeakPassage(oup, Language.ENGLISH);
    }
    else tts.SpeakPassage(oup,Language.VIETNAMESE);

  }

  private void initLoadingTransition() {
    Arc arc1 = (Arc) loadingPane.getChildren().getFirst();
    Arc arc2 = (Arc) loadingPane.getChildren().get(1);
    Arc arc3 = (Arc) loadingPane.getChildren().get(2);

    RotateTransition transition1 = new RotateTransition(Duration.millis(1000), arc1);
    transition1.setByAngle(360);
    transition1.setCycleCount(Transition.INDEFINITE);
    transition1.setAutoReverse(false);

    RotateTransition transition2 = new RotateTransition(Duration.millis(700), arc2);
    transition2.setByAngle(360);
    transition2.setCycleCount(Transition.INDEFINITE);
    transition2.setAutoReverse(false);

    RotateTransition transition3 = new RotateTransition(Duration.millis(400), arc3);
    transition3.setByAngle(360);
    transition3.setCycleCount(Transition.INDEFINITE);
    transition3.setAutoReverse(false);

    loadingTransition = new ParallelTransition(transition1, transition2, transition3);
    loadingTransition.play();
  }

  @Override
  public void applyDarkMode(boolean isDark) {
    if(isDark){
      this.wrapper.setBlendMode(BlendMode.DIFFERENCE);
      wrapper.setId("wrapper_dark");
    }
    else {
      this.wrapper.setBlendMode(BlendMode.SRC_OVER);
      wrapper.setId("wrapper_light");
    }
  }

  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {
    if(isTranslate){
      if(birthdayTag!=null){
        birthdayTag.setText("Birthday: ");
      }
      if(addressTag!=null){
        addressTag.setText("Address: ");
      }
      if(phoneNumberTag!=null){
        phoneNumberTag.setText("Phone number: ");
      }
      if(endBanDateTag!=null){
        endBanDateTag.setText("Banned until: ");
      }

    }
    else {
      if(birthdayTag!=null){
        birthdayTag.setText("Ngày sinh: ");
      }
      if(addressTag!=null){
        addressTag.setText("Địa chỉ: ");
      }
      if(phoneNumberTag!=null){
        phoneNumberTag.setText("Số điện thoại: ");
      }
      if(endBanDateTag!=null){
        endBanDateTag.setText("Bị cấm tới ngày: ");
      }
    }
  }

  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }

}

package org.example.demo.CustomUI;

import java.util.HashMap;
import java.util.Objects;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import org.example.demo.API.Network;
import org.example.demo.API.TextToSpeech;
import org.example.demo.API.Translate;
import org.example.demo.Controllers.BaseController;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Language;

public class BookView extends ScrollPane implements MainInfo {

  @FXML private VBox wrapper;
  @FXML private ImageView imageBook;
  
  @FXML private Label titleLabel;
  
  @FXML private VBox authorList;

  @FXML private VBox infoBox;

  @FXML private HBox publisherBox;
  @FXML private Label publisherTag;
  @FXML private Label publisherLabel;

  @FXML private HBox publishedDateBox;
  @FXML private Label publishedDateTag;
  @FXML private Label publishedDateLabel;

  @FXML private HBox descriptionBox;
  @FXML private Label descriptionTag;
  @FXML private Label descriptionLabel;

  @FXML private HBox categoryBox;
  @FXML private Label categoryTag;
  @FXML private VBox categoryList;

  @FXML private HBox pageCountBox;
  @FXML private Label pageCountTag;
  @FXML private Label pageCountLabel;

  @FXML private HBox ratingCountBox;
  @FXML private Label ratingCountTag;
  @FXML private Label ratingCountLabel;

  @FXML private HBox averageRatingBox;
  @FXML private Label averageRatingLabel;

  @FXML private HBox starList;
  @FXML private ImageView star1;
  @FXML private ImageView star2;
  @FXML private ImageView star3;
  @FXML private ImageView star4;
  @FXML private ImageView star5;

  @FXML private HBox quantityBox;
  @FXML private Label quantityTag;
  @FXML private Label quantityLabel;

  @FXML private AnchorPane viewPane;
  
  @FXML private Pane loadingPane;
  private Transition loadingTransition;

  
  private HashMap<Object,String > viLang;
  private HashMap<Object,String > enLang;

  private TextToSpeech tts;


  public BookView(){
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/BookView.fxml"));
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

  public void setBook(Book book){
    setImageBook(book);
    setTitle(book);
    setAuthorList(book);
    setPublisher(book);
    setPublishedDate(book);
    setDescription(book);
    setCategory(book);
    setPageCount(book);
    setRatingCount(book);
    setAverageRating(book);
    setQuantity(book);

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


  private void setImageBook(Book book){
    if(book.getImageLink()==null || !Network.isConnected()){
      imageBook.setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));
    }
    else imageBook.setImage(new Image(book.getImageLink()));
    if(BaseController.isDark){
      wrapper.setBlendMode(BlendMode.DIFFERENCE);
      wrapper.setId("wrapper_dark");
    }
    else{
      wrapper.setBlendMode(BlendMode.SRC_OVER);
    }
  }

  private void setTitle(Book book){
    if(book.getTitle()!=null)titleLabel.setText(book.getTitle());
    else {
      titleLabel=null;
    }
  }

  private void setAuthorList(Book book){
    if(book.getAuthors()==null){
      authorList=null;
      return;
    }
    if(book.getAuthors().isEmpty()){
      authorList=null;
      return;
    }
    for(String author:book.getAuthors()){
      Label authorLabel=new Label(author);
      authorLabel.setStyle("-fx-font-size:18");
      authorList.getChildren().add(authorLabel);
    }
  }

  private void setPublisher(Book book){
    if(book.getPublisher()==null){
      infoBox.getChildren().remove(publisherBox);
      publisherTag=null;
      publisherLabel=null;
      publisherBox=null;
      return;
    }
    publisherLabel.setText(book.getPublisher());
  }

  private void setPublishedDate(Book book){
    if(book.getPublishedDate()==-1){
      infoBox.getChildren().remove(publishedDateBox);
      publishedDateTag=null;
      publishedDateLabel=null;
      publishedDateBox=null;
      return;
    }
    publishedDateLabel.setText(String.valueOf(book.getPublishedDate()));
  }

  private void setDescription(Book book){
    if(book.getDescription()==null){
      infoBox.getChildren().remove(descriptionBox);
      descriptionTag=null;
      descriptionLabel=null;
      descriptionBox=null;
      return;
    }
    descriptionLabel.setText(book.getDescription());
  }

  private void setCategory(Book book){
    if(book.getCategories()==null){
      infoBox.getChildren().remove(categoryBox);
      categoryTag=null;
      categoryList=null;
      categoryBox=null;
      return;
    }
    if(book.getCategories().isEmpty()){
      infoBox.getChildren().remove(categoryBox);
      categoryTag=null;
      categoryList=null;
      categoryBox=null;
      return;
    }
    for(String category:book.getCategories()){
      Label categoryLabel=new Label(category);
      categoryLabel.setStyle("-fx-font-size:18");
      categoryList.getChildren().add(categoryLabel);
    }
  }

  private void setPageCount(Book book){
    if(book.getPageCount()<0){
      infoBox.getChildren().remove(pageCountBox);
      pageCountTag=null;
      pageCountLabel=null;
      pageCountBox=null;
      return;
    }
    pageCountLabel.setText(String.valueOf(book.getPageCount()));
  }

  private void setRatingCount(Book book){
    if(book.getRatingsCount()<0){
      infoBox.getChildren().remove(ratingCountBox);
      ratingCountTag=null;
      ratingCountLabel=null;
      ratingCountBox=null;
      return;
    }
    ratingCountLabel.setText(String.valueOf(book.getRatingsCount()));
  }

  private void setAverageRating(Book book){
    if(book.getAverageRating()<0){
      infoBox.getChildren().remove(averageRatingBox);
      averageRatingLabel=null;
      averageRatingBox=null;
      star1=null;
      star2=null;
      star3=null;
      star4=null;
      star5=null;
      starList=null;
      return;
    }
    averageRatingLabel.setText(String.valueOf(book.getAverageRating()));
    setStar(book);
  }

  private void setStar(Book book){
    int id=0;
    double averageRating=book.getAverageRating();
    while (averageRating>=1){
      ((ImageView)starList.getChildren().get(id)).setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/full_star.png"))));
      averageRating-=1;
      id++;
    }
    if(id==5)return;
    if(averageRating<0.3){
      ((ImageView)starList.getChildren().get(id)).setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/empty_star.png"))));
      id++;
    }
    else if(averageRating<0.6){
      ((ImageView)starList.getChildren().get(id)).setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/less_half_star.png"))));
      id++;
    }
    else {
      ((ImageView)starList.getChildren().get(id)).setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/more_half_star.png"))));
      id++;
    }

    while (id<5){
      ((ImageView)starList.getChildren().get(id)).setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/empty_star.png"))));
      id++;
    }

  }
  private void setQuantity(Book book){
    if(book.getQuantity()<0){
      infoBox.getChildren().remove(quantityBox);
      quantityTag=null;
      quantityLabel=null;
      quantityBox=null;
      return;
    }
    quantityLabel.setText(String.valueOf(book.getQuantity()));
  }

  @FXML
  public void ExitView(){
    stopSpeak();
    ScaleTransition transition=new ScaleTransition(Duration.millis(200),this);
    transition.setToX(0);
    transition.setToY(0);
    transition.setOnFinished(e->{
      AnchorPane mainPane=(AnchorPane) this.getParent();
      mainPane.getChildren().remove(this);
    });
    transition.play();
  }

  public void stopSpeak(){
    tts.stopSpeak();
  }

  @FXML
  private void Speak(){
    if(!Network.isConnected()){
      Node parent=this.getParent();
      if(parent!=null){
        while(parent.getParent()!=null){
          parent=parent.getParent();
        }
        if(parent instanceof AnchorPane){
          ((AnchorPane) parent).getChildren().add(
              new Warning("Mất kết nối mạng!",
                  "Vui lòng kiểm tra lại kết nối của bạn và thử lại."
              )
          );
        }
      }
      return;
    }
    String oup="";
    if(titleLabel!=null) {
      if (!titleLabel.getText().isEmpty()) {
        if(BaseController.isTranslate)oup=oup.concat("Book's name: "+titleLabel.getText()+"\n");
        else oup = oup.concat("Tên sách: " + titleLabel.getText() + "\n");
      }
    }
    if(authorList!=null){
      if(!authorList.getChildren().isEmpty()){
        oup=oup.concat("Các tác giả: ");
        for(Node node: authorList.getChildren()){
          if(node instanceof Label){
            String author=((Label) node).getText();
            oup=oup.concat(author+", ");
          }
        }
        oup=oup.concat("\n");
      }
    }
    if(publisherLabel!=null){
      if(!publisherLabel.getText().isEmpty()){
        oup=oup.concat(publisherTag.getText()+publisherLabel.getText()+"\n");
      }
    }
    if(publishedDateLabel!=null){
      if(!publishedDateLabel.getText().isEmpty()){
        oup=oup.concat(publishedDateTag.getText()+publishedDateLabel.getText()+"\n");
      }
    }
    if(descriptionLabel!=null){
      if(!descriptionLabel.getText().isEmpty()){
        oup=oup.concat(descriptionTag.getText()+descriptionLabel.getText()+"\n");
      }
    }
    if(categoryList!=null){
      if(!categoryList.getChildren().isEmpty()){
        oup=oup.concat(categoryTag.getText());
        for(Node node:categoryList.getChildren()){
          if(node instanceof Label){
            oup=oup.concat(((Label) node).getText()+", ");
          }
        }
        oup=oup.concat("\n");
      }
    }
    if(pageCountLabel!=null){
      if(!pageCountLabel.getText().isEmpty()){
        oup=oup.concat(pageCountTag.getText()+pageCountLabel.getText());
        if(BaseController.isTranslate)oup=oup.concat(" pages\n");
        else oup=oup.concat(" trang\n");
      }
    }
    if(ratingCountLabel!=null){
      if(!ratingCountLabel.getText().isEmpty()){
        oup=oup.concat(ratingCountTag.getText()+ratingCountLabel.getText()+"\n");
      }
    }
    if(averageRatingLabel!=null){
      if(!averageRatingLabel.getText().isEmpty()){
        if(BaseController.isTranslate){
          oup=oup.concat("Quality: "+averageRatingLabel.getText()+" out of 5 stars\n");
        }
        else oup=oup.concat("Chất lượng: "+averageRatingLabel.getText()+" trên 5 sao\n");
      }
    }
    if(quantityLabel!=null){
      if(!quantityLabel.getText().isEmpty()){
        oup=oup.concat(quantityTag.getText()+quantityLabel.getText()+"\n");
      }
    }
    tts.stopSpeak();
    tts=new TextToSpeech();
    if(BaseController.isTranslate)tts.SpeakPassage(oup,Language.ENGLISH);
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
      this.wrapper.setId("wrapper_dark");
    }
    else {
      this.wrapper.setBlendMode(BlendMode.SRC_OVER);
      this.wrapper.setId("wrapper_light");
    }
  }

  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {
    if(!isTranslate) {
      if (publisherTag != null) {
        publisherTag.setText("Nhà xuất bản: ");
      }
      if(publishedDateTag!=null){
        publishedDateTag.setText("Năm xuất bản: ");
      }
      if(descriptionTag!=null){
        descriptionTag.setText("Mô tả: ");
      }
      if(categoryTag!=null){
        categoryTag.setText("Thể loại: ");
      }
      if(pageCountTag!=null){
        pageCountTag.setText("Độ dài: ");
      }
      if(ratingCountTag!=null){
        ratingCountTag.setText("Số lượng đánh giá: ");
      }
      if(quantityTag!=null){
        quantityTag.setText("Số lượng: ");
      }
      if(descriptionLabel!=null){
        if(!descriptionLabel.getText().isEmpty()){
          descriptionLabel.setText(this.viLang.get(descriptionLabel));
        }
      }
    }
    else {
      if (publisherTag != null) {
        publisherTag.setText("Publisher: ");
      }
      if(publishedDateTag!=null){
        publishedDateTag.setText("Published Date: ");
      }
      if(descriptionTag!=null){
        descriptionTag.setText("Description: ");
      }
      if(categoryTag!=null){
        categoryTag.setText("Categories: ");
      }
      if(pageCountTag!=null){
        pageCountTag.setText("Page Count: ");
      }
      if(ratingCountTag!=null){
        ratingCountTag.setText("Ratings Count: ");
      }
      if(quantityTag!=null){
        quantityTag.setText("Quantity: ");
      }
      if(descriptionLabel!=null){
        if(!descriptionLabel.getText().isEmpty()){
          descriptionLabel.setText(this.enLang.get(descriptionLabel));
        }
      }
    }
  }

  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {
    if(descriptionLabel!=null){
      if(!descriptionLabel.getText().isEmpty()){
        viLang.put(descriptionLabel,descriptionLabel.getText());
        enLang.put(descriptionLabel, Translate.translate(descriptionLabel.getText(), Language.VIETNAMESE,Language.ENGLISH));
      }
    }
  }

}

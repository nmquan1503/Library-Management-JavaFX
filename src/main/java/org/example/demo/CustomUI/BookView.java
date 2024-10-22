package org.example.demo.CustomUI;

import java.util.Objects;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.demo.Models.BookShelf.Book;

public class BookView extends ScrollPane {
  @FXML private ImageView imageBook;
  @FXML private Label titleLabel;
  @FXML private VBox authorList;

  @FXML private VBox infoBox;

  @FXML private HBox publisherBox;
  @FXML private Label publisherLabel;

  @FXML private HBox publishedDateBox;
  @FXML private Label publishedDateLabel;

  @FXML private HBox descriptionBox;
  @FXML private Label descriptionLabel;

  @FXML private HBox categoryBox;
  @FXML private VBox categoryList;

  @FXML private HBox pageCountBox;
  @FXML private Label pageCountLabel;

  @FXML private HBox ratingCountBox;
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
  @FXML private Label quantityLabel;

  public BookView(Book book){
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
    setStar(book);
    setQuantity(book);

  }
  private void setImageBook(Book book){
    if(book.getImageLink()==null){
      imageBook.setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));
    }
    else imageBook.setImage(new Image(book.getImageLink()));
  }

  private void setTitle(Book book){
    if(book.getTitle()!=null)titleLabel.setText(book.getTitle());
  }

  private void setAuthorList(Book book){
    if(book.getAuthors()==null)return;
    for(String author:book.getAuthors()){
      Label authorLabel=new Label(author);
      authorLabel.setStyle("-fx-font-size:18");
      authorList.getChildren().add(authorLabel);
    }
  }

  private void setPublisher(Book book){
    if(book.getPublisher()==null){
      infoBox.getChildren().remove(publisherBox);
      return;
    }
    publisherLabel.setText(book.getPublisher());
  }

  private void setPublishedDate(Book book){
    if(book.getPublishedDate()==-1){
      infoBox.getChildren().remove(publishedDateBox);
      return;
    }
    publishedDateLabel.setText(String.valueOf(book.getPublishedDate()));
  }

  private void setDescription(Book book){
    if(book.getDescription()==null){
      infoBox.getChildren().remove(descriptionBox);
      return;
    }
    descriptionLabel.setText(book.getDescription());
  }

  private void setCategory(Book book){
    if(book.getCategories()==null){
      infoBox.getChildren().remove(categoryBox);
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
      return;
    }
    pageCountLabel.setText(String.valueOf(book.getPageCount()));
  }

  private void setRatingCount(Book book){
    if(book.getRatingsCount()<0){
      infoBox.getChildren().remove(ratingCountBox);
      return;
    }
    ratingCountLabel.setText(String.valueOf(book.getRatingsCount()));
  }

  private void setAverageRating(Book book){
    if(book.getAverageRating()<0){
      infoBox.getChildren().remove(averageRatingBox);
      return;
    }
    averageRatingLabel.setText(String.valueOf(book.getAverageRating()));
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
      return;
    }
    quantityLabel.setText(String.valueOf(book.getQuantity()));
  }

  @FXML
  private void ExitView(){
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

  }

}

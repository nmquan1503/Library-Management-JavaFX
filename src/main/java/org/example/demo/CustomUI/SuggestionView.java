package org.example.demo.CustomUI;

import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.demo.Models.Suggestion.Suggestion;

public class SuggestionView extends HBox {

  private final int id;

  public SuggestionView(Suggestion suggestion,int height,int width){
    this.id=suggestion.getId();

    StackPane stackPane=new StackPane();
    stackPane.setPrefHeight(height);
    stackPane.setPrefWidth(height/1.5);
    stackPane.setAlignment(Pos.CENTER);

    ImageView imageView=new ImageView(suggestion.getIcon());
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(height-10);
    imageView.setId("IconOfContent");

    stackPane.getChildren().add(imageView);

    Label content=new Label(suggestion.getContent());
    content.setPrefWidth(width - height/1.5-5);
    content.setPrefHeight(height-10);
    content.setWrapText(true);
    content.setAlignment(Pos.TOP_LEFT);
    content.setId("ContentOfSuggestion");

    this.getChildren().add(stackPane);
    this.getChildren().add(content);

    this.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/org/example/demo/CSS/Pagination.css")).toExternalForm());
    this.setPrefHeight(height);
    this.setPrefWidth(width);
    this.setAlignment(Pos.CENTER);
    this.setSpacing(5);

  }

  public int getID(){
    return id;
  }
}

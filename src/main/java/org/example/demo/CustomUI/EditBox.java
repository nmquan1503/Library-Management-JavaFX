package org.example.demo.CustomUI;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.example.demo.Models.Suggestion.Suggestion;

public class EditBox extends HBox {

  private final int id;


  public EditBox(Suggestion suggestion,Runnable removeAction, int height,int width){
    this.id=suggestion.getId();

    this.getChildren().addAll(
        initImage(suggestion,height),
        initLabel(suggestion,height,width),
        initRemoveButton(removeAction,height)
    );

    this.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/org/example/demo/CSS/Pagination.css")).toExternalForm());
    this.setPrefHeight(height);
    this.setPrefWidth(width);
    this.setAlignment(Pos.CENTER);
    this.setSpacing(5);

  }

  private StackPane initImage(Suggestion suggestion,int height){
    StackPane stackPane=new StackPane();
    stackPane.setPrefHeight(height);
    stackPane.setPrefWidth(height/1.5);
    stackPane.setAlignment(Pos.CENTER);

    ImageView imageView=new ImageView(suggestion.getIcon());
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(height-10);
    imageView.setId("IconOfContent");

    stackPane.getChildren().add(imageView);

    return stackPane;
  }

  private VBox initLabel(Suggestion suggestion,int height,int width){
    Label content=new Label(suggestion.getContent());
    content.setPrefWidth(width - 2*height/1.5-15);
    content.setPrefHeight(2.0*height/3);
    content.setWrapText(true);
    content.setAlignment(Pos.TOP_LEFT);
    content.setId("ContentOfSuggestion");

    Label idLabel=new Label("#"+suggestion.getId());
    idLabel.setPrefHeight(width - 2*height/1.5-15);
    idLabel.setPrefHeight(1.0*height/3);
    idLabel.setWrapText(true);
    idLabel.setAlignment(Pos.TOP_LEFT);
    idLabel.setId("ContentOfSuggestion");

    return new VBox(content,idLabel);
  }

  private JFXButton initRemoveButton(Runnable removeAction, int height){
    JFXButton removeButton=new JFXButton();
    removeButton.setPrefWidth(height/1.5);
    removeButton.setPrefHeight(height/1.5);
    removeButton.setContentDisplay(ContentDisplay.CENTER);
    removeButton.setOnAction(event -> {
      removeAction.run();
    });
    FontAwesomeIconView viewButton=new FontAwesomeIconView(FontAwesomeIcon.TRASH);
    viewButton.setFill(Color.RED);
    viewButton.setSize(String.valueOf(height/2));
    removeButton.setGraphic(viewButton);

    return removeButton;
  }

  public int getID(){
    return id;
  }

}

package org.example.demo.CustomUI;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.HashMap;
import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.demo.Controllers.BaseController;
import org.example.demo.Controllers.EditController;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;

public class EditBox extends HBox implements MainInfo {

  public enum TypeBox{
    BOOK,
    USER;
  }

  private final Suggestion suggestion;
  private StackPane wrapper;
  private ImageView image;
  private VBox content;
  private JFXButton removeButton;
  private EditController editController;

  public EditBox(Suggestion suggestion,TypeBox typeBox, EditController editController, int height,int width){
    this.suggestion=suggestion;
    this.editController=editController;

    initImage(suggestion,height);
    initLabel(suggestion,height,width);
    initRemoveButton(height,typeBox);

    this.getChildren().addAll(
        wrapper,
        content,
        removeButton
    );

    this.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/org/example/demo/CSS/Pagination.css")).toExternalForm());
    this.setPrefHeight(height);
    this.setPrefWidth(width);
    this.setAlignment(Pos.CENTER);
    this.setSpacing(5);

  }

  private void initImage(Suggestion suggestion,int height){
    wrapper=new StackPane();
    wrapper.setPrefHeight(height);
    wrapper.setPrefWidth(height/1.5);
    wrapper.setAlignment(Pos.CENTER);

    image=new ImageView(suggestion.getIcon());
    image.setPreserveRatio(true);
    image.setFitHeight(height-10);
    image.setId("IconOfContent");

    if(BaseController.isDark) wrapper.setBlendMode(BlendMode.DIFFERENCE);
    else wrapper.setBlendMode(BlendMode.SRC_OVER);
    wrapper.getChildren().add(image);
  }

  private void initLabel(Suggestion suggestion,int height,int width){
    Label contentLabel =new Label(suggestion.getContent());
    contentLabel.setPrefWidth(width - 2*height/1.5-15);
    contentLabel.setPrefHeight(2.0*height/3);
    contentLabel.setWrapText(true);
    contentLabel.setAlignment(Pos.TOP_LEFT);
    contentLabel.setId("ContentOfSuggestion");

    Label idLabel=new Label("#"+suggestion.getId());
    idLabel.setPrefHeight(width - 2*height/1.5-15);
    idLabel.setPrefHeight(1.0*height/3);
    idLabel.setWrapText(true);
    idLabel.setAlignment(Pos.TOP_LEFT);
    idLabel.setId("ContentOfSuggestion");

    content=new VBox(contentLabel,idLabel);
  }

  private void initRemoveButton(int height,TypeBox typeBox){
    removeButton=new JFXButton();
    removeButton.setPrefWidth(height/1.5);
    removeButton.setPrefHeight(height/1.5);
    removeButton.setContentDisplay(ContentDisplay.CENTER);
    removeButton.setOnAction(event -> {
      if(typeBox==TypeBox.BOOK){
        AnchorPane mainPane=editController.getMainPane();
        ConfirmBox confirmBox = new ConfirmBox(
            "Xác nhận xóa sách khỏi thư viện?",
            "",
            () -> {
              Library.getInstance().deleteBook(Library.getInstance().getBook(suggestion.getId()));
              editController.deleteBookSuggestion(suggestion);
              mainPane.getChildren().removeLast();
            },
            () -> {
              mainPane.getChildren().removeLast();
            }
        );
        mainPane.getChildren().add(confirmBox);
      }
      else {
        AnchorPane mainPane=editController.getMainPane();
        ConfirmBox confirmBox = new ConfirmBox(
            "Xác nhận xóa nguười mượn khỏi thư viện?",
            "",
            () -> {
              Library.getInstance().deleteUser(Library.getInstance().getUser(suggestion.getId()));
              editController.deleteUserSuggestion(suggestion);
              mainPane.getChildren().removeLast();
            },
            () -> {
              mainPane.getChildren().removeLast();
            }
        );
        mainPane.getChildren().add(confirmBox);
      }
    });
    FontAwesomeIconView viewButton=new FontAwesomeIconView(FontAwesomeIcon.TRASH);
    viewButton.setFill(Color.RED);
    viewButton.setSize(String.valueOf(height/2));
    removeButton.setGraphic(viewButton);

  }

  public int getID(){
    return suggestion.getId();
  }

  @Override
  public void applyDarkMode(boolean isDark) {
    if(isDark){
      wrapper.setBlendMode(BlendMode.DIFFERENCE);
    }
    else wrapper.setBlendMode(BlendMode.SRC_OVER);
  }

  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {

  }

  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }

}

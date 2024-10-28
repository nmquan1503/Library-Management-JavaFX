package org.example.demo.CustomUI;

import java.util.HashMap;
import java.util.Objects;
import java.util.Stack;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.Suggestion.Suggestion;

public class SuggestionView extends HBox implements MainInfo {

  private final int id;

  private StackPane wrapper;
  private ImageView image;
  private Label content;


  public SuggestionView(Suggestion suggestion,int height,int width, BlendMode blendMode){
    this.id=suggestion.getId();

    initImage(suggestion,height,width,blendMode);

    initContent(suggestion,height,width);

    this.getChildren().add(wrapper);
    this.getChildren().add(content);

    this.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/org/example/demo/CSS/Pagination.css")).toExternalForm());
    this.setPrefHeight(height);
    this.setPrefWidth(width);
    this.setAlignment(Pos.CENTER);
    this.setSpacing(5);

  }

  private void initImage(Suggestion suggestion,int height,int width, BlendMode blendMode){
    wrapper = new StackPane();
    wrapper.setPrefHeight(height);
    wrapper.setPrefWidth(height/1.5);
    wrapper.setAlignment(Pos.CENTER);

    image=new ImageView(suggestion.getIcon());
    image.setPreserveRatio(true);
    image.setFitHeight(height-10);
    image.setId("IconOfContent");

    wrapper.getChildren().add(image);
    if(blendMode==null) wrapper.setBlendMode(BlendMode.SRC_OVER);
    else wrapper.setBlendMode(blendMode);
  }
  
  private void initContent(Suggestion suggestion,int height,int width){
    content=new Label(suggestion.getContent());
    content.setPrefWidth(width - height/1.5-5);
    content.setPrefHeight(height-10);
    content.setWrapText(true);
    content.setAlignment(Pos.TOP_LEFT);
    content.setId("ContentOfSuggestion");
  }

  public int getID(){
    return id;
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

  @Override
  public void removeLang(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }
}

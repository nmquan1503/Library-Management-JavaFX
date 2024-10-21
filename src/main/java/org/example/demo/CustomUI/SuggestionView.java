package org.example.demo.CustomUI;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SuggestionView {

  public static void main(String[] args) {
    Text text=new Text("okkkkkkk");
    Text text1=new Text("okojoj");
    TextFlow textFlow=new TextFlow(text,text1);
    System.out.println(text.getBoundsInLocal().getWidth()+text1.getBoundsInLocal().getWidth());
    System.out.println(textFlow.getBoundsInLocal().getWidth());
  }
}

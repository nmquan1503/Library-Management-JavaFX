package org.example.demo.Controllers;

import java.util.HashMap;
import javafx.event.ActionEvent;
import org.example.demo.Interfaces.MainInfo;

public class ReturnBookController implements MainInfo {




  public void confirmButtonAction(ActionEvent event) {
  }

  public void DeclineButtonAction(ActionEvent event) {
  }

  public void backButtonAction(ActionEvent event) {
  }
  @Override
  public void applyDarkMode(boolean isDark) {

  }

  // Không gọi setUpLanguage ở đây
  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {

  }

  // viLang lưu nội dung tiếng Việt gắn với Object, enLang lưu tiếng Anh
  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }


}

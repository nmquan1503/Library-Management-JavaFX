package org.example.demo.Controllers;

import java.util.HashMap;
import org.example.demo.Interfaces.MainInfo;

public class ReturnBookController implements MainInfo {

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

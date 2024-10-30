package org.example.demo.Interfaces;

import java.util.HashMap;
import java.util.Objects;

public interface MainInfo {

  public void applyDarkMode(boolean isDark);

  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate);

  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang);
}
module org.example.demo {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.jfoenix;
  requires java.desktop;
  requires java.sql;
  requires org.json;
  requires jlayer;
  requires com.fasterxml.jackson.databind;
  requires jdk.compiler;
  requires de.jensd.fx.glyphs.fontawesome;
  requires java.prefs;
  requires mysql.connector.j;
  requires com.google.zxing;

  opens org.example.demo.Controllers;
  opens org.example.demo.CustomUI to javafx.fxml;
  exports org.example.demo;
  opens org.example.demo.Interfaces;
  opens org.example.demo to org.junit.jupiter.api, org.testfx;
}
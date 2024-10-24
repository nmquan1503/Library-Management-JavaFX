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
    requires mysql.connector.j;
    requires java.prefs;

    opens org.example.demo.Controllers;
  exports org.example.demo;
}
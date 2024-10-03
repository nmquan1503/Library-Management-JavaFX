package org.example.demo.Models.BookShelf;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.example.demo.Models.BookShelf.Book;
public class BookShelf {


    public static void main(String args[]) {
        ArrayList<String> authors = new ArrayList<>();
        ArrayList<String> cate = new ArrayList<>();
        authors.add("THAO");
        authors.add("XUÂN");
        authors.add("ĐÀO");
        cate.add("trinh thám");
        cate.add("tiểu thuyết");
        cate.add("kinh dị");
        ArrayList<String> authors1 = new ArrayList<>();
        authors1.add("THAO1");
        authors1.add("XUÂN1");
        authors1.add("ĐÀO1");
        Book dxt = new Book("Harry Potter", authors, "KIM ĐỒNG", 2024, "Story about a wizard", 203, cate, 100, 4.5, "thisisimagelink");
        Book dxt1 = new Book("Harry Potter 1", authors1, "KIM ĐỒNG", 2024, "Story about a wizard", 199, cate, 88, 4.0, "thisisimagelink1");
        dxt.SaveInfo();
        dxt1.SaveInfo();



    }
}

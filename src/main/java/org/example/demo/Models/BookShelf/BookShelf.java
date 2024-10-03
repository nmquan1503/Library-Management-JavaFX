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
        authors.add("THAODAO");
        authors.add("XUÂNQUYEN");
        authors.add("ĐÀO");
        cate.add("trinh thám");
        cate.add("tiểu thuyết");
        cate.add("hài kịch");

        Book dxt = new Book("Harry Potter 2", authors, "KIM ĐỒNG", 2024, "Story about a wizard", 203, cate, 100, 4.5, "thisisimagelink");
        dxt.SaveInfo();

    }
}

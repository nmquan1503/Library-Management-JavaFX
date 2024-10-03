package org.example.demo.Models.BookShelf;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import org.example.demo.Database.JDBC;

public class Book {


    private int idBook;
    private int idAuthor;
    private String title;
    private ArrayList<String> authors;
    private String publisher;
    private int publishedDate;
    private String description;
    private int pageCount;
    private ArrayList<String> categories;
    private double averageRating;
    private int ratingsCount;
    private String imageLink;


    public Book(String title, ArrayList<String> authors, String publisher,
            int publishedDate,
            String description, int pageCount, ArrayList<String> categories, int ratingsCount,
            double averageRating, String imageLink) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.categories = categories;
        this.ratingsCount = ratingsCount;
        this.averageRating = averageRating;
        this.imageLink=imageLink;
    }

    public Book() {
    }

    public int getId() {
        return idBook;
    }

    public void setId(int id) {
        this.idBook = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public int getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(int publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
    public int SaveInfo() {
        try (Connection connection = JDBC.getConnection()){
            // Tạo câu lệnh SQL với placeholders
            String sql = "INSERT INTO books (id_book, title, desciption, publisher, published_date, page_count, count_rating, average_rating, link_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // Sử dụng PreparedStatement để chèn dữ liệu
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idBook);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4, publisher);
            statement.setInt(5,publishedDate);
            statement.setInt(6,pageCount);
            statement.setInt(7,ratingsCount);
            statement.setDouble(8,averageRating);
            statement.setString(9,imageLink);

            String sql1 = "INSERT INTO authors (id_author, name_author) VALUES (?,?)";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            for (String s: authors) {
                statement1.setInt(1,idAuthor);
                statement1.setString(2,s);
                statement1.addBatch();
            }

            statement1.executeBatch();
            statement.executeUpdate();
        }
        catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return idBook;
    }
}

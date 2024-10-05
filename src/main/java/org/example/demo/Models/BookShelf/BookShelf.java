package org.example.demo.Models.BookShelf;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.example.demo.Database.JDBC;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Trie.Trie;

public class BookShelf {
    private Trie books;
    public BookShelf() {
        books = new Trie();
        String selectQuery = "SELECT title, id_book FROM books";
        try (Connection connection = JDBC.getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
                ResultSet rs = pstmt.executeQuery();
                while(rs.next()) {
                    String s = rs.getString("title");
                    int t = rs.getInt("id_book");
                    books.insertNode(s,t);
                }
            }
            JDBC.closeConnection(connection);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public BookShelf(Trie books) {
        this.books=books;
    }
    public Trie getBooks() {
        return books;
    }
    public Book getBook( int id ) {
        Book res = new Book();
        try (Connection connection = JDBC.getConnection()) {
            String selectQuery = "SELECT title,description,publisher,published_date,page_count, count_rating, average_rating, link_image FROM books WHERE id_book = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    res.setTitle(rs.getString("title"));
                    res.setDescription(rs.getString("description"));
                    res.setPublisher(rs.getString("publisher"));
                    res.setPublishedDate(rs.getInt("published_date"));
                    res.setPageCount(rs.getInt("page_count"));
                    res.setRatingsCount(rs.getInt("count_rating"));
                    res.setAverageRating(rs.getDouble("average_rating"));
                    res.setImageLink(rs.getString("link_image"));
                }
                else {
                    return res;
                }
            }
            selectQuery = "SELECT id_author FROM book_author WHERE id_book = ?";
            ArrayList<Integer> idAuthor = new ArrayList<>();
            try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    idAuthor.add(rs.getInt("id_author"));
                }
            }

            selectQuery = "SELECT name_author FROM authors WHERE id_author = ?";
            ArrayList<String> nameAuthor = new ArrayList<>();
            try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
                for (Integer x : idAuthor) {
                    pstmt.setInt(1,x);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        nameAuthor.add(rs.getString("name_author"));
                    }
                }
            }
            res.setAuthors(nameAuthor);

            selectQuery = "SELECT id_category FROM book_category WHERE id_book = ?";
            ArrayList<Integer> idCate = new ArrayList<>();
            try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    idCate.add(rs.getInt("id_category"));
                }
            }

            selectQuery = "SELECT name_category FROM categories WHERE id_category = ?";
            ArrayList<String> nameCate = new ArrayList<>();
            try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
                for (Integer x : idCate) {
                    pstmt.setInt(1,x);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        nameCate.add(rs.getString("name_category"));
                    }
                }
            }
            res.setCategories(nameCate);
            res.setId(id);
            JDBC.closeConnection(connection);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    public int insertBook( Book book) {
        int id;
        id=book.SaveInfo();
        books.insertNode(book.getTitle(),id);
        return id;
    }
    public void deleteBook(Book book) {
        String selectQuery = "DELETE FROM books WHERE id_book = ?";
        String deleteOrphanAuthorsQuery = "DELETE FROM authors WHERE NOT EXISTS (SELECT 1 FROM book_author WHERE authors.id_author = book_author.id_author)";
        String deleteOrphanCategoriesQuery = "DELETE FROM categories WHERE NOT EXISTS (SELECT 1 FROM book_category WHERE categories.id_category = book_category.id_category)";
        try (Connection connection = JDBC.getConnection()) {
            try (PreparedStatement pstmt = connection.prepareStatement(selectQuery)) {
                pstmt.setInt(1,book.getId());
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmtDeleteAuthors = connection.prepareStatement(deleteOrphanAuthorsQuery)) {
                pstmtDeleteAuthors.executeUpdate();
            }
            try (PreparedStatement pstmtDeleteCategory = connection.prepareStatement(deleteOrphanCategoriesQuery)) {
                pstmtDeleteCategory.executeUpdate();
            }
            JDBC.closeConnection(connection);
        }
        catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        books.deleteNode(book.getTitle(),book.getId());


    }
    ArrayList<Book> getListBook(String prefix) {
        ArrayList<Book> res = new ArrayList<>();
        ArrayList<Integer> list = books.getListIdStartWith(prefix);
        for ( Integer x : list) {
            res.add(getBook(x));
        }
        return res;
    }
}

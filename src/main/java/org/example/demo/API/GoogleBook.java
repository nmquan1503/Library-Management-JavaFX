package org.example.demo.API;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.BookShelf.BookShelf;

public class GoogleBook {

  /**
   * api create list book from a word. get a json from Google book api. traverse json to create
   * books.
   *
   * @param prefix the word typed in text field.
   * @return a list of book.
   */
  public static ArrayList<Book> getBooks(String prefix) {
    ArrayList<Book> listBooks = new ArrayList<>();
    try {
      String api =
          "https://www.googleapis.com/books/v1/volumes?q=" + URLEncoder.encode(prefix, "UTF-8");
      URL url = new URL(api);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      int responseCode = connection.getResponseCode();
      System.out.println("response code:" + responseCode);
      if (responseCode == HttpURLConnection.HTTP_OK) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(connection.getInputStream());
        JsonNode items = jsonNode.get("items");
        if (items.isArray()) {
          for (JsonNode item : items) {
            if(item.has("volumeInfo")) {
              listBooks.add(createBookFromJson(item.get("volumeInfo")));
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return listBooks;
  }

  /**
   * a function create a book from a JsonNode.
   *
   * @param jsonNode a JsonNode contains all information of a book.
   * @return a book has information got from jsonNode.
   */
  private static Book createBookFromJson(JsonNode jsonNode) {
    int id = -1;
    String title = null;
    ArrayList<String> authors = null;
    String publisher = null;
    int publishedDate = -1;
    String description = null;
    int pageCount = -1;
    ArrayList<String> categories = null;
    double averageRating = 0;
    int ratingsCount = 0;
    String imageLink = null;
    int quantity=0;

    if (jsonNode.has("title")) {
      title = jsonNode.get("title").asText();
    }
    if (jsonNode.has("authors")) {
      JsonNode listAuthor = jsonNode.get("authors");
      authors = new ArrayList<>();
      for (JsonNode author : listAuthor) {
        authors.add(author.asText());
      }
    }
    if (jsonNode.has("publisher")) {
      publisher = jsonNode.get("publisher").asText();
    }
    if (jsonNode.has("publishedDate")) {
      publishedDate = jsonNode.get("publishedDate").asInt();
    }
    if (jsonNode.has("description")) {
      description = jsonNode.get("description").asText();
    }
    if (jsonNode.has("pageCount")) {
      pageCount = jsonNode.get("pageCount").asInt();
    }
    if (jsonNode.has("categories")) {
      JsonNode listCategory = jsonNode.get("categories");
      categories = new ArrayList<>();
      for (JsonNode category : listCategory) {
        categories.add(category.asText());
      }
    }
    if (jsonNode.has("ratingsCount")) {
      ratingsCount = jsonNode.get("ratingsCount").asInt();
    }
    if (jsonNode.has("averageRating")) {
      averageRating = jsonNode.get("averageRating").asDouble();
    }
    if (jsonNode.has("imageLinks")) {
      JsonNode imageLinks = jsonNode.get("imageLinks");
      if (imageLinks.has("thumbnail")) {
        imageLink = imageLinks.get("thumbnail").asText();
      }
    }

    return new Book(id, title, authors, publisher, publishedDate, description, pageCount,
        categories, ratingsCount, averageRating, imageLink,quantity);
  }

  public static void main(String[] args) {
    ArrayList<Book> books = getBooks("Harry Potter");
    System.out.println("books size=" + books.size());
    BookShelf bookShelf = new BookShelf();
    for (Book book : books) {
      bookShelf.insertBook(book);
    }
  }
}
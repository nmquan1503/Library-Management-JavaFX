package org.example.demo.CustomUI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.example.demo.Database.JDBC;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;

public class EditBookView extends ScrollPane {

  @FXML private ImageView imageBook;
  @FXML private TextField titleTextField;
  @FXML private Label idLabel;
  @FXML private VBox authorList;
  @FXML private TextField publisherTextField;
  @FXML private TextField publishedDateTextField;
  @FXML private TextArea descriptionTextArea;
  @FXML private VBox categoryList;
  @FXML private TextField countPageTextField;
  @FXML private TextField quantityTextField;

  private Book oldBook;
  private Library library;
  private ArrayList<Suggestion>listBooks;
  private Runnable laterAction;

  public EditBookView(Library library,ArrayList<Suggestion> listBooks,Runnable laterAction){
    initView();
    initImage(null);
    initId(null);
    initAuthors(null);
    initCategories(null);

    this.library=library;
    this.listBooks=listBooks;
    this.oldBook=null;
    this.laterAction=laterAction;

  }

  public EditBookView(Library library,ArrayList<Suggestion>listBooks,Book book,Runnable laterAction){
    initView();
    initImage(book);
    initTitle(book);
    initId(book);
    initAuthors(book);
    initPublisher(book);
    initPublishedDate(book);
    initDescription(book);
    initCategories(book);
    initPageCount(book);
    initQuantity(book);

    this.library=library;
    this.listBooks=listBooks;
    this.oldBook=book;
    this.laterAction=laterAction;
  }

  private void initImage(Book book){
    if(book==null){
      imageBook.setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));
    }
    else if(book.getImageLink()==null){
      imageBook.setImage(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));
    }
    else {
      imageBook.setImage(new Image(book.getImageLink()));
    }
  }

  private void initTitle(Book book){
    if(book==null)return;
    if(book.getTitle()==null)return;
    titleTextField.setText(book.getTitle());
  }

  private void initId(Book book){
    if(book!=null) {
      idLabel.setText("#" + book.getId());
    }
    else {
      int id=-1;
      Connection connection= JDBC.getConnection();
      try{
        String query="select max(id_book) as id "+
                      "from books ";
        PreparedStatement statement=connection.prepareStatement(query);
        ResultSet resultSet=statement.executeQuery();
        if(resultSet.next()){
          id=resultSet.getInt("id")+1;
        }
      }
      catch (Exception e){
        e.printStackTrace();
      }
      JDBC.closeConnection(connection);

      if(id!=-1)idLabel.setText("#"+id);

    }
  }

  private void initAuthors(Book book){
    authorList.getChildren().add(newTextField(authorList,"Tác giả "));
    if(book!=null) {
      if (book.getAuthors() != null) {
        for(String at:book.getAuthors()){
          ((TextField)authorList.getChildren().getLast()).setText(at);
        }
      }
    }
  }

  private void initPublisher(Book book){
    if(book==null)return;
    if(book.getPublisher()==null)return;
    publisherTextField.setText(book.getPublisher());
  }

  private void initPublishedDate(Book book){
    if(book==null)return;
    if(book.getPublishedDate()<0)return;
    publishedDateTextField.setText(String.valueOf(book.getPublishedDate()));
  }

  private void initDescription(Book book){
    if(book==null)return;
    if(book.getDescription()==null)return;
    descriptionTextArea.setText(book.getDescription());
  }

  private void initCategories(Book book){
    categoryList.getChildren().add(newTextField(categoryList,"Thể loại "));
    if(book!=null) {
      if (book.getCategories() != null) {
        for(String ct:book.getCategories()){
          ((TextField)categoryList.getChildren().getLast()).setText(ct);
        }
      }
    }
  }

  private void initPageCount(Book book){
    if(book==null)return;
    if(book.getPageCount()<0)return;
    countPageTextField.setText(String.valueOf(book.getPageCount()));
  }

  private void initQuantity(Book book){
    if(book==null)return;
    if(book.getQuantity()<0)return;
    quantityTextField.setText(String.valueOf(book.getQuantity()));
  }

  private TextField newTextField(VBox list, String promptText){
    TextField tf=new TextField();
    tf.setPromptText(promptText+(list.getChildren().size()+1));
    tf.setFont(Font.font(14));
    tf.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String oldValue,
          String newValue) {
        if(newValue.isEmpty()){
          while(list.getChildren().size()>1){
            int size=list.getChildren().size();
            if(((TextField)list.getChildren().get(size-2)).getText().isEmpty()){
              list.getChildren().removeLast();
            }
            else break;
          }
        }
        else {
          if(list.getChildren().indexOf(tf)==list.getChildren().size()-1){
            list.getChildren().add(newTextField(list,promptText));
          }
        }
      }
    });
    return tf;
  }


  private void initView(){
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/demo/FXML/EditBookView.fxml"));
      fxmlLoader.setController(this);
      AnchorPane anchorPane = fxmlLoader.load();
      this.setContent(anchorPane);
    }
    catch (Exception e){
      e.printStackTrace();
    }

    this.setPrefHeight(540);
    this.setPrefWidth(900);
    this.getStylesheets().add(getClass().getResource("/org/example/demo/CSS/BookView.css").toExternalForm());
    this.setId("FadedScrollPane");

  }



  @FXML
  private void ExitView(){
    AnchorPane mainPane=(AnchorPane) this.getParent();
    ConfirmBox confirmBox=new ConfirmBox(
        "Xác nhận hủy sự thay đổi?",
        "Nếu bạn chọn \"No\", bạn sẽ được tiếp tục thay đổi nội dung sách muốn thêm.",
        ()->{
          mainPane.getChildren().removeLast();
          mainPane.getChildren().removeLast();
        },
        ()->{
          mainPane.getChildren().removeLast();
        }
    );
    mainPane.getChildren().add(confirmBox);
  }

  @FXML
  private void SaveBook(){
    AnchorPane mainPane=(AnchorPane) this.getParent();
    ConfirmBox confirmBox=new ConfirmBox(
        "Xác nhận Lưu?",
        "Nếu bạn chọn \"No\", bạn sẽ được tiếp tục thay đổi nội dung sách muốn thêm.",
        ()->{
          mainPane.getChildren().removeLast();
          Thread thread=new Thread(()->{
            if(oldBook!=null)library.deleteBook(oldBook);
            Book newBook=createNewBook();
            library.insertBookWithID(newBook,newBook.getId());

            if(oldBook==null){
              listBooks.add(new Suggestion(newBook));
            }
            else {
              for(int i=0;i<listBooks.size();i++){
                if(listBooks.get(i).getId()==newBook.getId()){
                  listBooks.set(i,new Suggestion(newBook));
                  break;
                }
              }
            }

            Platform.runLater(laterAction);

          });
          thread.start();
          mainPane.getChildren().removeLast();
        },
        ()->{
          mainPane.getChildren().removeLast();
        }
    );
    mainPane.getChildren().add(confirmBox);
  }

  private Book createNewBook(){
    Book book=new Book();

    if(oldBook!=null)book.setImageLink(oldBook.getImageLink());
    else book.setImageLink(null);

    book.setTitle(titleTextField.getText());

    book.setId(Integer.parseInt(idLabel.getText().substring(1)));
    System.out.println(book.getId());
    ArrayList<String> authors=new ArrayList<>();
    for(int i=0;i<authorList.getChildren().size()-1;i++){
      authors.add(((TextField)authorList.getChildren().get(i)).getText());
    }
    book.setAuthors(authors);

    book.setPublisher(publisherTextField.getText());

    if(!publishedDateTextField.getText().isEmpty()){
      book.setPublishedDate(Integer.parseInt(publishedDateTextField.getText()));
    }

    book.setDescription(descriptionTextArea.getText());

    ArrayList<String> categories=new ArrayList<>();
    for(int i=0;i<categoryList.getChildren().size()-1;i++){
      categories.add(((TextField)categoryList.getChildren().get(i)).getText());
    }
    book.setCategories(categories);

    if(!countPageTextField.getText().isEmpty()){
      book.setPageCount(Integer.parseInt(countPageTextField.getText()));
    }

    if(!quantityTextField.getText().isEmpty()){
      book.setQuantity(Integer.parseInt(quantityTextField.getText()));
    }

    if(oldBook!=null)book.setRatingsCount(oldBook.getRatingsCount());
    else book.setRatingsCount(0);

    if(oldBook!=null)book.setAverageRating(oldBook.getAverageRating());
    else book.setAverageRating(0);

    return book;

  }




}

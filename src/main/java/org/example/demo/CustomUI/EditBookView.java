package org.example.demo.CustomUI;

import com.jfoenix.controls.JFXButton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
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
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.example.demo.API.Network;
import org.example.demo.Controllers.BaseController;
import org.example.demo.Controllers.EditController;
import org.example.demo.Database.JDBC;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;

public class EditBookView extends ScrollPane implements MainInfo {

  @FXML private AnchorPane viewPane;

  @FXML private AnchorPane wrapper;
  @FXML private ImageView imageBook;

  @FXML private TextField titleTextField;
  @FXML private Label idLabel;

  @FXML private VBox authorList;

  @FXML private Label publisherTag;
  @FXML private TextField publisherTextField;

  @FXML private Label publishedDateTag;
  @FXML private TextField publishedDateTextField;

  @FXML private Label descriptionTag;
  @FXML private TextArea descriptionTextArea;

  @FXML private Label categoryTag;
  @FXML private VBox categoryList;

  @FXML private Label pageCountTag;
  @FXML private TextField countPageTextField;

  @FXML private Label quantityTag;
  @FXML private TextField quantityTextField;

  @FXML private JFXButton saveButton;

  @FXML private Pane loadingPane;
  private Transition loadingTransition;

  private Book oldBook;

  private HashMap<Object,String > viLang;
  private HashMap<Object,String > enLang;

  private EditController editController;

  public EditBookView(EditController editController){
    this.editController=editController;
    initView();
    initLoadingTransition();
  }

  public void setBook(Book book){
    if(book==null){
      initDefaultImage();
      initDefaultId();
      authorList.getChildren().add(newTextField(authorList,"Tác giả "));
      categoryList.getChildren().add(newTextField(categoryList,"Thể loại "));
    }
    else {
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
    }
    this.oldBook=book;
  }

  public void completeSetup(){
    viLang=new HashMap<>();
    enLang=new HashMap<>();
    setUpLanguage(viLang,enLang);
    if(BaseController.isTranslate){
      applyTranslate(null,null,true);
    }
    viewPane.getChildren().remove(loadingPane);
    loadingPane=null;
    loadingTransition.stop();
    loadingTransition=null;
  }

  private void initImage(Book book){
    if(book.getImageLink()==null || !Network.isConnected()){
      initDefaultImage();
      return;
    }
    else {
      imageBook.setImage(new Image(book.getImageLink()));
    }
    if(BaseController.isDark){
      wrapper.setBlendMode(BlendMode.DIFFERENCE);
      wrapper.setId("wrapper_dark");
    }
    else wrapper.setBlendMode(BlendMode.SRC_OVER);
  }

  private void initDefaultImage(){
    imageBook.setImage(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));
    if(BaseController.isDark){
      wrapper.setBlendMode(BlendMode.DIFFERENCE);
    }
    else wrapper.setBlendMode(BlendMode.SRC_OVER);
  }

  private void initTitle(Book book){
    if(book==null)return;
    if(book.getTitle()==null)return;
    titleTextField.setText(book.getTitle());
  }

  private void initId(Book book){
      if(book.getId()!=-1) {
        idLabel.setText("#" + book.getId());
        return;
      }
      initDefaultId();
  }

  private void initDefaultId(){
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
    idLabel.setText("#"+id);
  }

  private void initAuthors(Book book){
    authorList.getChildren().add(newTextField(authorList,"Tác giả "));
      if (book.getAuthors() != null) {
        for(String at:book.getAuthors()){
          ((TextField)authorList.getChildren().getLast()).setText(at);
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
    if(book.getPublishedDate()<=0)return;
    publishedDateTextField.setText(String.valueOf(book.getPublishedDate()));
  }

  private void initDescription(Book book){
    if(book==null)return;
    if(book.getDescription()==null)return;
    descriptionTextArea.setText(book.getDescription());
  }

  private void initCategories(Book book){
    categoryList.getChildren().add(newTextField(categoryList,"Thể loại "));
      if (book.getCategories() != null) {
        for(String ct:book.getCategories()){
          ((TextField)categoryList.getChildren().getLast()).setText(ct);
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
  public void ExitView(){
    AnchorPane mainPane=(AnchorPane) this.getParent();
    ConfirmBox confirmBox=new ConfirmBox(
        "Xác nhận hủy sự thay đổi?",
        "Nếu bạn chọn \"Hủy\", bạn sẽ được tiếp tục thay đổi nội dung sách muốn thêm.",
        ()->{
          mainPane.getChildren().removeLast();
          mainPane.getChildren().removeLast();
        },
        ()->{
          mainPane.getChildren().removeLast();
        }
    );
    while (mainPane.getChildren().getLast() instanceof ConfirmBox){
      mainPane.getChildren().removeLast();
    }
    mainPane.getChildren().add(confirmBox);
  }

  @FXML
  private void SaveBook(){
    AnchorPane mainPane=(AnchorPane) this.getParent();
    ConfirmBox confirmBox=new ConfirmBox(
        "Xác nhận Lưu?",
        "Nếu bạn chọn \"Hủy\", bạn sẽ được tiếp tục thay đổi nội dung sách muốn thêm.",
        ()->{
          Node parent=mainPane.getParent();
          if(parent!=null){
            if(parent instanceof AnchorPane){
              if(oldBook!=null){
                ((AnchorPane) parent).getChildren().add(new Warning(
                    "Thành công!",
                    "Đã thay đổi thông tin sách."
                ));
              }
              else {
                ((AnchorPane) parent).getChildren().add(new Warning(
                    "Thành công!",
                    "Sách đã được thêm thành công."
                ));
              }
            }
          }
          mainPane.getChildren().removeLast();
          Thread thread=new Thread(()->{
            if(oldBook!=null){
              if(oldBook.getId()!=-1) {
                Library.getInstance().deleteBook(oldBook);
              }
            }
            Book newBook=createNewBook();
            if(newBook!=null) {
              Library.getInstance().insertBookWithID(newBook, newBook.getId());

              if (oldBook == null) {
                Platform.runLater(()->{
                  editController.addBookSuggestion(new Suggestion(newBook));
                });
              } else if (oldBook.getId() == -1) {
                Platform.runLater(()->{
                  editController.addBookSuggestion(new Suggestion(newBook));
                });
              } else {
                Platform.runLater(()->{
                  editController.fixBookSuggestion(new Suggestion(newBook));
                });
              }
            }

          });
          thread.start();
          mainPane.getChildren().removeLast();
        },
        ()->{
          mainPane.getChildren().removeLast();
        }
    );
    while (mainPane.getChildren().getLast() instanceof ConfirmBox){
      mainPane.getChildren().removeLast();
    }
    mainPane.getChildren().add(confirmBox);
  }

  private Book createNewBook(){
    Book book=new Book();

    if(oldBook!=null)book.setImageLink(oldBook.getImageLink());
    else book.setImageLink(null);

    book.setTitle(titleTextField.getText());

    book.setId(Integer.parseInt(idLabel.getText().substring(1)));

    ArrayList<String> authors=new ArrayList<>();
    for(int i=0;i<authorList.getChildren().size()-1;i++){
      authors.add(((TextField)authorList.getChildren().get(i)).getText());
    }
    if(authors.isEmpty())authors=null;
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
    if(categories.isEmpty())categories=null;
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

  private void initLoadingTransition() {
    Arc arc1 = (Arc) loadingPane.getChildren().getFirst();
    Arc arc2 = (Arc) loadingPane.getChildren().get(1);
    Arc arc3 = (Arc) loadingPane.getChildren().get(2);

    RotateTransition transition1 = new RotateTransition(Duration.millis(1000), arc1);
    transition1.setByAngle(360);
    transition1.setCycleCount(Transition.INDEFINITE);
    transition1.setAutoReverse(false);

    RotateTransition transition2 = new RotateTransition(Duration.millis(700), arc2);
    transition2.setByAngle(360);
    transition2.setCycleCount(Transition.INDEFINITE);
    transition2.setAutoReverse(false);

    RotateTransition transition3 = new RotateTransition(Duration.millis(400), arc3);
    transition3.setByAngle(360);
    transition3.setCycleCount(Transition.INDEFINITE);
    transition3.setAutoReverse(false);

    loadingTransition = new ParallelTransition(transition1, transition2, transition3);
    loadingTransition.play();
  }


  @Override
  public void applyDarkMode(boolean isDark) {
    if(isDark){
      this.wrapper.setBlendMode(BlendMode.DIFFERENCE);
      wrapper.setId("wrapper_dark");
    }
    else {
      this.wrapper.setBlendMode(BlendMode.SRC_OVER);
      wrapper.setId("wrapper_light");
    }
  }

  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {
    if(isTranslate){
      titleTextField.setPromptText("Name");

      for(int i=0;i<authorList.getChildren().size();i++){
        if(authorList.getChildren().get(i) instanceof TextField){
          ((TextField) authorList.getChildren().get(i)).setPromptText("Author "+(i+1));
        }
      }

      publisherTag.setText("Publisher: ");
      publisherTextField.setPromptText("Publisher");

      publishedDateTag.setText("Published Date: ");
      publishedDateTextField.setPromptText("Published Date");

      descriptionTag.setText("Description: ");
      descriptionTextArea.setPromptText("Description");

      categoryTag.setText("Categories: ");
      for(int i=0;i<categoryList.getChildren().size();i++){
        if(categoryList.getChildren().get(i) instanceof TextField){
          ((TextField)categoryList.getChildren().get(i)).setPromptText("Category "+(i+1));
        }
      }

      pageCountTag.setText("Page count: ");
      countPageTextField.setPromptText("Page count:");

      quantityTag.setText("Quantity: ");
      quantityTextField.setPromptText("Quantity");

      saveButton.setText("Save");
    }
    else {
      titleTextField.setPromptText("Tên sách");

      for(int i=0;i<authorList.getChildren().size();i++){
        if(authorList.getChildren().get(i) instanceof TextField){
          ((TextField) authorList.getChildren().get(i)).setPromptText("Tác giả "+(i+1));
        }
      }

      publisherTag.setText("Nhà xuất bản: ");
      publisherTextField.setPromptText("Nhà xuất bản");

      publishedDateTag.setText("Năm xuất bản: ");
      publishedDateTextField.setPromptText("Năm xuất bản");

      descriptionTag.setText("Mô tả: ");
      descriptionTextArea.setPromptText("Mô tả");

      categoryTag.setText("Categories: ");
      for(int i=0;i<categoryList.getChildren().size();i++){
        if(categoryList.getChildren().get(i) instanceof TextField){
          ((TextField)categoryList.getChildren().get(i)).setPromptText("Thể loại "+(i+1));
        }
      }

      pageCountTag.setText("Độ dài: ");
      countPageTextField.setPromptText("Độ dài");

      quantityTag.setText("Số lượng: ");
      quantityTextField.setPromptText("Số lượng");

      saveButton.setText("Lưu");
    }
  }

  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {
  }

}

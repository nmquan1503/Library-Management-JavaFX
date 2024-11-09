package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.example.demo.API.GoogleBook;
import org.example.demo.CustomUI.ConfirmBox;
import org.example.demo.CustomUI.EditBookView;
import org.example.demo.CustomUI.EditBox;
import org.example.demo.CustomUI.EditBox.TypeBox;
import org.example.demo.CustomUI.EditUserView;
import org.example.demo.CustomUI.SuggestionView;
import org.example.demo.CustomUI.UserView;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Users.User;

public class EditController implements MainInfo {

  private BooksController booksController;
  private UsersController usersController;

  @FXML
  private AnchorPane mainPane;

  @FXML
  private TextField pageBookNumberTextField;
  @FXML
  private JFXButton nextPageBookButton;
  @FXML
  private JFXButton prevPageBookButton;

  @FXML private JFXButton addBookButton;

  @FXML
  private JFXListView<EditBox> listViewBooks;
  private ArrayList<Suggestion> listBooks;


  @FXML
  private TextField pageUserNumberTextField;
  @FXML
  private JFXButton nextPageUserButton;
  @FXML
  private JFXButton prevPageUserButton;

  @FXML private JFXButton addUserButton;

  @FXML
  private JFXListView<EditBox> listViewUsers;
  private ArrayList<Suggestion> listUsers;

  @FXML private TextField bookSuggestionsTextField;
  @FXML private JFXButton removeButton;
  @FXML private JFXListView<SuggestionView> googleBooksListView;


  @FXML
  private void initialize() {
    setUpPageNumberTextField(pageBookNumberTextField);
    setUpPageNumberTextField(pageUserNumberTextField);
    setUpFocusTextField();
    initListView();
  }

  private void initListView() {
    Thread threadCreateBooks = new Thread(() -> {
      listBooks = Library.getInstance().getBookSuggestions("");
      Platform.runLater(() -> {
        setPageBook(1);
        pageBookNumberTextField.setText("1");
        prevPageBookButton.setVisible(false);
        nextPageBookButton.setVisible(1 != (listBooks.size() - 1) / 20 + 1);
      });
    });
    threadCreateBooks.start();

    Thread threadCreateUsers = new Thread(() -> {
      listUsers = Library.getInstance().getUserSuggestions("");
      Platform.runLater(() -> {
        setPageUser(1);
        pageUserNumberTextField.setText("1");
        prevPageUserButton.setVisible(false);
        nextPageUserButton.setVisible(1 != (listUsers.size() - 1) / 20 + 1);
      });
    });
    threadCreateUsers.start();
  }

  @FXML
  private void switchToNextPageBook() {
    int pageNumber = Integer.parseInt(pageBookNumberTextField.getText());
    pageBookNumberTextField.setText(String.valueOf(pageNumber + 1));
    setPageBook(pageNumber + 1);
    nextPageBookButton.setVisible(pageNumber + 1 != (listBooks.size() - 1) / 20 + 1);
    prevPageBookButton.setVisible(pageNumber + 1 != 1);
  }

  @FXML
  private void switchToPrevPageBook() {
    int pageNumber = Integer.parseInt(pageBookNumberTextField.getText());
    pageBookNumberTextField.setText(String.valueOf(pageNumber - 1));
    setPageBook(pageNumber - 1);
    nextPageBookButton.setVisible(pageNumber - 1 != (listBooks.size() - 1) / 20 + 1);
    prevPageBookButton.setVisible(pageNumber - 1 != 1);
  }

  @FXML
  private void changePageBook() {
    int pageNumber = Integer.parseInt(pageBookNumberTextField.getText());
    if (pageNumber > (listBooks.size() - 1) / 20 + 1) {
      pageNumber = (listBooks.size() - 1) / 20 + 1;
      pageBookNumberTextField.setText(String.valueOf(pageNumber));
    } else if (pageNumber < 1) {
      pageNumber = 1;
      pageBookNumberTextField.setText(String.valueOf(pageNumber));
    }

    nextPageBookButton.setVisible(pageNumber != (listBooks.size() - 1) / 20 + 1);
    prevPageBookButton.setVisible(pageNumber != 1);

    setPageBook(pageNumber);
  }

  @FXML
  private void switchToNextPageUser() {
    int pageNumber = Integer.parseInt(pageUserNumberTextField.getText());
    pageUserNumberTextField.setText(String.valueOf(pageNumber + 1));
    setPageUser(pageNumber + 1);
    nextPageUserButton.setVisible(pageNumber + 1 != (listUsers.size() - 1) / 20 + 1);
    prevPageUserButton.setVisible(pageNumber+1!=1);
  }

  @FXML
  private void switchToPrevPageUser() {
    int pageNumber = Integer.parseInt(pageUserNumberTextField.getText());
    pageUserNumberTextField.setText(String.valueOf(pageNumber - 1));
    setPageUser(pageNumber - 1);
    nextPageUserButton.setVisible(pageNumber-1 !=(listUsers.size() - 1) / 20 + 1);
    prevPageUserButton.setVisible(pageNumber - 1 != 1);
  }

  @FXML
  private void changePageUser() {
    int pageNumber = Integer.parseInt(pageUserNumberTextField.getText());
    if (pageNumber > (listUsers.size() - 1) / 20 + 1) {
      pageNumber = (listUsers.size() - 1) / 20 + 1;
      pageUserNumberTextField.setText(String.valueOf(pageNumber));
    } else if (pageNumber < 1) {
      pageNumber = 1;
      pageUserNumberTextField.setText(String.valueOf(pageNumber));
    }

    nextPageUserButton.setVisible(pageNumber != (listUsers.size() - 1) / 20 + 1);
    prevPageUserButton.setVisible(pageNumber != 1);

    setPageUser(pageNumber);
  }


  private void setPageBook(int pageNumber) {

    if (pageNumber > (listBooks.size() - 1) / 20 + 1) {
      pageNumber = (listBooks.size() - 1) / 20 + 1;
      pageBookNumberTextField.setText(String.valueOf(pageNumber));
      nextPageBookButton.setVisible(pageNumber != (listBooks.size() - 1) / 20 + 1);
      prevPageBookButton.setVisible(pageNumber != 1);
    }

    int start = pageNumber * 20 - 20;
    int end = Math.min(start + 19, listBooks.size() - 1);
    ObservableList<EditBox> observableList = FXCollections.observableArrayList();
    listViewBooks.setItems(observableList);
    Thread thread = new Thread(() -> {
      Platform.runLater(() -> {
        for (int i = start; i <= end; i++) {
          observableList.add(new EditBox(listBooks.get(i),TypeBox.BOOK,this,50,400));
        }
      });
    });
    thread.start();
  }

  private void setPageUser(int pageNumber) {

    if (pageNumber > (listUsers.size() - 1) / 20 + 1) {
      pageNumber = (listUsers.size() - 1) / 20 + 1;
      pageUserNumberTextField.setText(String.valueOf(pageNumber));
      nextPageUserButton.setVisible(pageNumber != (listUsers.size() - 1) / 20 + 1);
      prevPageUserButton.setVisible(pageNumber != 1);
    }

    int start = pageNumber * 20 - 20;
    int end = Math.min(start + 19, listUsers.size() - 1);
    ObservableList<EditBox> observableList = FXCollections.observableArrayList();
    listViewUsers.setItems(observableList);
    Thread thread = new Thread(() -> {
      Platform.runLater(() -> {
        for (int i = start; i <= end; i++) {
          observableList.add(new EditBox(listUsers.get(i),TypeBox.USER,this,50,400));
        }
      });
    });
    thread.start();
  }

  private void setUpPageNumberTextField(TextField textField) {
    textField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        if (!newValue.matches("\\d*")) {
          textField.setText(newValue.replaceAll("[^\\d]", ""));
        }
      }
    });
  }

  @FXML
  private void AddBook() {
    Thread thread = new Thread(() -> {
      EditBookView editBookView=new EditBookView(this);
      editBookView.setScaleX(0);
      editBookView.setScaleY(0);
      Platform.runLater(() -> {
        while (mainPane.getChildren().getLast() instanceof ConfirmBox ||
            mainPane.getChildren().getLast() instanceof EditBookView ||
            mainPane.getChildren().getLast() instanceof EditUserView)
        {
          mainPane.getChildren().removeLast();
        }
        mainPane.getChildren().add(editBookView);
        editBookView.setBook(null);
        editBookView.completeSetup();
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), editBookView);
        transition.setToY(1);
        transition.setToX(1);
        transition.play();
      });
    });
    thread.start();


  }

  @FXML
  private void FixBook() {
    Thread thread = new Thread(() -> {
      EditBookView editBookView=new EditBookView(this);
      Platform.runLater(()->{
        while (mainPane.getChildren().getLast() instanceof ConfirmBox ||
            mainPane.getChildren().getLast() instanceof EditBookView ||
            mainPane.getChildren().getLast() instanceof EditUserView)
        {
          mainPane.getChildren().removeLast();
        }
        mainPane.getChildren().add(editBookView);
        editBookView.setScaleX(0);
        editBookView.setScaleY(0);
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), editBookView);
        transition.setToX(1);
        transition.setToY(1);
        transition.play();
        Thread thread1 = new Thread(()->{
          EditBox suggestionView = listViewBooks.getSelectionModel().getSelectedItem();
          Book book = Library.getInstance().getBook(suggestionView.getID());
          Platform.runLater(()->{
            PauseTransition pauseTransition=new PauseTransition(Duration.millis(700));
            pauseTransition.setOnFinished(e->{
              editBookView.setBook(book);
              editBookView.completeSetup();
            });
            pauseTransition.play();
          });
        });
        thread1.start();
      });

    });
    thread.start();

  }

  @FXML
  private void openAddUserView() {
    Thread thread = new Thread(() -> {
      EditUserView editUserView = new EditUserView(this);
      editUserView.setScaleX(0);
      editUserView.setScaleY(0);
      Platform.runLater(() -> {
        while (mainPane.getChildren().getLast() instanceof ConfirmBox ||
            mainPane.getChildren().getLast() instanceof EditBookView ||
            mainPane.getChildren().getLast() instanceof EditUserView)
        {
          mainPane.getChildren().removeLast();
        }
        mainPane.getChildren().add(editUserView);
        editUserView.setUser(null);
        editUserView.completeSetup();
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), editUserView);
        transition.setToX(1);
        transition.setToY(1);
        transition.play();

      });
    });
    thread.start();
  }

  @FXML
  private void FixUser() {
    Thread thread = new Thread(() -> {

      EditUserView editUserView=new EditUserView(this);
      editUserView.setScaleX(0);
      editUserView.setScaleY(0);

      Platform.runLater(() -> {
        while (mainPane.getChildren().getLast() instanceof ConfirmBox ||
                mainPane.getChildren().getLast() instanceof EditBookView ||
                mainPane.getChildren().getLast() instanceof EditUserView)
        {
          mainPane.getChildren().removeLast();
        }
        mainPane.getChildren().add(editUserView);
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), editUserView);
        transition.setToX(1);
        transition.setToY(1);
        transition.play();
        Thread thread1 = new Thread(()->{
          EditBox suggestionView = listViewUsers.getSelectionModel().getSelectedItem();
          User user = Library.getInstance().getUser(suggestionView.getID());
          Platform.runLater(()->{
            PauseTransition pauseTransition=new PauseTransition(Duration.millis(700));
            pauseTransition.setOnFinished(e->{
              editUserView.setUser(user);
              editUserView.completeSetup();
            });
            pauseTransition.play();
          });
        });
        thread1.start();

      });
    });
    thread.start();

  }

  public void addUserSuggestion(Suggestion suggestion){
    int pageNumber = Integer.parseInt(pageUserNumberTextField.getText());
    if (pageNumber == (listUsers.size() - 1) / 20 + 1){
      if(listViewUsers.getItems().size()<20){
        listViewUsers.getItems().add(new EditBox(suggestion, TypeBox.USER,this,50,400));
      }
      else {
        nextPageUserButton.setVisible(true);
      }
    }
    listUsers.add(suggestion);
    usersController.addUserSuggestion(suggestion);
  }

  public void addBookSuggestion(Suggestion suggestion){
    int pageNumber = Integer.parseInt(pageBookNumberTextField.getText());
    if (pageNumber == (listBooks.size() - 1) / 20 + 1){
      if(listViewBooks.getItems().size()<20){
        listViewBooks.getItems().add(new EditBox(suggestion, TypeBox.BOOK,this,50,400));
      }
      else {
        nextPageBookButton.setVisible(true);
      }
    }
    listBooks.add(suggestion);
    booksController.addBookSuggestion(suggestion);
  }

  public void deleteUserSuggestion(Suggestion suggestion){
    int pageNumber = Integer.parseInt(pageUserNumberTextField.getText());
    for(int i=0;i<listViewUsers.getItems().size();i++){
      if(listViewUsers.getItems().get(i).getID()==suggestion.getId()){
        listViewUsers.getItems().remove(i);
        break;
      }
    }
    if(listViewUsers.getItems().isEmpty()){
      if(pageNumber-1>=1) {
        setPageUser(pageNumber - 1);
      }
    }
    else if(pageNumber*20<listUsers.size()){
      listViewUsers.getItems().add(new EditBox(listUsers.get(pageNumber*20),TypeBox.USER,this,50,400));
    }
    usersController.deleteUserSuggestion(suggestion);
  }

  public void deleteBookSuggestion(Suggestion suggestion){
    int pageNumber = Integer.parseInt(pageBookNumberTextField.getText());
    for(int i=0;i<listViewBooks.getItems().size();i++){
      if(listViewBooks.getItems().get(i).getID()==suggestion.getId()){
        listViewBooks.getItems().remove(i);
        break;
      }
    }
    if(listViewBooks.getItems().isEmpty()){
      if(pageNumber-1>=1) {
        setPageBook(pageNumber - 1);
      }
    }
    else if(pageNumber*20<listBooks.size()){
      listViewBooks.getItems().add(new EditBox(listBooks.get(pageNumber*20),TypeBox.BOOK,this,50,400));
    }
    booksController.deleteBookSuggestion(suggestion);
  }

  public void fixUserSuggestion(Suggestion suggestion){
    int pageNumber = Integer.parseInt(pageUserNumberTextField.getText());
    for(int i=pageNumber*20-20;i<Math.min(pageNumber*20,listUsers.size());i++){
      if(listUsers.get(i).getId()==suggestion.getId()){
        listUsers.set(i,suggestion);
        break;
      }
    }
    for(int i=0;i<Math.min(20,listViewUsers.getItems().size());i++){
      if(listViewUsers.getItems().get(i).getID()==suggestion.getId()){
        listViewUsers.getItems().set(i,new EditBox(suggestion,TypeBox.USER,this, 50,400));
        break;
      }
    }
    usersController.fixUserSuggestion(suggestion);
  }

  public void fixBookSuggestion(Suggestion suggestion){
    int pageNumber = Integer.parseInt(pageBookNumberTextField.getText());
    for(int i=pageNumber*20-20;i<pageNumber*20;i++){
      if(listBooks.get(i).getId()==suggestion.getId()){
        listBooks.set(i,suggestion);
        break;
      }
    }
    for(int i=0;i<Math.min(20,listViewBooks.getItems().size());i++){
      if(listViewBooks.getItems().get(i).getID()==suggestion.getId()){
        listViewBooks.getItems().set(i,new EditBox(suggestion,TypeBox.BOOK,this, 50,400));
        break;
      }
    }
    booksController.fixBookSuggestion(suggestion);
  }

  @FXML
  private void SearchBooksFromGoogle(){
    String title=bookSuggestionsTextField.getText();
    if(title.isEmpty()){
      googleBooksListView.getItems().clear();
      googleBooksListView.setVisible(false);
      googleBooksListView.setMaxHeight(0);
      googleBooksListView.setMinHeight(0);
      return;
    }

    ObservableList<SuggestionView> observableList=FXCollections.observableArrayList();
    googleBooksListView.setItems(observableList);

    Thread thread=new Thread(()->{
      ArrayList<Book> books = GoogleBook.getBooks(title);

      Platform.runLater(()->{
        for(Book book:books){
          observableList.add(new SuggestionView(
              new Suggestion(book),
              50,
              300
          ));
        }
      });

      googleBooksListView.setOnMouseClicked(e->{
        int id=googleBooksListView.getSelectionModel().getSelectedIndex();
        bookSuggestionsTextField.requestFocus();
        Thread thread1 = new Thread(() -> {
          EditBookView editBookView=new EditBookView(this);
          Platform.runLater(()->{
            mainPane.getChildren().add(editBookView);
            editBookView.setScaleX(0);
            editBookView.setScaleY(0);
            ScaleTransition transition = new ScaleTransition(Duration.millis(200), editBookView);
            transition.setToX(1);
            transition.setToY(1);
            transition.play();
            Book book = books.get(id);
            PauseTransition pauseTransition=new PauseTransition(Duration.millis(700));
            pauseTransition.setOnFinished(event->{
              editBookView.setBook(book);
              editBookView.completeSetup();
            });
            pauseTransition.play();
          });

        });
        thread1.start();
      });

      Platform.runLater(() -> {
        googleBooksListView.setVisible(true);
        int heightOfListView = Math.min(googleBooksListView.getItems().size(), 5) * 55;
        googleBooksListView.setMinHeight(heightOfListView);
        googleBooksListView.setMaxHeight(heightOfListView);
      });


    });
    thread.start();

  }

  private void setUpFocusTextField(){
    bookSuggestionsTextField.focusedProperty().addListener((observable, oldValue,newValue)->{
      if(newValue){
        googleBooksListView.setVisible(true);
        int heightOfListView = Math.min(googleBooksListView.getItems().size(), 5) * 55;
        googleBooksListView.setMinHeight(heightOfListView);
        googleBooksListView.setMaxHeight(heightOfListView);
      }
      else {
        if(!googleBooksListView.isFocused()) {
          googleBooksListView.setVisible(false);
          googleBooksListView.setMinHeight(0);
          googleBooksListView.setMaxHeight(0);
        }
      }
    });

    bookSuggestionsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      removeButton.setVisible(!newValue.isEmpty());
    });

  }

  public void setBooksController(BooksController booksController){
    this.booksController=booksController;
  }

  public void setUsersController(UsersController usersController){
    this.usersController=usersController;
  }

  public AnchorPane getMainPane(){
    return this.mainPane;
  }

  @FXML
  private void removeText(){
    bookSuggestionsTextField.clear();
  }

  // set BlendMode của các ImageView là DIFFERENCE nếu isDark = true và SRC_OVER trong th còn lại
  @Override
  public void applyDarkMode(boolean isDark) {
    for(EditBox editBox:listViewUsers.getItems()){
      editBox.applyDarkMode(isDark);
    }
    for(EditBox editBox:listViewBooks.getItems()){
      editBox.applyDarkMode(isDark);
    }
    int id=mainPane.getChildren().size()-1;
    if(mainPane.getChildren().get(id) instanceof ConfirmBox){
      id--;
    }
    if(mainPane.getChildren().get(id) instanceof EditUserView){
      ((EditUserView) mainPane.getChildren().get(id)).applyDarkMode(isDark);
    }
    else if(mainPane.getChildren().get(id) instanceof EditBookView){
      ((EditBookView) mainPane.getChildren().get(id)).applyDarkMode(isDark);
    }
  }

  // Không gọi setUpLanguage ở đây
  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {
    if(isTranslate){
      bookSuggestionsTextField.setPromptText("Add books from Google Books");
      addBookButton.setText("Add new book");

      addUserButton.setText("Add new user");
    }
    else {
      bookSuggestionsTextField.setPromptText("Thêm sách từ Google Books");
      addBookButton.setText("Thêm sách");

      addUserButton.setText("Thêm user");
    }
    int id=mainPane.getChildren().size()-1;
    if(mainPane.getChildren().get(id) instanceof ConfirmBox){
      ((ConfirmBox) mainPane.getChildren().get(id)).applyTranslate(null,null,isTranslate);
      id--;
    }
    if(mainPane.getChildren().get(id) instanceof EditUserView){
      ((EditUserView) mainPane.getChildren().get(id)).applyTranslate(null,null,isTranslate);
    }
    else if(mainPane.getChildren().get(id) instanceof EditBookView){
      ((EditBookView) mainPane.getChildren().get(id)).applyTranslate(null,null,isTranslate);
    }
  }

  // viLang lưu nội dung tiếng Việt gắn với Object, enLang lưu tiếng Anh
  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }


}

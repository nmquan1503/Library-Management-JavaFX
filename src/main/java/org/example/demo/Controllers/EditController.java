package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.example.demo.CustomUI.EditUserView;
import org.example.demo.CustomUI.SuggestionView;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Users.User;

public class EditController implements MainInfo {

  @FXML
  private AnchorPane mainPane;

  @FXML
  private TextField pageBookNumberTextField;
  @FXML
  private JFXButton nextPageBookButton;
  @FXML
  private JFXButton prevPageBookButton;

  @FXML
  private JFXListView<EditBox> listViewBooks;
  private ArrayList<Suggestion> listBooks;


  @FXML
  private TextField pageUserNumberTextField;
  @FXML
  private JFXButton nextPageUserButton;
  @FXML
  private JFXButton prevPageUserButton;

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
  }

  @FXML
  private void switchToPrevPageUser() {
    int pageNumber = Integer.parseInt(pageUserNumberTextField.getText());
    pageUserNumberTextField.setText(String.valueOf(pageNumber - 1));
    setPageUser(pageNumber - 1);
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
          int finalI = i;
          observableList.add(new EditBox(listBooks.get(i),
              () -> {
                ConfirmBox confirmBox = new ConfirmBox(
                    "Xác nhận xóa sách khỏi thư viện?",
                    "",
                    () -> {
                      Library.getInstance().deleteBook(Library.getInstance().getBook(listBooks.get(finalI).getId()));
                      listBooks.remove(finalI);
                      mainPane.getChildren().removeLast();
                      setPageBook(Integer.parseInt(pageBookNumberTextField.getText()));
                    },
                    () -> {
                      mainPane.getChildren().removeLast();
                    }
                );
                mainPane.getChildren().add(confirmBox);
              },
              50,
              400,
              mainPane.getParent().getBlendMode()));
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
          int finalI = i;
          observableList.add(new EditBox(listUsers.get(i),
              () -> {
                ConfirmBox confirmBox = new ConfirmBox(
                    "Xác nhận xóa nguười mượn khỏi thư viện?",
                    "",
                    () -> {
                      Library.getInstance().deleteUser(Library.getInstance().getUser(listUsers.get(finalI).getId()));
                      listUsers.remove(finalI);
                      mainPane.getChildren().removeLast();
                      setPageUser(Integer.parseInt(pageUserNumberTextField.getText()));
                    },
                    () -> {
                      mainPane.getChildren().removeLast();
                    }
                );
                mainPane.getChildren().add(confirmBox);
              },
              50,
              400,
              mainPane.getParent().getBlendMode()));
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
      ScrollPane editView = new EditBookView(
          listBooks,
          () -> {
          setPageBook(Integer.parseInt(pageBookNumberTextField.getText()));
          },
          mainPane.getParent().getBlendMode()
      );
      editView.setScaleX(0);
      editView.setScaleY(0);
      Platform.runLater(() -> {
        mainPane.getChildren().add(editView);
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), editView);
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
      EditBox suggestionView = listViewBooks.getSelectionModel().getSelectedItem();
      Book book = Library.getInstance().getBook(suggestionView.getID());
      openEditBookView(book);

    });
    thread.start();

  }
  private void openEditBookView(Book book){
    EditBookView editView = new EditBookView(
        listBooks,
        book,
        () -> {
          setPageBook(Integer.parseInt(pageBookNumberTextField.getText()));
        },
        mainPane.getParent().getBlendMode()
    );
    editView.setScaleX(0);
    editView.setScaleY(0);

    Platform.runLater(() -> {
      mainPane.getChildren().add(editView);
      ScaleTransition transition = new ScaleTransition(Duration.millis(200), editView);
      transition.setToX(1);
      transition.setToY(1);
      transition.play();
    });
  }

  @FXML
  private void AddUser() {
    Thread thread = new Thread(() -> {
      EditUserView editView = new EditUserView(
          Library.getInstance(),
          listUsers,
          () -> {
            setPageUser(Integer.parseInt(pageUserNumberTextField.getText()));
          },
          mainPane.getParent().getBlendMode()
      );
      editView.setScaleX(0);
      editView.setScaleY(0);

      Platform.runLater(() -> {
        mainPane.getChildren().add(editView);
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), editView);
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
      EditBox suggestionView = listViewUsers.getSelectionModel().getSelectedItem();
      User user = Library.getInstance().getUser(suggestionView.getID());

      EditUserView editView = new EditUserView(
          Library.getInstance(),
          listUsers,
          user,
          () -> {
            setPageUser(Integer.parseInt(pageUserNumberTextField.getText()));
          },
          mainPane.getParent().getBlendMode()
      );
      editView.setScaleX(0);
      editView.setScaleY(0);

      Platform.runLater(() -> {
        mainPane.getChildren().add(editView);
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), editView);
        transition.setToX(1);
        transition.setToY(1);
        transition.play();
      });
    });
    thread.start();

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
              300,
              mainPane.getParent().getBlendMode()
          ));
        }
      });

      googleBooksListView.setOnMouseClicked(e->{
        int id=googleBooksListView.getSelectionModel().getSelectedIndex();
        bookSuggestionsTextField.requestFocus();
        openEditBookView(books.get(id));
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
  }

  // Không gọi setUpLanguage ở đây
  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {

  }

  // viLang lưu nội dung tiếng Việt gắn với Object, enLang lưu tiếng Anh
  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }

  // giải phóng các node vừa thêm vào hashMap
  @Override
  public void removeLang(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {
  }
}

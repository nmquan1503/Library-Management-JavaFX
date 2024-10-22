package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.util.ArrayList;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
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
import org.example.demo.CustomUI.ConfirmBox;
import org.example.demo.CustomUI.EditBookView;
import org.example.demo.CustomUI.EditBox;
import org.example.demo.CustomUI.EditUserView;
import org.example.demo.CustomUI.SuggestionView;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Users.Date;
import org.example.demo.Models.Users.User;

public class EditController {

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

  private Library library;

  @FXML
  private void initialize() {
    library = new Library();
    setUpPageNumberTextField(pageBookNumberTextField);
    setUpPageNumberTextField(pageUserNumberTextField);
    initListView();
  }

  private void initListView() {
    Thread threadCreateBooks = new Thread(() -> {
      listBooks = library.getBookSuggestions("");
      Platform.runLater(() -> {
        setPageBook(1);
        pageBookNumberTextField.setText("1");
        prevPageBookButton.setVisible(false);
        nextPageBookButton.setVisible(1 != (listBooks.size() - 1) / 20 + 1);
      });
    });
    threadCreateBooks.start();

    Thread threadCreateUsers = new Thread(() -> {
      listUsers = library.getUserSuggestions("");
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
                      library.deleteBook(library.getBook(listBooks.get(finalI).getId()));
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
              400));
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
                      library.deleteUser(library.getUser(listUsers.get(finalI).getId()));
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
              400));
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
      ScrollPane editView = new EditBookView(library, listBooks, () -> {
        setPageBook(Integer.parseInt(pageBookNumberTextField.getText()));
      });
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
      Book book = library.getBook(suggestionView.getID());
      EditBookView editView = new EditBookView(library, listBooks, book, () -> {
        setPageBook(Integer.parseInt(pageBookNumberTextField.getText()));
      });
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
  private void AddUser() {
    Thread thread = new Thread(() -> {
      EditUserView editView = new EditUserView(library, listUsers, () -> {
        setPageUser(Integer.parseInt(pageUserNumberTextField.getText()));
      });
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
      User user = library.getUser(suggestionView.getID());

      EditUserView editView = new EditUserView(library, listUsers, user, () -> {
        setPageUser(Integer.parseInt(pageUserNumberTextField.getText()));
      });
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


}

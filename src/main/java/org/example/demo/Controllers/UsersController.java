package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.util.Duration;
import org.example.demo.CustomUI.SuggestionView;
import org.example.demo.CustomUI.UserView;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;

public class UsersController implements MainInfo {

  @FXML
  private AnchorPane mainPane;

  @FXML
  private JFXButton deleteButton;
  @FXML
  private TextField nameTextField;
  @FXML
  private JFXListView<SuggestionView> userSuggestionsListView;

  @FXML
  private JFXListView<SuggestionView> usersListView;
  @FXML
  private TextField pageNumberTextField;
  @FXML
  private JFXButton nextPageButton;
  @FXML
  private JFXButton prevPageButton;

  @FXML
  private JFXListView<SuggestionView> BanList;

  private Library library;
  private ArrayList<Suggestion> listUser;

  private Queue<Thread> loadingThread;
  @FXML
  private Pane loadingPane;
  private Transition loadingTransition;

  @FXML
  private void initialize() {
    library = new Library();
    listUser = new ArrayList<>();
    loadingThread = new LinkedList<>();
    SetupFocusTextField();
    setUpPageNumberTextField();
    initView();
  }

  @FXML
  private void changePage() {
    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    if (pageNumber > (listUser.size() - 1) / 20 + 1) {
      pageNumber = (listUser.size() - 1) / 20 + 1;
      pageNumberTextField.setText(String.valueOf(pageNumber));
    } else if (pageNumber < 1) {
      pageNumber = 1;
      pageNumberTextField.setText(String.valueOf(pageNumber));
    }

    nextPageButton.setVisible(pageNumber != (listUser.size() - 1) / 20 + 1);
    prevPageButton.setVisible(pageNumber != 1);

    setListUsers(pageNumber);
  }

  @FXML
  private void switchToNextPage() {
    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    pageNumberTextField.setText(String.valueOf(pageNumber + 1));
    setListUsers(pageNumber + 1);
    nextPageButton.setVisible(pageNumber + 1 != (listUser.size() - 1) / 20 + 1);
    prevPageButton.setVisible(pageNumber + 1 != 1);
  }

  @FXML
  private void switchToPrevPage() {
    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    pageNumberTextField.setText(String.valueOf(pageNumber - 1));
    setListUsers(pageNumber - 1);
    nextPageButton.setVisible(pageNumber - 1 != (listUser.size() - 1) / 20 + 1);
    prevPageButton.setVisible(pageNumber - 1 != 1);
  }

  private void setListUsers(int pageNumber) {
    if (pageNumber > (listUser.size() - 1) / 20 + 1) {
      pageNumber = (listUser.size() - 1) / 20 + 1;
      pageNumberTextField.setText(String.valueOf(pageNumber));
      nextPageButton.setVisible(pageNumber != (listUser.size() - 1) / 20 + 1);
      prevPageButton.setVisible(pageNumber != 1);
    }
    int start = pageNumber * 20 - 20;
    int end = Math.min(start + 19, listUser.size() - 1);
    Thread thread = new Thread(() -> {
      ObservableList<SuggestionView> list = FXCollections.observableArrayList();
      Platform.runLater(() -> {
        usersListView.setItems(list);
        for (int i = start; i <= end; i++) {
          list.add(new SuggestionView(listUser.get(i), 80, 400));
        }
      });
    });
    thread.start();
  }

  private void SetupFocusTextField() {
    nameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        userSuggestionsListView.setVisible(true);
        int heightOfListView = Math.min(userSuggestionsListView.getItems().size(), 5) * 55;
        userSuggestionsListView.setMinHeight(heightOfListView);
        userSuggestionsListView.setMaxHeight(heightOfListView);
      } else {
        SuggestionView suggestionView = userSuggestionsListView.getSelectionModel()
            .getSelectedItem();
        if (suggestionView != null) {
          showUser(suggestionView.getID());
        }
        userSuggestionsListView.setVisible(false);
        userSuggestionsListView.setMinHeight(0);
        userSuggestionsListView.setMaxHeight(0);
      }
    });

    nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
      deleteButton.setVisible(!newValue.isEmpty());
      CreateUserSuggestions();
    });
  }

  private void setUpPageNumberTextField() {
    pageNumberTextField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        if (!newValue.matches("\\d*")) {
          pageNumberTextField.setText(newValue.replaceAll("[^\\d]", ""));
        }
      }
    });
  }


  private void showUser(int idUser) {
    Thread thread = new Thread(() -> {
      UserView userView = new UserView(library.getUser(idUser));
      Platform.runLater(() -> {
        mainPane.getChildren().add(userView);
        userView.setScaleX(0);
        userView.setScaleY(0);
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), userView);
        transition.setToX(1);
        transition.setToY(1);
        transition.play();
      });
    });
    thread.start();
  }

  @FXML
  private void loadUserList() {
    loadingPane.setVisible(true);
    loadingTransition.play();
    String prefixName = nameTextField.getText();
    Thread thread = new Thread(() -> {
      listUser = library.getUserSuggestions(prefixName);
      Platform.runLater(() -> {
        setListUsers(1);
        pageNumberTextField.setText("1");
        prevPageButton.setVisible(false);
        nextPageButton.setVisible(1 != (listUser.size() - 1) / 20 + 1);
        loadingThread.poll();
        if (!loadingThread.isEmpty()) {
          loadingThread.peek().start();
        }
        loadingTransition.stop();
        loadingPane.setVisible(false);
      });
    });
    while (!loadingThread.isEmpty()) {
      if (loadingThread.peek().isAlive()) {
        loadingThread.peek().interrupt();
      }
      loadingThread.poll();
    }

    loadingThread.add(thread);
    thread.start();
  }

  @FXML
  private void DeleteContentOfTextField() {
    nameTextField.clear();
  }

  @FXML
  private void CreateUserSuggestions() {
    String prefixName = nameTextField.getText();
    if (prefixName.isEmpty()) {
      userSuggestionsListView.getItems().clear();
      userSuggestionsListView.setVisible(false);
      userSuggestionsListView.setMaxHeight(0);
      userSuggestionsListView.setMinHeight(0);

      return;
    }

    ObservableList<SuggestionView> observableList = FXCollections.observableArrayList();

    Thread thread = new Thread(() -> {
      ArrayList<Suggestion> listSuggestions = library.getUserSuggestions(prefixName);
      for (Suggestion suggestion : listSuggestions) {
        observableList.add(new SuggestionView(suggestion, 35, 400));
      }
      Platform.runLater(() -> {
        userSuggestionsListView.setItems(observableList);
        userSuggestionsListView.setVisible(true);
        int heightOfListView = Math.min(userSuggestionsListView.getItems().size(), 5) * 55;
        userSuggestionsListView.setMinHeight(heightOfListView);
        userSuggestionsListView.setMaxHeight(heightOfListView);
      });
    });
    thread.start();
  }

  @FXML
  private void SelectUserFromUserListView() {
    SuggestionView suggestionView = usersListView.getSelectionModel().getSelectedItem();
    if (suggestionView != null) {
      showUser(suggestionView.getID());
    }
  }

  private void initBannedUsersList() {
    ObservableList<SuggestionView> observableList = FXCollections.observableArrayList();
    BanList.setItems(observableList);
    Thread thread = new Thread(() -> {
      ArrayList<Suggestion> bannedUsers = library.getBannedUserSuggestions("");
      Platform.runLater(() -> {
        for (Suggestion suggestion : bannedUsers) {
          observableList.add(new SuggestionView(suggestion, 30, 200));
        }
      });
    });
    thread.start();
  }

  @FXML
  private void SelectBannedUser() {
    SuggestionView suggestionView = BanList.getSelectionModel().getSelectedItem();
    if (suggestionView != null) {
      showUser(suggestionView.getID());
    }
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

  }

  private void initView() {
    Thread thread = new Thread(() -> {
      listUser = library.getUserSuggestions("");

      Platform.runLater(() -> {
        setListUsers(1);
        pageNumberTextField.setText("1");
        nextPageButton.setVisible(1 != (listUser.size() - 1) / 20 + 1);
        prevPageButton.setVisible(false);
        initLoadingTransition();
        initBannedUsersList();

      });
    });
    thread.start();
  }

  // set BlendMode của các ImageView là DIFFERENCE nếu isDark = true và SRC_OVER trong th còn lại
  @Override
  public void applyDarkMode(boolean isDark) {

  }

  // Không gọi setUpLanguage ở đây
  @Override
  public void applyTranslate(boolean isTranslate) {

  }

  // viLang lưu nội dung tiếng Việt gắn với Object, enLang lưu tiếng Anh
  public static void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }
}

package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.util.Duration;
import org.example.demo.CustomUI.SuggestionView;
import org.example.demo.CustomUI.UserView;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Users.User;

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
  private Label banListLabel;
  @FXML
  private JFXListView<SuggestionView> BanList;

  private ArrayList<Suggestion> listUser;

  private Queue<Thread> loadingThread;
  @FXML
  private Pane loadingPane;
  private Transition loadingTransition;

  public AnchorPane getMainPane() {
    return mainPane;
  }

  public JFXButton getDeleteButton() {
    return deleteButton;
  }

  public TextField getNameTextField() {
    return nameTextField;
  }

  public JFXListView<SuggestionView> getUserSuggestionsListView() {
    return userSuggestionsListView;
  }

  public JFXListView<SuggestionView> getUsersListView() {
    return usersListView;
  }

  public TextField getPageNumberTextField() {
    return pageNumberTextField;
  }

  public JFXButton getNextPageButton() {
    return nextPageButton;
  }

  public JFXButton getPrevPageButton() {
    return prevPageButton;
  }

  public Label getBanListLabel() {
    return banListLabel;
  }

  public JFXListView<SuggestionView> getBanList() {
    return BanList;
  }

  public ArrayList<Suggestion> getListUser() {
    return listUser;
  }

  public Queue<Thread> getLoadingThread() {
    return loadingThread;
  }

  public Pane getLoadingPane() {
    return loadingPane;
  }

  public Transition getLoadingTransition() {
    return loadingTransition;
  }

  /**
   * init view.
   */
  @FXML
  private void initialize() {
    listUser = new ArrayList<>();
    loadingThread = new LinkedList<>();
    SetupFocusTextField();
    setUpPageNumberTextField();
    initView();
  }

  /**
   * action when press enter key (when press key on number of page text field). if page number is
   * invalid, fix page number. render 20 suggestions of user based on page number.
   */
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

    nextPageButton.setVisible(pageNumber < (listUser.size() - 1) / 20 + 1);
    prevPageButton.setVisible(pageNumber > 1);

    setListUsers(pageNumber);
  }

  /**
   * action when click to next page button. switch to next page. if current page number is max, set
   * visible (false) for this button.
   */
  @FXML
  private void switchToNextPage() {
    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    pageNumberTextField.setText(String.valueOf(pageNumber + 1));
    setListUsers(pageNumber + 1);
    nextPageButton.setVisible(pageNumber + 1 < (listUser.size() - 1) / 20 + 1);
    prevPageButton.setVisible(pageNumber + 1 > 1);
  }

  /**
   * action when click to prev page button. switch to previous page. if current page number is 1,
   * set visible (false) for this button.
   */
  @FXML
  private void switchToPrevPage() {
    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    pageNumberTextField.setText(String.valueOf(pageNumber - 1));
    setListUsers(pageNumber - 1);
    nextPageButton.setVisible(pageNumber - 1 < (listUser.size() - 1) / 20 + 1);
    prevPageButton.setVisible(pageNumber - 1 > 1);
  }

  /**
   * render 20 suggestions of user based on page number.
   */
  public void setListUsers(int pageNumber) {
    if (pageNumber > (listUser.size() - 1) / 20 + 1) {
      pageNumber = (listUser.size() - 1) / 20 + 1;
    }
    if (pageNumber < 1) {
      pageNumber = 1;
      pageNumberTextField.setText("1");
    }
    nextPageButton.setVisible(pageNumber < (listUser.size() - 1) / 20 + 1);
    prevPageButton.setVisible(pageNumber > 1);
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

  /**
   * set up focus property of text field. when text field is not focused, hide all suggestions.
   */
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
      deleteButton.setVisible(!nameTextField.getText().isEmpty());
      CreateUserSuggestions();
    });
  }

  /**
   * set up for text field that only accept number.
   */
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

  /**
   * open user view to show all info of user u clicked. get user by id.
   */
  private void showUser(int idUser) {
    mainPane.requestFocus();
    Thread thread = new Thread(() -> {
      UserView userView = new UserView();
      Platform.runLater(() -> {
        mainPane.getChildren().add(userView);
        userView.setScaleX(0);
        userView.setScaleY(0);
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), userView);
        transition.setToX(1);
        transition.setToY(1);
        transition.play();
        Thread thread1 = new Thread(() -> {
          User user = Library.getInstance().getUser(idUser);
          Platform.runLater(() -> {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(700));
            pauseTransition.setOnFinished(e -> {
              userView.setUser(user);
            });
            pauseTransition.play();
          });
        });
        thread1.start();
      });
    });
    thread.start();
  }

  /**
   * load user list when click search button.
   */
  @FXML
  public void loadUserList() {
    loadingPane.setVisible(true);
    loadingTransition.play();
    String prefixName = nameTextField.getText();
    Thread thread = new Thread(() -> {
      listUser = Library.getInstance().getUserSuggestions(prefixName);
      Platform.runLater(() -> {
        setListUsers(1);
        pageNumberTextField.setText("1");
        prevPageButton.setVisible(false);
        nextPageButton.setVisible(1 < (listUser.size() - 1) / 20 + 1);
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

  /**
   * remove content of text field when click remove button.
   */
  @FXML
  private void DeleteContentOfTextField() {
    nameTextField.clear();
  }

  /**
   * create user suggestion when enter name user.
   */
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
    userSuggestionsListView.setItems(observableList);

    Thread thread = new Thread(() -> {
      ArrayList<Suggestion> listSuggestions = Library.getInstance().getUserSuggestions(prefixName);
      Platform.runLater(() -> {
        for (Suggestion suggestion : listSuggestions) {
          observableList.add(new SuggestionView(suggestion, 35, 400));
        }
        userSuggestionsListView.setVisible(true);
        int heightOfListView = Math.min(userSuggestionsListView.getItems().size(), 5) * 55;
        userSuggestionsListView.setMinHeight(heightOfListView);
        userSuggestionsListView.setMaxHeight(heightOfListView);
      });
    });
    thread.start();
  }

  /**
   * open user view.
   */
  @FXML
  private void SelectUserFromUserListView() {
    SuggestionView suggestionView = usersListView.getSelectionModel().getSelectedItem();
    if (suggestionView != null) {
      showUser(suggestionView.getID());
    }
  }

  /**
   * init list view of banned users.
   */
  private void initBannedUsersList() {
    ObservableList<SuggestionView> observableList = FXCollections.observableArrayList();
    BanList.setItems(observableList);
    Thread thread = new Thread(() -> {
      ArrayList<Suggestion> bannedUsers = Library.getInstance().getBannedUserSuggestions("");
      Platform.runLater(() -> {
        for (Suggestion suggestion : bannedUsers) {
          observableList.add(new SuggestionView(suggestion, 30, 200));
        }
      });
    });
    thread.start();
  }

  /**
   * open user view.
   */
  @FXML
  private void SelectBannedUser() {
    SuggestionView suggestionView = BanList.getSelectionModel().getSelectedItem();
    if (suggestionView != null) {
      showUser(suggestionView.getID());
    }
  }

  /**
   * loading transition when search user.
   */
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

  /**
   * init view.
   */
  private void initView() {
    Thread thread = new Thread(() -> {
      listUser = Library.getInstance().getUserSuggestions("");

      Platform.runLater(() -> {
        setListUsers(1);
        pageNumberTextField.setText("1");
        nextPageButton.setVisible(1 < (listUser.size() - 1) / 20 + 1);
        prevPageButton.setVisible(false);
        initLoadingTransition();
        initBannedUsersList();
        mainPane.setOnMouseClicked(e -> {
          mainPane.requestFocus();
        });
      });
    });
    thread.start();
  }

  /**
   * delete suggestion of user when it deleted.
   */
  public void deleteUserSuggestion(Suggestion suggestion) {
    if (userSuggestionsListView != null) {
      for (int i = 0; i < userSuggestionsListView.getItems().size(); i++) {
        if (userSuggestionsListView.getItems().get(i).getID() == suggestion.getId()) {
          userSuggestionsListView.getItems().remove(i);
          int heightOfListView = Math.min(userSuggestionsListView.getItems().size(), 5) * 55;
          userSuggestionsListView.setMinHeight(heightOfListView);
          userSuggestionsListView.setMaxHeight(heightOfListView);
          if (userSuggestionsListView.getItems().isEmpty()) {
            userSuggestionsListView.setVisible(false);
          }
          break;
        }
      }
    }

    if (BanList != null) {
      for (int i = 0; i < BanList.getItems().size(); i++) {
        if (BanList.getItems().get(i).getID() == suggestion.getId()) {
          BanList.getItems().remove(i);
          break;
        }
      }
    }

    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    for (int i = 0; i < listUser.size(); i++) {
      if (listUser.get(i).getId() == suggestion.getId()) {
        listUser.remove(i);
        if (i + 1 >= pageNumber * 20 - 19 && i + 1 <= pageNumber * 20) {
          usersListView.getItems().remove(i % 20);
        } else if (i + 1 < pageNumber * 20 - 19) {
          usersListView.getItems().removeFirst();
        }
        if (20 * pageNumber < listUser.size()) {
          usersListView.getItems().add(new SuggestionView(listUser.get(pageNumber * 20), 80, 400));
        }
        if (usersListView.getItems().isEmpty()) {
          if (pageNumber - 1 >= 1) {
            setListUsers(pageNumber - 1);
          }
        }
        return;
      }
    }
  }

  /**
   * delete a user suggestion when u delete a book.
   */
  public void fixUserSuggestion(Suggestion suggestion) {
    if (userSuggestionsListView != null) {
      for (int i = 0; i < userSuggestionsListView.getItems().size(); i++) {
        if (userSuggestionsListView.getItems().get(i).getID() == suggestion.getId()) {
          userSuggestionsListView.getItems().set(i, new SuggestionView(suggestion, 35, 400));
          break;
        }
      }
    }
    if (BanList != null) {
      for (int i = 0; i < BanList.getItems().size(); i++) {
        if (BanList.getItems().get(i).getID() == suggestion.getId()) {
          BanList.getItems().remove(i);
          break;
        }
      }
    }
    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    for (int i = 0; i < listUser.size(); i++) {
      if (listUser.get(i).getId() == suggestion.getId()) {
        listUser.set(i, suggestion);
        if (i + 1 >= pageNumber * 20 - 19 && i + 1 <= pageNumber * 20) {
          usersListView.getItems().set(i % 20, new SuggestionView(suggestion, 80, 400));
        }
        return;
      }
    }
  }

  /**
   * add a user suggestion when u add user completely.
   */
  public void addUserSuggestion(Suggestion suggestion) {
    if (userSuggestionsListView != null) {
      if (suggestion.getContent().startsWith(nameTextField.getText()) && !nameTextField.getText()
          .isEmpty()) {
        userSuggestionsListView.getItems().add(new SuggestionView(suggestion, 35, 400));
        int heightOfListView = Math.min(userSuggestionsListView.getItems().size(), 5) * 55;
        userSuggestionsListView.setMinHeight(heightOfListView);
        userSuggestionsListView.setMaxHeight(heightOfListView);
      }
    }
    if (Library.getInstance().getUser(suggestion.getId()).isBan()) {
      BanList.getItems().add(new SuggestionView(suggestion, 30, 200));
    }
    listUser.add(suggestion);
    if (usersListView.getItems().size() < 20) {
      usersListView.getItems().add(new SuggestionView(suggestion, 80, 400));
    }
  }

  /**
   * refresh screen. set page 1 for user list. clear edit user view.
   */
  public void refresh() {

    while (mainPane.getChildren().getLast() instanceof UserView) {
      ((UserView) mainPane.getChildren().getLast()).stopSpeak();
      mainPane.getChildren().removeLast();
    }

    nameTextField.setText("");
    BanList.scrollTo(0);
    loadUserList();
  }

  /**
   * set dark/light mode.
   */
  @Override
  public void applyDarkMode(boolean isDark) {
    for (SuggestionView suggestionView : userSuggestionsListView.getItems()) {
      suggestionView.applyDarkMode(isDark);
    }
    for (SuggestionView suggestionView : usersListView.getItems()) {
      suggestionView.applyDarkMode(isDark);
    }
    for (SuggestionView suggestionView : BanList.getItems()) {
      suggestionView.applyDarkMode(isDark);
    }

    if (mainPane.getChildren().getLast() instanceof UserView) {
      ((UserView) mainPane.getChildren().getLast()).applyDarkMode(isDark);
    }

  }

  /**
   * translate en/vi language for some text.
   */
  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {
    if (isTranslate) {
      nameTextField.setPromptText("Enter name");
      banListLabel.setText("List banned Users");
    } else {
      nameTextField.setPromptText("Nhập tên");
      banListLabel.setText("Danh sách người bị cấm");
    }
    if (mainPane.getChildren().getLast() instanceof UserView) {
      ((UserView) mainPane.getChildren().getLast()).applyTranslate(null, null, isTranslate);
    }
  }

  /**
   * set up en/vi language.
   */
  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {

  }
}

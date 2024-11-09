package org.example.demo.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.util.Duration;
import org.example.demo.API.Network;
import org.example.demo.CustomUI.BookView;
import org.example.demo.CustomUI.ConfirmBox;
import org.example.demo.CustomUI.EditBox;
import org.example.demo.CustomUI.EditBox.TypeBox;
import org.example.demo.CustomUI.NotificationView;
import org.example.demo.CustomUI.SuggestionView;
import org.example.demo.CustomUI.Warning;
import org.example.demo.Database.JDBC;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.BookShelf.Book;
import org.example.demo.Models.Borrowing.Borrowing;
import org.example.demo.Models.Library;
import org.example.demo.Models.Suggestion.Suggestion;
import org.example.demo.Models.Trie.Trie;
import org.example.demo.Models.Users.Date;

public class BooksController implements MainInfo {

  @FXML
  private Label topChoicesLabel;
  @FXML
  private AnchorPane advertisementPane;
  @FXML
  private ImageView content1;
  @FXML
  private ImageView content2;
  @FXML
  private ImageView content3;
  @FXML
  private RadioButton radioButton1;
  @FXML
  private RadioButton radioButton2;
  @FXML
  private RadioButton radioButton3;
  private Queue<Transition> advertisementTransitions;
  private Timeline timeline;

  @FXML
  private JFXComboBox<String> categoryComboBox;
  @FXML
  private TextField categoryTextField;
  @FXML
  private JFXListView<String> categoriesListView;
  @FXML
  private TextField titleTextField;
  @FXML
  private JFXButton removeTextFieldButton;
  @FXML
  private JFXListView<SuggestionView> titleListView;

  @FXML
  private TextField pageNumberTextField;
  @FXML
  private JFXButton nextPageButton;
  @FXML
  private JFXButton prevPageButton;

  @FXML
  private JFXListView<SuggestionView> ListBooks;

  private ArrayList<Suggestion> listSuggestions;
  private Trie categories;
  @FXML
  private AnchorPane mainPane;

  private Queue<Thread> loadingThread;
  @FXML
  private Pane loadingPane;
  private Transition loadingTransition;

  @FXML
  private void initialize() {
    advertisementTransitions = new LinkedList<>();
    loadingThread = new LinkedList<>();
    startTimeLine();
    setUpPageNumberTextField();
    setupFocusTextField();
    initView();
  }

  /**
   * Direction of content's movement. Considering of middle content's movement.
   */
  private enum Direction {
    Left,
    Right;
  }

  private Transition advertisementTransition(Direction direction) {
    ParallelTransition transition = new ParallelTransition(
        transitionOfContent(content1, direction),
        transitionOfContent(content2, direction),
        transitionOfContent(content3, direction)
    );
    transition.setOnFinished(e -> {
      advertisementPane.getChildren().removeFirst();
      advertisementPane.getChildren().removeFirst();
      advertisementPane.getChildren().remove(1);
      if (content1.getLayoutX() == 0) {
        radioButton2.setSelected(true);
        advertisementPane.getChildren().add(1, content2);
        advertisementPane.getChildren().addFirst(content3);
        advertisementPane.getChildren().addFirst(content1);
      } else if (content2.getLayoutX() == 0) {
        radioButton3.setSelected(true);
        advertisementPane.getChildren().add(1, content3);
        advertisementPane.getChildren().addFirst(content1);
        advertisementPane.getChildren().addFirst(content2);
      } else {
        radioButton1.setSelected(true);
        advertisementPane.getChildren().add(1, content1);
        advertisementPane.getChildren().addFirst(content2);
        advertisementPane.getChildren().addFirst(content3);
      }
      if (transition == advertisementTransitions.peek()) {
        advertisementTransitions.poll();
      }
      if (!advertisementTransitions.isEmpty()) {
        Transition nextTransition = advertisementTransitions.poll();
        if (nextTransition != null) {
          nextTransition.play();
        }
      }
    });
    return transition;

  }

  private Transition transitionOfContent(ImageView content, Direction direction) {
    TranslateTransition translateTransition = new TranslateTransition();
    translateTransition.setNode(content);
    translateTransition.setDuration(Duration.millis(200));

    ScaleTransition scaleTransition = new ScaleTransition();
    scaleTransition.setNode(content);
    scaleTransition.setDuration(Duration.millis(200));
    if (advertisementPane.getChildren().getFirst() == content) {
      if (direction == Direction.Left) {
        translateTransition.setByX(120);
        translateTransition.setOnFinished(e -> {
          content.setLayoutX(120);
          content.setTranslateX(0);
        });
        return translateTransition;
      } else {
        translateTransition.setByX(60);
        translateTransition.setOnFinished(e -> {
          content.setLayoutX(40);
          content.setLayoutY(0);
          content.setTranslateX(0);
        });
        scaleTransition.setToX(1.25);
        scaleTransition.setToY(1.25);
        scaleTransition.setOnFinished(e -> {
          content.setFitHeight(300);
          content.setFitWidth(200);
          content.setScaleX(1);
          content.setScaleY(1);
        });
      }
    } else if (advertisementPane.getChildren().get(1) == content) {
      if (direction == Direction.Left) {
        translateTransition.setByX(-60);
        translateTransition.setOnFinished(e -> {
          content.setLayoutX(40);
          content.setLayoutY(0);
          content.setTranslateX(0);
        });
        scaleTransition.setToX(1.25);
        scaleTransition.setToY(1.25);
        scaleTransition.setOnFinished(e -> {
          content.setFitWidth(200);
          content.setFitHeight(300);
          content.setScaleY(1);
          content.setScaleX(1);
        });
      } else {
        translateTransition.setByX(-120);
        translateTransition.setOnFinished(e -> {
          content.setLayoutX(0);
          content.setTranslateX(0);
        });
        return translateTransition;
      }
    } else {
      if (direction == Direction.Left) {
        translateTransition.setByX(-60);
        translateTransition.setOnFinished(e -> {
          content.setLayoutX(0);
          content.setLayoutY(30);
          content.setTranslateX(0);
        });
        scaleTransition.setToX(0.8);
        scaleTransition.setToY(0.8);
        scaleTransition.setOnFinished(e -> {
          content.setFitHeight(240);
          content.setFitWidth(160);
          content.setScaleX(1);
          content.setScaleY(1);
        });
      } else {
        translateTransition.setByX(60);
        translateTransition.setOnFinished(e -> {
          content.setLayoutX(120);
          content.setLayoutY(30);
          content.setTranslateX(0);
        });
        scaleTransition.setToX(0.8);
        scaleTransition.setToY(0.8);
        scaleTransition.setOnFinished(e -> {
          content.setFitHeight(240);
          content.setFitWidth(160);
          content.setScaleX(1);
          content.setScaleY(1);
        });
      }
    }
    return new ParallelTransition(translateTransition, scaleTransition);
  }

  private Transition undoTransitionOfContent(ImageView content) {
    TranslateTransition translateTransition = new TranslateTransition();
    translateTransition.setNode(content);
    translateTransition.setDuration(Duration.millis(100));
    translateTransition.setToX(0);
    translateTransition.setToY(0);

    ScaleTransition scaleTransition = new ScaleTransition();
    scaleTransition.setNode(content);
    scaleTransition.setDuration(Duration.millis(100));
    scaleTransition.setToX(1);
    scaleTransition.setToY(1);

    return new ParallelTransition(translateTransition, scaleTransition);
  }

  private Transition undoTransition() {
    ParallelTransition parallelTransition = new ParallelTransition(
        undoTransitionOfContent(content1),
        undoTransitionOfContent(content2),
        undoTransitionOfContent(content3)
    );
    parallelTransition.setOnFinished(e -> {
      if (parallelTransition == advertisementTransitions.peek()) {
        advertisementTransitions.poll();
      }
      if (!advertisementTransitions.isEmpty()) {
        Transition nextTransition = advertisementTransitions.peek();
        if (nextTransition != null) {
          nextTransition.play();
        }
      }
    });
    return parallelTransition;
  }

  private void startTimeLine() {
    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(2));
    pauseTransition.setOnFinished(e -> {
      if (pauseTransition == advertisementTransitions.peek()) {
        advertisementTransitions.poll();
      }
      if (!advertisementTransitions.isEmpty()) {
        Transition nextTransition = advertisementTransitions.poll();
        if (nextTransition != null) {
          nextTransition.play();
        }
      }
    });
    advertisementTransitions.offer(pauseTransition);
    if (advertisementTransitions.size() == 1) {
      Transition nextTransition = advertisementTransitions.poll();
      nextTransition.play();
    }

    if (timeline == null) {
      timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
        advertisementTransitions.offer(advertisementTransition(Direction.Left));
        if (advertisementTransitions.size() == 1) {
          Transition nextTransition = advertisementTransitions.poll();
          nextTransition.play();
        }
      }));
      timeline.setCycleCount(Timeline.INDEFINITE);
    }
    timeline.play();
  }

  private void stopTimeLine() {
    Transition currentTransition = advertisementTransitions.peek();
    if (currentTransition != null) {
      if (currentTransition.getStatus() == Status.RUNNING) {
        currentTransition.stop();
      }
    }
    advertisementTransitions.clear();
    if (timeline != null) {
      timeline.stop();
    }
    advertisementTransitions.offer(undoTransition());
    Transition nextTransition = advertisementTransitions.peek();
    if (nextTransition != null) {
      nextTransition.play();
    }
  }

  @FXML
  private void switchToContent1() {
    stopTimeLine();
    if (radioButton1.isSelected()) {
      startTimeLine();
      return;
    }
    if (radioButton2.isSelected()) {
      advertisementTransitions.offer(advertisementTransition(Direction.Right));
    } else {
      advertisementTransitions.offer(advertisementTransition(Direction.Left));
    }
    if (advertisementTransitions.size() == 1) {
      Transition nextTransition = advertisementTransitions.peek();
      if (nextTransition != null) {
        nextTransition.play();
      }
    }
    startTimeLine();
  }

  @FXML
  private void switchToContent2() {
    stopTimeLine();
    if (radioButton2.isSelected()) {
      startTimeLine();
      return;
    }
    if (radioButton1.isSelected()) {
      advertisementTransitions.offer(advertisementTransition(Direction.Left));
    } else {
      advertisementTransitions.offer(advertisementTransition(Direction.Right));
    }
    if (advertisementTransitions.size() == 1) {
      Transition nextTransition = advertisementTransitions.peek();
      if (nextTransition != null) {
        nextTransition.play();
      }
    }
    startTimeLine();
  }

  @FXML
  private void switchToContent3() {
    stopTimeLine();
    if (radioButton3.isSelected()) {
      startTimeLine();
      return;
    }
    if (radioButton1.isSelected()) {
      advertisementTransitions.offer(advertisementTransition(Direction.Right));
    } else {
      advertisementTransitions.offer(advertisementTransition(Direction.Left));
    }
    if (advertisementTransitions.size() == 1) {
      Transition nextTransition = advertisementTransitions.peek();
      if (nextTransition != null) {
        nextTransition.play();
      }
    }
    startTimeLine();
  }

  @FXML
  private void changePage() {
    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    if (pageNumber > (listSuggestions.size() - 1) / 20 + 1) {
      pageNumber = (listSuggestions.size() - 1) / 20 + 1;
      pageNumberTextField.setText(String.valueOf(pageNumber));
    } else if (pageNumber < 1) {
      pageNumber = 1;
      pageNumberTextField.setText(String.valueOf(pageNumber));
    }

    nextPageButton.setVisible(pageNumber != (listSuggestions.size() - 1) / 20 + 1);
    prevPageButton.setVisible(pageNumber != 1);

    setListBooks(pageNumber);
  }

  @FXML
  private void switchToNextPage() {
    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    pageNumberTextField.setText(String.valueOf(pageNumber + 1));
    setListBooks(pageNumber + 1);
    nextPageButton.setVisible(pageNumber + 1 != (listSuggestions.size() - 1) / 20 + 1);
    prevPageButton.setVisible(pageNumber + 1 != 1);
  }

  @FXML
  private void switchToPrevPage() {
    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    pageNumberTextField.setText(String.valueOf(pageNumber - 1));
    setListBooks(pageNumber - 1);
    nextPageButton.setVisible(pageNumber - 1 != (listSuggestions.size() - 1) / 20 + 1);
    prevPageButton.setVisible(pageNumber - 1 != 1);
  }

  private void setListBooks(int pageNumber) {
    if (pageNumber > (listSuggestions.size() - 1) / 20 + 1) {
      pageNumber = (listSuggestions.size() - 1) / 20 + 1;
      pageNumberTextField.setText(String.valueOf(pageNumber));
      nextPageButton.setVisible(pageNumber != (listSuggestions.size() - 1) / 20 + 1);
      prevPageButton.setVisible(pageNumber != 1);
    }
    int start = pageNumber * 20 - 20;
    int end = Math.min(start + 19, listSuggestions.size() - 1);
    Thread thread = new Thread(() -> {
      ObservableList<SuggestionView> list = FXCollections.observableArrayList();
      Platform.runLater(() -> {
        ListBooks.setItems(list);
        for (int i = start; i <= end; i++) {
          list.add(new SuggestionView(listSuggestions.get(i), 80, 400));
        }
      });
    });
    thread.start();
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


  @FXML
  private void SelectCategoryFromComboBox() {
    String category = categoryComboBox.getValue();
    categoryTextField.setText(category);
  }

  @FXML
  private void CreateCategorySuggestions() {
    String prefix = categoryTextField.getText();
    if (prefix.isEmpty()) {
      categoriesListView.getItems().clear();
      categoriesListView.setVisible(false);
      categoriesListView.setMinHeight(0);
      categoriesListView.setMaxHeight(0);
      return;
    }
    Thread thread = new Thread(() -> {
      ArrayList<String> namesCategory = categories.getAllNameStartWith(prefix);
      ObservableList<String> list = FXCollections.observableArrayList();
      Platform.runLater(() -> {
        categoriesListView.setItems(list);
        list.addAll(namesCategory);
        categoriesListView.setVisible(true);
        int height = Math.min(categoriesListView.getItems().size(), 5) * 34;
        categoriesListView.setMinHeight(height);
        categoriesListView.setMaxHeight(height);
      });
    });
    thread.start();
  }

  private void setupFocusTextField() {
    categoryTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue && !categoryTextField.getText().isEmpty()) {
        categoriesListView.setVisible(true);
        int height = Math.min(categoriesListView.getItems().size(), 5) * 35;
        categoriesListView.setMinHeight(height);
        categoriesListView.setMaxHeight(height);
      } else {
        String category = categoriesListView.getSelectionModel().getSelectedItem();
        if (category != null) {
          categoryTextField.setText(category);
        }
        categoriesListView.setMinHeight(0);
        categoriesListView.setMaxHeight(0);
        categoriesListView.setVisible(false);
      }
    });

    titleTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue && !titleTextField.getText().isEmpty()) {
        titleListView.setVisible(true);
        int heightOfListView = Math.min(titleListView.getItems().size(), 5) * 40;
        titleListView.setMinHeight(heightOfListView);
        titleListView.setMaxHeight(heightOfListView);
      } else {
        SuggestionView suggestionView = titleListView.getSelectionModel().getSelectedItem();
        if (suggestionView != null) {
          showBook(suggestionView.getID());
        }
        titleListView.setVisible(false);
        titleListView.setMinHeight(0);
        titleListView.setMaxHeight(0);
      }
    });
  }

  private void showBook(int idBook) {
    Thread thread = new Thread(() -> {
      BookView bookView = new BookView();
      Platform.runLater(() -> {
        mainPane.requestFocus();
        mainPane.getChildren().add(bookView);
        bookView.setScaleX(0);
        bookView.setScaleY(0);
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), bookView);
        transition.setToX(1);
        transition.setToY(1);
        transition.play();
        Thread thread1 = new Thread(() -> {
          Book book = Library.getInstance().getBook(idBook);
          Platform.runLater(() -> {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(700));
            pauseTransition.setOnFinished(e -> {
              bookView.setBook(book);
            });
            pauseTransition.play();
          });
        });
        thread1.start();
      });
    });
    thread.start();
  }

  private void showBook(Book book){
    Thread thread = new Thread(() -> {
      BookView bookView = new BookView();
      Platform.runLater(() -> {
        mainPane.requestFocus();
        mainPane.getChildren().add(bookView);
        bookView.setScaleX(0);
        bookView.setScaleY(0);
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), bookView);
        transition.setToX(1);
        transition.setToY(1);
        transition.play();
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(700));
            pauseTransition.setOnFinished(e -> {
              bookView.setBook(book);
            });
            pauseTransition.play();

      });
    });
    thread.start();
  }

  private void CreateBookSuggestions() {
    String prefix = titleTextField.getText();
    if (prefix.isEmpty()) {
      titleListView.getItems().clear();
      titleListView.setVisible(false);
      titleListView.setMinHeight(0);
      titleListView.setMaxHeight(0);
      return;
    }
    ObservableList<SuggestionView> observableList = FXCollections.observableArrayList();
    titleListView.setItems(observableList);
    Thread thread = new Thread(() -> {
      ArrayList<Suggestion> listSuggestions = Library.getInstance().getBookSuggestions(prefix);
      Platform.runLater(() -> {
        for (int i = 0; i < Math.min(10, listSuggestions.size()); i++) {
          observableList.add(new SuggestionView(listSuggestions.get(i), 35, 230));
        }
        titleListView.setVisible(true);
        int heightOfListView = Math.min(titleListView.getItems().size(), 5) * 40;
        titleListView.setMinHeight(heightOfListView);
        titleListView.setMaxHeight(heightOfListView);
      });
    });
    thread.start();
  }

  @FXML
  private void SelectBookFromListBook() {
    SuggestionView suggestionView = ListBooks.getSelectionModel().getSelectedItem();
    if (suggestionView != null) {
      showBook(suggestionView.getID());
    }
  }

  private void initCategories() {
    categories = new Trie();
    Connection connection = JDBC.getConnection();
    try {
      String query = "select * from categories";
      PreparedStatement preparedStatement = connection.prepareStatement(query);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        int id = resultSet.getInt("id_category");
        String name = resultSet.getString("name_category");
        categories.insertNode(name, id);
        categoryComboBox.getItems().add(name);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    JDBC.closeConnection(connection);
  }

  private void initTopChoicesBook(){
    content1.setImage(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));
    content2.setImage(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));
    content3.setImage(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/org/example/demo/Assets/basic.jpg"))));

    ArrayList<Book> list=Library.getInstance().getTop3Book();
    if(!list.isEmpty()){
      if(list.getFirst().getImageLink() != null && Network.isConnected() ){
        content1.setImage(new Image(list.getFirst().getImageLink()));
      }
      content1.setOnMouseClicked(e->{
        showBook(list.getFirst());
      });
    }
    if(list.size()>1){
      if(list.getFirst().getImageLink() != null && Network.isConnected() ){
        content2.setImage(new Image(list.get(1).getImageLink()));
      }
      content2.setOnMouseClicked(e->{
        showBook(list.get(1));
      });
    }
    if(list.size()>2){
      if(list.getFirst().getImageLink() != null && Network.isConnected() ){
        content3.setImage(new Image(list.get(2).getImageLink()));
      }
      content3.setOnMouseClicked(e->{
        showBook(list.get(2));
      });
    }
  }

  private void initView() {
    Thread thread = new Thread(() -> {
      listSuggestions = Library.getInstance().getBookSuggestions("");
      Platform.runLater(() -> {
        setListBooks(1);
        pageNumberTextField.setText("1");
        nextPageButton.setVisible(1 != (listSuggestions.size() - 1) / 20 + 1);
        prevPageButton.setVisible(false);

        initTopChoicesBook();

        titleTextField.textProperty().addListener(new ChangeListener<String>() {
          @Override
          public void changed(ObservableValue<? extends String> observableValue, String oldValue,
              String newValue) {
            removeTextFieldButton.setVisible(!newValue.isEmpty());
            CreateBookSuggestions();
          }
        });
        mainPane.setOnMouseClicked(e->{mainPane.requestFocus();});
      });
    });
    thread.start();
    Thread thread1 = new Thread(() -> {
      Platform.runLater(() -> {
        initCategories();
        initLoadingTransition();
      });
    });
    thread1.start();
  }

  @FXML
  private void Search() {
    loadingPane.setVisible(true);
    loadingTransition.play();
    String category = categoryTextField.getText();
    Thread thread1 = new Thread(() -> {
      if (!category.isEmpty()) {
        for (int i = 21; i < listSuggestions.size(); i++) {
          Book book = Library.getInstance().getBook(listSuggestions.get(i).getId());
          if (book.getCategories() == null) {
            listSuggestions.remove(i);
            i--;
          } else if (!book.getCategories().contains(categoryTextField.getText())) {
            listSuggestions.remove(i);
            i--;
          }
        }
      }
      loadingThread.poll();
      if (!loadingThread.isEmpty()) {
        loadingThread.peek().start();
      }
    });
    Thread thread = new Thread(() -> {
      listSuggestions = Library.getInstance().getBookSuggestions(titleTextField.getText());
      if (!category.isEmpty()) {
        for (int i = 0; i < Math.min(21, listSuggestions.size()); i++) {
          Book book = Library.getInstance().getBook(listSuggestions.get(i).getId());
          if (book.getCategories() == null) {
            listSuggestions.remove(i);
            i--;
          } else if (!book.getCategories().contains(categoryTextField.getText())) {
            listSuggestions.remove(i);
            i--;
          }
        }
      }
      Platform.runLater(() -> {
        setListBooks(1);
        pageNumberTextField.setText("1");
        prevPageButton.setVisible(false);
        nextPageButton.setVisible(1 != (listSuggestions.size() - 1) / 20 + 1);

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
    loadingThread.add(thread1);
    thread.start();
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

  @FXML
  private void RemoveTitleTextField() {
    titleTextField.clear();
  }

  public void deleteBookSuggestion(Suggestion suggestion){

    if(titleListView!=null){
      for(int i=0;i<titleListView.getItems().size();i++){
        if(titleListView.getItems().get(i).getID()==suggestion.getId()){
          titleListView.getItems().remove(i);
          int heightOfListView = Math.min(titleListView.getItems().size(), 5) * 40;
          titleListView.setMinHeight(heightOfListView);
          titleListView.setMaxHeight(heightOfListView);
          if(titleListView.getItems().isEmpty())titleListView.setVisible(false);
          break;
        }
      }
    }

    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    for(int i=0;i<listSuggestions.size();i++){
      if(listSuggestions.get(i).getId()==suggestion.getId()){
        listSuggestions.remove(i);
        if(i+1>=pageNumber*20-19 && i+1<=pageNumber*20){
          ListBooks.getItems().remove(i%20);
        }
        else if(i+1<pageNumber*20-19){
          ListBooks.getItems().removeFirst();
        }
        if(20*pageNumber<listSuggestions.size()){
          ListBooks.getItems().add(new SuggestionView(listSuggestions.get(pageNumber*20),80,400));
        }
        if(ListBooks.getItems().isEmpty()){
          if(pageNumber-1>=1){
            setListBooks(pageNumber-1);
          }
        }
        return;
      }
    }
  }

  public void fixBookSuggestion(Suggestion suggestion){
    if(titleListView!=null){
      for(int i=0;i<titleListView.getItems().size();i++){
        if(titleListView.getItems().get(i).getID()==suggestion.getId()){
          titleListView.getItems().set(i,new SuggestionView(suggestion,35,230));
          break;
        }
      }
    }
    int pageNumber = Integer.parseInt(pageNumberTextField.getText());
    for(int i=0;i<listSuggestions.size();i++){
      if(listSuggestions.get(i).getId()==suggestion.getId()){
        listSuggestions.set(i,suggestion);
        if(i+1>=pageNumber*20-19 && i+1<=pageNumber*20){
          ListBooks.getItems().set(i%20,new SuggestionView(suggestion,80,400));
        }
        return;
      }
    }
  }

  public void addBookSuggestion(Suggestion suggestion){
    if(titleListView!=null){
      if(suggestion.getContent().startsWith(titleTextField.getText()) && !titleTextField.getText().isEmpty()){
        titleListView.getItems().add(new SuggestionView(suggestion,35,230));
        int heightOfListView = Math.min(titleListView.getItems().size(), 5) * 40;
        titleListView.setMinHeight(heightOfListView);
        titleListView.setMaxHeight(heightOfListView);
      }
    }
    listSuggestions.add(suggestion);
    if(ListBooks.getItems().size()<20){
      ListBooks.getItems().add(new SuggestionView(suggestion,80,400));
    }
  }

  // set BlendMode của các ImageView là DIFFERENCE nếu isDark = true và SRC_OVER trong th còn lại
  @Override
  public void applyDarkMode(boolean isDark) {

    if (isDark) {
      advertisementPane.setBlendMode(BlendMode.DIFFERENCE);
    } else {
      advertisementPane.setBlendMode(BlendMode.SRC_OVER);
    }

    for (SuggestionView suggestionView : ListBooks.getItems()) {
      suggestionView.applyDarkMode(isDark);
    }

    for (SuggestionView suggestionView : titleListView.getItems()) {
      suggestionView.applyDarkMode(isDark);
    }

    int id = mainPane.getChildren().size() - 1;

    if (mainPane.getChildren().get(id) instanceof BookView) {
      ((BookView) mainPane.getChildren().get(id)).applyDarkMode(isDark);
    } else if (mainPane.getChildren().get(id - 1) instanceof BookView) {
      ((BookView) mainPane.getChildren().get(id - 1)).applyDarkMode(isDark);
    }

  }

  // Không gọi setUpLanguage ở đây
  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {
    if (isTranslate) {
      categoryTextField.setPromptText("Category");
      titleTextField.setPromptText("Title");
      topChoicesLabel.setText("Top choices");
    } else {
      categoryTextField.setPromptText("Thể loại");
      titleTextField.setPromptText("Tiêu đề");
      topChoicesLabel.setText("Lựa chọn hàng đầu");
    }

    int id = mainPane.getChildren().size() - 1;

    if (mainPane.getChildren().get(id) instanceof ConfirmBox) {
      ((ConfirmBox) mainPane.getChildren().getLast()).applyTranslate(null, null, isTranslate);
      id--;
    }
    if (mainPane.getChildren().get(id) instanceof BookView) {
      ((BookView) mainPane.getChildren().get(id)).applyTranslate(null, null, isTranslate);
    }
  }

  // viLang lưu nội dung tiếng Việt gắn với Object, enLang lưu tiếng Anh
  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {
  }
}

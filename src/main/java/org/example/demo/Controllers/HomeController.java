package org.example.demo.Controllers;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.example.demo.API.Translate;
import org.example.demo.Database.JDBC;
import org.example.demo.Interfaces.MainInfo;
import org.example.demo.Models.Language;
import org.example.demo.circularProgressUI.RingProgressIndicator;
import javafx.scene.text.Font;

public class HomeController implements MainInfo {

  @FXML
  private Label librarianName;

  @FXML
  private Label dayTime;

  @FXML
  private Label numBooks;

  @FXML
  private Label numUsers;

  @FXML
  private Label overDue;

  @FXML
  private Label numBan;

  @FXML
  private JFXTreeTableView<LibrarianTable> librarianView;

  @FXML
  private StackPane circleProgress;

  @FXML
  private ScatterChart<String, Number> scatter;

  @FXML
  private AreaChart<String, Number> areaChart;

  @FXML
  private AnchorPane homePane;

  @FXML
  private Label colon;

  @FXML
  private Label firstH;

  @FXML
  private Label firstM;

  @FXML
  private Label secH;

  @FXML
  private Label secM;

  @FXML
  private Label helloTxt;

  @FXML
  private Label numBookTxt;

  @FXML
  private Label numStuTxt;

  @FXML
  private Label overDueTxt;

  @FXML
  private Label banStuTxt;

  @FXML
  private Label tableLibTxt;

  @FXML
  private Label borrowRateTxt;

  @FXML
  private Button allBtn;

  private JFXTreeTableView<LibrarianTable> tempTable;

  private ScatterChart<String, Number> fullyScatterChart;

  private AreaChart<?, ?> fullyAreaChart;

  private Timeline shadowTimeline;

  private Timeline progressAnimation;

  private Timeline timeTimeline;

  private Timeline colonFlashTimeline;

  private Timeline animationTimeline;

  private Timeline shadowLabelTimeline;

  public void initialize() {
    displayTime();
    displayMiniPaneTotal();
    displayHomeTable();
    displayCirclePro();
    displayScatter();
    displayArea();
  }

  public void refresh() {
    clearTimeline();
    displayTime();
    displayMiniPaneTotal();
    displayHomeTable();
    displayCirclePro();
    displayScatter();
    displayArea();
  }

  public void displayTime() {
    Label[] digitLabels = {firstH, secH, colon, firstM, secM};
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter timeFormatter2 = DateTimeFormatter.ofPattern("dd-MM, yyyy");
    String currentTime = LocalDateTime.now().format(timeFormatter);
    dayTime.setText(LocalDateTime.now().format(timeFormatter2));

    for (int i = 0; i < digitLabels.length; i++) {
      digitLabels[i].setFont(
          Font.loadFont(getClass().getResourceAsStream("/org/example/demo/Font/E1234.ttf"), 80));
      digitLabels[i].setTextFill(Color.web("#FF6EC7"));
      digitLabels[i].setText(
          String.valueOf(currentTime.charAt(i)));
      shadowEffect(digitLabels[i]);
    }

    AnchorPane.setTopAnchor(digitLabels[0], 30.0);
    AnchorPane.setTopAnchor(digitLabels[1], 30.0);
    AnchorPane.setTopAnchor(digitLabels[2], 30.0);
    AnchorPane.setTopAnchor(digitLabels[3], 30.0);
    double distanceLeft = 40.0;
    AnchorPane.setLeftAnchor(digitLabels[0], distanceLeft);
    distanceLeft += 60;
    AnchorPane.setLeftAnchor(digitLabels[1], distanceLeft);
    distanceLeft += 70;
    AnchorPane.setLeftAnchor(digitLabels[2], distanceLeft);
    distanceLeft += 59;
    AnchorPane.setLeftAnchor(digitLabels[3], distanceLeft);
    AnchorPane.setTopAnchor(digitLabels[4], 30.0);
    distanceLeft += 70;
    AnchorPane.setLeftAnchor(digitLabels[4], distanceLeft);

    if (timeTimeline == null) {
      timeTimeline = new Timeline(
          new KeyFrame(Duration.seconds(1), event -> updateTime(digitLabels)));
      timeTimeline.setCycleCount(Timeline.INDEFINITE);
      timeTimeline.play();
    }

    if (colonFlashTimeline == null) {
      colonFlashTimeline = new Timeline(
          new KeyFrame(Duration.seconds(1), event -> {
            colon.setVisible(!colon.isVisible());
          })
      );
      colonFlashTimeline.setCycleCount(Animation.INDEFINITE);
      colonFlashTimeline.play();
    }
  }

  public void updateTime(Label[] digitLabels) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    String currentTime = now.format(formatter);

    for (int i = 0; i < digitLabels.length; i++) {
      if (digitLabels[i].getText().charAt(0) != currentTime.charAt(i)) {
        digitLabels[i].setText(String.valueOf(currentTime.charAt(i)));
      }
    }
  }

  public void displayMiniPaneTotal() {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      conn = JDBC.getConnection();

      // display total books
      String sql = "SELECT SUM(quantity) AS total FROM books";
      pstmt = conn.prepareStatement(sql);

      rs = pstmt.executeQuery();

      if (rs.next()) {
        int count = rs.getInt("total");
        this.numBooks.setText(String.valueOf(count));
      } else {
        this.numBooks.setText(String.valueOf(0));
      }

      // display total users
      String sql2 = "SELECT COUNT(*) AS total FROM user";
      pstmt = conn.prepareStatement(sql2);

      rs = pstmt.executeQuery();

      if (rs.next()) {
        int count = rs.getInt("total");
        this.numUsers.setText(String.valueOf(count));
      } else {
        this.numUsers.setText(String.valueOf(0));
      }

      // display total overdue books
      String sql3 = "SELECT COUNT(*) AS total FROM borrowing WHERE (due_date < NOW() AND returned_date IS NULL)";
      pstmt = conn.prepareStatement(sql3);

      rs = pstmt.executeQuery();

      if (rs.next()) {
        int count = rs.getInt("total");
        this.overDue.setText(String.valueOf(count));
      } else {
        this.overDue.setText(String.valueOf(0));
      }

      // display total banned users, ban_date is the date that user is unbanned
      String sql4 = "SELECT COUNT(*) AS total FROM user WHERE ban_date > NOW()";
      pstmt = conn.prepareStatement(sql4);

      rs = pstmt.executeQuery();

      if (rs.next()) {
        int count = rs.getInt("total");
        this.numBan.setText(String.valueOf(count));
      } else {
        this.numBan.setText(String.valueOf(0));
      }

      JDBC.closeConnection(conn);
    } catch (Exception se) {
      se.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (pstmt != null) {
          pstmt.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }

  public void displayHomeTable() {

    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    try {
      conn = JDBC.getConnection();

      String sql = "SELECT id_librarian, name_librarian, email_librarian FROM librarian LIMIT 3";
      assert conn != null;
      preparedStatement = conn.prepareStatement(sql);

      rs = preparedStatement.executeQuery();

      // display librarian table
      librarianView.setPrefWidth(415);

      JFXTreeTableColumn<LibrarianTable, Number> librarianIdColumn = new JFXTreeTableColumn<>("ID");
      librarianIdColumn.setCellValueFactory(param -> param.getValue().getValue().idProperty());
      librarianIdColumn.setPrefWidth(50);
      librarianIdColumn.setMinWidth(50);
      librarianIdColumn.setStyle("-fx-alignment: CENTER;");

      JFXTreeTableColumn<LibrarianTable, String> librarianNameColumn = new JFXTreeTableColumn<>(
          "Tên");
      if (BaseController.isTranslate) {
        librarianNameColumn.setText("Name");
      }
      librarianNameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
      librarianNameColumn.setPrefWidth(160);
      librarianNameColumn.setMinWidth(160);
      librarianNameColumn.setStyle("-fx-alignment: CENTER;");

      JFXTreeTableColumn<LibrarianTable, String> librarianEmailColumn = new JFXTreeTableColumn<>(
          "Email");
      librarianEmailColumn.setCellValueFactory(
          param -> param.getValue().getValue().emailProperty());
      librarianEmailColumn.setPrefWidth(180);
      librarianEmailColumn.setMinWidth(180);
      librarianEmailColumn.setStyle("-fx-alignment: CENTER;");

      ObservableList<LibrarianTable> librarian = FXCollections.observableArrayList();
      while (rs.next()) {
        int id = rs.getInt("id_librarian");
        String name = rs.getString("name_librarian");
        String email = rs.getString("email_librarian");

        librarian.add(new LibrarianTable(id, name, email));
      }

      final TreeItem<LibrarianTable> root = new RecursiveTreeItem<>(librarian,
          RecursiveTreeObject::getChildren);
      librarianView.setRoot(null);

      librarianView.getColumns()
          .setAll(librarianIdColumn, librarianNameColumn, librarianEmailColumn);
      librarianView.setRoot(root);
      librarianView.setShowRoot(false);

      JDBC.closeConnection(conn);
    } catch (Exception se) {
      se.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }

  }

  @Override
  public void applyDarkMode(boolean isDark) {
    // no image here
  }

  static class LibrarianTable extends RecursiveTreeObject<LibrarianTable> {

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty email;

    public LibrarianTable(int id, String name, String email) {
      this.id = new SimpleIntegerProperty(id);
      this.name = new SimpleStringProperty(name);
      this.email = new SimpleStringProperty(email);
    }

    public IntegerProperty idProperty() {
      return id;
    }

    public StringProperty nameProperty() {
      return name;
    }

    public StringProperty emailProperty() {
      return email;
    }
  }

  public void setLibName(String name) {
    librarianName.setText(name);
  }

  // Display borrowed books / total books
  private void displayCirclePro() {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;

    try {
      // Establish connection
      conn = JDBC.getConnection();

      // Query to get total number of books
      String totalBooksSql = "SELECT SUM(quantity) AS total FROM books";
      assert conn != null;
      preparedStatement = conn.prepareStatement(totalBooksSql);
      rs = preparedStatement.executeQuery();
      int totalBooks = 0;
      if (rs.next()) {
        totalBooks = rs.getInt("total");
      }

      // Query to get the number of borrowed books
      String borrowedBooksSql = "SELECT COUNT(*) AS total FROM borrowing WHERE (returned_date IS NULL) OR (returned_date > NOW())";
      preparedStatement = conn.prepareStatement(borrowedBooksSql);
      rs = preparedStatement.executeQuery();
      int borrowedBooks = 0;
      if (rs.next()) {
        borrowedBooks = rs.getInt("total");
      }

      double progress = (totalBooks > 0) ? (double) borrowedBooks / totalBooks : 0.0;

      // Create the RingProgressIndicator
      RingProgressIndicator ringProgressIndicator = new RingProgressIndicator();
      ringProgressIndicator.setMinSize(163, 163);
      ringProgressIndicator.setPrefSize(163, 163);
      ringProgressIndicator.setRingWidth(20);
      ringProgressIndicator.setProgress(0);

      circleProgress.getChildren().clear();

      circleProgress.getChildren().add(ringProgressIndicator);

      Circle animatedCircle = new Circle(7);
      animatedCircle.setFill(Color.web("#FF10F0"));

      animatedCircle.setTranslateX(0);
      animatedCircle.setTranslateY(-80);

      animationTimeline = new Timeline();
      for (int i = 0; i < 360; i++) {
        final int angle = i;
        animationTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(i / 60.0), e -> {
          double radians = Math.toRadians(angle);
          animatedCircle.setTranslateX(80 * Math.sin(radians));
          animatedCircle.setTranslateY(-80 * Math.cos(radians));
        }));
      }

      animationTimeline.setCycleCount(Animation.INDEFINITE);
      animationTimeline.play();

      circleProgress.getChildren().add(animatedCircle);

      // Create a circle for the growing and fading effect
      Circle effectCircle = new Circle(35);
      effectCircle.setFill(Color.web("#ff80ff", 0.5));
      effectCircle.setTranslateX(0);
      effectCircle.setTranslateY(0);
      circleProgress.getChildren().add(effectCircle);

      // Create the ScaleTransition
      ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.5), effectCircle);
      scaleTransition.setFromX(0);
      scaleTransition.setFromY(0);
      scaleTransition.setToX(3.3);
      scaleTransition.setToY(3.3);
      scaleTransition.setCycleCount(Animation.INDEFINITE);
//      scaleTransition.setAutoReverse(true);

      FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), effectCircle);
      fadeTransition.setFromValue(1);
      fadeTransition.setToValue(0);
      fadeTransition.setCycleCount(Animation.INDEFINITE);
//      fadeTransition.setAutoReverse(true);

      // Combine the transitions
      ParallelTransition parallelTransition = new ParallelTransition(scaleTransition,
          fadeTransition);
      parallelTransition.setCycleCount(Animation.INDEFINITE);
      parallelTransition.play();

      progressAnimation = new Timeline();
      int finalProgress = (int) (progress * 100);
      double totalDuration = 2.0;
      double incrementDuration = totalDuration / finalProgress;

      for (int i = 0; i <= finalProgress; i++) {
        final int currentProgress = i;
        progressAnimation.getKeyFrames()
            .add(new KeyFrame(Duration.seconds(i * incrementDuration), e -> {
              ringProgressIndicator.setProgress(currentProgress);
            }));
      }

      progressAnimation.play();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
        if (conn != null) {
          JDBC.closeConnection(conn);
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }

  // Display number of books borrowed by publish year
  private void displayScatter() {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      conn = JDBC.getConnection();

      Series<String, Number> adultSeries = new Series<>();
      if (BaseController.isTranslate) {
        adultSeries.setName("Adult");
      } else {
        adultSeries.setName("Người lớn");
      }

      Series<String, Number> childrenSeries = new Series<>();
      if (BaseController.isTranslate) {
        childrenSeries.setName("Student");
      } else {
        childrenSeries.setName("Học sinh");
      }

      Series<String, Number> elderlySeries = new Series<>();
      if (BaseController.isTranslate) {
        elderlySeries.setName("Elder");
      } else {
        elderlySeries.setName("Người già");
      }

      String sql = "SELECT b.published_date AS publish_year, " +
          "SUM(CASE WHEN EXTRACT(YEAR FROM now()) - EXTRACT(YEAR FROM u.birthday) BETWEEN 18 AND 59 THEN 1 ELSE 0 END) AS adult_count, "
          +
          "SUM(CASE WHEN EXTRACT(YEAR FROM now()) - EXTRACT(YEAR FROM u.birthday) BETWEEN 0 AND 17 THEN 1 ELSE 0 END) AS children_count, "
          +
          "SUM(CASE WHEN EXTRACT(YEAR FROM now()) - EXTRACT(YEAR FROM u.birthday) > 60 THEN 1 ELSE 0 END) AS elderly_count "
          +
          "FROM books b " +
          "INNER JOIN borrowing bo ON b.id_book = bo.id_book " +
          "INNER JOIN user u ON bo.id_user = u.id_user " +
          "GROUP BY publish_year " +
          "ORDER BY publish_year ASC;";

      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();

      ObservableList<String> allYears = FXCollections.observableArrayList();
      ObservableList<String> filteredYears = FXCollections.observableArrayList();

      while (rs.next()) {
        int year = rs.getInt("publish_year");
        int adultCount = rs.getInt("adult_count");
        int childrenCount = rs.getInt("children_count");
        int elderlyCount = rs.getInt("elderly_count");

        allYears.add(String.valueOf(year));

        if (year % 5 == 0) {
          filteredYears.add(String.valueOf(year));
        }

        Data<String, Number> adultData = new Data<>(String.valueOf(year),
            adultCount);
        Polygon adultTriangle = new Polygon();
        adultTriangle.getPoints().addAll(new Double[]{
            0.0, 7.5,
            7.5, 7.5,
            2.5 * 1.5, 0.0
        });
        adultTriangle.setFill(Color.web("#ff6666"));
        if (adultCount == 0) {
          adultTriangle.setOpacity(0);
        }
        adultData.setNode(adultTriangle);
        adultSeries.getData().add(adultData);

        Data<String, Number> childrenData = new Data<>(String.valueOf(year),
            childrenCount);
        Circle childrenCircle = new Circle(4.5, Color.web("#66ff66"));
        if (childrenCount == 0) {
          childrenCircle.setOpacity(0);
        }
        childrenData.setNode(childrenCircle);
        childrenSeries.getData().add(childrenData);

        Data<String, Number> elderlyData = new Data<>(String.valueOf(year),
            elderlyCount);
        Rectangle elderlyRectangle = new Rectangle(6, 6, Color.web("#00ccff"));
        if (elderlyCount == 0) {
          elderlyRectangle.setOpacity(0);
        }
        elderlyData.setNode(elderlyRectangle);
        elderlySeries.getData().add(elderlyData);
      }

      scatter.getData().clear();
      scatter.getData().addAll(adultSeries, childrenSeries, elderlySeries);

      if (!BaseController.isTranslate) {
        scatter.setTitle("Phân bố");
      } else {
        scatter.setTitle("Scatter");
      }

      CategoryAxis xAxis = (CategoryAxis) scatter.getXAxis();

      xAxis.setCategories(allYears);
      xAxis.setTickLabelsVisible(true);

      for (String year : allYears) {
        if (!filteredYears.contains(year)) {
          xAxis.lookupAll(".axis .tick")
              .forEach(node -> {
                if (node instanceof Labeled && ((Labeled) node).getText().equals(year)) {
                  node.setVisible(false);
                }
              });
        }
      }

    } catch (Exception se) {
      se.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (pstmt != null) {
          pstmt.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      } finally {
        if (conn != null) {
          JDBC.closeConnection(conn);
        }
      }
    }
  }

  private void displayArea() {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    try {
      conn = JDBC.getConnection();

      if (!BaseController.isTranslate) {
        areaChart.getXAxis().setLabel("Tháng");
        areaChart.getYAxis().setLabel("Số lượt mượn");
      } else {
        areaChart.getXAxis().setLabel("Month");
        areaChart.getYAxis().setLabel("Number of books borrowing");
      }
      areaChart.getXAxis().setStyle("-fx-font-size: 9px;");
      areaChart.getYAxis().setStyle("-fx-font-size: 9px;");

      areaChart.setLegendVisible(true);

      List<String> months = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11",
          "12");

      String sql = "SELECT EXTRACT(MONTH FROM borrowed_date) AS month, " +
          "SUM(CASE WHEN EXTRACT(YEAR FROM borrowed_date) = EXTRACT(YEAR FROM NOW()) THEN 1 ELSE 0 END) AS current_year_total, "
          +
          "SUM(CASE WHEN EXTRACT(YEAR FROM borrowed_date) = EXTRACT(YEAR FROM NOW()) - 1 THEN 1 ELSE 0 END) AS previous_year_total "
          +
          "FROM borrowing " +
          "GROUP BY month " +
          "ORDER BY month;";

      assert conn != null;
      preparedStatement = conn.prepareStatement(sql);
      rs = preparedStatement.executeQuery();

      Map<String, Integer> currentYearData = new HashMap<>();
      Map<String, Integer> previousYearData = new HashMap<>();

      while (rs.next()) {
        String month = String.valueOf(rs.getInt("month"));
        int currentTotal = rs.getInt("current_year_total");
        int previousTotal = rs.getInt("previous_year_total");

        currentYearData.put(month, currentTotal);
        previousYearData.put(month, previousTotal);
      }

      Series<String, Number> seriesCurrentYear = new Series<>();
      seriesCurrentYear.setName(String.valueOf(LocalDate.now().getYear()));
      for (String month : months) {
        int total = currentYearData.getOrDefault(month, 0);
        seriesCurrentYear.getData().add(new Data<>(month, total));
      }

      Series<String, Number> seriesPreviousYear = new Series<>();
      seriesPreviousYear.setName(String.valueOf(LocalDate.now().getYear() - 1));
      for (String month : months) {
        int total = previousYearData.getOrDefault(month, 0);
        seriesPreviousYear.getData().add(new Data<>(month, total));
      }

      areaChart.getData().clear();
      areaChart.getData().addAll(seriesCurrentYear, seriesPreviousYear);

    } catch (Exception se) {
      se.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }

  @FXML
  public void scatterClick() {
    AnchorPane blur = new AnchorPane();
    blur.setStyle("-fx-background-color: rgba(0,0,0,0.4);\n"
        + "  -fx-background-radius: 12px 0px 0px 0px;\n"
        + "  -fx-border-radius: 12px 0px 0px 0px;");
    blur.setPrefWidth(homePane.getBoundsInLocal().getWidth());
    blur.setPrefHeight(homePane.getBoundsInLocal().getHeight());
    homePane.getChildren().add(blur);

    AnchorPane fullyScatter = new AnchorPane();
    fullyScatter.getStyleClass().add("sub-pane");

    fullyScatterChart = createScatterChartCopy(scatter);
    fullyScatter.getChildren().add(fullyScatterChart);
    blur.getChildren().add(fullyScatter);

    scaleUp(fullyScatter);

    shadowEffect(fullyScatter);

    blur.setOnMouseClicked(event -> {
      scaleDown(fullyScatter, blur);
      fullyScatterChart = null;
    });

    fullyScatterChart.setOnMouseClicked(Event::consume);

    centerInParentDynamic(blur, fullyScatter, 0.6, 0.75);
  }

  private void scaleUp(AnchorPane anchorPane) {
    anchorPane.setScaleX(0.0);
    anchorPane.setScaleY(0.0);

    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), anchorPane);
    scaleTransition.setFromX(0.0);
    scaleTransition.setFromY(0.0);
    scaleTransition.setToX(1.0);
    scaleTransition.setToY(1.0);
    scaleTransition.setInterpolator(Interpolator.EASE_OUT);
    scaleTransition.play();
  }

  private void scaleDown(AnchorPane anchorPane, AnchorPane blur) {
    ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.3), anchorPane);
    scaleDown.setFromX(1.0);
    scaleDown.setFromY(1.0);
    scaleDown.setToX(0.0);
    scaleDown.setToY(0.0);
    scaleDown.setInterpolator(Interpolator.EASE_IN);

    scaleDown.setOnFinished(e -> {
      homePane.getChildren().remove(blur);
      if (shadowTimeline != null) {
        shadowTimeline.stop();
        shadowTimeline = null;
      }
    });

    scaleDown.play();
  }

  private void shadowEffect(AnchorPane anchorPane) {
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(15);
    dropShadow.setSpread(0.2);
    dropShadow.setColor(Color.rgb(255, 26, 148, 0.9));
    anchorPane.setEffect(dropShadow);

    Color[] colors = {
        Color.rgb(255, 26, 148, 0.9),
        Color.rgb(255, 51, 160, 0.9),
        Color.rgb(255, 77, 172, 0.9),
        Color.rgb(255, 102, 184, 0.9),
        Color.rgb(255, 128, 195, 0.9),
        Color.rgb(255, 153, 207, 0.8),
        Color.rgb(255, 153, 207, 0.8),
        Color.rgb(255, 128, 195, 0.9),
        Color.rgb(255, 102, 184, 0.9),
        Color.rgb(255, 77, 172, 0.9),
        Color.rgb(255, 51, 160, 0.9),
        Color.rgb(255, 26, 148, 0.9),
    };

    shadowTimeline = new Timeline();
    int duration = 6;
    int colorChangeCount = colors.length;
    double interval = (double) duration / (colorChangeCount * 3);

    for (int i = 0; i < colorChangeCount; i++) {
      final int index = i;
      shadowTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(i * interval),
          e -> {
            dropShadow.setColor(colors[index]);
            dropShadow.setRadius(15 + (10 * Math.sin(Math.PI * index / (colorChangeCount - 1))));
            dropShadow.setSpread(0.2 + (0.3 * Math.sin(Math.PI * index / (colorChangeCount - 1))));
            anchorPane.setEffect(dropShadow);
          }
      ));
    }

    for (int i = colorChangeCount - 1; i >= 0; i--) {
      final int index = i;
      shadowTimeline.getKeyFrames()
          .add(new KeyFrame(Duration.seconds((colorChangeCount + index) * interval),
              e -> {
                dropShadow.setColor(colors[index]);
                dropShadow.setRadius(
                    15 + (10 * Math.sin(Math.PI * index / (colorChangeCount - 1))));
                dropShadow.setSpread(0.2 + (0.3 * Math.sin(
                    Math.PI * index / (colorChangeCount - 1))));
                anchorPane.setEffect(dropShadow);
              }
          ));
    }

    shadowTimeline.setCycleCount(Timeline.INDEFINITE);
    shadowTimeline.setAutoReverse(true);

    shadowTimeline.play();
  }

  private void shadowEffect(Label label) {
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(15);
    dropShadow.setSpread(0.2);
    label.setEffect(dropShadow);

    Color[] colors = {
        Color.rgb(255, 77, 172, 0.9),
        Color.rgb(255, 102, 184, 0.9),
        Color.rgb(255, 128, 195, 0.9),
        Color.rgb(255, 153, 207, 0.8),
        Color.rgb(255, 153, 207, 0.8),
        Color.rgb(255, 128, 195, 0.9),
        Color.rgb(255, 102, 184, 0.9),
        Color.rgb(255, 77, 172, 0.9),
    };

    int duration = 6;
    int colorChangeCount = colors.length;
    double interval = (double) duration / (colorChangeCount * 3);

    if (shadowLabelTimeline == null) {
      shadowLabelTimeline = new Timeline();
    }

    for (int i = 0; i < colorChangeCount; i++) {
      final int index = i;
      shadowLabelTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(i * interval),
          e -> {
            dropShadow.setColor(colors[index]);
            label.setEffect(dropShadow);
          }
      ));
    }

    for (int i = colorChangeCount - 1; i >= 0; i--) {
      final int index = i;
      shadowLabelTimeline.getKeyFrames()
          .add(new KeyFrame(Duration.seconds((colorChangeCount + index) * interval),
              e -> {
                dropShadow.setColor(colors[index]);
                label.setEffect(dropShadow);
              }
          ));
    }

    shadowLabelTimeline.setCycleCount(Timeline.INDEFINITE);
    shadowLabelTimeline.setAutoReverse(true);

    shadowLabelTimeline.play();
  }

  private ScatterChart<String, Number> createScatterChartCopy(
      ScatterChart<String, Number> originalScatter) {
    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel(originalScatter.getXAxis().getLabel());

    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel(originalScatter.getYAxis().getLabel());
    yAxis.setAutoRanging(originalScatter.getYAxis().isAutoRanging());

    ScatterChart<String, Number> copiedScatterChart = new ScatterChart<>(xAxis, yAxis);

    if (BaseController.isTranslate) {
      copiedScatterChart.getXAxis().setLabel("Publish Year");
      copiedScatterChart.getYAxis().setLabel("Num of borrowing books");
    } else {
      copiedScatterChart.getXAxis().setLabel("Năm xuất bản");
      copiedScatterChart.getYAxis().setLabel("Sách mượn");
    }
    copiedScatterChart.getXAxis().setStyle("-fx-font-size: 15px;");
    copiedScatterChart.getYAxis().setStyle("-fx-font-size: 15px;");
    copiedScatterChart.setLegendVisible(true);

    for (Series<String, Number> series : originalScatter.getData()) {
      Series<String, Number> newSeries = new Series<>();
      newSeries.setName(series.getName());

      for (Data<String, Number> data : series.getData()) {
        Data<String, Number> newData = new Data<>(data.getXValue(),
            data.getYValue());

        if (data.getNode() != null) {
          Node originalNode = data.getNode();
          Node copiedNode;

          if (originalNode instanceof Polygon) {
            Polygon originalPolygon = (Polygon) originalNode;
            copiedNode = new Polygon();
            ((Polygon) copiedNode).getPoints().addAll(new Double[]{
                0.0, 10.0,
                10.0, 10.0,
                5.0, 0.0
            });
            ((Polygon) copiedNode).setFill(originalPolygon.getFill());
          } else if (originalNode instanceof Circle) {
            Circle originalCircle = (Circle) originalNode;
            copiedNode = new Circle(6, originalCircle.getFill());
          } else if (originalNode instanceof Rectangle) {
            Rectangle originalRectangle = (Rectangle) originalNode;
            copiedNode = new Rectangle(8, 8,
                originalRectangle.getFill());
          } else {
            copiedNode = originalNode;
          }

          if (newData.getYValue().doubleValue() == 0) {
            copiedNode.setOpacity(0);
          }
          newData.setNode(copiedNode);
        }

        newSeries.getData().add(newData);
      }

      copiedScatterChart.getData().add(newSeries);
    }

    customizeCopiedScatterLegend(copiedScatterChart);
    if (BaseController.isTranslate) {
      copiedScatterChart.setTitle("Number of borrowed books by Publication Year and Age Group");
    } else {
      copiedScatterChart.setTitle("Số lượng sách mượn theo Năm xuất Bản và Độ tuổi");
    }
    copiedScatterChart.setTitleSide(Side.TOP);
    copiedScatterChart.setPadding(new Insets(20, 0, 0, 0));

    return copiedScatterChart;
  }

  private void customizeCopiedScatterLegend(ScatterChart<String, Number> scatterChart) {
    for (Node legendItem : scatterChart.lookupAll(".chart-legend-item")) {
      Label label = (Label) legendItem;
      if (label.getText().equals("Người lớn") || label.getText().equals("Adult")) {
        Polygon triangle = new Polygon(0, 10, 10, 10, 5, 0);
        triangle.setStyle("-fx-fill: #ff6666;");
        label.setGraphic(triangle);
      } else if (label.getText().equals("Học sinh") || label.getText().equals("Student")) {
        Circle circle = new Circle(5);
        circle.setStyle("-fx-fill: #66ff66;");
        label.setGraphic(circle);
      } else if (label.getText().equals("Người già") || label.getText().equals("Elder")) {
        Rectangle rectangle = new Rectangle(10, 10);
        rectangle.setStyle("-fx-fill: #00ccff;");
        label.setGraphic(rectangle);
      }
    }
  }

  private void centerInParentDynamic(AnchorPane parent, AnchorPane child, double widthPercent,
      double heightPercent) {
    child.setPrefWidth(parent.getPrefWidth() * widthPercent);
    child.setPrefHeight(parent.getPrefHeight() * heightPercent);

    double topAnchor = (parent.getPrefHeight() - child.getPrefHeight()) / 2.2;
    double leftAnchor = (parent.getPrefWidth() - child.getPrefWidth()) / 2.5;

    AnchorPane.setTopAnchor(child, topAnchor);
    AnchorPane.setLeftAnchor(child, leftAnchor);
  }

  @FXML
  public void allOnClick() {
    AnchorPane blur = new AnchorPane();
    blur.setStyle("-fx-background-color: rgba(0,0,0,0.4);\n"
        + "  -fx-background-radius: 12px 0px 0px 0px;\n"
        + "  -fx-border-radius: 12px 0px 0px 0px;");
    blur.setPrefWidth(homePane.getBoundsInLocal().getWidth());
    blur.setPrefHeight(homePane.getBoundsInLocal().getHeight());
    homePane.getChildren().add(blur);

    AnchorPane fullyList = new AnchorPane();
    fullyList.getStyleClass().add("sub-pane");

    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    try {
      conn = JDBC.getConnection();

      String sql = "SELECT id_librarian, name_librarian, email_librarian FROM librarian";
      assert conn != null;
      preparedStatement = conn.prepareStatement(sql);

      rs = preparedStatement.executeQuery();

      tempTable = new JFXTreeTableView<>();

      tempTable.setPrefWidth(415);

      JFXTreeTableColumn<LibrarianTable, Number> librarianIdColumn = new JFXTreeTableColumn<>("ID");
      librarianIdColumn.setCellValueFactory(param -> param.getValue().getValue().idProperty());
      librarianIdColumn.setPrefWidth(50);
      librarianIdColumn.setMinWidth(50);
      librarianIdColumn.setStyle("-fx-alignment: CENTER;");

      JFXTreeTableColumn<LibrarianTable, String> librarianNameColumn = new JFXTreeTableColumn<>(
          "Tên");
      if (BaseController.isTranslate) {
        librarianNameColumn.setText("Name");
      }
      librarianNameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
      librarianNameColumn.setPrefWidth(170);
      librarianNameColumn.setMinWidth(170);
      librarianNameColumn.setStyle("-fx-alignment: CENTER;");

      JFXTreeTableColumn<LibrarianTable, String> librarianEmailColumn = new JFXTreeTableColumn<>(
          "Email");
      librarianEmailColumn.setCellValueFactory(
          param -> param.getValue().getValue().emailProperty());
      librarianEmailColumn.setPrefWidth(195);
      librarianEmailColumn.setMinWidth(195);
      librarianEmailColumn.setStyle("-fx-alignment: CENTER;");

      ObservableList<LibrarianTable> librarian = FXCollections.observableArrayList();
      while (rs.next()) {
        int id = rs.getInt("id_librarian");
        String name = rs.getString("name_librarian");
        String email = rs.getString("email_librarian");

        librarian.add(new LibrarianTable(id, name, email));
      }

      final TreeItem<LibrarianTable> root = new RecursiveTreeItem<>(librarian,
          RecursiveTreeObject::getChildren);
      tempTable.getColumns().clear();
      tempTable.getColumns()
          .setAll(librarianIdColumn, librarianNameColumn, librarianEmailColumn);
      tempTable.setRoot(root);
      tempTable.setShowRoot(false);
      fullyList.getChildren().add(tempTable);
      AnchorPane.setTopAnchor(tempTable, 10.0);
      AnchorPane.setBottomAnchor(tempTable, 10.0);
      AnchorPane.setLeftAnchor(tempTable, 10.0);
      AnchorPane.setRightAnchor(tempTable, 10.0);

      tempTable.setStyle("-fx-background-color: transparent;");

      blur.getChildren().add(fullyList);

      scaleUp(fullyList);

      shadowEffect(fullyList);

      blur.setOnMouseClicked(event -> {
        scaleDown(fullyList, blur);
        tempTable = null;
      });

      tempTable.setOnMouseClicked(event -> {
        event.consume();
      });

      JDBC.closeConnection(conn);
    } catch (Exception se) {
      se.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }

    centerInParentDynamic(blur, fullyList, 0.6, 0.75);
  }

  @FXML
  public void areaClick() {
    AnchorPane blur = new AnchorPane();
    blur.setStyle("-fx-background-color: rgba(0,0,0,0.4);\n"
        + "  -fx-background-radius: 12px 0px 0px 0px;\n"
        + "  -fx-border-radius: 12px 0px 0px 0px;");
    blur.setPrefWidth(homePane.getBoundsInLocal().getWidth());
    blur.setPrefHeight(homePane.getBoundsInLocal().getHeight());
    homePane.getChildren().add(blur);

    AnchorPane fullyList = new AnchorPane();
    fullyList.getStyleClass().add("sub-pane");

    scaleUp(fullyList);
    fullyAreaChart = createAreaChartCopy(areaChart);
    fullyList.getChildren().add(fullyAreaChart);
    blur.getChildren().add(fullyList);

    scaleUp(fullyList);

    shadowEffect(fullyList);

    blur.setOnMouseClicked(event -> {
      scaleDown(fullyList, blur);
      fullyAreaChart = null;
    });

    fullyAreaChart.setOnMouseClicked(event -> {
      event.consume();
    });

    centerInParentDynamic(blur, fullyList, 0.6, 0.75);
  }

  private AreaChart<String, Number> createAreaChartCopy(AreaChart<String, Number> originalArea) {
    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel(originalArea.getXAxis().getLabel());

    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel(originalArea.getYAxis().getLabel());
    yAxis.setAutoRanging(originalArea.getYAxis().isAutoRanging());

    AreaChart<String, Number> copiedAreaChart = new AreaChart<>(xAxis, yAxis);

    copiedAreaChart.getXAxis().setStyle("-fx-font-size: 15px;");
    copiedAreaChart.getYAxis().setStyle("-fx-font-size: 15px;");

    for (Series<String, Number> series : originalArea.getData()) {
      Series<String, Number> newSeries = new Series<>();
      newSeries.setName(series.getName());

      for (Data<String, Number> data : series.getData()) {
        Data<String, Number> newData = new Data<>(data.getXValue(),
            data.getYValue());

        newSeries.getData().add(newData);
      }

      copiedAreaChart.getData().add(newSeries);
    }

    if (BaseController.isTranslate) {
      copiedAreaChart.setTitle("The number of books borrowed each month throughout the year");
    } else {
      copiedAreaChart.setTitle("Số lượng sách mượn theo từng tháng trong các năm");
    }
    copiedAreaChart.setTitleSide(Side.TOP);
    copiedAreaChart.setPadding(new Insets(20, 0, 0, 0));

    return copiedAreaChart;
  }

  // Không gọi setUpLanguage ở đây
  @Override
  public void applyTranslate(HashMap<Object, String> viLang, HashMap<Object, String> enLang,
      boolean isTranslate) {
    renameSeries(scatter);
    customizeCopiedScatterLegend(scatter);
    if (isTranslate) {
      helloTxt.setText(enLang.get(helloTxt));
      numBookTxt.setText("Books");
      numStuTxt.setText("Students");
      overDueTxt.setText("Overdue books");
      banStuTxt.setText("Banned students");
      tableLibTxt.setText(enLang.get(tableLibTxt));
      borrowRateTxt.setText("Borrowing Rate");

      allBtn.setText("All");
      if (tempTable != null) {
        tempTable.getColumns().get(1).setText("Name");
      }
      librarianView.getColumns().get(1).setText("Name");

      scatter.setTitle("Scatter");
      if (fullyScatterChart != null) {
        fullyScatterChart.getXAxis().setLabel("Publish Year");
        fullyScatterChart.getYAxis().setLabel("Num of borrowing books");
        fullyScatterChart.setTitle("Number of borrowed books by Publication Year and Age Group");
      }

      areaChart.setTitle("Book borrowing Statistics");
      areaChart.getXAxis().setLabel("Month");
      areaChart.getYAxis().setLabel("Num of loans");
      if (fullyAreaChart != null) {
        fullyAreaChart.getXAxis().setLabel("Month");
        fullyAreaChart.getYAxis().setLabel("Number of books borrowing");
        fullyAreaChart.setTitle("The number of books borrowed each month throughout the year");
      }
    } else {
      helloTxt.setText(viLang.get(helloTxt));
      numBookTxt.setText("Số sách");
      numStuTxt.setText("Số sinh viên");
      overDueTxt.setText("Số sách quá hạn");
      banStuTxt.setText("Số sinh viên bị cấm");
      tableLibTxt.setText(viLang.get(tableLibTxt));
      borrowRateTxt.setText("Tỉ lệ sách đã mượn");
      scatter.setTitle("Phân bố");

      allBtn.setText("Tất cả");
      if (tempTable != null) {
        tempTable.getColumns().get(1).setText("Tên");
      }
      librarianView.getColumns().get(1).setText("Tên");

      if (fullyScatterChart != null) {
        fullyScatterChart.getXAxis().setLabel("Năm xuất bản");
        fullyScatterChart.getYAxis().setLabel("Sách mượn");
        fullyScatterChart.setTitle("Số lượng sách mượn theo Năm xuất Bản và Độ tuổi");
      }

      areaChart.setTitle("Thống kê mượn sách");
      areaChart.getXAxis().setLabel("Tháng");
      areaChart.getYAxis().setLabel("Số lượt mượn");
      if (fullyAreaChart != null) {
        fullyAreaChart.getXAxis().setLabel("Tháng");
        fullyAreaChart.getYAxis().setLabel("Số lượt mượn");
        fullyAreaChart.setTitle("Số lượng sách mượn theo từng tháng trong các năm");
      }
    }
    if (fullyScatterChart != null) {
      renameSeries(fullyScatterChart);
      customizeCopiedScatterLegend(fullyScatterChart);
    }

  }

  private void renameSeries(ScatterChart<String, Number> scatter) {
    for (Series<String, Number> series : scatter.getData()) {
      String currentName = series.getName();

      if (!BaseController.isTranslate) {
        if (currentName.equals("Người lớn")) {
          series.setName("Adult");
        } else if (currentName.equals("Học sinh")) {
          series.setName("Student");
        } else if (currentName.equals("Người già")) {
          series.setName("Elder");
        }
      } else {
        if (currentName.equals("Adult")) {
          series.setName("Người lớn");
        } else if (currentName.equals("Student")) {
          series.setName("Học sinh");
        } else if (currentName.equals("Elder")) {
          series.setName("Người già");
        }
      }
    }
  }
  
  // viLang lưu nội dung tiếng Việt gắn với Object, enLang lưu tiếng Anh
  @Override
  public void setUpLanguage(HashMap<Object, String> viLang, HashMap<Object, String> enLang) {
    viLang.put(helloTxt, helloTxt.getText());
    viLang.put(tableLibTxt, tableLibTxt.getText());
    enLang.put(helloTxt,
        Translate.translate(helloTxt.getText(), Language.VIETNAMESE, Language.ENGLISH) + " ");
    enLang.put(tableLibTxt,
        Translate.translate(tableLibTxt.getText(), Language.VIETNAMESE, Language.ENGLISH));
  }

  public void clearTimeline() {
    if (shadowTimeline != null) {
      shadowTimeline.stop();
      shadowTimeline = null;
    }
    if (progressAnimation != null) {
      progressAnimation.stop();
      progressAnimation = null;
    }

    if (animationTimeline != null) {
      animationTimeline.stop();
      animationTimeline = null;
    }

    if (shadowLabelTimeline != null) {
      shadowLabelTimeline.stop();
      shadowLabelTimeline = null;
    }

    if (timeTimeline != null) {
      timeTimeline.stop();
      timeTimeline = null;
    }

    if (colonFlashTimeline != null) {
      colonFlashTimeline.stop();
      colonFlashTimeline = null;
    }
  }
}
package org.example.clovervilleprogram.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {

  @FXML private AnchorPane contentPane;
  @FXML private Button showUserPage;
  @FXML private Button showPointsPage;
  @FXML private Button showTasksPage;
  @FXML private Button showTradesPage;

  private Button activeButton;

  @FXML
  private void showUserPage() {
    loadContent("/org/example/clovervilleprogram/UserRegister/UserRegister.fxml");
    setActiveButton(showUserPage);
  }

  @FXML
  private void showPointsPage() {
    loadContent("/org/example/clovervilleprogram/PointsPage/PointsPage.fxml");
    setActiveButton(showPointsPage);
  }

  @FXML
  private void showTasksPage() {
    loadContent("/org/example/clovervilleprogram/TasksPage/IndividualTasks.fxml");
    setActiveButton(showTasksPage);
  }

  @FXML
  private void showTradesPage() {
    loadContent("/org/example/clovervilleprogram/TradesPage/TradesPage.fxml");
    setActiveButton(showTradesPage);
  }


  private void loadContent(String fxmlFile) {
    try {
      Parent pane = FXMLLoader.load(getClass().getResource(fxmlFile));
      contentPane.getChildren().setAll(pane);

      AnchorPane.setTopAnchor(pane, 0.0);
      AnchorPane.setBottomAnchor(pane, 0.0);
      AnchorPane.setLeftAnchor(pane, 0.0);
      AnchorPane.setRightAnchor(pane, 0.0);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private void setActiveButton(Button button) {
    if (activeButton != null) {
      activeButton.getStyleClass().remove("sidebar-button-active");
      activeButton.getStyleClass().add("sidebar-button");
    }

    button.getStyleClass().remove("sidebar-button");
    button.getStyleClass().add("sidebar-button-active");
    activeButton = button;
  }
}

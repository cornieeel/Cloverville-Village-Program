package org.example.clovervilleprogram.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * MainController controls the main navigation of the application.
 * It handles switching between different pages and managing active sidebar buttons.
 */
public class MainController {

  // AnchorPane where different pages are dynamically loaded
  @FXML private AnchorPane contentPane;

  // Sidebar navigation buttons
  @FXML private Button showUserPage;
  @FXML private Button showPointsPage;
  @FXML private Button showTasksPage;
  @FXML private Button showTradesPage;

  // Keeps track of the currently active button
  private Button activeButton;

  /**
   * Loads the User Register page and highlights its button
   */
  @FXML
  private void showUserPage() {
    loadContent("/org/example/clovervilleprogram/UserRegister/UserRegister.fxml");
    setActiveButton(showUserPage);
  }

  /**
   * Loads the Points page and highlights its button
   */
  @FXML
  private void showPointsPage() {
    loadContent("/org/example/clovervilleprogram/PointsPage/PointsPage.fxml");
    setActiveButton(showPointsPage);
  }

  /**
   * Loads the Tasks page and highlights its button
   */
  @FXML
  private void showTasksPage() {
    loadContent("/org/example/clovervilleprogram/TasksPage/IndividualTasks.fxml");
    setActiveButton(showTasksPage);
  }

  /**
   * Loads the Trades page and highlights its button
   */
  @FXML
  private void showTradesPage() {
    loadContent("/org/example/clovervilleprogram/TradesPage/TradesPage.fxml");
    setActiveButton(showTradesPage);
  }

  /**
   * Loads an FXML file into the content pane
   *
   * @param fxmlFile the path to the FXML file
   */
  private void loadContent(String fxmlFile) {
    try {
      // Load the FXML file
      Parent pane = FXMLLoader.load(getClass().getResource(fxmlFile));

      // Replace existing content with the new pane
      contentPane.getChildren().setAll(pane);

      // Anchor the pane to all sides so it resizes correctly
      AnchorPane.setTopAnchor(pane, 0.0);
      AnchorPane.setBottomAnchor(pane, 0.0);
      AnchorPane.setLeftAnchor(pane, 0.0);
      AnchorPane.setRightAnchor(pane, 0.0);
    } catch (IOException e) {
      // Print stack trace if loading fails
      e.printStackTrace();
    }
  }

  /**
   * Updates the sidebar button styles to reflect the active page
   *
   * @param button the button to set as active
   */
  private void setActiveButton(Button button) {
    // Reset previously active button style
    if (activeButton != null) {
      activeButton.getStyleClass().remove("sidebar-button-active");
      activeButton.getStyleClass().add("sidebar-button");
    }

    // Apply active style to the selected button
    button.getStyleClass().remove("sidebar-button");
    button.getStyleClass().add("sidebar-button-active");
    activeButton = button;
  }
}

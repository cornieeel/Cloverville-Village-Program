package org.example.clovervilleprogram.Points;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for creating a new activity.
 * Handles user input, validation, and passing data back to the main points controller.
 */
public class ActivityCreateController {

  // Text field for entering the activity name
  @FXML
  private TextField activityName;

  // Text field for entering points per activity
  @FXML
  private TextField pointsPerActivity;

  // Label used to display validation errors
  @FXML private Label errorLabel;

  // Reference to the parent AddPointsController
  private AddPointsController pointsController;

  /**
   * Sets the parent points controller so the new activity can be added
   *
   * @param pointsController the AddPointsController instance
   */
  public void setPointsController(AddPointsController pointsController) {
    this.pointsController = pointsController;
  }

  /**
   * Handles saving a new activity
   * Validates input and passes the activity to the parent controller
   */
  @FXML
  private void handleSaveButton() {
    String name = activityName.getText().trim();
    String pointsText = pointsPerActivity.getText().trim();

    // Validate input fields
    if (name.isEmpty() || pointsText.isEmpty()) {
      errorLabel.setVisible(true);
      errorLabel.setText("You need to fill both fields");
      errorLabel.setStyle("-fx-text-fill: red");
      return;
    }

    int points;
    try {
      points = Integer.parseInt(pointsText);
    } catch (NumberFormatException e) {
      return;
    }

    // Create and add the new activity
    Activity activity = new Activity(name, points);
    pointsController.addActivity(activity);

    // Close the window after saving
    Stage stage = (Stage) activityName.getScene().getWindow();
    stage.close();
  }

  /**
   * Handles cancel button action
   * Closes the window without saving
   */
  @FXML
  private void handleCancelButton() {
    Stage stage = (Stage) activityName.getScene().getWindow();
    stage.close();
  }
}

package org.example.clovervilleprogram.Points;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ActivityCreateController {

  @FXML
  private TextField activityName;
  @FXML
  private TextField pointsPerActivity;

  @FXML private Label errorLabel;

  private AddPointsController pointsController;

  public void setPointsController(AddPointsController pointsController) {
    this.pointsController = pointsController;
  }

  @FXML
  private void handleSaveButton() {
    String name = activityName.getText().trim();
    String pointsText = pointsPerActivity.getText().trim();

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

    Activity activity = new Activity(name, points);
    pointsController.addActivity(activity);

    Stage stage = (Stage) activityName.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void handleCancelButton() {
    Stage stage = (Stage) activityName.getScene().getWindow();
    stage.close();
  }
}

package org.example.clovervilleprogram.Points;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ActivityCreateController {

  @FXML
  private TextField activityName;
  @FXML
  private TextField pointsPerActivity;

  private AddPointsController pointsController;


  public void setPointsController(AddPointsController pointsController) {
    this.pointsController = pointsController;
  }


  @FXML
  private void handleSaveButton() {
    pointsController.addActivity(new Activity(activityName.getText(), pointsPerActivity.getText()));



    Stage stage = (Stage) activityName.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void handleCancelButton() {
    Stage stage = (Stage) activityName.getScene().getWindow();
    stage.close();
  }
}

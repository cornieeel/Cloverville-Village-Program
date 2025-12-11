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
    Activity activity = new Activity();
    activity.setActivity(activityName.getText());
    activity.setPointsPerActivity(pointsPerActivity.getText());

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

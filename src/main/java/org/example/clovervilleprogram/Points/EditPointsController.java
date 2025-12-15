package org.example.clovervilleprogram.Points;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditPointsController {

  @FXML private Button cancelButton;
  @FXML private Button saveButton;
  @FXML private TextField citizenIdField;
  @FXML private TextField pointsField;
  @FXML private ComboBox<String> activityBox;
  @FXML private Label errorLabel;

  private Activity activity;
  private ObservableList<Activity> activities;
  private final ObservableList<String> activityNames = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    activityBox.setItems(activityNames);
    errorLabel.setVisible(false);

    pointsField.textProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal.matches("\\d*")) pointsField.setText(newVal.replaceAll("\\D", ""));
    });

    activityBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal == null || activities == null) {
        pointsField.clear();
        return;
      }
      for (Activity a : activities) {
        if (a.getActivity().equals(newVal)) {
          pointsField.setText(String.valueOf(a.getPointsPerActivity()));
          break;
        }
      }
    });
  }

  public void setActivities(ObservableList<Activity> activities) {
    this.activities = activities;
    activityNames.clear();
    for (Activity a : activities) activityNames.add(a.getActivity());

    activities.addListener((ListChangeListener<Activity>) change -> {
      activityNames.clear();
      for (Activity a : activities) activityNames.add(a.getActivity());
    });
  }

  public void setActivity(Activity activity) {
    this.activity = activity;
    citizenIdField.setEditable(false);
    citizenIdField.setText(activity.getResidentId());
    pointsField.setText(String.valueOf(activity.getPointsPerActivity()));
    activityBox.setValue(activity.getActivity());
  }

  @FXML
  public void handleSaveButton() {
    errorLabel.setVisible(false);

    String activityName = activityBox.getValue();
    String pointsText = pointsField.getText();

    if (activityName == null || activityName.isEmpty()) {
      showError("Select an activity!");
      return;
    }
    if (pointsText == null || pointsText.isEmpty()) {
      showError("Points cannot be empty!");
      return;
    }

    activity.setActivity(activityName);
    activity.setPointsPerActivity(pointsText);

    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }

  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-alignment: center;");
    errorLabel.setVisible(true);
  }

  @FXML
  public void handleCancelButton() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }
}

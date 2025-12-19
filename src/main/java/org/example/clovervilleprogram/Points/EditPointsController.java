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

/**
 * Controller for editing an existing resident activity and its points.
 * Handles updating activity selection and points while validating user input.
 */
public class EditPointsController {

  // Buttons for saving or canceling changes
  @FXML private Button cancelButton;
  @FXML private Button saveButton;

  // Input fields
  @FXML private TextField citizenIdField;
  @FXML private TextField pointsField;
  @FXML private ComboBox<String> activityBox;

  // Label for displaying validation errors
  @FXML private Label errorLabel;

  // The activity being edited
  private Activity activity;

  // List of available activities
  private ObservableList<Activity> activities;

  // List of activity names for the ComboBox
  private final ObservableList<String> activityNames = FXCollections.observableArrayList();

  /**
   * Initializes UI behavior and input validation.
   */
  @FXML
  public void initialize() {
    activityBox.setItems(activityNames);
    errorLabel.setVisible(false);

    // Ensure points field only allows numeric input
    pointsField.textProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal.matches("\\d*")) pointsField.setText(newVal.replaceAll("\\D", ""));
    });

    // Update points when activity selection changes
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

  /**
   * Sets the list of available activities and keeps the ComboBox updated.
   *
   * @param activities the observable list of activities
   */
  public void setActivities(ObservableList<Activity> activities) {
    this.activities = activities;
    activityNames.clear();
    for (Activity a : activities) activityNames.add(a.getActivity());

    activities.addListener((ListChangeListener<Activity>) change -> {
      activityNames.clear();
      for (Activity a : activities) activityNames.add(a.getActivity());
    });
  }

  /**
   * Sets the activity to be edited and populates fields with its data.
   *
   * @param activity the selected activity
   */
  public void setActivity(Activity activity) {
    this.activity = activity;
    citizenIdField.setEditable(false);
    citizenIdField.setText(activity.getResidentId());
    pointsField.setText(String.valueOf(activity.getPointsPerActivity()));
    activityBox.setValue(activity.getActivity());
  }

  /**
   * Handles saving changes to the activity.
   */
  @FXML
  public void handleSaveButton() {
    errorLabel.setVisible(false);

    String activityName = activityBox.getValue();
    String pointsText = pointsField.getText();

    // Validate input
    if (activityName == null || activityName.isEmpty()) {
      showError("Select an activity!");
      return;
    }
    if (pointsText == null || pointsText.isEmpty()) {
      showError("Points cannot be empty!");
      return;
    }

    // Apply changes to the activity
    activity.setActivity(activityName);
    activity.setPointsPerActivity(Integer.parseInt(pointsText));

    // Close the window
    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }

  /**
   * Displays an error message.
   */
  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-alignment: center;");
    errorLabel.setVisible(true);
  }

  /**
   * Cancels editing and closes the window.
   */
  @FXML
  public void handleCancelButton() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }
}

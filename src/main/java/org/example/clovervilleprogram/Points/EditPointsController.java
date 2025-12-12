package org.example.clovervilleprogram.Points;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.clovervilleprogram.Users.User;

public class EditPointsController {

  @FXML private Button cancelButton;
  @FXML private Button saveButton;
  @FXML private TextField citizenIdField;
  @FXML private TextField pointsField;
  @FXML private ComboBox<String> activityBox;

  private User user;
  private Activity activity;
  private ObservableList<Activity> activities;

  private final ObservableList<String> activityNames =
      FXCollections.observableArrayList();


  @FXML
  public void initialize() {
    activityBox.setItems(activityNames);

    activityBox.getSelectionModel()
        .selectedItemProperty()
        .addListener((obs, oldVal, newVal) -> {
          if (newVal == null || activities == null) {
            pointsField.clear();
            return;
          }

          for (Activity a : activities) {
            if (a.getActivity().equals(newVal)) {
              pointsField.setText(
                  String.valueOf(a.getPointsPerActivity())
              );
              break;
            }
          }
        });
  }

  public void setActivities(ObservableList<Activity> activities) {
    this.activities = activities;

    activityNames.clear();
    for (Activity a : activities) {
      activityNames.add(a.getActivity());
    }

    activities.addListener((ListChangeListener<Activity>) change -> {
      activityNames.clear();
      for (Activity a : activities) {
        activityNames.add(a.getActivity());
      }
    });
  }

  public void setActivity(Activity activity) {
    this.activity = activity;

    citizenIdField.setEditable(false);
    citizenIdField.setText(activity.getResidentId());


    pointsField.setText(
        String.valueOf(activity.getPointsPerActivity())
    );
    activityBox.setValue(activity.getActivity());
  }

  @FXML
  public void handleSaveButton() {
    activity.setActivity(activityBox.getValue());
    activity.setPointsPerActivity(pointsField.getText());
    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void handleCancelButton() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }
}

package org.example.clovervilleprogram.IndividualTasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clovervilleprogram.Users.User;

import java.io.File;
import java.util.List;

/**
 * Controller for editing an existing task.
 * Handles loading task data, validating user input, and saving changes.
 */
public class EditTasksController {

  // The task being edited
  private Tasks task;

  // Reference to the parent TasksController for refreshing the table
  private TasksController tasksController;

  // UI components from the FXML file
  @FXML private ComboBox<String> residentCombo;
  @FXML private TextField tasksEdit;
  @FXML private TextField pointsEdit;
  @FXML private Button saveButton;
  @FXML private Button cancelButton;
  @FXML private Label errorLabel;

  // JSON file containing user data
  private final File usersFile = new File("users.json");

  /**
   * Sets the task to be edited and populates the fields with its data
   *
   * @param task the selected task
   */
  public void setTask(Tasks task) {
    this.task = task;

    // Populate text fields with existing task data
    tasksEdit.setText(task.getIndividualActivity());
    pointsEdit.setText(String.valueOf(task.getPointsPerActivity()));

    // Load residents and select the current one
    loadResidents();
    residentCombo.setValue(task.getResidentId());
  }

  /**
   * Sets the parent TasksController for refreshing the task table
   *
   * @param controller the TasksController instance
   */
  public void setTasksController(TasksController controller) {
    this.tasksController = controller;
  }

  /**
   * Handles saving the edited task
   * Validates input fields before applying changes
   */
  @FXML
  public void handleSaveButton() {

    // Check if any required field is empty
    if (residentCombo.getValue() == null ||
        tasksEdit.getText().isEmpty() ||
        pointsEdit.getText().isEmpty()) {
      errorLabel.setVisible(true);
      errorLabel.setStyle("-fx-text-fill: red");
      errorLabel.setText("You need to fill all fields!");
      return;
    }

    // Apply changes to the task object
    task.setResidentId(residentCombo.getValue());
    task.setIndividualActivity(tasksEdit.getText());
    task.setPointsPerActivity(Integer.parseInt(pointsEdit.getText()));

    // Refresh the task table in the parent controller
    if (tasksController != null) {
      tasksController.refreshTable();
    }

    // Close the edit window
    closeWindow();
  }

  /**
   * Handles cancel button action
   * Closes the edit window without saving changes
   */
  @FXML
  public void handleCancelButton() {
    closeWindow();
  }

  /**
   * Loads resident names from the users.json file into the ComboBox
   */
  private void loadResidents() {

    // Do nothing if the users file does not exist
    if (!usersFile.exists()) return;

    try {
      ObjectMapper mapper = new ObjectMapper();

      // Read users from JSON file
      List<User> users =
          mapper.readValue(usersFile, new TypeReference<List<User>>() {});

      // Extract full names into an observable list
      ObservableList<String> names = FXCollections.observableArrayList();
      for (User u : users) {
        names.add(u.getFullName());
      }

      // Populate the ComboBox with resident names
      residentCombo.setItems(names);

    } catch (Exception e) {
      // Print stack trace if loading fails
      e.printStackTrace();
    }
  }

  /**
   * Closes the current window
   */
  private void closeWindow() {
    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }
}

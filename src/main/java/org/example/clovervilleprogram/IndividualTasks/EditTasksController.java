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

public class EditTasksController {

  private Tasks task;
  private TasksController tasksController;

  @FXML private ComboBox<String> residentCombo;
  @FXML private TextField tasksEdit;
  @FXML private TextField pointsEdit;
  @FXML private Button saveButton;
  @FXML private Button cancelButton;

  private final File usersFile = new File("users.json");

  // ===================== SETTERS =====================
  public void setTask(Tasks task) {
    this.task = task;

    tasksEdit.setText(task.getIndividualActivity());
    pointsEdit.setText(String.valueOf(task.getPointsPerActivity()));

    loadResidents();
    residentCombo.setValue(task.getResidentId());
  }

  public void setTasksController(TasksController controller) {
    this.tasksController = controller;
  }

  // ===================== ACTIONS =====================
  @FXML
  public void handleSaveButton() {

    if (residentCombo.getValue() == null ||
        tasksEdit.getText().isEmpty() ||
        pointsEdit.getText().isEmpty()) {
      return;
    }

    task.setResidentId(residentCombo.getValue());
    task.setIndividualActivity(tasksEdit.getText());
    task.setPointsPerActivity(Integer.parseInt(pointsEdit.getText()));

    if (tasksController != null) {
      tasksController.refreshTable();
    }

    closeWindow();
  }

  @FXML
  public void handleCancelButton() {
    closeWindow();
  }

  // ===================== HELPERS =====================
  private void loadResidents() {

    if (!usersFile.exists()) return;

    try {
      ObjectMapper mapper = new ObjectMapper();
      List<User> users =
          mapper.readValue(usersFile, new TypeReference<List<User>>() {});

      ObservableList<String> names = FXCollections.observableArrayList();
      for (User u : users) {
        names.add(u.getFullName());
      }
      residentCombo.setItems(names);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void closeWindow() {
    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }
}

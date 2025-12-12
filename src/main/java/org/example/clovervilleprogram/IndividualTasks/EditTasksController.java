package org.example.clovervilleprogram.IndividualTasks;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.clovervilleprogram.Users.User;

public class EditTasksController
{

  private Tasks task;
  private TasksController tasksController;

  @FXML private TextField fullNameEdit;
  @FXML private TextField tasksEdit;
  @FXML private TextField pointsEdit;
  @FXML private Button saveButton;
  @FXML private Button cancelButton;
  private User user;

  public void setTask(Tasks task){
    this.task = task;


    fullNameEdit.setText(task.getResidentId());
    tasksEdit.setText(task.getIndividualActivity());
    pointsEdit.setText(String.valueOf(task.getPointsPerActivity()));

  }
  public void setUser(User user){
    this.user = user;
  }
  public void setTasksController(TasksController tasksController){
    this.tasksController = tasksController;
  }
  public void handleSaveButton(){
    task.setResidentId(fullNameEdit.getText());
    task.setPointsPerActivity(Integer.parseInt(pointsEdit.getText()));
    task.setIndividualActivity(tasksEdit.getText());

    if(tasksController != null){
      tasksController.refreshTable();
    }
    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }
  public void handleCancelButton(){
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }
}

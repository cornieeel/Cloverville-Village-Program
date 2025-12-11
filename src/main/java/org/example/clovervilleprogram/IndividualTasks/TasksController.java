package org.example.clovervilleprogram.IndividualTasks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.clovervilleprogram.Points.Activity;

public class TasksController
{
  @FXML private ComboBox<String> residentId;
  @FXML private DatePicker dateOfActivity;
  @FXML private TextField activity;

  @FXML private TableView<Tasks> individualTasksTable;
  @FXML private TableColumn<Tasks, Number> numberTable;
  @FXML private TableColumn<Tasks, String> fullName;
  @FXML private TableColumn<Tasks, String> individualTask;
  @FXML private TableColumn<Tasks, Number> pointsPerActivity;

  private final ObservableList<Tasks> tasks = FXCollections.observableArrayList();

  public void initialize(){
    fullName.setCellValueFactory(
        new PropertyValueFactory<>("fullName")
    );
    individualTask.setCellValueFactory(
        new PropertyValueFactory<>("individualTasks")
    );
    pointsPerActivity.setCellValueFactory(
        new PropertyValueFactory<>("pointsPerActivity")
    );
    numberTable.setCellFactory(col -> new TableCell<>(){
      @Override
      protected void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setText(null);
        } else {
          setText(String.valueOf(getIndex() + 1));
        }
      }
    });
    individualTasksTable.setItems(tasks);
  }

  public void handleAddActivity(){

    Tasks tasks = new Tasks(
        activity.getText(),
        residentId.getValue(),
        pointsPerActivity.getValue().toString,
        pointsPerActivity.getText()


    );

  }
  public void handleResetFields(){

  }
}

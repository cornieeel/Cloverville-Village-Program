package org.example.clovervilleprogram.IndividualTasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.clovervilleprogram.Points.Activity;
import org.example.clovervilleprogram.Users.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TasksController
{
  @FXML private ComboBox<String> residentId;
  @FXML private DatePicker dateOfActivity;
  @FXML private TextField activity;
  @FXML private TextField pointsPerActivity;

  @FXML private TableView<Tasks> individualTasksTable;
  @FXML private TableColumn<Tasks, Number> numberTable;
  @FXML private TableColumn<Tasks, String> fullName;
  @FXML private TableColumn<Tasks, String> individualTask;
  @FXML private TableColumn<Tasks, Number> pointsPerActivityTable;

  private final ObservableList<Tasks> tasksLists = FXCollections.observableArrayList();

  private final File tasksFile = new File("tasks.json");
  private final File usersFile = new File("users.json");

  public void initialize(){
    fullName.setCellValueFactory(data -> data.getValue().residentIdProperty());
    individualTask.setCellValueFactory(data -> data.getValue()
            .activityProperty());
    pointsPerActivityTable.setCellValueFactory(data -> data.getValue()
            .pointsPerActivityProperty());
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
    loadTasksFromJson();
    individualTasksTable.setItems(tasksLists);
    loadCitizensFromJson();

  }

  public void handleAddActivity(){

    Tasks tasks = new Tasks(
        activity.getText(),
        residentId.getValue(),
        Integer.parseInt(pointsPerActivity.getText()),
        dateOfActivity.getValue().toString()


    );
    tasksLists.add(tasks);

  }
  public void handleResetFields(){

  }
  public void loadCitizensFromJson(){
      if(!usersFile.exists()) return;
      try
      {
        ObjectMapper mapper = new ObjectMapper();
        List<User> users = mapper.readValue(usersFile,
            new TypeReference<List<User>>(){});

        ObservableList<String> names = FXCollections.observableArrayList();
        for (User u : users)
        {
          names.add(u.getFullName());
        }
        residentId.setItems(names);


      } catch (Exception e){
        e.printStackTrace();
      }
  }
  public void loadTasksFromJson(){
    ObjectMapper mapper = new ObjectMapper();
    if(tasksFile.exists()){
    try{
      List<Tasks> tasks = mapper.readValue(tasksFile, new TypeReference<List<Tasks>>(){});
       tasksLists.setAll(tasks);
    }catch (Exception e){
      e.printStackTrace();
    }
  }}
  public void handleExportButton(){
    try{
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writeValue(tasksFile, tasksLists);
    } catch (IOException e){
      e.printStackTrace();
    }
  }
  public void handleEditTasks(){
    Tasks selectedTask =individualTasksTable.getSelectionModel().getSelectedItem();

    if(selectedTask == null){
      System.out.println("No user selected");
      return;

    }
    try{
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/clovervilleprogram/TasksPage/EditTasks.fxml"));
      Parent root = fxmlLoader.load();

      EditTasksController controller = fxmlLoader.getController();
      controller.setTask(selectedTask);
      controller.setTasksController(this);


      Stage stage = new Stage();
      stage.setTitle("Edit Task");
      stage.setScene(new Scene(root));
      stage.show();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  public void handleDeleteActivity(){
    Tasks selectedTask = individualTasksTable.getSelectionModel().getSelectedItem();

    tasksLists.remove(selectedTask);
  }
  public void refreshTable(){
    individualTasksTable.refresh();
  }
}

package org.example.clovervilleprogram.IndividualTasks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.clovervilleprogram.Users.User;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksController {

  @FXML private ComboBox<String> residentId;
  @FXML private DatePicker dateOfActivity;
  @FXML private TextField individualActivity;
  @FXML private TextField pointsPerActivity;
  @FXML private TextField searchBar;
  @FXML private Label errorLabel;
  @FXML private Label errorLabel1;

  @FXML private TableView<Tasks> individualTasksTable;
  @FXML private TableColumn<Tasks, Number> numberTable;
  @FXML private TableColumn<Tasks, String> fullName;
  @FXML private TableColumn<Tasks, String> individualTask;
  @FXML private TableColumn<Tasks, Number> pointsPerActivityTable;

  private final ObservableList<Tasks> tasksLists = FXCollections.observableArrayList();

  private final File tasksFile = new File("tasks.json");
  private final File usersFile = new File("users.json");
  private final File userPointsFile = new File("userPoints.json");

  @FXML
  public void initialize() {
    fullName.setCellValueFactory(data -> data.getValue().residentIdProperty());
    individualTask.setCellValueFactory(data -> data.getValue().individualActivityProperty());
    pointsPerActivityTable.setCellValueFactory(data -> data.getValue().pointsPerActivityProperty());

    numberTable.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : String.valueOf(getIndex() + 1));
      }
    });

    individualTasksTable.setItems(tasksLists);

    loadTasksFromJson();
    loadCitizensFromJson();
    setupSearchBar();
  }

  private void setupSearchBar() {
    FilteredList<Tasks> filteredList = new FilteredList<>(tasksLists, p -> true);
    searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
      String filter = newVal.toLowerCase().trim();
      filteredList.setPredicate(task -> filter.isEmpty()
          || task.getResidentId().toLowerCase().contains(filter)
          || task.getIndividualActivity().toLowerCase().contains(filter));
    });
    SortedList<Tasks> sortedList = new SortedList<>(filteredList);
    sortedList.comparatorProperty().bind(individualTasksTable.comparatorProperty());
    individualTasksTable.setItems(sortedList);
  }

  @FXML
  public void handleAddActivity() {
    if (residentId.getValue() == null ||
        individualActivity.getText().isEmpty() ||
        pointsPerActivity.getText().isEmpty() ||
        dateOfActivity.getValue() == null) {
      errorLabel.setVisible(true);
      errorLabel.setStyle("-fx-text-fill: red");
      errorLabel.setText("You need to fill all the data!");
      return;
    } else {
      errorLabel.setVisible(true);
      errorLabel.setStyle("-fx-text-fill: green");
      errorLabel.setText("Task and points have been successfully added!");
    };

    int points;
    try {
      points = Integer.parseInt(pointsPerActivity.getText());
    } catch (NumberFormatException e) {
      return;
    }

    Tasks task = new Tasks(
        individualActivity.getText(),
        residentId.getValue(),
        points,
        dateOfActivity.getValue().toString()
    );

    tasksLists.add(task);
    clearFields();
  }

  @FXML
  public void handleDeleteActivity() {
    Tasks selected = individualTasksTable.getSelectionModel().getSelectedItem();
    if (selected == null){
      errorLabel1.setVisible(true);
      errorLabel1.setText("You need to select a activity!");
      errorLabel1.setStyle("-fx-text-fill: red");
    } else  tasksLists.remove(selected);
  }

  @FXML
  public void handleResetFields() {
    individualActivity.clear();
    pointsPerActivity.clear();
    dateOfActivity.setValue(null);
    residentId.getSelectionModel().clearSelection();
    errorLabel.setVisible(false);
  }

  @FXML
  public void handleEditTasks() {
    Tasks selectedTask = individualTasksTable.getSelectionModel().getSelectedItem();
    if (selectedTask == null){
      errorLabel1.setVisible(true);
    errorLabel1.setText("You need to select a activity!");
    errorLabel1.setStyle("-fx-text-fill: red");
      return;}

    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/org/example/clovervilleprogram/TasksPage/EditTasks.fxml")
      );
      Parent root = loader.load();
      EditTasksController controller = loader.getController();
      controller.setTask(selectedTask);
      controller.setTasksController(this);
      Stage stage = new Stage();
      stage.setTitle("Edit Task");
      stage.setScene(new Scene(root));
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void handleExportButton() {
    try {
      errorLabel1.setVisible(true);
      errorLabel1.setText("Successfully exported to JSON!");
      errorLabel1.setStyle("-fx-text-fill: green");
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writeValue(tasksFile, tasksLists);

      Map<String, Integer> pointsMap = new HashMap<>();
      for (Tasks task : tasksLists) {
        pointsMap.merge(task.getResidentId(), task.getPointsPerActivity(), Integer::sum);
      }
      mapper.writeValue(userPointsFile, pointsMap);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadTasksFromJson() {
    if (!tasksFile.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      List<Tasks> tasks = mapper.readValue(tasksFile, new TypeReference<List<Tasks>>() {});
      tasksLists.setAll(tasks);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadCitizensFromJson() {
    if (!usersFile.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      List<User> users = mapper.readValue(usersFile, new TypeReference<List<User>>() {});
      ObservableList<String> names = FXCollections.observableArrayList();
      for (User u : users) names.add(u.getFullName());
      residentId.setItems(names);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void refreshTable() {
    individualTasksTable.refresh();
  }

  private void clearFields() {
    individualActivity.clear();
    pointsPerActivity.clear();
    dateOfActivity.setValue(null);
    residentId.getSelectionModel().clearSelection();
  }
}

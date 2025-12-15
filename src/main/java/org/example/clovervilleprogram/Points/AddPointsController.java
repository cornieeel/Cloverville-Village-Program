package org.example.clovervilleprogram.Points;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.clovervilleprogram.Users.User;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AddPointsController {

  @FXML
  private TableView<Activity> pointsTable;
  @FXML
  private TableColumn<Activity, String> greenActivityTable;
  @FXML
  private TableColumn<Activity, Number> pointsPerActivityTable;
  @FXML
  private TableView<Activity> pointsResidentTable;
  @FXML
  private TableColumn<Activity, String> residentIDpointsTable;
  @FXML
  private TableColumn<Activity, String> greenActivityResidentTable;
  @FXML
  private TableColumn<Activity, Number> pointsPerActivityResidentTable;
  @FXML
  private TableColumn<Activity, Number> numberOfActivities;
  @FXML
  private TableColumn<Activity, Number> numberOfAvailableActivities;
  @FXML
  private TableColumn<Activity, Number> dateOfActivity;
  @FXML
  private ComboBox<String> activitiesDropDown;
  @FXML
  private ComboBox<String> citizenIdDropDown;
  @FXML
  private TextField pointsField;
  @FXML
  private DatePicker datePoints;
  @FXML
  private Label errorLabel;

  private final ObservableList<Activity> activities = FXCollections.observableArrayList();
  private final ObservableList<Activity> residentActivities = FXCollections.observableArrayList();
  private final File usersFile = new File("users.json");
  private final File pointsFile = new File("points.json");
  private final File actualPointsFile = new File("actualPoints.json");

  @FXML
  private void initialize() {

    greenActivityTable.setCellValueFactory(new PropertyValueFactory<>("activity"));
    pointsPerActivityTable.setCellValueFactory(new PropertyValueFactory<>("pointsPerActivity"));
    numberOfAvailableActivities.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : String.valueOf(getIndex() + 1));
      }
    });
    pointsTable.setItems(activities);

    residentIDpointsTable.setCellValueFactory(new PropertyValueFactory<>("residentId"));
    dateOfActivity.setCellValueFactory(new PropertyValueFactory<>("date"));
    greenActivityResidentTable.setCellValueFactory(new PropertyValueFactory<>("activity"));
    pointsPerActivityResidentTable.setCellValueFactory(new PropertyValueFactory<>("pointsPerActivity"));
    numberOfActivities.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : String.valueOf(getIndex() + 1));
      }
    });
    pointsResidentTable.setItems(residentActivities);

    ObservableList<String> activityNames = FXCollections.observableArrayList();
    activitiesDropDown.setItems(activityNames);

    activities.addListener((ListChangeListener<Activity>) change -> {
      activityNames.clear();
      for (Activity a : activities) activityNames.add(a.getActivity());
    });

    activitiesDropDown.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal == null) {
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

    loadCitizensFromJson();
    loadActivitiesFromJson();
    loadActualActivitiesFromJson();
    errorLabel.setVisible(false);

    pointsField.textProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal.matches("\\d*")) pointsField.setText(newVal.replaceAll("\\D", ""));
    });

    citizenIdDropDown.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal.matches("\\d*")) citizenIdDropDown.getEditor().setText(newVal.replaceAll("\\D", ""));
    });
  }

  private void loadCitizensFromJson() {
    if (!usersFile.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      List<User> users = mapper.readValue(usersFile, new TypeReference<List<User>>() {});
      ObservableList<String> ids = FXCollections.observableArrayList();
      for (User u : users) ids.add(u.getCitizenId());
      citizenIdDropDown.setItems(ids);
    } catch (IOException e) { e.printStackTrace(); }
  }

  private void loadActivitiesFromJson() {
    if (!pointsFile.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      List<Activity> list = mapper.readValue(pointsFile, new TypeReference<List<Activity>>() {});
      activities.addAll(list);
    } catch (IOException e) { e.printStackTrace(); }
  }

  private void loadActualActivitiesFromJson() {
    if (!actualPointsFile.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      List<Activity> list = mapper.readValue(actualPointsFile, new TypeReference<List<Activity>>() {});
      residentActivities.addAll(list);
    } catch (Exception e) { e.printStackTrace(); }
  }

  @FXML
  public void handleResetFields() {
    activitiesDropDown.setValue(null);
    citizenIdDropDown.setValue(null);
    pointsField.clear();
    datePoints.setValue(null);
    errorLabel.setVisible(false);
  }

  @FXML
  public void handleAddPointsButton() {
    errorLabel.setVisible(false);
    String citizenId = citizenIdDropDown.getValue();
    String activityName = activitiesDropDown.getValue();
    String pointsText = pointsField.getText();
    String date = datePoints.getValue() != null ? datePoints.getValue().toString() : "";

    if (citizenId == null || citizenId.isEmpty()) { showError("Citizen ID cannot be empty!"); return; }
    if (activityName == null || activityName.isEmpty()) { showError("Select an activity!"); return; }
    if (pointsText == null || pointsText.isEmpty()) { showError("Points cannot be empty!"); return; }
    if (date == null || date.isEmpty()) { showError("Date cannot be empty!"); return; }

    Activity activity = new Activity(citizenId, activityName, pointsText, date);
    residentActivities.add(activity);
    handleResetFields();
  }

  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-alignment: center;");
    errorLabel.setVisible(true);
  }

  @FXML
  public void handleExportButton() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writeValue(pointsFile, activities);
    } catch (IOException e) { e.printStackTrace(); }
  }

  public void handleExportActualPointsButton() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writeValue(actualPointsFile, residentActivities);
    } catch (IOException e) { e.printStackTrace(); }
  }

  @FXML
  public void handleAddButton() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/clovervilleprogram/PointsPage/AddPoints.fxml"));
      Stage stage = new Stage();
      stage.setScene(new Scene(loader.load()));
      ActivityCreateController controller = loader.getController();
      controller.setPointsController(this);
      stage.setTitle("Add Green Activity");
      stage.show();
    } catch (IOException e) { e.printStackTrace(); }
  }

  public void handleDeleteActivity() {
    Activity selectedActivity = pointsTable.getSelectionModel().getSelectedItem();
    if (selectedActivity != null) activities.remove(selectedActivity);
  }

  public void handleEditActivity() {
    Activity selectedResidentActivity = pointsResidentTable.getSelectionModel().getSelectedItem();
    if (selectedResidentActivity == null) return;
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/clovervilleprogram/PointsPage/EditPoints.fxml"));
      Parent root = fxmlLoader.load();
      EditPointsController controller = fxmlLoader.getController();
      controller.setActivity(selectedResidentActivity);
      controller.setActivities(activities);
      Stage stage = new Stage();
      stage.setTitle("Edit Points");
      stage.setScene(new Scene(root));
      stage.show();
    } catch (IOException e) { throw new RuntimeException(e); }
  }

  public void handleDeleteResidentActivity() {
    Activity selectedActivity = pointsResidentTable.getSelectionModel().getSelectedItem();
    if (selectedActivity != null) residentActivities.remove(selectedActivity);
  }

  public void addActivity(Activity activity) {
    activities.add(activity);
  }
}

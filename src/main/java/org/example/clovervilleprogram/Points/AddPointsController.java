package org.example.clovervilleprogram.Points;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

/**
 * Controller for managing points and green activities.
 * Handles available activities, resident activities, searching, editing,
 * deleting, and exporting data to JSON files.
 */
public class AddPointsController {

  // Tables and columns for available activities
  @FXML private TableView<Activity> pointsTable;
  @FXML private TableColumn<Activity, String> greenActivityTable;
  @FXML private TableColumn<Activity, Number> pointsPerActivityTable;

  // Tables and columns for resident activities
  @FXML private TableView<Activity> pointsResidentTable;
  @FXML private TableColumn<Activity, String> residentIDpointsTable;
  @FXML private TableColumn<Activity, String> greenActivityResidentTable;
  @FXML private TableColumn<Activity, Number> pointsPerActivityResidentTable;
  @FXML private TableColumn<Activity, Number> numberOfActivities;
  @FXML private TableColumn<Activity, Number> numberOfAvailableActivities;
  @FXML private TableColumn<Activity, Number> dateOfActivity;

  // Input controls
  @FXML private ComboBox<String> activitiesDropDown;
  @FXML private ComboBox<String> citizenIdDropDown;
  @FXML private TextField pointsField;
  @FXML private DatePicker datePoints;

  // Labels and search bars for feedback and filtering
  @FXML private Label errorLabel;
  @FXML private TextField searchBarAvailablePoints;
  @FXML private TextField searchBarGreenPoints;
  @FXML private Label errorLabel1;
  @FXML private Label errorLabel2;

  // Observable lists for activities and resident activities
  private final ObservableList<Activity> activities = FXCollections.observableArrayList();
  private final ObservableList<Activity> residentActivities = FXCollections.observableArrayList();

  // JSON files for persistence
  private final File usersFile = new File("users.json");
  private final File pointsFile = new File("points.json");
  private final File actualPointsFile = new File("actualPoints.json");

  /**
   * Initializes tables, dropdowns, listeners, loads JSON data,
   * and sets up search bars.
   */
  @FXML
  private void initialize() {
    greenActivityTable.setCellValueFactory(new PropertyValueFactory<>("activity"));
    pointsPerActivityTable.setCellValueFactory(new PropertyValueFactory<>("pointsPerActivity"));

    // Auto-numbering rows for available activities table
    numberOfAvailableActivities.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : String.valueOf(getIndex() + 1));
      }
    });
    pointsTable.setItems(activities);

    // Setting up resident activities table columns
    residentIDpointsTable.setCellValueFactory(new PropertyValueFactory<>("residentId"));
    dateOfActivity.setCellValueFactory(new PropertyValueFactory<>("date"));
    greenActivityResidentTable.setCellValueFactory(new PropertyValueFactory<>("activity"));
    pointsPerActivityResidentTable.setCellValueFactory(new PropertyValueFactory<>("pointsPerActivity"));

    // Auto-numbering rows for resident activities table
    numberOfActivities.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : String.valueOf(getIndex() + 1));
      }
    });
    pointsResidentTable.setItems(residentActivities);

    // Populate activity dropdown dynamically from activities list
    ObservableList<String> activityNames = FXCollections.observableArrayList();
    activitiesDropDown.setItems(activityNames);
    activities.addListener((ListChangeListener<Activity>) change -> {
      activityNames.clear();
      for (Activity a : activities) activityNames.add(a.getActivity());
    });

    // Auto-fill points field when activity is selected
    activitiesDropDown.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal == null) pointsField.clear();
      else {
        for (Activity a : activities) {
          if (a.getActivity().equals(newVal)) {
            pointsField.setText(String.valueOf(a.getPointsPerActivity()));
            break;
          }
        }
      }
    });

    // Load data from JSON files
    loadCitizensFromJson();
    loadActivitiesFromJson();
    loadActualActivitiesFromJson();
    errorLabel.setVisible(false);

    // Ensure points field only allows digits
    pointsField.textProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal.matches("\\d*")) pointsField.setText(newVal.replaceAll("\\D", ""));
    });

    // Ensure citizen ID input only allows digits
    citizenIdDropDown.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal.matches("\\d*")) citizenIdDropDown.getEditor().setText(newVal.replaceAll("\\D", ""));
    });

    setupSearchBars();
  }

  /**
   * Sets up filtering and sorting logic for both search bars.
   */
  private void setupSearchBars() {
    FilteredList<Activity> filteredAvailable = new FilteredList<>(activities, p -> true);
    searchBarAvailablePoints.textProperty().addListener((obs, oldVal, newVal) -> {
      String filter = newVal.toLowerCase().trim();
      filteredAvailable.setPredicate(a -> filter.isEmpty() || a.getActivity().toLowerCase().contains(filter));
    });
    SortedList<Activity> sortedAvailable = new SortedList<>(filteredAvailable);
    sortedAvailable.comparatorProperty().bind(pointsTable.comparatorProperty());
    pointsTable.setItems(sortedAvailable);

    FilteredList<Activity> filteredResident = new FilteredList<>(residentActivities, p -> true);
    searchBarGreenPoints.textProperty().addListener((obs, oldVal, newVal) -> {
      String filter = newVal.toLowerCase().trim();
      filteredResident.setPredicate(a -> filter.isEmpty()
          || a.getActivity().toLowerCase().contains(filter)
          || a.getResidentId().toLowerCase().contains(filter));
    });
    SortedList<Activity> sortedResident = new SortedList<>(filteredResident);
    sortedResident.comparatorProperty().bind(pointsResidentTable.comparatorProperty());
    pointsResidentTable.setItems(sortedResident);
  }

  /**
   * Loads citizen IDs from users.json into the dropdown.
   */
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

  /**
   * Loads available activities from points.json.
   */
  private void loadActivitiesFromJson() {
    if (!pointsFile.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      List<Activity> list = mapper.readValue(pointsFile, new TypeReference<List<Activity>>() {});
      activities.addAll(list);
    } catch (IOException e) { e.printStackTrace(); }
  }

  /**
   * Loads actual resident activities from actualPoints.json.
   */
  private void loadActualActivitiesFromJson() {
    if (!actualPointsFile.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      List<Activity> list = mapper.readValue(actualPointsFile, new TypeReference<List<Activity>>() {});
      residentActivities.addAll(list);
    } catch (Exception e) { e.printStackTrace(); }
  }

  /**
   * Resets all input fields.
   */
  @FXML
  public void handleResetFields() {
    activitiesDropDown.setValue(null);
    citizenIdDropDown.setValue(null);
    pointsField.clear();
    datePoints.setValue(null);
    errorLabel.setVisible(false);
  }

  /**
   * Adds points to a resident based on selected activity and input data.
   */
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

    int points;
    try { points = Integer.parseInt(pointsText); }
    catch (NumberFormatException e) { showError("Points must be a number!"); return; }

    Activity activity = new Activity(citizenId, activityName, points, date);
    residentActivities.add(activity);
    handleResetFields();
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
   * Exports available activities to points.json.
   */
  @FXML
  public void handleExportButton() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writeValue(pointsFile, activities);
      errorLabel2.setVisible(true);
      errorLabel2.setText("Uploaded to JSON successfully!");
      errorLabel2.setStyle("-fx-text-fill: green");
    } catch (IOException e) { e.printStackTrace(); }
  }

  /**
   * Exports resident activities to actualPoints.json.
   */
  public void handleExportActualPointsButton() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writeValue(actualPointsFile, residentActivities);
      errorLabel1.setVisible(true);
      errorLabel1.setText("Uploaded to JSON successfully!");
      errorLabel1.setStyle("-fx-text-fill: green");
    } catch (IOException e) {
      System.out.println("Need to select something");; }
  }

  /**
   * Opens a window for adding a new green activity.
   */
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
    } catch (IOException e) {
      System.out.println("Error");; }
  }

  /**
   * Deletes the selected available activity.
   */
  public void handleDeleteActivity() {
    Activity selectedActivity = pointsTable.getSelectionModel().getSelectedItem();
    if (selectedActivity == null){
      errorLabel2.setVisible(true);
      errorLabel2.setText("You need to select a activity!");
      errorLabel2.setStyle("-fx-text-fill: red");
    }else{ activities.remove(selectedActivity);
      errorLabel2.setVisible(false);}
  }

  /**
   * Opens a window to edit a selected resident activity.
   */
  public void handleEditActivity() {
    Activity selectedResidentActivity = pointsResidentTable.getSelectionModel().getSelectedItem();
    if (selectedResidentActivity == null) {
      errorLabel1.setVisible(true);
      errorLabel1.setText("You need to select one of the users!");
      errorLabel1.setStyle("-fx-text-fill: red");
    }
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
    } catch (IOException e) {
      System.out.println("Normal Error");; }
  }

  /**
   * Deletes a selected resident activity.
   */
  public void handleDeleteResidentActivity() {
    Activity selectedActivity = pointsResidentTable.getSelectionModel().getSelectedItem();
    if (selectedActivity == null){
      errorLabel1.setVisible(true);
      errorLabel1.setText("You need to select one of the users!");
      errorLabel1.setStyle("-fx-text-fill: red");

    } else{residentActivities.remove(selectedActivity);
      errorLabel1.setVisible(false);}}

  /**
   * Adds a new activity to the available activities list.
   */
  public void addActivity(Activity activity) {
    activities.add(activity);
  }

}

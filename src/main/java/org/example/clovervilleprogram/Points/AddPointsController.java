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

  // ===================== TABLES =====================

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
  @FXML private TableColumn<Activity, Number> dateOfActivity;

  // ===================== INPUTS =====================

  @FXML
  private ComboBox<String> activitiesDropDown;

  @FXML
  private ComboBox<String> citizenIdDropDown;

  @FXML
  private TextField pointsField;

  @FXML
  private DatePicker datePoints;

  // ===================== DATA =====================

  private final ObservableList<Activity> activities = FXCollections.observableArrayList();
  private final ObservableList<Activity> residentActivities = FXCollections.observableArrayList();

  private final File usersFile = new File("users.json");
  private final File pointsFile = new File("points.json");
  private final File actualPointsFile = new File("actualPoints.json");

  // ===================== INITIALIZE =====================

  @FXML
  private void initialize() {

    // ----- Available activities table -----
    greenActivityTable.setCellValueFactory(
        new PropertyValueFactory<>("activity")
    );
    pointsPerActivityTable.setCellValueFactory(
        new PropertyValueFactory<>("pointsPerActivity")
    );
    numberOfAvailableActivities.setCellFactory(col -> new TableCell<>(){
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


    pointsTable.setItems(activities);

    // ----- Resident points table -----
    residentIDpointsTable.setCellValueFactory(
        new PropertyValueFactory<>("residentId")
    );
    dateOfActivity.setCellValueFactory(
        new PropertyValueFactory<>("date")
    );
    greenActivityResidentTable.setCellValueFactory(
        new PropertyValueFactory<>("activity")
    );
    pointsPerActivityResidentTable.setCellValueFactory(
        new PropertyValueFactory<>("pointsPerActivity")
    );
    numberOfActivities.setCellFactory(col -> new TableCell<>() {
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

    pointsResidentTable.setItems(residentActivities);

    // ----- Dropdown setup -----
    ObservableList<String> activityNames = FXCollections.observableArrayList();
    activitiesDropDown.setItems(activityNames);

    activities.addListener((ListChangeListener<Activity>) change -> {
      activityNames.clear();
      for (Activity a : activities) {
        activityNames.add(a.getActivity());
      }
    });

    activitiesDropDown.getSelectionModel()
        .selectedItemProperty()
        .addListener((obs, oldVal, newVal) -> {
          if (newVal == null) {
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

    loadCitizensFromJson();
    loadActivitiesFromJson();
    loadActualActivitiesFromJson();
  }

  // ===================== LOADERS =====================

  private void loadCitizensFromJson() {
    if (!usersFile.exists()) return;

    try {
      ObjectMapper mapper = new ObjectMapper();
      List<User> users =
          mapper.readValue(usersFile, new TypeReference<List<User>>() {});

      ObservableList<String> ids = FXCollections.observableArrayList();
      for (User u : users) {
        ids.add(u.getCitizenId());
      }
      citizenIdDropDown.setItems(ids);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadActivitiesFromJson() {
    if (!pointsFile.exists()) return;

    try {
      ObjectMapper mapper = new ObjectMapper();
      List<Activity> list =
          mapper.readValue(pointsFile, new TypeReference<List<Activity>>() {});
      activities.addAll(list);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private void loadActualActivitiesFromJson(){
    if(!actualPointsFile.exists()) return;

    try
    {
      ObjectMapper mapper = new ObjectMapper();
      List<Activity> list = mapper.readValue(actualPointsFile, new TypeReference<List<Activity>>(){});
      residentActivities.addAll(list);
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  // ===================== ACTIONS =====================

  @FXML
  public void handleAddPointsButton() {

    String citizenId = citizenIdDropDown.getValue();
    String activityName = activitiesDropDown.getValue();
    String pointsText = pointsField.getText();
    String date = datePoints.getValue() != null ? datePoints.getValue().toString() : "";


    Activity activity =
        new Activity(citizenId, activityName, pointsText, date);

    residentActivities.add(activity);
  }

  @FXML
  public void handleExportButton() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writeValue(pointsFile, activities);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void handleExportActualPointsButton(){
    try{
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writeValue(actualPointsFile, residentActivities);
    } catch (IOException e){
      e.printStackTrace();
    }
  }

  @FXML
  public void handleAddButton() {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource(
              "/org/example/clovervilleprogram/PointsPage/AddPoints.fxml"
          )
      );

      Stage stage = new Stage();
      stage.setScene(new Scene(loader.load()));

      ActivityCreateController controller = loader.getController();
      controller.setPointsController(this);

      stage.setTitle("Add Green Activity");
      stage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void handleDeleteActivity(){
    Activity selectedActivity = pointsTable.getSelectionModel().getSelectedItem();
    if(selectedActivity != null){
      activities.remove(selectedActivity);
      System.out.println("Button working");
    } else {
      System.out.println("Button not working");
    }
  }
  public void handleEditActivity(){
    Activity selectedResidentActivity = pointsResidentTable.getSelectionModel().getSelectedItem();
    if(selectedResidentActivity ==null){
      System.out.println("No user selected");
      return;
    }
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/clovervilleprogram/PointsPage/EditPoints.fxml"));
    try
    {
      Parent root = fxmlLoader.load();

      EditPointsController controller = fxmlLoader.getController();
      controller.setActivity(selectedResidentActivity);
      controller.setActivities(activities);
      controller.setActivity(selectedResidentActivity);

      Stage stage = new Stage();
      stage.setTitle("Edit Points");
      stage.setScene(new Scene(root));
      stage.show();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

  }
  public void handleDeleteResidentActivity(){
    Activity selectedActivity = pointsResidentTable.getSelectionModel().getSelectedItem();
    if(selectedActivity != null){
      residentActivities.remove(selectedActivity);
    }
  }

  // ===================== CALLBACK =====================

  public void addActivity(Activity activity) {
    activities.add(activity);
  }
}

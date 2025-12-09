package org.example.clovervilleprogram.Points;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
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
  private TableColumn<Activity, String> pointsPerActivityTable;

  @FXML
  private ComboBox<String> activitiesDropDown;

  @FXML
  private ComboBox<String> citizenIdDropDown;
  @FXML
  private TextField pointsField;

  private final ObservableList<Activity> activities = FXCollections.observableArrayList();

  private final File usersFile = new File("users.json");
  private final File jsonFile = new File("points.json");

  @FXML
  private void initialize() {

    greenActivityTable.setCellValueFactory(new PropertyValueFactory<>("activity"));
    pointsPerActivityTable.setCellValueFactory(new PropertyValueFactory<>("pointsPerActivity"));
    pointsTable.setItems(activities);

    ObservableList<String> activityItems = FXCollections.observableArrayList();
    activitiesDropDown.setItems(activityItems);

    activities.addListener((ListChangeListener<Activity>) change -> {
      activityItems.clear();
      for (Activity activity : activities) {
        activityItems.add(activity.getActivity());
      }
    });
    activitiesDropDown.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
      if (newValue != null) {
        for (Activity activity : activities) {
          if (activity.getActivity().equals(newValue)) {
            pointsField.setText(activity.getPointsPerActivity());
            break;
          }
        }
      } else {
        pointsField.clear();
      }
    });


    loadCitizensFromJson();
    loadActivitiesFromJson();
  }

  private void loadCitizensFromJson() {
    if (!usersFile.exists()) return;

    ObjectMapper mapper = new ObjectMapper();

    try {
      List<User> users =
          mapper.readValue(usersFile, new TypeReference<List<User>>() {});

      ObservableList<String> citizenIds = FXCollections.observableArrayList();
      for (User user : users) {
        citizenIds.add(user.getCitizenId());
      }

      citizenIdDropDown.setItems(citizenIds);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadActivitiesFromJson() {
    ObjectMapper mapper = new ObjectMapper();
    if (!jsonFile.exists()) return;

    try {
      List<Activity> list = mapper.readValue(jsonFile, new TypeReference<List<Activity>>() {});
      activities.addAll(list);
      System.out.println("Loaded activities count: " + activities.size());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }



  public void exportToJson() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    try {
      mapper.writeValue(jsonFile, activities);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void handleExportButton() {
    exportToJson();
  }

  @FXML
  public void handleAddButton() {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/org/example/clovervilleprogram/PointsPage/AddPoints.fxml")
      );

      Stage stage = new Stage();
      stage.setScene(new Scene(loader.load()));

      ActivityCreateController popupController = loader.getController();
      popupController.setPointsController(this);

      stage.setTitle("Add Green Activity");
      stage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void addActivity(Activity activity) {
    activities.add(activity);
  }
}

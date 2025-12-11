package org.example.clovervilleprogram.Users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserController {

  @FXML private TextField fullName;
  @FXML private DatePicker userAge;
  @FXML private TextField userGender;
  @FXML private TextField citizenID;
  @FXML private Label errorLabel;
  @FXML private TableView<User> userTable;

  @FXML private TableColumn<User, String> fullNameTable;
  @FXML private TableColumn<User, String> ageTable;
  @FXML private TableColumn<User, String> genderTable;
  @FXML private TableColumn<User, String> citizenIdTable;
  @FXML private TableColumn<User, Number> numberOfPeople;

  private final File jsonFile = new File("users.json");
  private final ObservableList<User> userList = FXCollections.observableArrayList();

  @FXML
  public void initialize() {

    fullNameTable.setCellValueFactory(data -> data.getValue().fullNameProperty());
    ageTable.setCellValueFactory(data -> data.getValue().ageProperty());
    genderTable.setCellValueFactory(data -> data.getValue().genderProperty());
    citizenIdTable.setCellValueFactory(data -> data.getValue().citizenIdProperty());
    numberOfPeople.setCellFactory(col -> new TableCell<>(){
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

    loadUsersFromJson();

    userTable.setItems(userList);
  }

  private void loadUsersFromJson() {
    ObjectMapper mapper = new ObjectMapper();
    if (jsonFile.exists()) {
      try {
        List<User> users = mapper.readValue(jsonFile, new TypeReference<List<User>>() {});
        userList.setAll(users);
        System.out.println("Loaded " + users.size() + " users from JSON.");
      } catch (IOException e) {
        e.printStackTrace();
        errorLabel.setVisible(true);
        errorLabel.setText("Failed to load users from JSON!");
        errorLabel.setStyle("-fx-text-fill: red;");
      }
    }
  }

  public void handleRandomizeButton() {
    citizenID.setText(String.valueOf((int)(Math.random() * 90000000 + 10000000)));
  }

  public void handleAddPerson() {
    if (fullName.getText().isEmpty() || userAge.getValue() == null ||
        userGender.getText().isEmpty() || citizenID.getText().isEmpty()) {
      errorLabel.setVisible(true);
      errorLabel.setText("You need to fill all fields!");
      errorLabel.setStyle("-fx-text-fill: red;");
      return;
    }

    User user = new User(
        fullName.getText(),
        userAge.getValue().toString(),
        userGender.getText(),
        citizenID.getText()
    );

    userList.add(user);

    errorLabel.setVisible(true);
    errorLabel.setText("User added successfully!");
    errorLabel.setStyle("-fx-text-fill: green;");

    handleResetFields();
  }

  public void handleResetFields() {
    fullName.clear();
    userAge.setValue(null);
    userGender.clear();
    citizenID.clear();
  }

  public void handleEditUser() {
    User selectedUser = userTable.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
      errorLabel.setVisible(true);
      errorLabel.setText("No user selected to edit!");
      errorLabel.setStyle("-fx-text-fill: red;");
      return;
    }

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/clovervilleprogram/UserRegister/EditUser.fxml"));
      Parent root = fxmlLoader.load();

      EditUserController controller = fxmlLoader.getController();
      controller.setUser(selectedUser);
      controller.setUserController(this);

      Stage stage = new Stage();
      stage.setTitle("Edit User");
      stage.setScene(new Scene(root));
      stage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void handleDeleteButton() {
    User selectedUser = userTable.getSelectionModel().getSelectedItem();
    if (selectedUser != null) {
      userList.remove(selectedUser);
    } else {
      errorLabel.setVisible(true);
      errorLabel.setText("No user selected to delete!");
      errorLabel.setStyle("-fx-text-fill: red;");
    }
  }

  public void exportToJson() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    try {
      mapper.writeValue(jsonFile, userList);
      System.out.println("Exported " + userList.size() + " users to " + jsonFile.getAbsolutePath());
      errorLabel.setVisible(true);
      errorLabel.setText("Exported users to JSON successfully!");
      errorLabel.setStyle("-fx-text-fill: green;");
    } catch (IOException e) {
      e.printStackTrace();
      errorLabel.setVisible(true);
      errorLabel.setText("Failed to export users!");
      errorLabel.setStyle("-fx-text-fill: red;");
    }
  }

  public void handleExportButton() {
    exportToJson();
  }

  public void refreshTable() {
    userTable.refresh();
  }
}

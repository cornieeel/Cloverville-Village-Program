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
  @FXML private ComboBox<String> userGender;
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

    userGender.getItems().addAll("Male", "Female", "Prefer not to specify");

    fullNameTable.setCellValueFactory(data -> data.getValue().fullNameProperty());
    ageTable.setCellValueFactory(data -> data.getValue().ageProperty());
    genderTable.setCellValueFactory(data -> data.getValue().genderProperty());
    citizenIdTable.setCellValueFactory(data -> data.getValue().citizenIdProperty());

    numberOfPeople.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : String.valueOf(getIndex() + 1));
      }
    });

    enforceCitizenIdNumbersOnly();
    autoCapitalizeFullName();

    loadUsersFromJson();
    userTable.setItems(userList);
  }



  private void enforceCitizenIdNumbersOnly() {
    citizenID.textProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        citizenID.setText(newValue.replaceAll("\\D", ""));
      }
    });
  }

  private void autoCapitalizeFullName() {
    fullName.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
      if (!isFocused) {
        fullName.setText(capitalizeName(fullName.getText()));
      }
    });
  }

  private String capitalizeName(String name) {
    String[] parts = name.trim().toLowerCase().split("\\s+");
    StringBuilder result = new StringBuilder();

    for (String part : parts) {
      if (!part.isEmpty()) {
        result.append(Character.toUpperCase(part.charAt(0)))
            .append(part.substring(1))
            .append(" ");
      }
    }
    return result.toString().trim();
  }

  private boolean isValidFullName(String name) {
    return name.matches("[A-Za-z]+\\s+[A-Za-z]+");
  }


  public void handleRandomizeButton() {
    citizenID.setText(String.valueOf((int)(Math.random() * 90000000 + 10000000)));
  }

  public void handleAddPerson() {

    if (fullName.getText().isEmpty() ||
        userAge.getValue() == null ||
        userGender.getValue() == null ||
        citizenID.getText().isEmpty()) {

      showError("You need to fill all fields!");
      return;
    }

    if (!isValidFullName(fullName.getText())) {
      showError("Please enter First and Last name (letters only).");
      return;
    }

    User user = new User(
        capitalizeName(fullName.getText()),
        userAge.getValue().toString(),
        userGender.getValue(),
        citizenID.getText()
    );

    userList.add(user);
    showSuccess("User added successfully!");
    handleResetFields();
  }

  public void handleResetFields() {
    fullName.clear();
    userAge.setValue(null);
    userGender.setValue(null);
    citizenID.clear();
  }

  public void handleEditUser() {
    User selectedUser = userTable.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
      showError("No user selected to edit!");
      return;
    }

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          getClass().getResource("/org/example/clovervilleprogram/UserRegister/EditUser.fxml")
      );
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
      showError("No user selected to delete!");
    }
  }

  public void handleExportButton() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);

    try {
      mapper.writeValue(jsonFile, userList);
      showSuccess("Exported users to JSON successfully!");
    } catch (IOException e) {
      e.printStackTrace();
      showError("Failed to export users!");
    }
  }

  public void refreshTable() {
    userTable.refresh();
  }


  private void loadUsersFromJson() {
    if (!jsonFile.exists()) return;

    ObjectMapper mapper = new ObjectMapper();
    try {
      List<User> users = mapper.readValue(jsonFile, new TypeReference<>() {});
      userList.setAll(users);
    } catch (IOException e) {
      e.printStackTrace();
      showError("Failed to load users from JSON!");
    }
  }

  private void showError(String msg) {
    errorLabel.setVisible(true);
    errorLabel.setText(msg);
    errorLabel.setStyle("-fx-text-fill: red;");
  }

  private void showSuccess(String msg) {
    errorLabel.setVisible(true);
    errorLabel.setText(msg);
    errorLabel.setStyle("-fx-text-fill: green;");
  }
}

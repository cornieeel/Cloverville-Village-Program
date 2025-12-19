package org.example.clovervilleprogram.Users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserController {

  // FXML components for input fields
  @FXML private TextField fullName;          // Input for full name
  @FXML private DatePicker userAge;          // Input for age (as DatePicker)
  @FXML private ComboBox<String> userGender; // Dropdown for gender selection
  @FXML private TextField citizenID;         // Input for citizen ID
  @FXML private Label errorLabel;            // Label to display errors
  @FXML private TableView<User> userTable;   // TableView to display users
  @FXML private TextField searchBar;         // Search bar to filter users
  @FXML private Label errorLabel1;           // Label to display success/errors for actions

  // Table columns for displaying user details
  @FXML private TableColumn<User, String> fullNameTable;
  @FXML private TableColumn<User, String> ageTable;
  @FXML private TableColumn<User, String> genderTable;
  @FXML private TableColumn<User, String> citizenIdTable;
  @FXML private TableColumn<User, Number> numberOfPeople; // Row numbers

  // File to store users and observable list for TableView
  private final File jsonFile = new File("users.json");
  private final ObservableList<User> userList = FXCollections.observableArrayList();

  /**
   * Initializes the controller.
   * Sets up gender options, table columns, input validation, loads users from JSON,
   * and configures the search/filter functionality for the table.
   */
  @FXML
  public void initialize() {

    // Populate gender dropdown
    userGender.getItems().addAll("Male", "Female", "Prefer not to specify");

    // Bind table columns to User properties
    fullNameTable.setCellValueFactory(data -> data.getValue().fullNameProperty());
    ageTable.setCellValueFactory(data -> data.getValue().ageProperty());
    genderTable.setCellValueFactory(data -> data.getValue().genderProperty());
    citizenIdTable.setCellValueFactory(data -> data.getValue().citizenIdProperty());

    // Set up row numbering for table
    numberOfPeople.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : String.valueOf(getIndex() + 1));
      }
    });

    // Enforce numeric-only input for citizen ID
    enforceCitizenIdNumbersOnly();

    // Auto-capitalize full name on focus loss
    autoCapitalizeFullName();

    // Load existing users from JSON file
    loadUsersFromJson();

    // Set up search/filter functionality for the table
    FilteredList<User> filteredData = new FilteredList<>(userList, p -> true);

    searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
      String filter = newVal.toLowerCase().trim();
      filteredData.setPredicate(user -> {
        if (filter.isEmpty()) return true;
        return user.getFullName().toLowerCase().contains(filter)
            || user.getAge().toLowerCase().contains(filter)
            || user.getGender().toLowerCase().contains(filter)
            || user.getCitizenId().toLowerCase().contains(filter);
      });
    });

    SortedList<User> sortedData = new SortedList<>(filteredData);
    sortedData.comparatorProperty().bind(userTable.comparatorProperty());
    userTable.setItems(sortedData);
  }

  // Enforce that citizen ID input contains only numbers
  private void enforceCitizenIdNumbersOnly() {
    citizenID.textProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) citizenID.setText(newValue.replaceAll("\\D", ""));
    });
  }

  // Automatically capitalize each word in full name when focus is lost
  private void autoCapitalizeFullName() {
    fullName.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
      if (!isFocused) fullName.setText(capitalizeName(fullName.getText()));
    });
  }

  // Capitalize each part of the name (first and last)
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

  // Validate full name contains only letters and has at least two words
  private boolean isValidFullName(String name) {
    return name.matches("[A-Za-z]+\\s+[A-Za-z]+");
  }

  // Randomize citizen ID
  public void handleRandomizeButton() {
    citizenID.setText(String.valueOf((int)(Math.random() * 90000000 + 10000000)));
  }

  // Add a new user to the table
  public void handleAddPerson() {
    if (fullName.getText().isEmpty() || userAge.getValue() == null
        || userGender.getValue() == null || citizenID.getText().isEmpty()) {
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

  // Reset all input fields and hide error label
  public void handleResetFields() {
    fullName.clear();
    userAge.setValue(null);
    userGender.setValue(null);
    citizenID.clear();
    errorLabel.setVisible(false);
  }

  // Open edit user window for selected user
  public void handleEditUser() {
    User selectedUser = userTable.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
      errorLabel1.setVisible(true);
      errorLabel1.setText("You must select an user!");
      errorLabel1.setStyle("-fx-text-fill: red");
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

  // Delete selected user from table
  public void handleDeleteButton() {
    User selectedUser = userTable.getSelectionModel().getSelectedItem();
    if (selectedUser != null) userList.remove(selectedUser);
    else {
      errorLabel1.setVisible(true);
      errorLabel1.setText("You must select an user!");
      errorLabel1.setStyle("-fx-text-fill: red");
    }
  }

  // Export users to JSON file
  public void handleExportButton() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    try {
      mapper.writeValue(jsonFile, userList);
      errorLabel1.setVisible(true);
      errorLabel1.setText("Users sent to JSON Successfully!");
      errorLabel1.setStyle("-fx-text-fill: green");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Refresh the table view
  public void refreshTable() {
    userTable.refresh();
  }

  // Load users from JSON file
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

  // Display error message in errorLabel
  private void showError(String msg) {
    errorLabel.setVisible(true);
    errorLabel.setText(msg);
    errorLabel.setStyle("-fx-text-fill: red;");
  }

  // Display success message in errorLabel
  private void showSuccess(String msg) {
    errorLabel.setVisible(true);
    errorLabel.setText(msg);
    errorLabel.setStyle("-fx-text-fill: green;");
  }

}

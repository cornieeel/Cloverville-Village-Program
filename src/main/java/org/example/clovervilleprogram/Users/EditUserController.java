package org.example.clovervilleprogram.Users;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for editing user information.
 * Allows updating full name, citizen ID, age, and gender.
 */
public class EditUserController {

  // The User object being edited
  private User user;

  // Reference to main UserController for refreshing table after edit
  private UserController userController;

  // UI components
  @FXML private TextField editCitizenID; // Field for citizen ID
  @FXML private TextField editFullName;  // Field for full name
  @FXML private DatePicker editAge;      // Field for age (as date)
  @FXML private TextField editGender;    // Field for gender
  @FXML private Button saveButton;       // Button to save changes
  @FXML private Button cancelButton;     // Button to cancel editing

  /**
   * Sets the user to be edited and populates the UI fields.
   *
   * @param user the User object to edit
   */
  public void setUser(User user) {
    this.user = user;

    editCitizenID.setText(user.getCitizenId());
    editFullName.setText(user.getFullName());
    editAge.setValue(java.time.LocalDate.parse(user.getAge()));
    editGender.setText(user.getGender());
  }

  /**
   * Sets the UserController for refreshing the table after edits.
   *
   * @param userController the main UserController
   */
  public void setUserController(UserController userController) {
    this.userController = userController;
  }

  /**
   * Handles the save button action.
   * Updates the User object with new values and refreshes the table.
   */
  @FXML
  public void handleSaveButton() {
    user.setFullName(editFullName.getText());
    user.setCitizenId(editCitizenID.getText());
    user.setGender(editGender.getText());
    user.setAge(editAge.getValue().toString());

    if (userController != null) {
      userController.refreshTable(); // Refresh the table in main controller
    }

    // Close the edit window
    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }

  /**
   * Handles the cancel button action.
   * Closes the edit window without saving changes.
   */
  @FXML
  public void handleCancelButton() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  /**
   * Handles the randomize button action.
   * Generates a random 8-digit number for the citizen ID.
   */
  @FXML
  public void handleRandomizeButton() {
    editCitizenID.setText(
        String.valueOf((int)(Math.random() * 90000000 + 10000000))
    );
  }

}

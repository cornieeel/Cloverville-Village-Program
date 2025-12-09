package org.example.clovervilleprogram.Users;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUserController {

  private User user;
  private UserController userController;

  @FXML private TextField editCitizenID;
  @FXML private Button saveButton;
  @FXML private Button cancelButton;
  @FXML private TextField editFullName;
  @FXML private DatePicker editAge;
  @FXML private TextField editGender;


  public void setUser(User user) {
    this.user = user;

    editCitizenID.setText(user.getCitizenId());
    editFullName.setText(user.getFullName());
    editAge.setValue(java.time.LocalDate.parse(user.getAge()));
    editGender.setText(user.getGender());
  }


  public void setUserController(UserController userController) {
    this.userController = userController;
  }

  @FXML
  public void handleSaveButton() {
    user.setFullName(editFullName.getText());
    user.setCitizenId(editCitizenID.getText());
    user.setGender(editGender.getText());
    user.setAge(editAge.getValue().toString());

    if (userController != null) {
      userController.refreshTable();
    }

    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void handleCancelButton() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void handleRandomizeButton() {
    editCitizenID.setText(
        String.valueOf((int)(Math.random() * 90000000 + 10000000))
    );
  }

}

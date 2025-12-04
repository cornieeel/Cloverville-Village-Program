package org.example.clovervilleprogram;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUserController
{
  private UserController userController;
  @FXML private TextField citizenID;
  @FXML private Button saveButton;
  @FXML private Button cancelButton;

  public void setUserController(UserController userController)
  {
    this.userController = userController;
  }
  public void handleSaveButton(){
    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }
  public void handleCancelButton(){
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }
  public void handleRandomizeButton(){
    citizenID.setText(
        String.valueOf((int)(Math.random() * 90000000 + 10000000)));
  }


}

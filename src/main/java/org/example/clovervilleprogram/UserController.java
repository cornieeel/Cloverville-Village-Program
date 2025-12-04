package org.example.clovervilleprogram;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserController
{


 @FXML private TextField fullName;
 @FXML private DatePicker userAge;
 @FXML private TextField userGender;
 @FXML private TextField citizenID;
 @FXML private Label errorLabel;

 public void handleRandomizeButton(){

    citizenID.setText(
        String.valueOf((int)(Math.random() * 90000000 + 10000000)));
 }
 public void handleAddPerson(){
   if(fullName.getText().isEmpty() ||
       userGender.getText().isEmpty() ||
       citizenID.getText().isEmpty() ||
        userAge.getValue() == null){

     errorLabel.setVisible(true);
     errorLabel.setText("You need to fill all fields!");
     errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10px;");
   } else {
     errorLabel.setText("User added successfully");
     errorLabel.setStyle("-fx-text-fill: green; -fx-font-size: 10px;");
   }
 }
 public void handleResetFields(){
    fullName.clear();
    userAge.setValue(null);
    userGender.clear();
    citizenID.clear();
 }
 public void handleEditUser(){
   try
   {
     FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserRegister/EditUser.fxml"));
     Parent root = fxmlLoader.load();

     EditUserController controller = fxmlLoader.getController();

     controller.setUserController(this);


     Stage stage = new Stage();
     stage.setTitle("Edit an User");
     stage.setScene(new Scene(root));


     stage.show();
   } catch (Exception e){
     e.printStackTrace();
   }
 }
}


package org.example.clovervilleprogram.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController
{
  @FXML private AnchorPane contentPane;


  @FXML
  private void showUserPage() {
    loadContent("/org/example/clovervilleprogram/UserRegister/UserRegister.fxml");
  }

  @FXML
  private void showPointsPage() {
    loadContent("/org/example/clovervilleprogram/PointsPage/PointsPage.fxml");
  }

  @FXML
  private void showTasksPage() {
    loadContent("/org/example/clovervilleprogram/TasksPage/IndividualTasks.fxml");
  }

  private void loadContent(String fxmlFile){
      try
      {
        Parent pane = FXMLLoader.load(getClass().getResource(fxmlFile));
        contentPane.getChildren().setAll(pane);

        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
      } catch (IOException e){
        e.printStackTrace();
      }
  }
}

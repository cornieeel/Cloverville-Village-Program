package org.example.clovervilleprogram;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddPointsController
{
  public void handleAddButton(){
    try
    {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PointsPage/AddPoints.fxml"));
      Parent root = fxmlLoader.load();

      Stage stage = new Stage();
      stage.setTitle("Create Activity");
      stage.setScene(new Scene(root));
      stage.show();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

  }
}

module org.example.clovervilleprogram {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.fasterxml.jackson.databind;


  exports org.example.clovervilleprogram.Users;
  opens org.example.clovervilleprogram.Users to javafx.fxml;
  exports org.example.clovervilleprogram.Points;
  opens org.example.clovervilleprogram.Points to javafx.fxml;
  exports org.example.clovervilleprogram.App;
  opens org.example.clovervilleprogram.App to javafx.fxml;
}
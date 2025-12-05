module org.example.clovervilleprogram {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.fasterxml.jackson.databind;

  opens org.example.clovervilleprogram to javafx.fxml;
  exports org.example.clovervilleprogram;
}
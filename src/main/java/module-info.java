module org.example.clovervilleprogram {
  requires javafx.controls;
  requires javafx.fxml;

  opens org.example.clovervilleprogram to javafx.fxml;
  exports org.example.clovervilleprogram;
}
package org.example.clovervilleprogram.IndividualTasks;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tasks {

  private final StringProperty individualActivity = new SimpleStringProperty();
  private final StringProperty residentId = new SimpleStringProperty();
  private final IntegerProperty pointsPerActivity = new SimpleIntegerProperty();
  private final StringProperty date = new SimpleStringProperty();

  public Tasks() {}

  public Tasks(String individualActivity, String residentId, int pointsPerActivity, String date) {
    this.individualActivity.set(individualActivity);
    this.residentId.set(residentId);
    this.pointsPerActivity.set(pointsPerActivity);
    this.date.set(date);
  }

  // ===== Getters (for JSON & logic) =====
  public String getIndividualActivity() { return individualActivity.get(); }
  public String getResidentId() { return residentId.get(); }
  public int getPointsPerActivity() { return pointsPerActivity.get(); }
  public String getDate() { return date.get(); }

  // ===== Setters =====
  public void setIndividualActivity(String individualActivity) { this.individualActivity.set(individualActivity); }
  public void setResidentId(String residentId) { this.residentId.set(residentId); }
  public void setPointsPerActivity(int pointsPerActivity) { this.pointsPerActivity.set(pointsPerActivity); }
  public void setDate(String date) { this.date.set(date); }

  // ===== JavaFX Properties =====
  public StringProperty individualActivityProperty() { return individualActivity; }
  public StringProperty residentIdProperty() { return residentId; }
  public IntegerProperty pointsPerActivityProperty() { return pointsPerActivity; }
  public StringProperty dateProperty() { return date; }
}

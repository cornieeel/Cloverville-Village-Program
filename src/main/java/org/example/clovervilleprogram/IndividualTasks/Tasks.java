package org.example.clovervilleprogram.IndividualTasks;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Tasks represents a single task assigned to a resident.
 * It uses JavaFX properties to support data binding with UI components.
 */
public class Tasks {


  // Name/description of the individual activity
  private final StringProperty individualActivity = new SimpleStringProperty();

  // Identifier (or name) of the resident assigned to the task
  private final StringProperty residentId = new SimpleStringProperty();

  // Points awarded for completing the activity
  private final IntegerProperty pointsPerActivity = new SimpleIntegerProperty();

  // Date associated with the task
  private final StringProperty date = new SimpleStringProperty();

  /**
   * No-argument constructor in order for json to work
   */
  public Tasks() {}

  /**
   * Constructor to initialize all task fields
   *
   * @param individualActivity the activity description
   * @param residentId the resident identifier
   * @param pointsPerActivity points awarded for the activity
   * @param date the date of the task
   */
  public Tasks(String individualActivity, String residentId, int pointsPerActivity, String date) {
    this.individualActivity.set(individualActivity);
    this.residentId.set(residentId);
    this.pointsPerActivity.set(pointsPerActivity);
    this.date.set(date);
  }

  // Getter methods for task fields
  public String getIndividualActivity() { return individualActivity.get(); }
  public String getResidentId() { return residentId.get(); }
  public int getPointsPerActivity() { return pointsPerActivity.get(); }
  public String getDate() { return date.get(); }

  // Setter methods for task fields
  public void setIndividualActivity(String individualActivity) { this.individualActivity.set(individualActivity); }
  public void setResidentId(String residentId) { this.residentId.set(residentId); }
  public void setPointsPerActivity(int pointsPerActivity) { this.pointsPerActivity.set(pointsPerActivity); }
  public void setDate(String date) { this.date.set(date); }

  // Property methods for JavaFX bindings
  public StringProperty individualActivityProperty() { return individualActivity; }
  public StringProperty residentIdProperty() { return residentId; }
  public IntegerProperty pointsPerActivityProperty() { return pointsPerActivity; }
  public StringProperty dateProperty() { return date; }
}

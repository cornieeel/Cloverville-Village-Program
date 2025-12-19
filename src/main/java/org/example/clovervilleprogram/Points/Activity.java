package org.example.clovervilleprogram.Points;

import com.fasterxml.jackson.annotation.JsonInclude;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Activity represents a points-related activity performed by a resident.
 * Uses JavaFX properties for UI data binding and Jackson annotations for JSON handling.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Activity {

  // Resident identifier (or name) associated with the activity
  private final StringProperty residentId;

  // Name or description of the activity
  private final StringProperty activity;

  // Points awarded for the activity
  private final IntegerProperty pointsPerActivity;

  // Date when the activity was performed
  private final StringProperty date;

  /**
   * No-argument constructor
   * Initializes fields with default empty values
   */
  public Activity() {
    this("", "", 0, "");
  }

  /**
   * Full constructor initializing all activity fields
   *
   * @param residentId the resident identifier
   * @param activity the activity name
   * @param pointsPerActivity points awarded
   * @param date the activity date
   */
  public Activity(String residentId, String activity, int pointsPerActivity, String date) {
    this.residentId = new SimpleStringProperty(residentId);
    this.activity = new SimpleStringProperty(activity);
    this.pointsPerActivity = new SimpleIntegerProperty(pointsPerActivity);
    this.date = new SimpleStringProperty(date);
  }

  /**
   * Constructor used when only activity name and points are needed
   *
   * @param activityName the activity name
   * @param pointsPerActivity points awarded
   */
  public Activity(String activityName, int pointsPerActivity) {
    this.activity = new SimpleStringProperty(activityName);
    this.pointsPerActivity = new SimpleIntegerProperty(pointsPerActivity);
    this.residentId = new SimpleStringProperty();
    this.date = new SimpleStringProperty();
  }

  // Getter, setter, and property methods for residentId
  public String getResidentId() { return residentId.get(); }
  public void setResidentId(String residentId) { this.residentId.set(residentId); }
  public StringProperty residentIdProperty() { return residentId; }

  // Getter, setter, and property methods for activity
  public String getActivity() { return activity.get(); }
  public void setActivity(String activity) { this.activity.set(activity); }
  public StringProperty activityProperty() { return activity; }

  // Getter, setter, and property methods for pointsPerActivity
  public int getPointsPerActivity() { return pointsPerActivity.get(); }
  public void setPointsPerActivity(int pointsPerActivity) { this.pointsPerActivity.set(pointsPerActivity); }
  public IntegerProperty pointsPerActivityProperty() { return pointsPerActivity; }

  // Getter, setter, and property methods for date
  public String getDate() { return date.get(); }
  public void setDate(String date) { this.date.set(date); }
  public StringProperty dateProperty() { return date; }
}

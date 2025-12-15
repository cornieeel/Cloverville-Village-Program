package org.example.clovervilleprogram.Points;

import com.fasterxml.jackson.annotation.JsonInclude;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Activity {

  private final StringProperty residentId;
  private final StringProperty activity;
  private final IntegerProperty pointsPerActivity;
  private final StringProperty date;

  public Activity() {
    this("", "", 0, "");
  }

  public Activity(String residentId, String activity, int pointsPerActivity, String date) {
    this.residentId = new SimpleStringProperty(residentId);
    this.activity = new SimpleStringProperty(activity);
    this.pointsPerActivity = new SimpleIntegerProperty(pointsPerActivity);
    this.date = new SimpleStringProperty(date);
  }
  public Activity(String activityName, int pointsPerActivity) {
    this.activity = new SimpleStringProperty(activityName);
    this.pointsPerActivity = new SimpleIntegerProperty(pointsPerActivity);
    this.residentId = new SimpleStringProperty();
    this.date = new SimpleStringProperty();

  }


  public String getResidentId() { return residentId.get(); }
  public void setResidentId(String residentId) { this.residentId.set(residentId); }
  public StringProperty residentIdProperty() { return residentId; }


  public String getActivity() { return activity.get(); }
  public void setActivity(String activity) { this.activity.set(activity); }
  public StringProperty activityProperty() { return activity; }


  public int getPointsPerActivity() { return pointsPerActivity.get(); }
  public void setPointsPerActivity(int pointsPerActivity) { this.pointsPerActivity.set(pointsPerActivity); }
  public IntegerProperty pointsPerActivityProperty() { return pointsPerActivity; }


  public String getDate() { return date.get(); }
  public void setDate(String date) { this.date.set(date); }
  public StringProperty dateProperty() { return date; }
}
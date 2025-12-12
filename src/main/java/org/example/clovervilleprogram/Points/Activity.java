package org.example.clovervilleprogram.Points;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Activity {

  private final StringProperty residentId;
  private final StringProperty activity;
  private final StringProperty pointsPerActivity;
  private final StringProperty date;

  public Activity() {
    this("", "", "", "");
  }

  public Activity(String residentId, String activity, String pointsPerActivity, String date) {
    this.residentId = new SimpleStringProperty(residentId);
    this.activity = new SimpleStringProperty(activity);
    this.pointsPerActivity = new SimpleStringProperty(pointsPerActivity);
    this.date = new SimpleStringProperty(date);
  }


  public String getResidentId() { return residentId.get(); }
  public void setResidentId(String residentId) { this.residentId.set(residentId); }
  public StringProperty residentIdProperty() { return residentId; }


  public String getActivity() { return activity.get(); }
  public void setActivity(String activity) { this.activity.set(activity); }
  public StringProperty activityProperty() { return activity; }


  public String getPointsPerActivity() { return pointsPerActivity.get(); }
  public void setPointsPerActivity(String pointsPerActivity) { this.pointsPerActivity.set(pointsPerActivity); }
  public StringProperty pointsPerActivityProperty() { return pointsPerActivity; }


  public String getDate() { return date.get(); }
  public void setDate(String date) { this.date.set(date); }
  public StringProperty dateProperty() { return date; }
}

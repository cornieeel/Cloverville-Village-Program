package org.example.clovervilleprogram.Points;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Activity {

  private String activity; // plain field for Jackson
  private String pointsPerActivity; // plain field for Jackson

  private final StringProperty activityProperty;
  private final StringProperty pointsPerActivityProperty;

  public Activity() {
    this.activity = "";
    this.pointsPerActivity = "";
    this.activityProperty = new SimpleStringProperty(activity);
    this.pointsPerActivityProperty = new SimpleStringProperty(pointsPerActivity);
  }

  public Activity(String activity, String pointsPerActivity) {
    this.activity = activity;
    this.pointsPerActivity = pointsPerActivity;
    this.activityProperty = new SimpleStringProperty(activity);
    this.pointsPerActivityProperty = new SimpleStringProperty(pointsPerActivity);
  }

  // Getters/setters for Jackson
  public String getActivity() {
    return activity;
  }

  public void setActivity(String activity) {
    this.activity = activity;
    this.activityProperty.set(activity);
  }

  public String getPointsPerActivity() {
    return pointsPerActivity;
  }

  public void setPointsPerActivity(String pointsPerActivity) {
    this.pointsPerActivity = pointsPerActivity;
    this.pointsPerActivityProperty.set(pointsPerActivity);
  }

  // JavaFX properties for TableView
  public StringProperty activityProperty() {
    return activityProperty;
  }

  public StringProperty pointsPerActivityProperty() {
    return pointsPerActivityProperty;
  }
}

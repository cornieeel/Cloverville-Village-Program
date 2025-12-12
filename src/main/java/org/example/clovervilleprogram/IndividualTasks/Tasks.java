package org.example.clovervilleprogram.IndividualTasks;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.clovervilleprogram.Users.User;

import java.time.LocalDate;

public class Tasks
{
  private String activity;
  private String residentId;
  private int pointsPerActivity;
  private String date;



  public Tasks(){};

  public Tasks(String activity, String residentId, int pointsPerActivity, String date){
    this.activity = activity;
    this.residentId = residentId;
    this.pointsPerActivity = pointsPerActivity;
    this.date = date;
  }

  public String getIndividualActivity()
  {
    return activity;
  }

  public String getDate()
  {
    return date;
  }

  public int getPointsPerActivity()
  {
    return pointsPerActivity;
  }

  public String getResidentId()
  {
    return residentId;
  }
  public void setIndividualActivity(String activity){
    this.activity = activity;
  }
  public void setResidentId(String residentId){
    this.residentId = residentId;
  }
  public void setPointsPerActivity(int pointsPerActivity){
    this.pointsPerActivity = pointsPerActivity;
  }
  public void setDate(String date){
    this.date = date;
  }

  public StringProperty activityProperty(){return new SimpleStringProperty(activity);}
  public StringProperty residentIdProperty(){return new SimpleStringProperty(residentId);}
  public IntegerProperty pointsPerActivityProperty(){return new SimpleIntegerProperty(pointsPerActivity);}
  public StringProperty dateProperty(){return new SimpleStringProperty(date);}
}

package org.example.clovervilleprogram;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

  private String fullName;
  private String age;
  private String gender;
  private String citizenId;

  public User() {}

  public User(String fullName, String age, String gender, String citizenId) {
    this.fullName = fullName;
    this.age = age;
    this.gender = gender;
    this.citizenId = citizenId;
  }


  public String getFullName() { return fullName; }
  public void setFullName(String fullName) { this.fullName = fullName; }

  public String getAge() { return age; }
  public void setAge(String age) { this.age = age; }

  public String getGender() { return gender; }
  public void setGender(String gender) { this.gender = gender; }

  public String getCitizenId() { return citizenId; }
  public void setCitizenId(String citizenId) { this.citizenId = citizenId; }


  public StringProperty fullNameProperty() { return new SimpleStringProperty(fullName); }
  public StringProperty ageProperty() { return new SimpleStringProperty(age); }
  public StringProperty genderProperty() { return new SimpleStringProperty(gender); }
  public StringProperty citizenIdProperty() { return new SimpleStringProperty(citizenId); }
}

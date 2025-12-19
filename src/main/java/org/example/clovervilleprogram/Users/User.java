package org.example.clovervilleprogram.Users;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a user (resident) in the system.
 * Contains basic user details like full name, age, gender, and citizen ID.
 */
public class User {

  // Fields for storing user information
  private String fullName;    // User's full name
  private String age;         // User's age (as String, usually a date)
  private String gender;      // User's gender
  private String citizenId;   // Unique citizen ID

  /**
   * Default constructor for creating an empty user.
   */
  public User() {}

  /**
   * Constructor to initialize a user with all fields.
   *
   * @param fullName   Full name of the user
   * @param age        Age of the user (as String)
   * @param gender     Gender of the user
   * @param citizenId  Unique citizen ID
   */
  public User(String fullName, String age, String gender, String citizenId) {
    this.fullName = fullName;
    this.age = age;
    this.gender = gender;
    this.citizenId = citizenId;
  }

  // Getter and setter methods for all fields

  public String getFullName() { return fullName; }
  public void setFullName(String fullName) { this.fullName = fullName; }

  public String getAge() { return age; }
  public void setAge(String age) { this.age = age; }

  public String getGender() { return gender; }
  public void setGender(String gender) { this.gender = gender; }

  public String getCitizenId() { return citizenId; }
  public void setCitizenId(String citizenId) { this.citizenId = citizenId; }

  /**
   * Property getters for JavaFX TableView bindings.
   * These return StringProperty objects so that TableView can bind to them directly.
   */

  public StringProperty fullNameProperty() { return new SimpleStringProperty(fullName); }
  public StringProperty ageProperty() { return new SimpleStringProperty(age); }
  public StringProperty genderProperty() { return new SimpleStringProperty(gender); }
  public StringProperty citizenIdProperty() { return new SimpleStringProperty(citizenId); }

}

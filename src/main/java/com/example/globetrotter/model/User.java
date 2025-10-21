package com.example.globetrotter.model;

public class User {
    // User table fields
    private int UserID;
    private String UserType;
    private String Username; // ADDED
    private String FirstName;
    private String LastName;
    private String Email;
    private String Password;

    // Constructor without UserID
    public User(String UserType, String Username, String FirstName, String LastName, String Email, String Password) {
        this.UserType = UserType;
        this.Username = Username; // ADDED
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
        this.Password = Password;
    }

    // Constructor with UserID
    public User(int UserID, String UserType, String Username, String FirstName, String LastName, String Email, String Password) {
        this.UserID = UserID;
        this.UserType = UserType;
        this.Username = Username; // ADDED
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Email = Email;
        this.Password = Password;
    }

    public int getUserID() {
        return UserID;
    }

    // Getters and setters
    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String UserType) {
        this.UserType = UserType;
    }

    public String getUsername() {return Username; } // Added

    public void setUsername(String Username) {this.Username = Username; } // Added

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getFullName() {
        return FirstName + " " + LastName;
    }

    @Override
    public String toString() {
        return getFullName() + " (" + Email + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return UserID == user.UserID;
    }
}
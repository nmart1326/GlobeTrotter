package com.example.globetrotter.util;

import com.example.globetrotter.model.User;

// utility to store and retrieve the currently logged-in user - keeps track of session info
public class UserSession {

    private static UserSession instance;
    private User currentUser;

    private UserSession() {}

    // Access singleton instance
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Store the logged-in user
    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) {
            System.out.println("UserSession: Logged in as " + user.getUsername() +
                    " (UserID=" + user.getUserID() + ")");
        } else {
            System.out.println("UserSession: Cleared user session.");
        }
    }

    // Retrieve the logged-in user
    public User getUser() {
        return currentUser;
    }

    // Retrieve the logged-in user's ID safely
    public int getUserId() {
        return (currentUser != null) ? currentUser.getUserID() : -1;
    }

    // Retrieve the username (optional helper)
    public String getUsername() {
        return (currentUser != null) ? currentUser.getUsername() : "Guest";
    }

    // Clear the session (logout)
    public void clear() {
        currentUser = null;
        System.out.println("UserSession: Logged out.");
    }
}

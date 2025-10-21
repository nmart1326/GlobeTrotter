package com.example.globetrotter.model;

import java.util.*;

// MOCK!!! Doesn't use the database!!!
public class MockUserDAO implements IUserDAO {

    private Map<Integer, User> users;
    private int nextId;

    public MockUserDAO() {
        users = new HashMap<>();
        nextId = 1;
    }

    // Add
    @Override
    public void addUser(User user) {
        // Check entry is not null
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Check email is not already in use
        if (getUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        // Give user an ID
        if (user.getUserID() == 0) {
            user.setUserID(nextId++);
        } else {
            if (user.getUserID() >= nextId) {
                nextId = user.getUserID() + 1;
            }
        }

        users.put(user.getUserID(), user);
    }

    // Update
    @Override
    public void updateUser(User user) {
        // Check entry is not null
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Check user entry exists
        if (!users.containsKey(user.getUserID())) {
            throw new IllegalArgumentException("User with ID " + user.getUserID() + " does not exist");
        }

        // Check email is not already in use (without including the current user)
        User existingEmailUser = getUserByEmail(user.getEmail());
        if (existingEmailUser != null && existingEmailUser.getUserID() != user.getUserID()) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        users.put(user.getUserID(), user);
    }

    // Delete
    @Override
    public void deleteUser(User user) {
        // Check entry is not null
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Check user entry exists
        if (!users.containsKey(user.getUserID())) {
            throw new IllegalArgumentException("User with ID " + user.getUserID() + " does not exist");
        }

        users.remove(user.getUserID());
    }

    // Get (ID)
    @Override
    public User getUser(int userID) {
        return users.get(userID);
    }

    // List
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // Get (email)
    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    // Count
    public int getUserCount() {
        return users.size();
    }

    public boolean userExists(int userID) {
        return users.containsKey(userID);
    }

    public User getUserByUsername(String username) {
        if (username == null) return null;
        String lower = username.toLowerCase();
        for (User user : users.values()) {
            if (user.getFirstName().equalsIgnoreCase(lower) || user.getLastName().equalsIgnoreCase(lower)) {
                return user;
            }
        }
        return null;
    }
}
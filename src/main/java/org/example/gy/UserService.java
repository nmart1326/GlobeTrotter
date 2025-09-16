package org.example.gy;

import java.util.List;
import java.util.regex.Pattern;

public class UserService {

    private final IUserDAO userDAO;
    // Email validation tool
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    // Password validation tools
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 100;
    // REMEMBER TO DO MORE PASSWORD VALIDATION!!!!!
    private static final int MAX_NAME_LENGTH = 50;

    public UserService(IUserDAO userDAO) {
        if (userDAO == null) {
            throw new IllegalArgumentException("UserDAO cannot be null");
        }
        this.userDAO = userDAO;
    }

    // Signup
    public User createUser(String userType, String firstName, String lastName,
                           String email, String password) throws UserValidationException {

        validateUserInput(userType, firstName, lastName, email, password);

        // Check email is not already in use
        if (isEmailTaken(email)) {
            throw new UserValidationException("Email already exists: " + email);
        }

        User user = new User(userType, firstName.trim(), lastName.trim(),
                email.trim().toLowerCase(), password);

        userDAO.addUser(user);
        return user;
    }

    // Login
    public User authenticateUser(String email, String password) throws AuthenticationException {
        if (email == null || email.trim().isEmpty()) {
            throw new AuthenticationException("Email is required");
        }

        if (password == null || password.isEmpty()) {
            throw new AuthenticationException("Password is required");
        }

        // Get (email)
        User user = null;
        if (userDAO instanceof SqliteUserDAO) {
            user = ((SqliteUserDAO) userDAO).getUserByEmail(email.trim().toLowerCase());
        } else if (userDAO instanceof MockUserDAO) {
            user = ((MockUserDAO) userDAO).getUserByEmail(email.trim().toLowerCase());
        }

        if (user == null) {
            throw new AuthenticationException("Invalid email or password");
        }

        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid email or password");
        }

        return user;
    }

    // Update profile
    public void updateUser(User user, String userType, String firstName, String lastName,
                           String email, String password) throws UserValidationException {

        if (user == null) {
            throw new UserValidationException("User cannot be null");
        }

        validateUserInput(userType, firstName, lastName, email, password);

        // Check email is not already in use
        String normalizedEmail = email.trim().toLowerCase();
        if (!user.getEmail().equalsIgnoreCase(normalizedEmail) && isEmailTaken(normalizedEmail)) {
            throw new UserValidationException("Email already exists: " + email);
        }

        user.setUserType(userType);
        user.setFirstName(firstName.trim());
        user.setLastName(lastName.trim());
        user.setEmail(normalizedEmail);
        user.setPassword(password);

        userDAO.updateUser(user);
    }

    // Delete account
    public void deleteUser(User user) throws UserValidationException {
        if (user == null) {
            throw new UserValidationException("User cannot be null");
        }

        if (user.getUserID() <= 0) {
            throw new UserValidationException("Invalid user ID");
        }

        userDAO.deleteUser(user);
    }

    // Get (ID)
    public User getUserById(int userId) {
        if (userId <= 0) {
            return null;
        }
        return userDAO.getUser(userId);
    }

    // List
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // Check email is not already in use
    public boolean isEmailTaken(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String normalizedEmail = email.trim().toLowerCase();

        if (userDAO instanceof SqliteUserDAO) {
            return ((SqliteUserDAO) userDAO).getUserByEmail(normalizedEmail) != null;
        } else if (userDAO instanceof MockUserDAO) {
            return ((MockUserDAO) userDAO).getUserByEmail(normalizedEmail) != null;
        }

        return false;
    }

    // Email validation
    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    // Password validation
    public boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= MIN_PASSWORD_LENGTH &&
                password.length() <= MAX_PASSWORD_LENGTH;
    }

    // Input validation
    private void validateUserInput(String userType, String firstName, String lastName,
                                   String email, String password) throws UserValidationException {

        // User type validation
        if (userType == null || userType.trim().isEmpty()) {
            throw new UserValidationException("User type is required");
        }

        if (!"Teacher".equals(userType) && !"Student".equals(userType)) {
            throw new UserValidationException("User type must be either 'Teacher' or 'Student'");
        }

        // Firstname validation
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new UserValidationException("First name is required");
        }

        if (firstName.trim().length() > MAX_NAME_LENGTH) {
            throw new UserValidationException("First name must be " + MAX_NAME_LENGTH + " characters or less");
        }

        // Lastname validation
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new UserValidationException("Last name is required");
        }

        if (lastName.trim().length() > MAX_NAME_LENGTH) {
            throw new UserValidationException("Last name must be " + MAX_NAME_LENGTH + " characters or less");
        }

        // Email validation
        if (email == null || email.trim().isEmpty()) {
            throw new UserValidationException("Email is required");
        }

        if (!isValidEmail(email)) {
            throw new UserValidationException("Please enter a valid email address");
        }

        // Password validation
        if (password == null || password.isEmpty()) {
            throw new UserValidationException("Password is required");
        }

        if (!isValidPassword(password)) {
            throw new UserValidationException("Password must be between " + MIN_PASSWORD_LENGTH +
                    " and " + MAX_PASSWORD_LENGTH + " characters long");
        }
    }
}
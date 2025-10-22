package com.example.globetrotter.model;

import com.example.globetrotter.util.SqliteConnection;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteUserDAO implements IUserDAO {
    private Connection connection;

    public SqliteUserDAO() {
        connection = SqliteConnection.getInstance();

        // Check if username exists
        try (Statement stmt = connection.createStatement()) {
            boolean hasUsername = false;
            try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(users);")) {
                while (rs.next()) {
                    String colName = rs.getString("name");
                    if ("Username".equalsIgnoreCase(colName)) {
                        hasUsername = true;
                        break;
                    }
                }
            }
            if (!hasUsername) {
                System.out.println("[SqliteUserDAO] Adding missing 'Username' column to users table...");
                stmt.execute("ALTER TABLE users ADD COLUMN Username TEXT;");
            }
            System.out.println("[SqliteUserDAO] Database verified successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[SqliteUserDAO] Database verification failed: " + e.getMessage());
        }


        createTable();
        insertSampleData();
    }

    // Sample data insertion, comment out when applicable
    private void insertSampleData() {
        try {
            Statement checkStatement = connection.createStatement();
            ResultSet rs = checkStatement.executeQuery("SELECT COUNT(*) as count FROM users");
            if (rs.next() && rs.getInt("count") > 0) {
                System.out.println("Sample data already exists, skipping insertion.");
                return;
            }
            //Fraser - added include username in insert query
            String insertQuery = "INSERT INTO users (UserType, Username, FirstName, LastName, Email, Password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertQuery);

            pstmt.setString(1, "Student");
            pstmt.setString(2, "zoe123"); // ADDED username
            pstmt.setString(3, "Zoe");
            pstmt.setString(4, "Alloway");
            pstmt.setString(5, "zalloway@example.com");
            pstmt.setString(6, "Password123");
            pstmt.addBatch();

            pstmt.setString(1, "Teacher");
            pstmt.setString(2, "madisonP"); // ADDED username
            pstmt.setString(3, "Madison");
            pstmt.setString(4, "Paranihi");
            pstmt.setString(5, "mparanihi@example.com");
            pstmt.setString(6, "VerySecure@3");
            pstmt.addBatch();

            pstmt.setString(1, "Student");
            pstmt.setString(2, "toivoB"); // ADDED username
            pstmt.setString(3, "Toivo");
            pstmt.setString(4, "Blanc");
            pstmt.setString(5, "tblanc@example.com");
            pstmt.setString(6, "ILoveDogs769");
            pstmt.addBatch();

            pstmt.executeBatch();
            System.out.println("Sample data inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Table creation
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS users ("
                    + "UserID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "UserType VARCHAR NOT NULL,"
                    + "Username VARCHAR NOT NULL," //Added by Fraser - get username login working
                    + "FirstName VARCHAR NOT NULL,"
                    + "LastName VARCHAR NOT NULL,"
                    + "Email VARCHAR NOT NULL,"
                    + "Password VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
            System.out.println("Users table created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add
    @Override
    public void addUser(User user) {
        // Added username and corrected parameter indexes
        String query = "INSERT INTO users (UserType, Username, FirstName, LastName, Email, Password) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUserType());
            pstmt.setString(2, user.getUsername());  // ADDED
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getPassword());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setUserID(generatedKeys.getInt(1));
                }
                System.out.println("User added successfully with ID: " + user.getUserID());
            }
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update
    @Override
    public void updateUser(User user) {
        //Fraser - added Username =? and corrected parameter indexes
        String query = "UPDATE users SET UserType = ?, Username =?, FirstName = ?, LastName = ?, Email = ?, Password = ? WHERE UserID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, user.getUserType());
            pstmt.setString(2, user.getUsername()); // ADDED
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getPassword());
            pstmt.setInt(7, user.getUserID());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("No user found with ID: " + user.getUserID());
            }
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete
    @Override
    public void deleteUser(User user) {
        String query = "DELETE FROM users WHERE UserID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user.getUserID());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("No user found with ID: " + user.getUserID());
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get (ID)
    @Override
    public User getUser(int UserID) {
        String query = "SELECT * FROM users WHERE UserID = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, UserID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("UserType"),
                        rs.getString("Username"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get (email)
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE Email = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("UserType"),
                        rs.getString("Username"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE lower(Username)=?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username.toLowerCase());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("UserType"),
                        rs.getString("Username"), // ADDED
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;

    }
    // List
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users ORDER BY UserID";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                User user = new User(
                        rs.getInt("UserID"),
                        rs.getString("UserType"),
                        rs.getString("Username"), // ADDED
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Password")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
}
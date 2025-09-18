import com.example.globetrotter.util.SqliteConnection;
import com.example.globetrotter.model.SqliteUserDAO;
import com.example.globetrotter.model.User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqliteUserDAOTest {

    private SqliteUserDAO userDAO;
    private Connection connection;

    @BeforeAll
    void setUpDatabase() {
        System.setProperty("test.db.url", "jdbc:sqlite:test_users.db");
        userDAO = new SqliteUserDAO();
        connection = SqliteConnection.getInstance();
    }

    @BeforeEach
    void clearDatabase() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM users");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='users'");
        } catch (Exception e) {
            fail("Failed to clear database: " + e.getMessage());
        }
    }

    @AfterAll
    void cleanUpDatabase() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS users");
            SqliteConnection.closeConnection();
        } catch (Exception e) {
            System.err.println("Failed to clean up test database: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should add user and assign ID correctly")
    void testAddUser_ShouldAddUserAndAssignId() {
        User user = new User("Student", "John", "Doe", "john@test.com", "password123");

        userDAO.addUser(user);

        assertTrue(user.getUserID() > 0);

        User retrieved = userDAO.getUser(user.getUserID());
        assertNotNull(retrieved);
        assertEquals("john@test.com", retrieved.getEmail());
        assertEquals("John", retrieved.getFirstName());
    }

    @Test
    @DisplayName("Should update user correctly")
    void testUpdateUser_ShouldUpdateUserCorrectly() {
        User user = new User("Student", "John", "Doe", "john@test.com", "password123");
        userDAO.addUser(user);

        user.setFirstName("Johnny");
        user.setEmail("johnny@updated.com");
        userDAO.updateUser(user);

        User updated = userDAO.getUser(user.getUserID());
        assertEquals("Johnny", updated.getFirstName());
        assertEquals("johnny@updated.com", updated.getEmail());
    }

    @Test
    @DisplayName("Should delete user correctly")
    void testDeleteUser_ShouldDeleteUserCorrectly() {
        User user = new User("Student", "John", "Doe", "john@test.com", "password123");
        userDAO.addUser(user);
        int userId = user.getUserID();

        userDAO.deleteUser(user);

        User deleted = userDAO.getUser(userId);
        assertNull(deleted);
    }

    @Test
    @DisplayName("Should find user by email correctly")
    void testGetUserByEmail_ShouldFindUserCorrectly() {
        User user = new User("Teacher", "Jane", "Smith", "jane@test.com", "password456");
        userDAO.addUser(user);

        User found = userDAO.getUserByEmail("jane@test.com");

        assertNotNull(found);
        assertEquals("Jane", found.getFirstName());
        assertEquals("Teacher", found.getUserType());
    }

    @Test
    @DisplayName("Should return null for non-existent email")
    void testGetUserByEmail_ShouldReturnNullForNonExistentEmail() {
        User result = userDAO.getUserByEmail("nonexistent@test.com");

        assertNull(result);
    }

    @Test
    @DisplayName("Should get all users correctly")
    void testGetAllUsers_ShouldReturnAllUsers() {
        User user1 = new User("Student", "John", "Doe", "john@test.com", "password123");
        User user2 = new User("Teacher", "Jane", "Smith", "jane@test.com", "password456");

        userDAO.addUser(user1);
        userDAO.addUser(user2);

        List<User> allUsers = userDAO.getAllUsers();

        assertEquals(2, allUsers.size());

        boolean johnFound = allUsers.stream().anyMatch(u -> "john@test.com".equals(u.getEmail()));
        boolean janeFound = allUsers.stream().anyMatch(u -> "jane@test.com".equals(u.getEmail()));

        assertTrue(johnFound);
        assertTrue(janeFound);
    }

    @Test
    @DisplayName("Should handle database constraints properly")
    void testDatabaseConstraints() {
        User user1 = new User("Student", "John", "Doe", "john@test.com", "password123");
        User user2 = new User("Teacher", "Jane", "Smith", "john@test.com", "password456");

        userDAO.addUser(user1);

        assertDoesNotThrow(() -> userDAO.addUser(user2));
    }
}
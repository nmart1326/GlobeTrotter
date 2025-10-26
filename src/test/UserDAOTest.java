import com.example.globetrotter.model.MockUserDAO;
import com.example.globetrotter.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class UserDAOTest {

    private MockUserDAO userDAO;
    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        userDAO = new MockUserDAO();
        testUser1 = new User("Student", "username", "John", "Doe", "john@test.com", "password123");
        testUser2 = new User("Teacher", "janeuser", "Jane", "Smith", "jane@test.com", "securepass");
    }

    @Test
    void testAddUser_ShouldAssignIdAndStoreUser() {
        userDAO.addUser(testUser1);

        assertEquals(1, testUser1.getUserID());
        assertEquals(1, userDAO.getUserCount());
        assertTrue(userDAO.userExists(1));

        User retrieved = userDAO.getUser(1);
        assertEquals("john@test.com", retrieved.getEmail());
        assertEquals("John", retrieved.getFirstName());
    }

    @Test
    void testAddUser_ShouldIncrementIds() {
        userDAO.addUser(testUser1);
        userDAO.addUser(testUser2);

        assertEquals(1, testUser1.getUserID());
        assertEquals(2, testUser2.getUserID());
        assertEquals(2, userDAO.getUserCount());
    }

    @Test
    void testAddUser_ShouldThrowExceptionForNullUser() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userDAO.addUser(null)
        );
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void testAddUser_ShouldThrowExceptionForDuplicateEmail() {
        userDAO.addUser(testUser1);
        User duplicateEmailUser = new User("Teacher", "teacheruser", "Different", "Name", "john@test.com", "differentpass");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userDAO.addUser(duplicateEmailUser)
        );
        assertEquals("Email already exists: john@test.com", exception.getMessage());
    }

    @Test
    void testGetUser_ShouldReturnCorrectUser() {
        userDAO.addUser(testUser1);
        userDAO.addUser(testUser2);

        User retrieved1 = userDAO.getUser(1);
        User retrieved2 = userDAO.getUser(2);

        assertNotNull(retrieved1);
        assertNotNull(retrieved2);
        assertEquals("john@test.com", retrieved1.getEmail());
        assertEquals("jane@test.com", retrieved2.getEmail());
    }

    @Test
    void testGetUser_ShouldReturnNullForNonExistentUser() {
        User result = userDAO.getUser(999);

        assertNull(result);
    }

    @Test
    void testGetUserByEmail_ShouldReturnCorrectUser() {
        userDAO.addUser(testUser1);
        userDAO.addUser(testUser2);

        User found = userDAO.getUserByEmail("jane@test.com");

        assertNotNull(found);
        assertEquals("Jane", found.getFirstName());
        assertEquals("Teacher", found.getUserType());
    }

    @Test
    void testGetUserByEmail_ShouldReturnNullForNonExistentEmail() {
        userDAO.addUser(testUser1);

        User result = userDAO.getUserByEmail("nonexistent@test.com");

        assertNull(result);
    }

    @Test
    void testGetUserByEmail_ShouldReturnNullForNullEmail() {
        User result = userDAO.getUserByEmail(null);

        assertNull(result);
    }

    @Test
    void testGetUserByEmail_ShouldReturnNullForEmptyEmail() {
        User result = userDAO.getUserByEmail("");

        assertNull(result);
    }

    @Test
    void testUpdateUser_ShouldModifyExistingUser() {
        userDAO.addUser(testUser1);
        testUser1.setFirstName("Johnny");
        testUser1.setEmail("johnny@updated.com");

        userDAO.updateUser(testUser1);

        User updated = userDAO.getUser(testUser1.getUserID());
        assertEquals("Johnny", updated.getFirstName());
        assertEquals("johnny@updated.com", updated.getEmail());
    }

    @Test
    void testUpdateUser_ShouldThrowExceptionForNullUser() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userDAO.updateUser(null)
        );
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void testUpdateUser_ShouldThrowExceptionForDuplicateEmail() {
        userDAO.addUser(testUser1);
        userDAO.addUser(testUser2);
        testUser2.setEmail("john@test.com");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userDAO.updateUser(testUser2)
        );
        assertEquals("Email already exists: john@test.com", exception.getMessage());
    }

    @Test
    void testUpdateUser_ShouldAllowSameEmailForSameUser() {
        userDAO.addUser(testUser1);
        testUser1.setFirstName("Updated Name");

        assertDoesNotThrow(() -> userDAO.updateUser(testUser1));

        User updated = userDAO.getUser(testUser1.getUserID());
        assertEquals("Updated Name", updated.getFirstName());
        assertEquals("john@test.com", updated.getEmail()); // Email unchanged
    }

    @Test
    void testDeleteUser_ShouldRemoveUser() {
        userDAO.addUser(testUser1);
        userDAO.addUser(testUser2);
        assertEquals(2, userDAO.getUserCount());

        userDAO.deleteUser(testUser1);

        assertEquals(1, userDAO.getUserCount());
        assertFalse(userDAO.userExists(testUser1.getUserID()));
        assertNull(userDAO.getUser(testUser1.getUserID()));

        assertTrue(userDAO.userExists(testUser2.getUserID()));
        assertNotNull(userDAO.getUser(testUser2.getUserID()));
    }

    @Test
    void testDeleteUser_ShouldThrowExceptionForNullUser() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userDAO.deleteUser(null)
        );
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void testGetAllUsers_ShouldReturnAllUsers() {
        userDAO.addUser(testUser1);
        userDAO.addUser(testUser2);

        List<User> allUsers = userDAO.getAllUsers();

        assertEquals(2, allUsers.size());
        assertTrue(allUsers.contains(testUser1));
        assertTrue(allUsers.contains(testUser2));
    }

    @Test
    void testGetAllUsers_ShouldReturnEmptyListWhenNoUsers() {
        List<User> allUsers = userDAO.getAllUsers();

        assertNotNull(allUsers);
        assertTrue(allUsers.isEmpty());
        assertEquals(0, allUsers.size());
    }

    @Test
    void testGetAllUsers_ShouldReturnIndependentList() {
        userDAO.addUser(testUser1);

        List<User> allUsers = userDAO.getAllUsers();
        allUsers.clear();

        assertEquals(1, userDAO.getUserCount());
        assertNotNull(userDAO.getUser(testUser1.getUserID()));
    }

    @Test
    void testCRUDLifecycle() {
        // Create
        userDAO.addUser(testUser1);
        assertEquals(1, userDAO.getUserCount());

        // Read
        User retrieved = userDAO.getUser(testUser1.getUserID());
        assertNotNull(retrieved);
        assertEquals("john@test.com", retrieved.getEmail());

        // Update
        retrieved.setFirstName("Updated John");
        retrieved.setEmail("updated.john@test.com");
        userDAO.updateUser(retrieved);

        User updated = userDAO.getUser(retrieved.getUserID());
        assertEquals("Updated John", updated.getFirstName());
        assertEquals("updated.john@test.com", updated.getEmail());

        // Delete
        userDAO.deleteUser(updated);
        assertEquals(0, userDAO.getUserCount());
        assertNull(userDAO.getUser(updated.getUserID()));
    }
}
import org.example.gy.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private User userWithId;

    @BeforeEach
    void setUp() {
        user = new User("Student", "John", "Doe", "john.doe@test.com", "password123");
        userWithId = new User(1, "Teacher", "Jane", "Smith", "jane.smith@test.com", "securePass");
    }

    @Test
    void testUserConstructorWithoutId() {
        assertEquals("Student", user.getUserType());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@test.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(0, user.getUserID()); // Default ID should be 0
    }

    @Test
    void testUserConstructorWithId() {
        assertEquals(1, userWithId.getUserID());
        assertEquals("Teacher", userWithId.getUserType());
        assertEquals("Jane", userWithId.getFirstName());
        assertEquals("Smith", userWithId.getLastName());
        assertEquals("jane.smith@test.com", userWithId.getEmail());
        assertEquals("securePass", userWithId.getPassword());
    }

    @Test
    void testGetFullName() {
        assertEquals("John Doe", user.getFullName());
        assertEquals("Jane Smith", userWithId.getFullName());
    }

    @Test
    void testToString() {
        String expected = "John Doe (john.doe@test.com)";
        assertEquals(expected, user.toString());
    }

    @Test
    void testSettersAndGetters() {
        user.setUserID(5);
        user.setUserType("Teacher");
        user.setFirstName("Johnny");
        user.setLastName("Doeson");
        user.setEmail("johnny@newdomain.com");
        user.setPassword("newPassword456");

        assertEquals(5, user.getUserID());
        assertEquals("Teacher", user.getUserType());
        assertEquals("Johnny", user.getFirstName());
        assertEquals("Doeson", user.getLastName());
        assertEquals("johnny@newdomain.com", user.getEmail());
        assertEquals("newPassword456", user.getPassword());
        assertEquals("Johnny Doeson", user.getFullName());
    }

    @Test
    void testEquals() {
        User user1 = new User(1, "Student", "Test", "User", "test@test.com", "pass");
        User user2 = new User(1, "Teacher", "Different", "Name", "different@email.com", "differentPass");
        User user3 = new User(2, "Student", "Test", "User", "test@test.com", "pass");

        assertEquals(user1, user2);

        assertNotEquals(user1, user3);

        assertNotEquals(user1, null);

        assertNotEquals(user1, "string");
    }

    @Test
    void testEqualsWithSameObject() {
        assertEquals(user, user);
    }

    @Test
    void testUserTypeValidation() {
        user.setUserType("Student");
        assertEquals("Student", user.getUserType());

        user.setUserType("Teacher");
        assertEquals("Teacher", user.getUserType());
    }

    @Test
    void testEmailHandling() {
        user.setEmail("simple@test.com");
        assertEquals("simple@test.com", user.getEmail());

        user.setEmail("complex.email+tag@domain.co.uk");
        assertEquals("complex.email+tag@domain.co.uk", user.getEmail());
    }

    @Test
    void testPasswordHandling() {
        user.setPassword("shortpw");
        assertEquals("shortpw", user.getPassword());

        user.setPassword("verylongpasswordwithspecialchars!@#$%^&*()");
        assertEquals("verylongpasswordwithspecialchars!@#$%^&*()", user.getPassword());
    }
}
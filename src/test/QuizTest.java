import com.example.globetrotter.model.Quiz;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class QuizTest {

    private Quiz quiz;
    private Quiz quizWithId;

    @BeforeEach
    void setUp() {
        quiz = new Quiz(
                "Australian Knowledge Quiz",
                "What is the capital city of Australia?",
                "Which one of these animals is unique to Australia?",
                "How many states does Australia have?",
                "What is the population of Australia (to the nearest million)?",
                "Which of these is not the name of an Australian state?",
                "What initially attracted immigrants to Australia?",
                "What resource does Australia provide the most of to other countries?",
                "What does ANZAC stand for?",
                "What two animals can be found on the Australian crest?",
                "What is the name of the Australian national anthem?"
        );

        quizWithId = new Quiz(
                1,
                "Australian Knowledge Quiz",
                "What is the capital city of Australia?",
                "Which one of these animals is unique to Australia?",
                "How many states does Australia have?",
                "What is the population of Australia (to the nearest million)?",
                "Which of these is not the name of an Australian state?",
                "What initially attracted immigrants to Australia?",
                "What resource does Australia provide the most of to other countries?",
                "What does ANZAC stand for?",
                "What two animals can be found on the Australian crest?",
                "What is the name of the Australian national anthem?"
        );
    }

    @Test
    void testQuizConstructorWithoutId() {
        assertEquals("Australian Knowledge Quiz", quiz.getTitle());
        assertEquals("What is the capital city of Australia?", quiz.getQ1());
        assertEquals("Which one of these animals is unique to Australia?", quiz.getQ2());
        assertEquals("How many states does Australia have?", quiz.getQ3());
        assertEquals("What is the population of Australia (to the nearest million)?", quiz.getQ4());
        assertEquals("Which of these is not the name of an Australian state?", quiz.getQ5());
        assertEquals("What initially attracted immigrants to Australia?", quiz.getQ6());
        assertEquals("What resource does Australia provide the most of to other countries?", quiz.getQ7());
        assertEquals("What does ANZAC stand for?", quiz.getQ8());
        assertEquals("What two animals can be found on the Australian crest?", quiz.getQ9());
        assertEquals("What is the name of the Australian national anthem?", quiz.getQ10());
        assertEquals(0, quiz.getQuizID()); // Default ID should be 0
    }

    @Test
    void testQuizConstructorWithId() {
        assertEquals(1, quizWithId.getQuizID());
        assertEquals("Australian Knowledge Quiz", quizWithId.getTitle());
        assertEquals("What is the capital city of Australia?", quizWithId.getQ1());
        assertEquals("Which one of these animals is unique to Australia?", quizWithId.getQ2());
        assertEquals("How many states does Australia have?", quizWithId.getQ3());
    }

    @Test
    void testQuizWithNullQuestions() {
        Quiz quizWithNulls = new Quiz(
                "Partial Australian Quiz",
                "What is the capital city of Australia?",
                "Which one of these animals is unique to Australia?",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertEquals("Partial Australian Quiz", quizWithNulls.getTitle());
        assertEquals("What is the capital city of Australia?", quizWithNulls.getQ1());
        assertEquals("Which one of these animals is unique to Australia?", quizWithNulls.getQ2());
        assertNull(quizWithNulls.getQ3());
        assertNull(quizWithNulls.getQ4());
        assertNull(quizWithNulls.getQ5());
    }

    @Test
    void testSettersAndGetters() {
        quiz.setQuizID(5);
        quiz.setTitle("Updated Title");
        quiz.setQ1("Updated Q1?");
        quiz.setQ2("Updated Q2?");
        quiz.setQ3("Updated Q3?");
        quiz.setQ4("Updated Q4?");
        quiz.setQ5("Updated Q5?");
        quiz.setQ6("Updated Q6?");
        quiz.setQ7("Updated Q7?");
        quiz.setQ8("Updated Q8?");
        quiz.setQ9("Updated Q9?");
        quiz.setQ10("Updated Q10?");

        assertEquals(5, quiz.getQuizID());
        assertEquals("Updated Title", quiz.getTitle());
        assertEquals("Updated Q1?", quiz.getQ1());
        assertEquals("Updated Q2?", quiz.getQ2());
        assertEquals("Updated Q3?", quiz.getQ3());
        assertEquals("Updated Q4?", quiz.getQ4());
        assertEquals("Updated Q5?", quiz.getQ5());
        assertEquals("Updated Q6?", quiz.getQ6());
        assertEquals("Updated Q7?", quiz.getQ7());
        assertEquals("Updated Q8?", quiz.getQ8());
        assertEquals("Updated Q9?", quiz.getQ9());
        assertEquals("Updated Q10?", quiz.getQ10());
    }

    @Test
    void testEquals() {
        Quiz quiz1 = new Quiz(1, "Title", "Q1", "Q2", "Q3", "Q4", "Q5", "Q6", "Q7", "Q8", "Q9", "Q10");
        Quiz quiz2 = new Quiz(1, "Different Title", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
        Quiz quiz3 = new Quiz(2, "Title", "Q1", "Q2", "Q3", "Q4", "Q5", "Q6", "Q7", "Q8", "Q9", "Q10");

        // Same ID should be equal
        assertEquals(quiz1, quiz2);

        // Different ID should not be equal
        assertNotEquals(quiz1, quiz3);

        // Null comparison
        assertNotEquals(null, quiz1);

        // Different type comparison
        assertNotEquals("string", quiz1);
    }

    @Test
    void testToString() {
        String result = quiz.toString();

        assertTrue(result.contains("QuizID="));
        assertTrue(result.contains("title='Australian Knowledge Quiz'"));
        assertTrue(result.contains("Q1='What is the capital city of Australia?'"));
        assertTrue(result.contains("Q10='What is the name of the Australian national anthem?'"));
    }

    @Test
    void testSetQuestionsToNull() {
        quiz.setQ1(null);
        quiz.setQ5(null);
        quiz.setQ10(null);

        assertNull(quiz.getQ1());
        assertNotNull(quiz.getQ2());
        assertNull(quiz.getQ5());
        assertNull(quiz.getQ10());
    }

    @Test
    void testQuizIdAssignment() {
        assertEquals(0, quiz.getQuizID());

        quiz.setQuizID(100);
        assertEquals(100, quiz.getQuizID());

        quiz.setQuizID(0);
        assertEquals(0, quiz.getQuizID());
    }
}
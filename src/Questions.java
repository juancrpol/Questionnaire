import java.util.ArrayList;

/**
 * Class that sets the game questions.
 */
public class Questions {
    private ArrayList<ArrayList<String>> questions;

    public Questions() {
        questions = new ArrayList<>();
    }

    /* Getters a Setters. */
    public void setQuestions(ArrayList<String> questions) {
        this.questions.add(questions);
    }

    public ArrayList<ArrayList<String>> getQuestions() {
        return questions;
    }

    /**
     * Allows printing tests.
     *
     * @return an organized string with all the questions.
     */
    @Override
    public String toString() {
        StringBuilder questionsString = new StringBuilder();
        for (ArrayList<String> strings : questions) {
            questionsString.append(strings).append("\n");
        }
        return questionsString.toString();
    }
}
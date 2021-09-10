import java.util.ArrayList;

public class Questions {
    private ArrayList<ArrayList<String>> questions;

    public Questions() {
        questions = new ArrayList<>();
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions.add(questions);
    }

    public ArrayList<ArrayList<String>> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        StringBuilder questionsString = new StringBuilder();
        for (ArrayList<String> strings : questions) {
            questionsString.append(strings).append("\n");
        }
        return questionsString.toString();
    }
}
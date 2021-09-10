import java.util.ArrayList;

public class Questions {
    private ArrayList<ArrayList<String>> question;

    public void setQuestion(ArrayList<String> question) {
        this.question.add(question);
    }

    public ArrayList<ArrayList<String>> getQuestion() {
        return question;
    }
}
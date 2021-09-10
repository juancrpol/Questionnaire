import utils.SerializationUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static Scanner SCAN;
    private static Questions questions;
    private static Player player;

    public static void main(String[] args) throws FileNotFoundException {
        printHeader();
        setQuestions();
        playQuestionnaire();
    }

    private static void playQuestionnaire() {
        player = new Player();

        try {
            player = (Player) SerializationUtils.deserialize("player.data");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        boolean isFinished = false;
        while (!isFinished) {
            switch (player.getRound()) {
                case 1:
                    printHeaderRound(1);
                    isFinished = playRound(1);
                    break;
                case 2:
                    printHeaderRound(2);
                    isFinished = playRound(2);
                    break;
                case 3:
                    printHeaderRound(3);
                    isFinished = playRound(3);
                    break;
                case 4:
                    printHeaderRound(4);
                    isFinished = playRound(4);
                    break;
                case 5:
                    printHeaderRound(5);
                    isFinished = playRound(5);
                    break;
            }
        }
    }

    private static void printHeaderRound(int round) {
        String headerRound = "";
        headerRound += "+---------+\n";
        headerRound += "| Round " + round + " |\n";
        headerRound += "+---------+";
        System.out.println(headerRound);
    }

    private static boolean playRound(int round) {
        String correctAnswer;
        String userAnswer;
        ArrayList<String> question;

        question = selectQuestion(round);
        printQuestion(question);
        correctAnswer = question.get(5);
        userAnswer = printAnswer();

        if (userAnswer.equals("0")) {
            return true;
        }

        if (userAnswer.equals(correctAnswer)) {
            player.setPoints(round == 5 ? 0 : player.getPoints() + 1000);
            player.setRound(round == 5 ? 1 : round + 1);
            System.out.println(round == 5 ? "Congrats! You win QUESTIONNAIRE." :
                    "Correct! you have " + player.getPoints() + " points.\n");
            try {
                SerializationUtils.serialize(player, "player.data");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return round == 5;
        } else {
            System.out.println("Wrong! your final score is " + player.getPoints() + " points.");
            player.setPoints(0);
            player.setRound(1);
            try {
                SerializationUtils.serialize(player, "player.data");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    private static String printAnswer() {
        while (true) {
            System.out.print("Enter your answer (0 to exit): ");
            SCAN = new Scanner(System.in);
            String answer = SCAN.next();
            if (answer.equals("a") || answer.equals("b") || answer.equals("c")
                    || answer.equals("d") || answer.equals("0")) {
                return answer;
            }
        }
    }

    private static void printQuestion(ArrayList<String> question) {
        System.out.println(question.get(0));
        System.out.println(question.get(1));
        System.out.println(question.get(2));
        System.out.println(question.get(3));
        System.out.println(question.get(4));
    }

    private static ArrayList<String> selectQuestion(int round) {
        Random random = new Random();
        ArrayList<String> questionSelected;
        while (true) {
            int questionNumber = random.nextInt(25);
            questionSelected = questions.getQuestions().get(questionNumber);
            int category = Integer.parseInt(questionSelected.get(6));
            if (category == round) {
                return questionSelected;
            }
        }
    }

    private static void setQuestions() throws FileNotFoundException {
        SCAN = new Scanner(new File("Questions.txt"));
        questions = new Questions();

        while (SCAN.hasNextLine()) {
            ArrayList<String> line = new ArrayList<>();
            final String nextLine = SCAN.nextLine();
            final String[] items = nextLine.split(", ");

            Collections.addAll(line, items);
            questions.setQuestions(line);
        }
    }

    private static void printHeader() {
        String header = "";
        header += "+--------------------------+\n";
        header += "| WELCOME TO QUESTIONNAIRE |\n";
        header += "+--------------------------+\n";
        header += "| Created by: Juan Carlos. |\n";
        header += "+--------------------------+\n";

        System.out.println(header);
    }
}
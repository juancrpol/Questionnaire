import utils.SerializationUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static Scanner SCAN;
    private static Questions questions;
    private static Player player;
    private static File file;

    public static void main(String[] args) throws FileNotFoundException {
        printHeader();
        setQuestions();
        playQuestionnaire();
    }

    private static void playQuestionnaire() {
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
            if (round == 5) {
                try {
                    player.setPoints(player.getPoints() + 1000);
                    writeRecord(5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            player.setPoints(round == 5 ? 0 : player.getPoints() + 1000);
            player.setRound(round == 5 ? 1 : round + 1);
            System.out.println(round == 5 ? "Congrats! You win QUESTIONNAIRE." :
                    "Correct! You have " + player.getPoints() + " points.\n");
            try {
                SerializationUtils.serialize(player, file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return round == 5;
        } else {
            System.out.println("Wrong! Your final score is " + player.getPoints() + " points.");
            try {
                writeRecord(round);
                player.setPoints(0);
                player.setRound(1);
                SerializationUtils.serialize(player, file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    private static void writeRecord(int round) throws IOException {
        FileWriter writer = new FileWriter("record.txt", true);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        writer.write(formatter.format(date) + " points: " + player.getPoints() + " round: " + round +
                (player.getPoints() == 5000 ? " (Winner)" : "") + "\n");
        writer.close();
    }

    private static String printAnswer() {
        while (true) {
            System.out.print("Enter your answer (0 to exit): ");
            SCAN = new Scanner(System.in);
            String answer = SCAN.next();
            if (answer.matches("[a-d0]")) {
                return answer;
            }
        }
    }

    private static void printQuestion(ArrayList<String> question) {
        String printQuestion = "";
        printQuestion += question.get(0) + "\n";
        printQuestion += question.get(1) + "\n";
        printQuestion += question.get(2) + "\n";
        printQuestion += question.get(3) + "\n";
        printQuestion += question.get(4);
        System.out.println(printQuestion);
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

        String option;
        do {
            System.out.println("Press 0 to see record or enter to continue...");
            SCAN = new Scanner(System.in);
            option = SCAN.nextLine();
            if (option.equals("0")) {
                readRecord();
            }
        } while (option.equals("0"));


        file = new File("player.data");
        player = new Player();
        if (file.exists() && !file.isDirectory()) {
            // do something
            try {
                player = (Player) SerializationUtils.deserialize(file.getName());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                SerializationUtils.serialize(player, file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void readRecord() {
        file = new File("record.txt");
        if (file.exists() && !file.isDirectory()) {
            String header = "";
            header += "+--------+\n";
            header += "| RECORD |\n";
            header += "+--------+";
            System.out.println(header);

            try {
                FileReader reader = new FileReader(file.getName());
                int character;

                while ((character = reader.read()) != -1) {
                    System.out.print((char) character);
                }
                System.out.println();
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Record doesn't exist.\n");
        }
    }
}
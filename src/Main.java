import utils.SerializationUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class that sets the questionnaire game.
 */
public class Main {

    private static Scanner SCAN;
    private static Questions questions;
    private static Player player;
    private static File file;

    public static void main(String[] args) throws FileNotFoundException {
        initializeGame();
        setQuestions();
        playQuestionnaire();
    }

    /**
     * Method that creates a new player and loads or saves their status, depending on their situation.
     */
    private static void initializeGame() {
        String header = "";
        header += "╔═╗ ╦ ╦╔═╗╔═╗╔╦╗╦╔═╗╔╗╔╔╗╔╔═╗╦╦═╗╔═╗\n";
        header += "║═╬╗║ ║║╣ ╚═╗ ║ ║║ ║║║║║║║╠═╣║╠╦╝║╣ \n";
        header += "╚═╝╚╚═╝╚═╝╚═╝ ╩ ╩╚═╝╝╚╝╝╚╝╩ ╩╩╩╚═╚═╝\n";
        header += "+----------------------------------+\n";
        header += "|     Created by: Juan Carlos.     |\n";
        header += "+----------------------------------+\n";
        System.out.println(header);

        /*  Print the record or continue with the questions. */
        String option;
        System.out.print("Press 0 to see record or enter to continue...");
        SCAN = new Scanner(System.in);
        option = SCAN.nextLine();
        if (option.equals("0")) {
            readRecord();
        }

        /* Save or load a player's data. */
        file = new File("Player.data");
        player = new Player();
        if (file.exists() && !file.isDirectory()) {
            // do something
            try {
                player = (Player) SerializationUtils.deserialize(file.getName());
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {
            try {
                SerializationUtils.serialize(player, file.getName());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method that loads all the questions of the game from a text file.
     */
    private static void setQuestions() {
        try {
            SCAN = new Scanner(new File("Questions.txt"));
            questions = new Questions();

            while (SCAN.hasNextLine()) {
                ArrayList<String> line = new ArrayList<>();
                final String nextLine = SCAN.nextLine();
                final String[] items = nextLine.split(", ");

                Collections.addAll(line, items);
                questions.setQuestions(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }

    /**
     * Method that chooses a question depending on the round.
     */
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

    /**
     * Print the header of every round.
     *
     * @param round of the game.
     */
    private static void printHeaderRound(int round) {
        String headerRound = "";
        headerRound += "+---------+\n";
        headerRound += "| Round " + round + " |\n";
        headerRound += "+---------+";
        System.out.println(headerRound);
    }

    /**
     * Method that compares the user's answer with the correct answer.
     *
     * @param round of the game.
     * @return true if the  game ends otherwise false.
     */
    private static boolean playRound(int round) {
        String correctAnswer;
        String userAnswer;
        ArrayList<String> question;

        /* Select user answer and correct answer. */
        question = selectQuestion(round);
        printQuestion(question);
        correctAnswer = question.get(5);
        userAnswer = printAnswer();

        /* Exit game. */
        if (userAnswer.equals("0")) {
            System.out.println("Saving state... Goodbye!");
            return true;
        }

        /* Compare answers. */
        if (userAnswer.equals(correctAnswer)) {
            if (round == 5) {
                try {
                    player.setPoints(player.getPoints() + 1000);
                    writeRecord(5);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }

            player.setPoints(round == 5 ? 0 : player.getPoints() + 1000);
            player.setRound(round == 5 ? 1 : round + 1);
            System.out.println(round == 5 ? "Congrats! YOU WIN QUESTIONNAIRE." :
                    "Correct! You have " + player.getPoints() + " points.\n");
            try {
                SerializationUtils.serialize(player, file.getName());
            } catch (IOException e) {
                System.out.println(e.getMessage());
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
                System.out.println(e.getMessage());
            }
            return true;
        }
    }

    /**
     * Method that selects a random question, depending on the round (category).
     *
     * @param round of the game.
     * @return a Question.
     */
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

    /**
     * Print the body of the question.
     *
     * @param question about Java.
     */
    private static void printQuestion(ArrayList<String> question) {
        String printQuestion = "";
        printQuestion += question.get(0) + "\n";
        printQuestion += question.get(1) + "\n";
        printQuestion += question.get(2) + "\n";
        printQuestion += question.get(3) + "\n";
        printQuestion += question.get(4);
        System.out.println(printQuestion);
    }

    /**
     * @return the user answer.
     */
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

    /**
     * Save the game record.
     *
     * @param round of the game.
     * @throws IOException
     */
    private static void writeRecord(int round) throws IOException {
        FileWriter writer = new FileWriter("Record.txt", true);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        writer.write("| " + formatter.format(date) + " |  " + String.format("%04d", player.getPoints())
                + "  |   " + round + (player.getPoints() == 5000 ? "   | (Winner)" : "   |") + "\n");
        writer.close();
    }

    /**
     * Read the game record if exists.
     */
    private static void readRecord() {
        file = new File("Record.txt");
        if (file.exists() && !file.isDirectory()) {
            String header = "";
            header += "+---------------------+--------+-------+\n";
            header += "|        DATE         | POINTS | ROUND |\n";
            header += "+---------------------+--------+-------+";
            System.out.println(header);

            try {
                FileReader reader = new FileReader(file.getName());
                int character;

                while ((character = reader.read()) != -1) {
                    System.out.print((char) character);
                }
                reader.close();
                System.out.println("+---------------------+--------+-------+\n");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            String message = "";
            message += "+-----------------------+\n";
            message += "| Record doesn't exist. |\n";
            message += "+-----------------------+\n";
            System.out.println(message);
        }
    }
}
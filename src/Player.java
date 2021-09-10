import java.io.Serial;
import java.io.Serializable;

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 7L;
    private int points;
    private int round;

    public Player() {
        points = 0;
        round = 1;
    }

    public int getPoints() {
        return points;
    }

    public int getRound() {
        return round;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
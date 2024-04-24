import java.util.ArrayList;

public class Wave {
    private ArrayList<Balloon> wave;
    private int waveSize;
    // read from csv for the type of balloon and how many
    public Wave(ArrayList<Balloon> wave) {
        this.wave = wave;
    }

    public ArrayList<Balloon> getBalloons() {
        return wave;
    }

    // Calculate the coordinates of the balloon closest to the inputted coordinates
    public Balloon getClosestBalloon(int x, int y) {
        int smallestDist = Integer.MAX_VALUE;
        Balloon closestBalloon = null;
        for (Balloon b : wave) {
            // pythag
        }
        if (closestBalloon == null) {
            return null;
        }
        return closestBalloon;
    }

    public void remove(Balloon balloon) {

    }
}

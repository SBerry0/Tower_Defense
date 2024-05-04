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

    public void moveBalloons(Game g) {
        for (Balloon b : wave) {
            b.move(g);
        }
    }
    public boolean isOver() {
        return wave.isEmpty();
    }

    // Calculate the coordinates of the balloon closest to the inputted coordinates
    public Balloon getClosestBalloon(int x, int y) {
        int smallestDist = Integer.MAX_VALUE;
        Balloon closestBalloon = null;
        for (int i = 0; i < wave.size(); i++) {
            int dist = wave.get(i).getDistance(x, y);
            if (dist < smallestDist) {
                smallestDist = dist;
//                System.out.println("distance: " + smallestDist);
                closestBalloon = wave.get(i);
            }
        }
        return closestBalloon;
    }

    public void refreshBalloons() {
        for (int i = 0; i < getBalloons().size(); i++) {
            if (!getBalloons().get(i).isAlive()) {
                getBalloons().remove(i--);
            }
        }
    }

    public int getClosestBalloonDist(int x, int y) {
        int smallestDist = Integer.MAX_VALUE;
        for (int i = 0; i < wave.size(); i++) {
            int dist = wave.get(i).getDistance(x, y);
            if (dist < smallestDist) {
                smallestDist = dist;
//                System.out.println("distance: " + smallestDist);
            }
        }
        return smallestDist;
    }

    public void remove(Balloon balloon) {
        wave.remove(balloon);
    }
}

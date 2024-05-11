import java.util.ArrayList;

public class Wave {
    private ArrayList<Balloon> wave;

    public Wave(ArrayList<Balloon> wave) {
        this.wave = wave;
    }

    public ArrayList<Balloon> getBalloons() {
        return wave;
    }

    public boolean isEmpty() { return wave.isEmpty(); }

    // Calculate the coordinates of the balloon closest to the inputted coordinates
    public Balloon getClosestBalloon(int x, int y) {
        int smallestDist = Integer.MAX_VALUE;
        Balloon closestBalloon = null;
        // Check the ArrayList backwards so it sends the higher health balloons first
        for (int i = wave.size()-1; i >= 0; i--) {
            int dist = wave.get(i).getDistance(x, y);
            if (dist < smallestDist) {
                smallestDist = dist;
                closestBalloon = wave.get(i);
            }
        }
        return closestBalloon;
    }

    public int getClosestBalloonDist(int x, int y) {
        int smallestDist = Integer.MAX_VALUE;
        for (int i = 0; i < wave.size(); i++) {
            int dist = wave.get(i).getDistance(x, y);
            if (dist < smallestDist) {
                smallestDist = dist;
            }
        }
        return smallestDist;
    }

    public void moveBalloons(Game g) {
        for (Balloon b : wave) {
            b.move(g);
        }
    }

    public void refreshBalloons() {
        for (int i = 0; i < getBalloons().size(); i++) {
            if (!getBalloons().get(i).isAlive()) {
                getBalloons().remove(i--);
            }
        }
    }
}

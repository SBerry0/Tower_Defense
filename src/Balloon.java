import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Balloon extends Sprite {

    private static final int[] SPEEDS = {10, 10, 15, 20, 5, 10};
    private int health;
    private int speed;
    private Wave balloons;

    public Balloon(int health, int balloonNum, Wave balloons) {
        super(new ImageIcon("Resources/Balloons/" + health + ".png").getImage(),
                Game.BALLOON_STARTING_X - (balloonNum* (int) (Math.random() + 12) + 8),
                Game.BALLOON_STARTING_Y,
                49, 63);

        if (health > 6 || health < 0) {
            balloons.remove(this);
        }
        else {
            this.speed = SPEEDS[health - 1];
            this.balloons = balloons;
            this.health = health;
        }
    }

    public void move() {
        // i have no idea how to make it follow a set path.....
        // Something like a csv of EAST, EAST, EAST.... NORTH, NORTH
        if (health > 6 || health < 0) {
            balloons.remove(this);
        }
        if (super.getX() > 700) {

        }
    }

    public int getDistance(int x, int y) {
        int a = super.getX() - x;
        int b = super.getY() - y;
        return (int) (Math.sqrt(1.0 * (a*a)+(b*b)));
    }

    public void checkCompleted(Game g) {
        if (super.getY() > Game.WINDOW_HEIGHT) {
            balloons.remove(this);
            g.reduceHealth();
        }
    }

    public void draw(Graphics g, GameViewer viewer) {
        super.draw(g, viewer);
    }

    public String toString() {
        return super.getX() + ", " + super.getY();
    }
}

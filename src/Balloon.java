import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Balloon extends Sprite {
    private static final int STARTING_X = 0,
                            STARTING_Y = 70;
    private static final int[] SPEEDS = {10, 10, 15, 20, 5, 10};
    private int health;
    private int speed;
    private Wave balloons;

    public Balloon(int health, Wave balloons) {
        super(new ImageIcon("Resources/Balloons/" + health + ".png").getImage(), STARTING_X, STARTING_Y);

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
        // SOmething like a csv of EAST, EAST, EAST.... NORTH, NORTH
        if (health > 6 || health < 0) {
            balloons.remove(this);
        }
        if (super.getX() > 700) {

        }
    }

    public void checkCompleted(Game g) {
        if (super.getY() > Game.WINDOW_HEIGHT) {
            balloons.remove(this);
            g.reduceHealth();
        }
    }

    public void draw(Graphics g, GameViewer viewer) {
        g.drawImage(super.getImage(), super.getX(), super.getY(), viewer);
    }

    public String toString() {
        return super.getX() + ", " + super.getY();
    }
}

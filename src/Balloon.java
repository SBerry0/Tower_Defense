import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Balloon extends Sprite {

    private static final int[] SPEEDS = {10, 10, 15, 20, 5, 10};
    private int health;
    private int speed;
    private Wave balloons;
    private int direction;
    private int nodeNum;

    // TODO: Create isAlive boolean and change that instead of removing it from its own arrayList
    public Balloon(int health, int balloonNum, Wave balloons) {
        super(new ImageIcon("Resources/Balloons/" + health + ".png").getImage(),
                Game.BALLOON_STARTING_X - (balloonNum* (int) (Math.random() + 12) + 8),
                Game.BALLOON_STARTING_Y,
                49, 63);
        this.balloons = balloons;
        if (health > 6 || health < 0) {
            balloons.remove(this);
        }
        else {
            this.speed = SPEEDS[health - 1];
            this.health = health;
        }
        this.direction = Game.EAST;
        nodeNum = 0;
    }

    public void move(Game g) {
        if (health > 6 || health < 0) {
            balloons.remove(this);
        }
        if (super.getY() > Game.WINDOW_HEIGHT) {
            g.reduceHealth(health);
            balloons.remove(this);
        }
        switch (direction) {
            case Game.NORTH:
                super.addY(-1*speed);
                break;
            case Game.EAST:
                super.addX(speed);
                break;
            case Game.SOUTH:
                super.addY(speed);
                break;
            case Game.WEST:
                super.addX(-1*speed);
                break;
        }
        if (g.getNode(nodeNum).isOnNode(super.getX(), super.getY())) {
            direction = g.getNode(nodeNum).getNewDirection();
            nodeNum++;
        }

    }
    public void nextNode() {
        this.nodeNum = Math.min(nodeNum + 1, Game.NODES.length - 1);
    }

    public int getDistance(int x, int y) {
        int a = super.getX() - x;
        int b = super.getY() - y;
        return (int) (Math.sqrt(1.0 * (a*a)+(b*b)));
    }

    public void draw(Graphics g, GameViewer viewer) {
        super.draw(g, viewer);
    }

    public String toString() {
        return super.getX() + ", " + super.getY();
    }
}

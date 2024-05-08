import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Balloon extends Sprite {

    private static final int[] SPEEDS = {3, 4, 5, 6, 7, 10};
    private int health;
    private int speed;
    private int direction;
    private int nodeNum;
    private boolean isAlive;

    public Balloon(int health, int balloonNum) throws IOException {
        super(ImageIO.read(new File("Resources/Balloons/" + health + ".png")),
                Game.BALLOON_STARTING_X - (balloonNum * Game.BALLOON_SEPARATION),
                Game.BALLOON_STARTING_Y,
                49, 63);
        if (health > 6 || health < 0) {
            isAlive = false;
        }
        else {
            this.speed = SPEEDS[health - 1];
            this.health = health;
            isAlive = true;
        }
        this.direction = Game.EAST;
        nodeNum = 0;
    }

    public void reduceHealth() throws IOException {
        health --;
        if (health <= 0) {
            isAlive = false;
        } else {
            refreshBalloonState();
        }
    }

    public void refreshBalloonState() throws IOException {
        super.changeImage(ImageIO.read(new File("Resources/Balloons/" + health + ".png")));
        this.speed = SPEEDS[health - 1];
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void move(Game g) {
        if ((health > 6 || health < 0) && isAlive) {
            isAlive = false;
        }
        if (super.getY() > Game.WINDOW_HEIGHT && isAlive) {
            g.reduceHealth(health);
            isAlive = false;
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
            nextNode();
        }

    }
    public void nextNode() {
        this.nodeNum = Math.min(nodeNum + 1, Game.NODES.length - 1);
    }



    public void draw(Graphics g, GameViewer viewer) {
        if (isAlive)
            super.draw(g, viewer);
    }
}

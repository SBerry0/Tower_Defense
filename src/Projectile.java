import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Projectile extends Sprite {
    private int direction;
    private int speed;
    private int numPopped;
    private int piercing;
    private boolean isActive;
    private int deltaX, deltaY;
    private ArrayList<Balloon> poppedBalloons;

    public Projectile(int dartType, int x, int y, Wave currentWave) {
        super(new ImageIcon("Resources/Darts/"+dartType).getImage(), x, y, 10, 40);
        if (dartType < Monkey.NUM_MONKEYS) {
            this.poppedBalloons = new ArrayList<>();
            this.piercing = Monkey.PIERCINGS[dartType];
            this.speed = Monkey.SPEED[dartType];
            this.isActive = true;

            int diffX = currentWave.getClosestBalloon(x, y).getX() - x;
            int diffY = currentWave.getClosestBalloon(x, y).getY() - y;

            int origDist = (int) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

            int ratio = origDist / speed;

            this.deltaX = diffX / ratio;
            this.deltaY = diffY / ratio;
        }
    }

    public boolean isTouchingBalloon (Wave balloons) {
        Balloon closest = balloons.getClosestBalloon(super.getX(), super.getY());
        // probably won't work, I have to check the range of the balloon
        if (super.getX() >= closest.getX() && super.getX() <= closest.getX() + Sprite.DEFAULT_SPRITE_LENGTH
                && super.getY() < closest.getY() && super.getY() < closest.getY() + Sprite.DEFAULT_SPRITE_LENGTH
                && !poppedBalloons.contains(closest)) {
            System.out.println("HIT!");
            closest.reduceHealth();
            poppedBalloons.add(closest);
            return true;
        }
        return false;
    }
    public void move(Wave currentWave) {
        if (numPopped > piercing || super.getX() < 0 || super.getX() > Game.WINDOW_WIDTH ||
                super.getY() < 0 || super.getX() > Game.WINDOW_HEIGHT) {
            isActive = false;
        }
        if (isTouchingBalloon(currentWave)) {
            numPopped++;
            System.out.println("adding to popped");
        }
        super.addX(deltaX);
        super.addY(deltaY);
    }

    public boolean isActive() {
        return isActive;
    }

    public void draw(Graphics g, GameViewer viewer) {
        // affline transform
//        super.draw(g, viewer);
        g.fillRect(super.getX(), super.getY(), 50, 15);
    }

}

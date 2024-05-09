import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Projectile extends Sprite {
    public static final int WIDTH = 60, HEIGHT = 56;
    private int speed;
    private int piercing;
    private boolean isActive;
    private int deltaX, deltaY;
    private ArrayList<Balloon> poppedBalloons;

    public Projectile(int dartType, int x, int y, Wave currentWave) throws IOException {
        super(ImageIO.read(new File("Resources/Darts/"+dartType+".png")), x, y, WIDTH, HEIGHT);
        if (dartType < Monkey.NUM_MONKEYS) {
            this.poppedBalloons = new ArrayList<>();
            this.piercing = Monkey.PIERCINGS[dartType];
            this.speed = Monkey.SPEED[dartType];
            this.isActive = true;

            int diffX = currentWave.getClosestBalloon(x, y).getX() - x;
            int diffY = currentWave.getClosestBalloon(x, y).getY() - y;

            int origDist = (int) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

            int ratio = origDist / speed;
            if (ratio == 0) {
                this.deltaX = diffX;
                this.deltaY = diffY;
            } else {
                this.deltaX = diffX / ratio;
                this.deltaY = diffY / ratio;
            }


        }
    }

    public boolean isTouchingBalloon (Game game) throws IOException {
        Balloon closest = game.getCurrentWave().getClosestBalloon(super.getX(), super.getY());
        if (closest == null) {
            return false;
        }
//        System.out.println(closest.getDistance(super.getX(), super.getY()));
        // probably won't work, I have to check the range of the balloon
        if (closest.getDistance(super.getX(), super.getY()) < Game.PROJECTILE_HIT_DISTANCE) {
//            System.out.println("in range");
            if (!poppedBalloons.contains(closest)) {
//        }
//        if (super.getX() >= closest.getX() && super.getX() <= closest.getX() + Sprite.DEFAULT_SPRITE_LENGTH
//                && super.getY() < closest.getY() && super.getY() < closest.getY() + Sprite.DEFAULT_SPRITE_LENGTH
//                && !poppedBalloons.contains(closest)) {
                System.out.println("HIT!");
                closest.reduceHealth();
                game.incrementMoney();
                poppedBalloons.add(closest);
                return true;
            }
        }
        return false;
    }
    public void move(Game game) throws IOException {
        if (poppedBalloons.size() >= piercing || super.getX() < 0 || super.getX() > Game.SELECTION_START ||
                super.getY() < 0 || super.getY() > Game.WINDOW_HEIGHT) {
            isActive = false;
        }
        if (isTouchingBalloon(game)) {
//            System.out.println("adding to popped: " + poppedBalloons.size());
//            System.out.println(piercing);
        }
        super.addX(deltaX);
        super.addY(deltaY);
    }

    public boolean isActive() {
        return isActive;
    }

    public void draw(Graphics g, GameViewer viewer) {
        g.drawImage(Sprite.rotateImage(super.getImage(),
                (deltaX > 0 ? Math.atan((double) (deltaY) / (deltaX)) :
                        (Math.toRadians(180) + Math.atan((double) (deltaY) / (deltaX))))),
                super.getX(), super.getY(), WIDTH, HEIGHT, viewer);

//        super.draw(g, viewer);
    }

}

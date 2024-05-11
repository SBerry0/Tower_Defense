import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

public class Monkey extends Sprite{
    public static final int NUM_MONKEYS = 3;
    public static final int[] RANGES = {150, 150, 180};
    public static final int[] PIERCINGS = {2, 3, 11};
    public static final int[] SPEED = {15, 9, 10};
    public static final int[] PRICES = {200, 225, 525};
    public static final String[] NAMES = {"Dart Monkey", "Glue Gunner", "     Cannon"};

    private int counter;
    private int range;
    private int monkeyType;

    public Monkey (int monkeyType, int x, int y, int counter) throws IOException {
        super(ImageIO.read(new File("Resources/Monkeys/"+monkeyType+".png")), x, y);
        if (monkeyType < NUM_MONKEYS) {
            this.counter = counter;
            this.monkeyType = monkeyType;
            this.range = RANGES[monkeyType];
        }
    }

    public int getRange() {
        return range;
    }

    public int getDelayNum() {
        return counter;
    }

    public int getMonkeyNum() {
        return monkeyType;
    }

    public void shoot(Wave wave, Game game) throws IOException {
        if (wave.getClosestBalloonDist(super.getX(), super.getY()) <= range) {
            System.out.println(monkeyType);
            game.addProjectile(new Projectile(monkeyType, super.getX(), super.getY()-Sprite.DEFAULT_SPRITE_LENGTH/2,
                    game.getCurrentWave()));
        }
    }

    public double getTheta(Balloon closest) {
        double theta = 0.0;
        if (closest.getX() - super.getX() > 0 && closest.getY() - super.getY() < 0) {
            // Balloon is in Q1
            theta = Math.atan(-1.0*(closest.getX() - super.getX()) / (closest.getY() - super.getY()));
        }
        if (closest.getX() - super.getX() < 0 && closest.getY() - super.getY() < 0) {
            // Balloon is in Q2
            theta = Math.atan(-1.0*(closest.getX() - super.getX()) / (closest.getY() - super.getY()));
        }
        else if (closest.getX() - super.getX() < 0 && closest.getY() - super.getY() > 0) {
            // Balloon is in Q3
            theta = Math.atan(-1.0*(closest.getX() - super.getX()) / (closest.getY() - super.getY()));
            theta = Math.toRadians(180) + theta;
        }
        else if (closest.getX() - super.getX() > 0 && closest.getY() - super.getY() > 0) {
            // Balloon is in Q4
            theta = Math.atan(-1.0*(closest.getX() - super.getX()) / (closest.getY() - super.getY())) - Math.toRadians(180);
        }
        else if (closest.getX() - super.getX() == 0) {
            // Balloon is directly above or below the monkey
            if (closest.getY() - super.getY() > 0) {
                // Balloon is below the monkey
                theta = Math.toRadians(180);
            }
            // Otherwise the balloon is above the monkey so keep theta at 0
        }
        return theta;
    }

    public void draw(Graphics g, GameViewer viewer, Game game) {
        // From Noah P.
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform a = g2d.getTransform();
        Balloon closest = null;
        if (game.getCurrentWave() != null)
            closest = game.getCurrentWave().getClosestBalloon(super.getX(), super.getY());
        if (closest == null) {
            super.draw(g, viewer);
            return;
        }
        double theta = getTheta(closest);

        if (closest.getDistance(super.getX(), super.getY()) > range) {
            super.draw(g, viewer);
        } else {
            g2d.rotate(theta, super.getX(), super.getY() - (monkeyType == Game.CANNON ? -5 : 15));
            g2d.drawImage((Image) (super.getImage()), super.getX() - Sprite.DEFAULT_SPRITE_LENGTH/2, super.getY() - Sprite.DEFAULT_SPRITE_LENGTH/2, Sprite.DEFAULT_SPRITE_LENGTH, Sprite.DEFAULT_SPRITE_LENGTH, viewer);
            g2d.setTransform(a);
        }
    }
}

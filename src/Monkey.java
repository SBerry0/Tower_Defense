import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Monkey extends Sprite{
    public static final int NUM_MONKEYS = 3;
    public static final int[] RANGES = {150, 150, 20};
    public static final int[] PIERCINGS = {2, 3, 3};
    public static final int[] SPEED = {15, 3, 10};
    public static final int[] PRICES = {150, 190, 400};
    public static final String[] NAMES = {"Dart Monkey", "Glue Gunner", "      Cannon"};

    private int counter;
    private int range;
    int monkeyType;
    public Monkey (int monkeyType, int x, int y, int counter) throws IOException {
        super(ImageIO.read(new File("Resources/Monkeys/"+monkeyType+".png")), x, y);
        if (monkeyType < NUM_MONKEYS) {
            this.counter = counter;
            this.monkeyType = monkeyType;
            this.range = RANGES[monkeyType];
        }
    }

    public void draw(Graphics g, GameViewer viewer, Game game) {
        Balloon closest = null;
        if (game.getCurrentWave() != null)
            closest = game.getCurrentWave().getClosestBalloon(super.getX(), super.getY());
        if (closest == null) {
            super.draw(g, viewer);
            return;
        }
        double theta = Math.atan(1.0 * (closest.getX() - super.getX()) / (closest.getY() - super.getY()));
        theta += (closest.getY() - super.getY()) > 0 ? 0 : Math.toRadians(180);
        if (closest.getDistance(super.getX(), super.getY()) > range) {
            super.draw(g, viewer);
        } else {
//            g.drawImage(Sprite.rotateImage(super.getImage(), theta),
//                    super.getX(), super.getY(), Sprite.DEFAULT_SPRITE_LENGTH, Sprite.DEFAULT_SPRITE_LENGTH, viewer);
            super.draw(g, viewer);
        }
    }

    public void shoot(Wave wave, Game game) throws IOException {
        if (wave.getClosestBalloonDist(super.getX(), super.getY()) <= range) {
            game.addProjectile(new Projectile(monkeyType, super.getX(), super.getY()-Sprite.DEFAULT_SPRITE_LENGTH/2,
                    game.getCurrentWave()));
            System.out.println("new projectile");
        }
    }

    public int getRange() {
        return range;
    }

    public int getDelayNum() {
        return counter;
    }
}

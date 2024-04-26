import javax.swing.*;
import java.awt.*;

public class Monkey extends Sprite{
    public static final int NUM_MONKEYS = 3;
    public static final int[] RANGES = {10, 10, 20};
    public static final int[] PIERCINGS = {2, 3, 3};
    public static final int[] SPEED = {3, 3, 10};
    // Milliseconds
    private static final int[] DELAYS = {1000, 1000, 750};
    private static final String[] NAMES = {"Dart Monkey", "Glue Monkey", "Cannon"};
    private static final int SELECT_WIDTH = 44,
                            SELECT_HEIGHT = 57;
    private int range;
    private int piercing;
    private int delay;
    private String name;
    public Monkey (int monkeyType, int x, int y) {
        super(new ImageIcon("Resources/Monkeys/"+monkeyType+".png").getImage(), x, y);
        if (monkeyType < NUM_MONKEYS) {
            this.range = RANGES[monkeyType];
            this.piercing = PIERCINGS[monkeyType];
            this.delay = DELAYS[monkeyType];
            this.name = NAMES[monkeyType];
        }
    }

    public static void drawMonkeySelectable(Graphics g, GameViewer viewer, int monkeyType, int x, int y, boolean isSelected) {
        g.setColor(Color.GRAY);
        g.fillRect(x - 20, y-10, SELECT_WIDTH + 40, SELECT_HEIGHT+45);
        if (isSelected) {
            g.setColor(Color.YELLOW);
            g.fillRoundRect(x - 20, y-8, SELECT_WIDTH + 40, SELECT_HEIGHT + 15, 8, 8);
        }
        g.drawImage(new ImageIcon("Resources/MonkeySelections/"+monkeyType+".png").getImage(), x, y, SELECT_WIDTH, SELECT_HEIGHT, viewer);
        g.setColor(Color.WHITE);
        g.drawString(NAMES[monkeyType], x - 20, y+80);
    }

    public static int getSelection(int x, int y) {
        // column one
        if (x > Game.SELECTION_START && x < Game.SELECTION_START+SELECT_WIDTH+(2*Game.SELECTION_X_PADDING)) {
            if (y < Game.SELECTION_Y_PADDING+SELECT_HEIGHT) {
                return Game.DART_MONKEY;
            }
        }
        // column two
        else if (x > Game.SELECTION_START+SELECT_WIDTH+(2*Game.SELECTION_X_PADDING) && x < Game.WINDOW_WIDTH) {
            return Game.DART_MONKEY;
        }
        return -1;
    }

    public void draw(Graphics g, GameViewer viewer) {
        super.draw(g, viewer);
    }

    public void shoot(Balloon[] balloons) {
        return;
        // iff the closest balloon's distance is less than range...
        // shoot in its direction then wait the delay
    }

    public int calcClosestBalloonDirection(Wave wave) {
        Balloon balloon = wave.getClosestBalloon(super.getX(), super.getY());
        return 0;
    }

    public int calcClosestBalloonDistance(Wave wave) {
        Balloon balloon = wave.getClosestBalloon(super.getX(), super.getY());
        return balloon.getDistance(super.getX(), super.getY());
    }
}

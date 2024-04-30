import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game implements MouseListener, MouseMotionListener, ActionListener {
    public static final int NORTH = 9,
                            EAST = 18,
                            SOUTH = 27,
                            WEST = 36;
    public static final int NODE_NUM_LENGTH = 65;
    public static final BalloonNode[] NODES = {new BalloonNode(605, 300, NORTH),
                            new BalloonNode(585, 100, WEST),
                            new BalloonNode(345, 100, SOUTH),
                            new BalloonNode(365, 630, WEST),
                            new BalloonNode(145, 605, NORTH),
                            new BalloonNode(175, 410, EAST),
                            new BalloonNode(770, 425, NORTH),
                            new BalloonNode(760, 215, EAST),
                            new BalloonNode(930, 220, SOUTH),
                            new BalloonNode(905, 580, WEST),
                            new BalloonNode(495, 555, SOUTH),
                            new BalloonNode(0, 0, 0)};
    public static final int SLEEP_TIME = 75;
    public static final int WINDOW_HEIGHT = 798,
                            WINDOW_WIDTH = 1539;
    public static final int SELECTION_START = 1239,
                            SELECTION_X_PADDING = 50,
                            SELECTION_Y_PADDING  = 70;
    public static final int PLAY_X_PADDING = 30,
                            PLAY_WIDTH = (WINDOW_WIDTH - SELECTION_START) - PLAY_X_PADDING*2,
                            PLAY_HEIGHT = 100,
                            PLAY_X_START = SELECTION_START + PLAY_X_PADDING,
                            PLAY_Y_START = WINDOW_HEIGHT - PLAY_HEIGHT - PLAY_X_PADDING;
    public static final int HEALTH_X_PADDING = 75,
                            HEART_X_PADDING = 60,
                            HEALTH_Y_PADDING = 75,
                            HEART_Y_PADDING = 60,
                            HEALTH_SPACING = 20;

    public static final int BALLOON_STARTING_X = -20,
                            BALLOON_STARTING_Y = 328,
                            BALLOON_SEPARATION = 14;
    public static final int DART_MONKEY = 0,
                            GLUE_MONKEY = 1,
                            CANNON = 2;
    public static final String BG_PATH = "Resources/MonkeyMeadow.png";
    private boolean[] selections;
    private int health;
    private int money;
    private int wave;
    private ArrayList<Monkey> monkeys;
    private Wave[] waves;
    private boolean isOver;
    private boolean isPlaying;
    private GameViewer viewer;
    private Timer timer;

    public Game() throws Exception {
        money = 100;
        health = 200;
        wave = 0;
        monkeys = new ArrayList<>();
        selections = new boolean[] {false, false, false};
        // read the waves from a csv
        // temporary hardcoded first wave
        waves = new Wave[1];
        ArrayList<Balloon> loons = new ArrayList<Balloon>();
        for (int i = 0; i < 15; i++) {
            loons.add(new Balloon(1, i));
        }
        waves[0] = new Wave(loons);
        isOver = false;
        isPlaying = false;
        viewer = new GameViewer(this);

        this.viewer.addMouseListener(this);
        this.viewer.addMouseMotionListener(this);
        timer = new Timer(SLEEP_TIME, this);
        timer.start();
    }

    public static void main(String[] args) throws Exception {
        Game g = new Game();
    }

    public void addMonkey(Monkey monkey) {
        monkeys.add(monkey);
    }

    public int getHealth() {
        return health;
    }
    public String getHealthDigit(int i) {
        if (Integer.toString(health).length() <= i) {
            return 0 + "";
        }
        return "" + (Integer.toString(health)).charAt(i);
    }

    public BalloonNode getNode(int nodeNum) {
        if (nodeNum >= NODES.length || nodeNum < 0) {
            return null;
        }
        return NODES[nodeNum];
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public Wave getCurrentWave() {
        return waves[wave];
    }

    public boolean[] getSelections() {
        return selections;
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<Monkey> getMonkeys() {
        return monkeys;
    }

    public void reduceHealth(int num) {
        health -= num;
        if (health <= 0) {
           isOver = true;
        }
        System.out.println(health);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println("clicked");

        // check if clicking play button...
        if (playButtonIsClicked(x, y)) {
            isPlaying = true;
            System.out.println("we playing now");
            return;
        }

        int monkeyType = Monkey.getSelection(x, y);
        // If clicked on the field...
        if (monkeyType == -1) {
            int selected = whichSelected();
            if (selected == -1) {
                // if nothing is selected, and they click somewhere else, do nothing
                return;
            }
            if (isOnMonkey(x, y)) {
                System.out.println("on monkey dummy");
            }
            // Check if placed on the path
            else if (viewer.containsGray(x, y)) {
                monkeys.add(new Monkey(selected, x, y));
                selections[selected] = false;
            } else {
                System.out.println("on gray dummy");
            }
//            viewer.repaint();
        }
        else {
            selections[monkeyType] = !selections[monkeyType];
//            viewer.repaint();
        }
    }

    private boolean playButtonIsClicked(int x, int y) {
        return (x > PLAY_X_START && x < PLAY_X_START+PLAY_WIDTH && y > PLAY_Y_START && y < PLAY_Y_START+PLAY_HEIGHT);
    }

    private boolean isOnMonkey(int x, int y) {
        for (Monkey m: monkeys) {
            if (Math.abs(m.getX() - x) < Sprite.DEFAULT_SPRITE_LENGTH / 3 &&
                    Math.abs(m.getY() - y) < Sprite.DEFAULT_SPRITE_LENGTH / 3) {
                System.out.println(Math.abs(m.getX() - x));
                return true;
            }
        }
        return false;
    }

    private int whichSelected() {
        for (int i = 0; i < selections.length; i++) {
            if (selections[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (whichSelected() != -1) {
            int x = e.getX();
            int y = e.getY();
            if (!monkeys.isEmpty()) {
                int radius = monkeys.get(whichSelected()).getRange();
            }
            // draw a circle around the center of the mouse as they drag it representing the radius of the selected monkey
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPlaying)
            getCurrentWave().moveBalloons(this);
        viewer.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}
}

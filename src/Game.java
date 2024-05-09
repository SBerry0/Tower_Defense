import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game implements MouseListener, MouseMotionListener, ActionListener {
    public static final int NORTH = 90,
                            EAST = 180,
                            SOUTH = 270,
                            WEST = 360;
    public static final int NODE_NUM_LENGTH = 65;
    public static final BalloonNode[] NODES = {new BalloonNode(605, 300, NORTH),
                            new BalloonNode(585, 100, WEST),
                            new BalloonNode(342, 100, SOUTH),
                            new BalloonNode(365, 630, WEST),
                            new BalloonNode(145, 605, NORTH),
                            new BalloonNode(175, 410, EAST),
                            new BalloonNode(770, 425, NORTH),
                            new BalloonNode(760, 215, EAST),
                            new BalloonNode(930, 220, SOUTH),
                            new BalloonNode(905, 580, WEST),
                            new BalloonNode(495, 555, SOUTH),
                            new BalloonNode(0, 0, 0)};
    public static final int SLEEP_TIME = 25;
    public static final int COUNTER_MAX = 950 / SLEEP_TIME;
    public static final int WINDOW_HEIGHT = 798,
                            WINDOW_WIDTH = 1539;
    public static final int SELECTION_START = 1239,
                            SELECTION_X_PADDING = 120,
                            SELECTION_Y_PADDING  = 70;
    public static final int PLAY_X_PADDING = 30,
                            PLAY_WIDTH = (WINDOW_WIDTH - SELECTION_START) - PLAY_X_PADDING*2,
                            PLAY_HEIGHT = 100,
                            PLAY_X_START = SELECTION_START + PLAY_X_PADDING,
                            PLAY_Y_START = WINDOW_HEIGHT - PLAY_HEIGHT - PLAY_X_PADDING - 15;
    public static final int HEALTH_X_PADDING = 75,
                            MONEY_X_PADDING = 233,
                            WAVE_X_PADDING = 967;
    public static final int HEART_X_PADDING = 15,
                            HEART_Y_PADDING = 15,
                            HEART_WIDTH = 52,
                            HEART_HEIGHT = 52;
    public static final int CHAR_SPACING = 20,
                            CHAR_Y_PADDING = 50;
    public static final int COIN_X_PADDING = 175,
                            COIN_Y_PADDING = 15,
                            COIN_WIDTH = 52,
                            COIN_HEIGHT = 52;
    public static final int BALLOON_STARTING_X = -20,
                            BALLOON_STARTING_Y = 328,
                            BALLOON_SEPARATION = 14;

    public static final int DART_MONKEY = 0,
                            GLUE_MONKEY = 1,
                            CANNON = 2;

    public static final int NUM_WAVES = 16,
                            STARTING_MONEY = 250,
                            STARTING_HEALTH = 200;
    public static final String BG_PATH = "Resources/MonkeyMeadow.png";
    public static final int PROJECTILE_HIT_DISTANCE = 50;
    private boolean[] selections;
    private int health;
    private int money;
    private int wave;
    private int counter;
    private ArrayList<Monkey> monkeys;
    private ArrayList<Projectile> activeProjectiles;
    private Wave[] waves;
    private boolean isOver;
    private boolean isPlaying;
    private GameViewer viewer;
    private Timer timer;

    public Game() throws Exception {
        money = STARTING_MONEY;
        health = STARTING_HEALTH;
        wave = 0;
        counter = 0;
        monkeys = new ArrayList<>();
        selections = new boolean[] {false, false, false};
        activeProjectiles = new ArrayList<>();
        // read the waves from a csv
        // temporary hardcoded first two waves
        Scanner sc = new Scanner(new File("Resources/balloonCount.csv"));
        int index = 0;
        waves = new Wave[NUM_WAVES];
        while (sc.hasNextLine())  //returns a boolean value
        {
            waves[index++] = new Wave(getBalloonsFromLine(sc.nextLine()));
        }
        sc.close();

        isOver = false;
        isPlaying = false;
        viewer = new GameViewer(this);

        this.viewer.addMouseListener(this);
        this.viewer.addMouseMotionListener(this);
        timer = new Timer(SLEEP_TIME, this);
        timer.start();
    }

    // Modified from https://www.baeldung.com/java-csv-file-array
    public ArrayList<Balloon> getBalloonsFromLine(String line) throws IOException {
        int balloonNum = 0;
        int health = 1;
        ArrayList<Balloon> balloons = new ArrayList<Balloon>();
        Scanner rowScanner = new Scanner(line);
        rowScanner.useDelimiter(",");   //sets the delimiter pattern
        while (rowScanner.hasNext()) {
            int num = Integer.parseInt(rowScanner.next());
            for (int i = 0; i < num; i++) {
                balloons.add(new Balloon(health, balloonNum++));
            }
            health++;
        }
        return balloons;
    }

    public static void main(String[] args) throws Exception {
        Game g = new Game();
    }

    public void addProjectile(Projectile projectile) {
        activeProjectiles.add(projectile);
    }

    public void refreshProjectiles() {
        for (int i = 0; i < activeProjectiles.size(); i++) {
            if (!activeProjectiles.get(i).isActive()) {
                activeProjectiles.remove(i--);
            }
        }
    }

    private void incrementCounter() {
        counter = (counter + 1) % COUNTER_MAX;
    }

    public int getHealth() {
        return health;
    }

    public String getIntThreeDigit(int i, int number) {
        if (Integer.toString(number).length() == 4) {
            if (i == 0)
                return (Integer.toString(number)).charAt(i) + ",";
            return "" + (Integer.toString(number)).charAt(i);
        }
        if (Integer.toString(number).length() == 3)
            return "" + (Integer.toString(number)).charAt(i);
        if (Integer.toString(number).length() == 2) {
            if (i == 0)
                return "0";
            return "" + (Integer.toString(number)).charAt(i-1);
        }
        if (Integer.toString(number).length() == 1) {
            if (i == 0 || i == 1)
                return "0";
            return "" + (Integer.toString(number)).charAt(0);
        }
        return "0";
    }

    public String getIntTwoDigit(int i, int number) {
        if (Integer.toString(number).length() == 2) {
            return "" + (Integer.toString(number)).charAt(i);
        }
        if (Integer.toString(number).length() == 1) {
            if (i == 0)
                return "0";
            return "" + (Integer.toString(number)).charAt(0);
        }
        return "0";
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

    public int getWaveNum() {
        return wave;
    }

    public boolean[] getSelections() {
        return selections;
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<Projectile> getActiveProjectiles() {
        return activeProjectiles;
    }

    public ArrayList<Monkey> getMonkeys() {
        return monkeys;
    }

    public void reduceHealth(int num) {
        health -= num;
        if (health <= 0) {
           isOver = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
//        System.out.println("clicked");

        // check if clicking play button...
        if (playButtonIsClicked(x, y)) {
            isPlaying = true;
//            System.out.println("we playing now");
            return;
        }

        int monkeyType = GameViewer.getMonkeySelection(x, y);
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
            else if (!viewer.isOnGray(x, y)) {
                try {
                    if (Monkey.PRICES[selected] <= money) {
                        money -= Monkey.PRICES[selected];
                        monkeys.add(new Monkey(selected, x, y, counter));
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                selections[selected] = false;
            } else {
                System.out.println("on gray dummy");
            }
        }
        else {
            selections[monkeyType] = !selections[monkeyType];
        }
    }

    public void incrementMoney() {
        money++;
        System.out.println(money);
    }

    private boolean playButtonIsClicked(int x, int y) {
        return (x > PLAY_X_START && x < PLAY_X_START+PLAY_WIDTH && y > PLAY_Y_START && y < PLAY_Y_START+PLAY_HEIGHT);
    }

    private boolean isOnMonkey(int x, int y) {
        for (Monkey m: monkeys) {
            if (Math.abs(m.getX() - x) < Sprite.DEFAULT_SPRITE_LENGTH / 3 &&
                    Math.abs(m.getY() - y) < Sprite.DEFAULT_SPRITE_LENGTH / 3) {
//                System.out.println(Math.abs(m.getX() - x));
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

    public Monkey getClosestMonkey(int x, int y) {
        int smallestDist = Integer.MAX_VALUE;
        Monkey closestMonkey = null;
        for (int i = 0; i < monkeys.size(); i++) {
            int dist = monkeys.get(i).getDistance(x, y);
            if (dist < smallestDist) {
                smallestDist = dist;
//                System.out.println("distance: " + smallestDist);
                closestMonkey = monkeys.get(i);
            }
        }
        return closestMonkey;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int radius = 100;
        if (GameViewer.getMonkeySelection(x, y) == -1) {
            // if about to place a monkey, draw a circle around the mouse
            if (whichSelected() != -1 && !monkeys.isEmpty()) {
                radius = monkeys.get(whichSelected()).getRange();
                // draw a circle around the mouse
            } else {
                if (isOnMonkey(x, y)) {
                    Monkey monkey = getClosestMonkey(x, y);
                    radius = monkey.getRange();
//                    int centerX = monkeyString.split(",");
                    // draw a circle with the center point of the monkey's center with radius of the range
                }
            }
        }
            // draw a circle around the center of the mouse as they drag it representing the radius of the selected monkey
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isOver) {
            if (isPlaying)
                getCurrentWave().moveBalloons(this);
            refreshProjectiles();
            if (getCurrentWave() == null) {
                isOver = true;
                return;
            }
            getCurrentWave().refreshBalloons();
            incrementCounter();
            for (Projectile p : activeProjectiles) {
                try {
                    p.move(this);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            for (Monkey m : monkeys) {
//            System.out.println("monkey: " + m.getDelayNum());
                if (m.getDelayNum() == counter) {
                    try {
                        m.shoot(getCurrentWave(), this);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            viewer.repaint();
            if (getCurrentWave().isEmpty()) {
                isPlaying = false;
                nextWave();
            }
        } else {
            System.out.println("game over");
            timer.stop();
        }
    }

    private void nextWave() {
        wave++;
        if (wave >= waves.length) {
            isOver = true;
        }
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

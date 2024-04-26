import java.awt.event.*;
import java.util.ArrayList;

public class Game implements MouseListener, MouseMotionListener, ActionListener {
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

    public static final int BALLOON_STARTING_X = -20,
                            BALLOON_STARTING_Y = 328;
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
            loons.add(new Balloon(1, i, waves[0]));
        }
        waves[0] = new Wave(loons);
        isOver = false;
        isPlaying = false;
        viewer = new GameViewer(this);

        this.viewer.addMouseListener(this);
        this.viewer.addMouseMotionListener(this);
        // something for mouse input
    }

    public void play() {
        if (isPlaying) {
            wave++;
            if (wave == waves.length) {
                System.out.println("You won!");
                isOver = true;
            }
            waves[wave].moveBalloons();
        } else {
            // present play button
        }
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

    public boolean isPlaying() {
//        return isPlaying;
        return true;
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

    public void reduceHealth() {
        health--;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println("clicked");

        int monkeyType = Monkey.getSelection(x, y);
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
            else if (viewer.containsGray(x, y, Sprite.DEFAULT_SPRITE_LENGTH, Sprite.DEFAULT_SPRITE_LENGTH)) {
                monkeys.add(new Monkey(selected, x, y));
                selections[selected] = false;
            } else {
                System.out.println("on gray dummy");
            }
            viewer.repaint();
        }
        else {
            selections[monkeyType] = !selections[monkeyType];
            viewer.repaint();
        }
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
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        viewer.repaint();
    }
}

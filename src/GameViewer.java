import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameViewer extends JFrame {
    public static final int GRAY_TOLERANCE = 800,
                            COLOR_TOLERANCE = 15;
    private BufferedImage map;
    private Game game;

    public GameViewer(Game g) throws Exception {
        this.game = g;
        File file= new File(Game.BG_PATH);
        map = ImageIO.read(file);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Tower Defense");
        this.setSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
        this.setVisible(true);
        createBufferStrategy(2);
    }

    public boolean containsGray(int xValue, int yValue) {
        // From https://www.tutorialspoint.com/how-to-get-pixels-rgb-values-of-an-image-using-java-opencv-library
        // Modified by Sohum Berry

        // Get top left coordinate to traverse from
        yValue -= Sprite.DEFAULT_SPRITE_LENGTH/2;
        xValue -= Sprite.DEFAULT_SPRITE_LENGTH/2;
        int numGray = 0;
        for (int y = yValue; y < yValue + Sprite.DEFAULT_SPRITE_LENGTH; y+=2) {
            for (int x = xValue; x < xValue + Sprite.DEFAULT_SPRITE_LENGTH; x+=2) {
                if (x >= 0 && x < Game.SELECTION_START &&
                        y > 0 && y < Game.WINDOW_HEIGHT) {
                    int pixel = map.getRGB(x, y);
                    //Creating a Color object from pixel value
                    Color color = new Color(pixel, true);
                    int avg = getAverage(new int[]{color.getRed(), color.getGreen(), color.getBlue()});
                    if (Math.abs(color.getRed() - avg) < COLOR_TOLERANCE &&
                            Math.abs(color.getGreen() - avg) < COLOR_TOLERANCE &&
                            Math.abs(color.getBlue() - avg) < COLOR_TOLERANCE) {
                        numGray++;
                        if (numGray > GRAY_TOLERANCE) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    private int getAverage(int[] nums) {
        int total = 0;
        for(int i : nums) {
            total += i;
        }
        return total / nums.length;
    }

    public void drawPlayButton(Graphics g) {
        g.setColor(Color.green);
        // change to image of gradient later
        g.fillRoundRect(Game.PLAY_X_START, Game.PLAY_Y_START, Game.PLAY_WIDTH, Game.PLAY_HEIGHT, 20, 20);
        Polygon p = new Polygon();
        p.addPoint(Game.PLAY_X_START + (Game.PLAY_WIDTH / 2) - 23, Game.PLAY_Y_START + 15);
        p.addPoint(Game.PLAY_X_START + (Game.PLAY_WIDTH / 2) - 23, Game.PLAY_Y_START + (Game.PLAY_HEIGHT-15));
        p.addPoint(Game.PLAY_X_START + (Game.PLAY_WIDTH / 2) - 23 + (int) (Math.sqrt(Math.pow((Game.PLAY_HEIGHT-30), 2) - Math.pow((Game.PLAY_HEIGHT-30)/2, 2))), Game.PLAY_Y_START + (Game.PLAY_HEIGHT-30)/2 + 15);
        g.setColor(Color.WHITE);
        g.fillPolygon(p);
    }

    public void paint(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
        g.drawImage(map, 0, 0, Game.SELECTION_START, Game.WINDOW_HEIGHT, this);
        Monkey.drawMonkeySelectable(g, this, Game.DART_MONKEY, Game.SELECTION_START + Game.SELECTION_X_PADDING, Game.SELECTION_Y_PADDING, game.getSelections()[Game.DART_MONKEY]);
        if (game.isPlaying()) {
            for (Balloon b : game.getCurrentWave().getBalloons()) {
                b.draw(g, this);
            }
        }
        for (Monkey m : game.getMonkeys()) {
            m.draw(g, this);
        }
        if (!game.isPlaying()) {
            drawPlayButton(g);
        }
        for (BalloonNode node : Game.NODES) {
            node.draw(g);
        }
    }
}
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameViewer extends JFrame {
    public static final int GRAY_TOLERANCE = 800,
                            COLOR_TOLERANCE = 15;
    private static final int SELECT_WIDTH = 44,
                            SELECT_HEIGHT = 57;
    private BufferedImage map;
    private Game game;

    public GameViewer(Game g) throws Exception {
        this.game = g;
        File file= new File(Game.BG_PATH);
        map = ImageIO.read(file);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Tower Defense");
        this.setSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
        this.add(new DrawingPanel());
        this.setVisible(true);
        createBufferStrategy(2);
    }

    public static int getMonkeySelection(int x, int y) {
        if (x > Game.SELECTION_START && x < Game.WINDOW_WIDTH) {
            if (y < Game.SELECTION_Y_PADDING+SELECT_HEIGHT+100) {
                return Game.DART_MONKEY;
            }
            if (y < Game.SELECTION_Y_PADDING+SELECT_HEIGHT+300) {
                return Game.GLUE_MONKEY;
            }
            if (y < Game.SELECTION_Y_PADDING+2*SELECT_HEIGHT+600) {
                return Game.CANNON;
            }
        }
        return -1;
    }
    public boolean isOnGray(int xValue, int yValue) {
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
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private int getAverage(int[] nums) {
        int total = 0;
        for(int i : nums) {
            total += i;
        }
        return total / nums.length;
    }

    public void drawMonkeySelectable(Graphics g, GameViewer viewer, int monkeyType, int x, int y, boolean isSelected) {
        g.setColor(Color.GRAY);
        g.fillRect(x - 20, y-25, SELECT_WIDTH + 40, SELECT_HEIGHT+45);
        if (isSelected) {
            g.setColor(Color.YELLOW);
            g.fillRoundRect(x - 35, y-23, SELECT_WIDTH + 70, SELECT_HEIGHT + 15, 8, 8);
        }
        Image i = new ImageIcon("Resources/MonkeySelections/"+monkeyType+".png").getImage();
        g.drawImage(i, x, y-15, i.getWidth(viewer) / 10, i.getHeight(viewer) / 10, viewer);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Luckiest Guy", Font.BOLD, 18));
        g.drawString(Monkey.NAMES[monkeyType], x - 33, y+65);
        g.setFont(new Font("Luckiest Guy", Font.BOLD, 20));
        g.drawString("$" + Monkey.PRICES[monkeyType], x - 5, y+88);
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

    // Found this code online to stop the flickering of the screen
    private class DrawingPanel extends JPanel {
        private void drawHealth(Graphics g) {
            g.drawImage(new ImageIcon("Resources/black heart.png").getImage(), Game.HEART_X_PADDING+2, Game.HEART_Y_PADDING-3, Game.HEART_WIDTH, Game.HEART_HEIGHT, this);
            g.drawImage(new ImageIcon("Resources/heart.png").getImage(), Game.HEART_X_PADDING, Game.HEART_Y_PADDING, Game.HEART_WIDTH, Game.HEART_HEIGHT, this);
            for (int i = 0; i < 3; i++) {
                g.setFont(new Font("Luckiest Guy", Font.BOLD, 40));
                g.setColor(Color.BLACK);
                g.drawString(game.getIntThreeDigit(i, game.getHealth()), Game.HEALTH_X_PADDING + i*Game.CHAR_SPACING, Game.CHAR_Y_PADDING);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Luckiest Guy", Font.BOLD, 32));
                g.drawString(game.getIntThreeDigit(i, game.getHealth()), Game.HEALTH_X_PADDING + i*(Game.CHAR_SPACING+2), Game.CHAR_Y_PADDING-2);
            }
        }

        private void drawMoney(Graphics g) {
            g.drawImage(new ImageIcon("Resources/black coin.png").getImage(), Game.COIN_X_PADDING+2, Game.COIN_Y_PADDING-3, Game.COIN_WIDTH, Game.COIN_HEIGHT, this);
            g.drawImage(new ImageIcon("Resources/coin.png").getImage(), Game.COIN_X_PADDING, Game.COIN_Y_PADDING, Game.COIN_WIDTH, Game.COIN_HEIGHT, this);
            for (int i = 0; i < 3; i++) {
                g.setFont(new Font("Luckiest Guy", Font.BOLD, 40));
                g.setColor(Color.BLACK);
                g.drawString(game.getIntThreeDigit(i, game.getMoney()), Game.MONEY_X_PADDING + i*Game.CHAR_SPACING, Game.CHAR_Y_PADDING);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Luckiest Guy", Font.BOLD, 32));
                g.drawString(game.getIntThreeDigit(i, game.getMoney()), Game.MONEY_X_PADDING + i*(Game.CHAR_SPACING+2), Game.CHAR_Y_PADDING-2);
            }
        }

        private void drawWave(Graphics g) {
            String out = "Wave";
            for (int i = 0; i < 4; i++) {
                g.setFont(new Font("Luckiest Guy", Font.BOLD, 40));
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(out.charAt(i)), Game.WAVE_X_PADDING + i*Game.CHAR_SPACING, Game.CHAR_Y_PADDING);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Luckiest Guy", Font.BOLD, 32));
                g.drawString(String.valueOf(out.charAt(i)), Game.WAVE_X_PADDING + i*(Game.CHAR_SPACING+2), Game.CHAR_Y_PADDING-2);
            }

            for (int i = 0; i < 2; i++) {
                g.setFont(new Font("Luckiest Guy", Font.BOLD, 40));
                g.setColor(Color.BLACK);
                g.drawString(game.getIntTwoDigit(i, game.getWaveNum() + 1), Game.WAVE_X_PADDING + 110 + i*Game.CHAR_SPACING, Game.CHAR_Y_PADDING);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Luckiest Guy", Font.BOLD, 32));
                g.drawString(game.getIntTwoDigit(i, game.getWaveNum() + 1), Game.WAVE_X_PADDING + 110 + i*(Game.CHAR_SPACING+2), Game.CHAR_Y_PADDING-2);
            }
            out = "/15";
            for (int i = 0; i < 3; i++) {
                g.setFont(new Font("Luckiest Guy", Font.BOLD, 40));
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(out.charAt(i)), Game.WAVE_X_PADDING + 150 + i*Game.CHAR_SPACING, Game.CHAR_Y_PADDING);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Luckiest Guy", Font.BOLD, 32));
                g.drawString(String.valueOf(out.charAt(i)), Game.WAVE_X_PADDING + 150 + i*(Game.CHAR_SPACING+2), Game.CHAR_Y_PADDING-2);
            }

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.GRAY);
            g.fillRect(0, -15, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
            g.drawImage(map, 0, -15, Game.SELECTION_START, Game.WINDOW_HEIGHT, this);

            for (int i = 0; i < 3; i++) {
                drawMonkeySelectable(g, GameViewer.this, i,
                        Game.SELECTION_START + Game.SELECTION_X_PADDING, Game.SELECTION_Y_PADDING + i*200,
                        game.getSelections()[i]);
            }

            if (game.isPlaying()) {
                for (Balloon b : game.getCurrentWave().getBalloons()) {
                    b.draw(g, GameViewer.this);
                }
            }
            for (Projectile p : game.getActiveProjectiles()) {
                p.draw(g, GameViewer.this);
            }
            for (Monkey m : game.getMonkeys()) {
                m.draw(g, GameViewer.this, game);
            }
            if (!game.isPlaying()) {
                drawPlayButton(g);
            }
            drawHealth(g);
            drawMoney(g);
            drawWave(g);
        }
    }
}
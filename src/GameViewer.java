import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
public class GameViewer extends JFrame {
    private Image map;
    private Game game;

    public GameViewer(Game g) {
        this.game = g;
        map = new ImageIcon(Game.BG_PATH).getImage();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Tower Defense");
        this.setSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
        this.setVisible(true);
        createBufferStrategy(2);
    }

    public void paint(Graphics g) {
        g.drawImage(map, 0, 0, 1239, Game.WINDOW_HEIGHT, this);
        Monkey.drawMonkeySelectable(g, this, Game.DART_MONKEY, Game.SELECTION_START + Game.SELECTION_X_PADDING, Game.SELECTION_Y_PADDING, game.getSelections()[Game.DART_MONKEY]);
        for (Monkey m : game.getMonkeys()) {
            m.draw(g, this);
        }
        for (Balloon b : game.getCurrentWave().getBalloons()) {
            b.draw(g, this);
        }
    }
}

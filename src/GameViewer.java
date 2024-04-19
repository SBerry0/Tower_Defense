import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
public class GameView extends JFrame {
    private Image map;
    private Game game;

    public GameView(Game g) {
        this.game = g;
        map = new ImageIcon(Game.BG_PATH).getImage();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("The Aquarium");
        this.setSize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
        this.setVisible(true);
        createBufferStrategy(2);
    }

    public void paint(Graphics g) {
        BufferStrategy bf = this.getBufferStrategy();
        if (bf == null)
            return;
        Graphics g2 = null;
        try {
            g2 = bf.getDrawGraphics();
            myPaint(g2);
        }
        finally {
            g2.dispose();
        }
        bf.show();
        Toolkit.getDefaultToolkit().sync();
    }

    public void myPaint(Graphics g) {
        g.drawImage(map, 0, 0, this);
        for (Monkey m : game.getMonkeys()) {
            m.draw(g);
        }

    }
}

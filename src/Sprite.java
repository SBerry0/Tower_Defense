import java.awt.*;

public class Sprite {
    private Image image;
    private int x, y;
    private int width, height;

    public Sprite (Image image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 100;
    }

    public Image getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g, GameViewer v) {

        g.drawImage(image, x, y, width, height, v);
    }
}

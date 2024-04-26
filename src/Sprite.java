import java.awt.*;

public class Sprite {
    public static final int DEFAULT_SPRITE_LENGTH = 90;
    private Image image;
    private int x, y;
    private int width, height;

    public Sprite (Image image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = DEFAULT_SPRITE_LENGTH;
        this.height = DEFAULT_SPRITE_LENGTH;
    }
    public Sprite (Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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

        g.drawImage(image, x-width/2, y-height/2, width, height, v);
    }
}

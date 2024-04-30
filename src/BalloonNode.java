import java.awt.*;

public class BalloonNode {
    private int minX, maxX;
    private int minY, maxY;
    private int newDirection;

    public BalloonNode(int minX, int minY, int newDirection) {
        this.minX = minX;
        this.maxX = minX + Game.NODE_NUM_LENGTH;
        this.minY = minY;
        this.maxY = minY + Game.NODE_NUM_LENGTH;
        this.newDirection = newDirection;
    }

    public int getNewDirection() {
        return newDirection;
    }

    public boolean isOnNode(int x, int y) {
        return x > minX && x < maxX && y > minY && y < maxY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.pink);
        g.fillRect(minX, minY, maxX- minX, maxY - minY);
    }
}

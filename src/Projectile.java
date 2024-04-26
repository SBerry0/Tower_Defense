import javax.swing.*;

public class Projectile extends Sprite {
    private int direction;
    private int speed;
    private int numPopped;
    private int piercing;
    public Projectile(int dartType, int x, int y) {
        super(new ImageIcon("Resources/Darts/"+dartType).getImage(), x, y, 10, 40);
        if (dartType < Monkey.NUM_MONKEYS) {
            this.piercing = Monkey.PIERCINGS[dartType];
            this.speed = Monkey.SPEED[dartType];
        }
    }

    public boolean isTouchingBalloon (Wave balloons) {
        if (super.toString().equals(balloons.getClosestBalloon(super.getX(), super.getY()).toString())) {
            return true;
        }
        return false;
    }

}

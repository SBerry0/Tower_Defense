import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Projectile extends Sprite {
    public static final int WIDTH = 60, HEIGHT = 56;
    private int speed;
    private int piercing;
    private boolean isActive;
    private int deltaX, deltaY;
    private int width, height;
    private int dartType;
    private ArrayList<Balloon> poppedBalloons;

    public Projectile(int dartType, int x, int y, Wave currentWave) throws IOException {
        super(ImageIO.read(new File("Resources/Darts/"+dartType+".png")), x, y, WIDTH, HEIGHT);
        if (dartType < Monkey.NUM_MONKEYS) {
            this.poppedBalloons = new ArrayList<>();
            this.piercing = Monkey.PIERCINGS[dartType];
            this.speed = Monkey.SPEED[dartType];
            this.isActive = true;
            this.width = dartType == 1 ? 90 : WIDTH;
            this.height = dartType == 1 ? 38 : HEIGHT;
            this.dartType = dartType;

            int diffX = currentWave.getClosestBalloon(x, y).getX() - x;
            int diffY = currentWave.getClosestBalloon(x, y).getY() - y;

            int origDist = (int) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

            int ratio = origDist / speed;
            if (ratio == 0) {
                this.deltaX = diffX;
                this.deltaY = diffY;
            } else {
                this.deltaX = diffX / ratio;
                this.deltaY = diffY / ratio;
            }
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isTouchingBalloon (Game game) throws IOException {
        Balloon closest = game.getCurrentWave().getClosestBalloon(super.getX(), super.getY());
        if (closest == null) {
            return false;
        }
        if (closest.getDistance(super.getX(), super.getY()) < Game.PROJECTILE_HIT_DISTANCE) {
            if (!poppedBalloons.contains(closest)) {

                System.out.println("HIT!");
                if (dartType == Game.DART_MONKEY || dartType == Game.CANNON) {
                    closest.reduceHealth();
                } else {
                    closest.slowDown();
                }
                game.addMoney(closest.getHealth());
                poppedBalloons.add(closest);
                return true;
            }
        }
        return false;
    }

    public void move() throws IOException {
        if (poppedBalloons.size() >= piercing || super.getX() < 0 || super.getX() > Game.SELECTION_START ||
                super.getY() < 0 || super.getY() > Game.WINDOW_HEIGHT) {
            isActive = false;
        }
        super.addX(deltaX);
        super.addY(deltaY);
    }

    public void draw(Graphics g, GameViewer viewer) {
        double theta = deltaX > 0 ? Math.atan((double) (deltaY) / (deltaX)) :
                (Math.toRadians(180) + Math.atan((double) (deltaY) / (deltaX)));
        if (deltaX == 0) {
            if (deltaY < 0) {
                theta = Math.toRadians(90);
            } else {
                theta = Math.toRadians(270);
            }
        }
        g.drawImage(Sprite.rotateImage(super.getImage(), theta),
                super.getX(), super.getY(), width, height, viewer);
    }

}

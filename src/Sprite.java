import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Sprite {
    public static final int DEFAULT_SPRITE_LENGTH = 90;
    private BufferedImage image;
    private int x, y;
    private int width, height;

    public Sprite (BufferedImage image, int x, int y) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = DEFAULT_SPRITE_LENGTH;
        this.height = DEFAULT_SPRITE_LENGTH;
    }
    public Sprite (BufferedImage image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addX(int x) {
        this.x += x;
    }

    public void addY(int y) {
        this.y += y;
    }

    // From: https://stackoverflow.com/a/47064247
//    public BufferedImage rotateImage (BufferedImage image, double rotationRequired) { //n rotation in radians
//
//        double locationX = image.getWidth() / 2;
//        double locationY = image.getHeight() / 2;
//        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
//        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
//        BufferedImage newImage =new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
//        op.filter(image, newImage);
//
//        //this.img = newImage;
//        return(newImage);
//    }

    public static BufferedImage rotateImage(BufferedImage buffImage, double radian) {
        double sin = Math.abs(Math.sin(radian));
        double cos = Math.abs(Math.cos(radian));

        int width = buffImage.getWidth();
        int height = buffImage.getHeight();

        int nWidth = (int) Math.floor((double) width * cos + (double) height * sin);
        int nHeight = (int) Math.floor((double) height * cos + (double) width * sin);

        BufferedImage rotatedImage = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = rotatedImage.createGraphics();

        graphics.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        graphics.translate((nWidth - width) / 2, (nHeight - height) / 2);
        // rotation around the center point
        graphics.rotate(radian, (double) (width / 2), (double) (height / 2));
        graphics.drawImage(buffImage, 0, 0, null);
        graphics.dispose();

        return rotatedImage;
    }

    public void draw(Graphics g, GameViewer v) {
        g.drawImage(image, x-width/2, y-height/2 - 15, width, height, v);
    }

    public String toString() {
        return x + ", " + y;
    }

    public int getDistance(int x, int y) {
        int a = this.x - x;
        int b = this.y - y;
        return (int) (Math.sqrt((a*a)+(b*b)));
    }

    protected void changeImage(BufferedImage image) {
        this.image = image;
    }
}

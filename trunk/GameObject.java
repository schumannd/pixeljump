import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;


public abstract class GameObject extends Sprite {
    protected double posX;
    protected double posY;

    public GameObject(Image image, double x, double y) {
        super(image);
        posX = x;
        posY = y;
        setRefPixelPosition((int) posX, (int) posY);
    }
    
    public void moveDown(double dist) {
        posY += dist;
        setRefPixelPosition((int) posX, (int) posY);
    }
}

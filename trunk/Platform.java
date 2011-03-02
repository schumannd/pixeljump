import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.*;
public class Platform extends Sprite {
    
    public int type; // 0 = normal, 1 = break, 2 = fake
    public double posX;
    public double posY;
    public int size;
    
    
    public Platform(Image img, double x, double y, int l, int t) {
        super(img, 30, 3);
        posX = x;
        posY = y;
        setRefPixelPosition((int) posX, (int) posY);
        size = l;
        type = t;
    }   

}
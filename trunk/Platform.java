import java.util.Random;

import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.*;
import java.util.Random;

public class Platform extends Sprite {
    
    public int type; // 0 = normal, 1 = break, 2 = fake
    public double posX;
    public double posY;
    public int size;
    public Item item= null;
    private Random r = new Random();
    
    public static final int NORMAL = 0;
    public static final int BREAK = 1;
    public static final int FAKE = 2;
    public static final int MOVE = 3;
    
    boolean moves = false;
    
    
    public Platform(Image img, double x, double y, int l, int t) {
        super(img, 30, 3);
        posX = x;
        posY = y;
        setRefPixelPosition((int) posX, (int) posY);
        size = l;
        type = t;
        
        if (new Random().nextDouble() < 0.5)
            moves = true;

    }
    
    
    public void moveDown(double dist) {
        posY += dist;
        setRefPixelPosition((int) posX, (int) posY);
    }
    
    
    public void moveSide(int ms) {
        if (!moves)
            return;
//        posX += 20f/(1000f/ms);
//        setRefPixelPosition((int) posX, (int) posY);
    }


}
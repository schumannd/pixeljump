import java.util.Random;
import javax.microedition.lcdui.*;

public class Platform extends GameObject{
    
    public int type; // 0 = normal, 1 = break, 2 = fake
    public int size;
    public Item item = null;
    private Random r = new Random();
    
    public static final int NORMAL = 0;
    public static final int BREAK = 1;
    public static final int FAKE = 2;
    public static final int MOVE = 3;
    
    boolean moves = false;
    
    
    public Platform(double x, double y, int t) {
        super(Tools.platImages[t], x, y);
        size = getWidth();
        type = t;
        
        if (r.nextDouble() < 0.5)
            moves = true;
    }
    
    
    public void moveDown(double dist) {
        super.moveDown(dist);
        if (item != null) {
            item.moveDown(dist);
        }
    }
    
    
    public void moveSide(int ms) {
        if (!moves)
            return;
//        posX += 20f/(1000f/ms);
//        setRefPixelPosition((int) posX, (int) posY);
        if (item != null)
            item.updatePos();
    }
    
    
    public void paint2(Graphics g) {
        if (this.posY < -10)
            return;
        super.paint(g);
        if (item != null)
            item.paint(g);
    }
}
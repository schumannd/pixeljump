import javax.microedition.lcdui.Graphics;

public class Platform extends GameObject{
    
    public int type;
    public Item item = null;
    
    public static final int NORMAL = 0;
    public static final int BREAK = 1;
    public static final int FAKE = 2;
    public static final int MOVE = 3;
    
    private boolean moves = false;
    
    
    public Platform(double x, double y, int type, boolean moves) {
        super(Tools.platImages[type], x, y);
        this.type = type;
        this.moves = moves;
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
//        if (item != null)
//            item.updatePos();
    }
    
    
    public void paint2(Graphics g) {
        if (this.posY < -10)
            return;
        super.paint(g);
        if (item != null)
            item.paint(g);
    }
}

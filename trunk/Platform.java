import javax.microedition.lcdui.Graphics;

public class Platform extends GameObject{
    
    private int type;
    public Item item = null;
    
    public static final int NORMAL = 0;
    public static final int BREAK = 1;
    public static final int FAKE = 2;
    public static final int MOVE = 3;
    
    private boolean moves = false;
    private final double startX;
    private double startY;
    private final double endX;
    private double endY;
    
    
    public Platform(double x, double y, int type, double xdist, double ydist) {
        super(Tools.platImages[type], x, y);
        startX = x;
        startY = y;
        if (xdist != 0 || ydist != 0)
            moves = true;
        endX = startX + xdist;
        endY = startY + ydist;
        this.type = type;
        this.moves = moves;
    }
    
    public int getType() {
        return type;
    }
    
    public void moveDown(double dist) {
        super.moveDown(dist);
        startY += dist;
        endY += dist;
        if (item != null) {
            item.moveDown(dist);
        }
    }
    
    
    public void moveSide(int ms) {
        if (!moves)
            return;
        
        //posX += 1;
        setRefPixelPosition((int) posX, (int) posY);
        //if (item != null)
            //item.updatePos();
    }
    
    
    public void paint2(Graphics g) {
        if (this.posY < -10)
            return;
        super.paint(g);
        if (item != null)
            item.paint(g);
    }
}

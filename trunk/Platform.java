import javax.microedition.lcdui.Graphics;

public class Platform extends GameObject{
    
    private int type;
    public Item item = null;
    
    public static final int NORMAL = 0;
    public static final int BREAK = 1;
    public static final int FAKE = 2;
    public static final int MOVE = 3;
    
    private boolean moves = false;
    
    private double [][] coords;
    private int targetCoord = 0;
    private final double SPEED = 10;
    private double distToTarget = 0;
    
    
    public Platform(int type, double [][] coordinates) {
        super(Tools.platImages[type], coordinates[0][0], coordinates[0][1]);
        this.coords = coordinates;
        if (coordinates.length > 1) {
            moves = true;
            targetCoord = 1;
            double distX = posX - coords[targetCoord][0];
            double distY = posY - coords[targetCoord][1];
            distToTarget = Math.sqrt(distX*distX + distY*distY);
        }
        this.type = type;
        this.moves = moves;
    }
    
    public int getType() {
        return type;
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
        
        double distX = posX - coords[targetCoord][0];
        double distY = posY - coords[targetCoord][1];
        posX += distX * (SPEED/distToTarget);
        posY += distY * (SPEED/distToTarget);
        
        distToTarget = Math.sqrt(distX*distX + distY*distY);
        
        //posX += 1;
        setRefPixelPosition((int) posX, (int) posY);
        //if (item != null)
            //item.updatePos();
    }
    
    private double distToNextCoord() {
        double distX = posX - coords[targetCoord][0];
        double distY = posY - coords[targetCoord][1];
        return Math.sqrt(distX*distX + distY*distY);
    }
    
    
    public void paint2(Graphics g) {
        if (this.posY < -10)
            return;
        super.paint(g);
        if (item != null)
            item.paint(g);
    }
}

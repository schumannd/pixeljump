import javax.microedition.lcdui.game.Sprite;


public class Monster extends Sprite{
    
    private double posX;
    private double posY;
    private int type;
    
    public Monster(double x, double y, int t) {
        super(Tools.monsterImages[t]);
        posX = x;
        posY = y;
        setRefPixelPosition((int) posX, (int) posY);
        type = t;
    }
    
    public void moveDown(double dist) {
        posY += dist;
        setRefPixelPosition((int) posX, (int) posY);
    }
}

import javax.microedition.lcdui.game.Sprite;


public class Projectile extends Sprite {
    
    double posX;
    double posY;
    private double SPEED = -50;
    
    
    public Projectile(double x, double y){
        super(Tools.projectileImage);
        this.posX = x;
        this.posY = y;
    }
    
    public void move(int ms) {
        posY += SPEED * (15*ms/1000d);
        setRefPixelPosition((int) posX, (int) posY);
    }
    
    public void moveDown(double dist) {
        posY += dist;
        setRefPixelPosition((int) posX, (int) posY);
    }
}

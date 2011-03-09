
public class Projectile extends GameObject {
    private double SPEED = -50;
    
    
    public Projectile(double x, double y){
        super(Tools.projectileImage, x, y);
    }
    
    public void move(int ms) {
        posY += SPEED * (15*ms/1000d);
        setRefPixelPosition((int) posX, (int) posY);
    }
}

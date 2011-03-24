
public class Projectile extends GameObject {
    private double speed = -50;
    private double speedX;
    private double speedY;
    
    public Projectile(double x, double y, double addSpeed){
        super(Tools.projectileImage, x, y);
        speed += addSpeed;
    }
    
    public void move(double time) {
        posY += speedY * time;
        posX += speedX * time;
        setRefPixelPosition((int) posX, (int) posY);
    }
    
    public void setDirection (double xdist, double ydist) {
        double dist = Math.sqrt(xdist*xdist+ydist*ydist);
        double fraction = speed / dist;
        speedX = xdist * fraction;
        speedY = ydist * fraction;
    }
}

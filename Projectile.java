
public class Projectile extends GameObject {
    private double SPEED = -50;
    private double speedX;
    private double speedY;
    
    public Projectile(double x, double y){
        super(Tools.projectileImage, x, y);
    }
    
    public void move(int ms) {
        //posY += SPEED * (15*ms/1000d);
        posY += speedY * (15*ms/1000d);
        posX += speedX * (15*ms/1000d);
        setRefPixelPosition((int) posX, (int) posY);
    }
    
    public void setDirection (double xdist, double ydist) {
        double dist = Math.sqrt(xdist*xdist+ydist*ydist);
        double fraction = SPEED / dist;
        speedX = xdist * fraction;
        speedY = ydist * fraction;
    }
}

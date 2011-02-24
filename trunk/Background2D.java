import java.util.*;
import javax.microedition.lcdui.Graphics;


public class Background2D {
    private Vector stars = new Vector();
    int height;
    
    Random r = new Random();
    
    
    public Background2D(int width, int height) {
        this.height = height;
        Random r = new Random();
        for (int i = 0; i < 200; i++) {
            stars.addElement(new Star(r.nextInt(width), r.nextInt(height), r.nextInt(2)+1));
        }
    }
    
    
    
    public void draw(Graphics g) {
        for(int i = 0; i < stars.size(); i++) {
            Star star = (Star) stars.elementAt(i);
            g.fillRect((int)star.x, (int)star.y, (int)star.z, (int)star.z);
        }
    }
    
    public void setDist(double dist) {
        for(int i = 0; i < stars.size(); i++) {
            Star star = (Star) stars.elementAt(i);
            star.move(dist);
            if (star.y > height)
                star.y = 0;
        }
    }
    
    
    
    private class Star {
        double x;
        double y;
        double z;
        
        public Star (double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        
        public void move(double d) {
            this.y += d*z/3;
        }
    }
}

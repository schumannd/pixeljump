import java.util.*;
import javax.microedition.lcdui.Image;

public class Level {
    
    public int lvl;
    public Vector platforms = new Vector();
    private int width;
    private int height;
    Random r = new Random();
    
    
    public Level(int l, int w, int h) {
        lvl = l;
        width = w;
        height = h;
        createLvl(lvl);
    }
    
    public Platform getPlat(int i){
        return (Platform) platforms.elementAt(i);
    }
    
    public int getSize(){
        return platforms.size();
    }
    
    
    private void createLvl(int lvl) {
        solvable();
        for(int i = 0; i < lvl; i++)
            easier();
    }
    
    public void move(double dist) {
        //Wenn der Pixel ueber der Mitte ist, bewege alle Plattformen und den Pixel entsprchend.
        
        for (int i = 0; i < getSize(); i++) {
            Platform p = getPlat(i);
            p.posY += dist;
            p.setRefPixelPosition((int) p.posX, (int) p.posY);
            if (p.posY > height)
                platforms.removeElementAt(i);
            //TODO: hier deletePlatforms inlinen
            
        }
    }
    
    private void solvable() {
        Image img2 = null;
        try{
            img2 = Image.createImage("/plattform.png");
        }catch(Exception e){}
        //mittige Plattform, sodass man nicht gleich zu Beginn runterfaellt
        platforms.addElement(new Platform(img2,width / 2 - 15, height - 30, 30, 1));
        
        for (int i = 1; i <= 20; i++) {
            int x = r.nextInt(width - 30);
            int y = height-30-i*91;
            platforms.addElement(new Platform(img2, x, y, 30, r.nextInt(2)));
        }
    }
    
    private void easier() {
        Image img2 = null;
        try{
            img2 = Image.createImage("/plattform.png");
        }catch(Exception e){}
        for (int i = 0; i < 10; i++) {
            int x = r.nextInt(width - 30);
            int y = r.nextInt(height+1820)-1820;
            platforms.addElement(new Platform(img2, x, y, 30, r.nextInt(3)));
        }
    }

}
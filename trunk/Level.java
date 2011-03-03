import java.util.*;
import javax.microedition.lcdui.Image;

public class Level {
    
    public int diff;
    public Vector platforms = new Vector();
    private int width;
    private int height;
    Random r = new Random();
    private static int num = 0;
    public static int highest;
    
    public Level(int d, int w, int h) {
        diff = d;
        width = w;
        height = h;
        highest = height/2;
        createLvl();
    }
    
    public Platform getPlat(int i){
        return (Platform) platforms.elementAt(i);
    }
    
    
    public int getSize(){
        return platforms.size();
    }
    
    
    private void createLvl() {
        solvable();
        for(int i = diff; i > num; i--)
            easier();
        
    }
    
    public void move(double dist, int ms) {
        //Wenn der Pixel ueber der Mitte ist, bewege alle Plattformen und den Pixel entsprchend.
        highest += dist;
        for (int i = 0; i < getSize(); i++) {
            Platform p = getPlat(i);
            p.moveDown(dist);
            if (p.item != null)
                p.item.setRefPixelPosition((int) p.posX, (int) (p.posY + dist));
            
            p.moveSide(ms);
            
            if(highest > 1500 + 1840*num) {
                num++;
                createLvl();
                }
                
            
            if (p.posY > height) {
                platforms.removeElementAt(i);
                i--;
            }
        }
    }
    
    private void solvable() {
        addNewPlat(width / 2 - 15, height - 30, 0);

        for (int i = 1; i <= 21; i++) {
            int x = r.nextInt(width - 30);
            int y = height-30-i*92 -1800*num;
            int type = r.nextInt(1);

            addNewPlat(x, y, type);
        }
    }
    
    private void easier() {
        for (int i = 0; i < 10; i++) {
            int x = r.nextInt(width - 30);
            int y = r.nextInt(height+1840)-1840 - 1840*num;
            int type = r.nextInt(3);

            addNewPlat(x, y, type);
        }
    }

    private void addNewPlat(int x, int y, int type){
        Image img = null;
        try{
                img = Image.createImage("/res/plattform"+type+".png");
        }catch(Exception e){}
        platforms.addElement(new Platform(img, x, y, 30, type));

    }

}
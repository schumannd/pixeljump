import java.util.*;
import javax.microedition.lcdui.Image;

public class Level {
    
    public int diff;
    public Vector platforms = new Vector();
    private int width;
    private int height;
    Random r = new Random();
    private int id;
    private static int num = 0;
    
    public Level(int d, int w, int h, int id) {
        this.id = id;
        diff = d;
        width = w;
        height = h;
        createLvl(diff);
    }
    
    public Platform getPlat(int i){
        return (Platform) platforms.elementAt(i);
    }
    
    
    public int getSize(){
        return platforms.size();
    }
    
    
    private void createLvl(int diff) {
        solvable(0);
        for(int i = 0; i < diff; i++)
            easier(0);
        
    }
    
    public void update(int num, int diffic) {
        solvable(num);
        for(int i = 0; i < diffic; i++)
            easier(num);
            
    }
    
    public void move(double dist, int ms) {
        //Wenn der Pixel ueber der Mitte ist, bewege alle Plattformen und den Pixel entsprchend.
        for (int i = 0; i < getSize(); i++) {
            Platform p = getPlat(i);
            p.moveDown(dist);
            if (p.item != null)
                p.item.setRefPixelPosition((int) p.posX, (int) (p.posY + dist));
            
            p.moveSide(ms);
            
            //if(.posY >= 1400+1840*num) {
                
            
            if (p.posY > height) {
                platforms.removeElementAt(i);
                i--;
            }
        }
    }
    
    private void solvable(int num) {
        addNewPlat(width / 2 - 15, height - 30, 0);

        for (int i = 0; i <= 20; i++) {
            int x = r.nextInt(width - 30);
            int y = height-30-i*92 -1840*num;
            int type = r.nextInt(2);

            addNewPlat(x, y, type);
        }
    }
    
    private void easier(int num) {
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
                img = Image.createImage("/plattform"+type+".png");
        }catch(Exception e){}
        platforms.addElement(new Platform(img, x, y, 30, type));

    }

}
import java.util.*;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.util.Random;

public class Level {
    
    public int diff;
    public Vector platforms = new Vector();
    public Vector items = new Vector();
    private int width;
    private int height;
    private Random r = new Random();
    private static int num = 0;
    public static int highest;
    private Random ran;
    
    public Level(int d, int w, int h) {
        diff = d;
        width = w;
        height = h;
        highest = height/2;
        createLvl();
        ran = new Random();
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
        for (int j = 0; j < items.size(); j++) {
            Item it = (Item) items.elementAt(j);
            it.updatePos();
//            it.moveSide(ms);

            if (it.posY > height) {
                items.removeElementAt(j);
                j--;
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

        if(r.nextDouble() < 0.6 && (type == Platform.NORMAL || type == Platform.MOVE)){
            int itemType = 0;
            int n = r.nextInt(100);
        if(80 < n ) itemType++;
            try{
                img = Image.createImage("/res/item"+itemType+".png");
            }catch(Exception e){}
            Platform p = (Platform) platforms.elementAt(platforms.size()-1);
            items.addElement( new Item(p, img, itemType));
            Item item = (Item)items.elementAt(items.size()-1);
            item.defineReferencePixel(0, 16);
            item.setRefPixelPosition(item.getItemX() + x, y - 32);
        }

    }
    
    
    public void paintHeight(Graphics g) {
        g.drawString("Height: "+Integer.toString(highest), 15, 15, Graphics.TOP | Graphics.LEFT);
    }

}
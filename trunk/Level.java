import java.util.*;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.util.Random;

public class Level {
    
    private int diff;
    public Vector platforms = new Vector();
    public Vector items = new Vector();
    private int width;
    private int height;
    private Random r = new Random();
    private int num = 0;
    private int highest;
    
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
    
    public Item getItem(int i){
        return (Item) items.elementAt(i);
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
            
            if(highest > 950-height + 950*num) {
                num += 1;
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
        addNewPlat(width / 2 - 15, height -30, 0);

        for (int i = 1; i <= 11; i++) {
            int x = r.nextInt(width - 30);
            int y = height-30 -i*92;
            if(num != 0)
                y -=height;
            int type = r.nextInt(1);

            addNewPlat(x, y, type);
        }
    }
    
    private void easier() {
        for (int i = 0; i < 10; i++) {
            int x = r.nextInt(width - 30);
            int y = r.nextInt(950)-949+height -30;
            if(num != 0)
                y -=height;
            int type = r.nextInt(2)+1;

            addNewPlat(x, y, type);
        }
    }

    private void addNewPlat(int x, int y, int type){
        platforms.addElement(new Platform(x, y, 30, type));

        if(r.nextDouble() < 0.6 && (type == Platform.NORMAL || type == Platform.MOVE)){
            int itemType = 0;
            int n = r.nextInt(100);
        if(80 < n ) itemType++;
            Platform p = getPlat(platforms.size()-1);
            items.addElement(new Item(p, itemType));
            Item item = getItem(items.size()-1);
            item.defineReferencePixel(0, 16);
            item.setRefPixelPosition(item.getItemX() + x, y - 32);
        }

    }
    
    public void paintPlatAndItems(Graphics g) {
        for (int i = 0; i < getSize(); i++) {
            getPlat(i).paint2(g);
        }
        for (int i = 0; i < items.size(); i++) {
            getItem(i).paint2(g);  
        }
    }
    
    public void paintHeight(Graphics g) {
        g.drawString("Height: "+Integer.toString(highest), 15, 15, Graphics.TOP | Graphics.LEFT);
    }

}
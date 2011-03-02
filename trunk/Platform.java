import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.*;
import java.util.Random;

public class Platform extends Sprite {
    
    public int type; // 0 = normal, 1 = break, 2 = fake
    public double posX;
    public double posY;
    public int size;
    public Item item= null;
    private Random r = new Random();
    
    
    public Platform(Image img, double x, double y, int l, int t) {
        super(img, 30, 3);
        posX = x;
        posY = y;
        setRefPixelPosition((int) posX, (int) posY);
        size = l;
        type = t;
        
        if(r.nextDouble() < 1){
            int itemType = 0;
            try{
                img = Image.createImage("/res/item"+itemType+".png");
            }catch(Exception e){}
            item = new Item(img);
            item.defineReferencePixel(0, 16);
            item.setRefPixelPosition((int) (item.getItemX() + posX),(int) posY - 32);
        }
    }   

}
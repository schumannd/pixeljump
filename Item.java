import java.util.Random;
import javax.microedition.lcdui.game.Sprite;

public class Item extends Sprite {

    private int x;
    private double posX;
    public double posY;
    private Platform p;
    private int type; // 0 = Feder, 1 = Trampolin
    private Random r = new Random();
    public static Pixel pixel;

    public static final int SPRING = 0;
    public static final int TRAMPOLINE = 1;


    public Item(Platform p, int type){
        super(Tools.itemImages[type]);
        this.p = p;
        this.type = type;
        x = r.nextInt(30-Tools.itemImages[type].getWidth());
        posX = p.posX + x;
        posY = p.posY;

        defineReferencePixel(0, 16);
        setRefPixelPosition((int) posX,(int) posY);
    }
    
    
    public void causeEffect(){
        switch (type) {
            case SPRING: pixel.speedY = pixel.JUMPSPEED*1.33;
                    break;
            case TRAMPOLINE: pixel.speedY = pixel.JUMPSPEED*2.25;
                    break;

        }
    }
    
    
    public void updatePos() {
        posX = p.posX + x;
        posY = p.posY;
        setRefPixelPosition((int) posX, (int) posY);
    }
}
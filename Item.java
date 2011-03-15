import java.util.Random;
import java.util.Vector;

public class Item extends GameObject {

    private int x;
    private Platform p;
    private int type; // 0 = Feder, 1 = Trampolin, 2 = Springschuh, 3 = Rakete
    private Random r = new Random();
    public static Pixel pixel;

    public static final int NOITEM = -1;
    public static final int SPRING = 0;
    public static final int TRAMPOLINE = 1;
    public static final int SPRINGSHOE = 2;
    public static final int ROCKET = 3;
    public static final int SHIELD = 4;


    public Item(Platform p, int type){
        super(Tools.itemImages[type], p.posX, p.posY);
        this.p = p;
        this.type = type;
        x = r.nextInt(30-Tools.itemImages[type].getWidth());
        posX = p.posX + x;

        defineReferencePixel(0, 16);
        setRefPixelPosition((int) posX,(int) posY);
    }
    
    
    public void causeEffect(Vector items, int i){
        switch (type) {
        case SPRING:
            //einmalig den jumpspeed erh�hen
            pixel.speedY = pixel.JUMPSPEED*1.33;
            break;

        case TRAMPOLINE:
            //einmalig den jumpspeed erh�hen
            pixel.speedY = pixel.JUMPSPEED*2.25;
            break;

        case SPRINGSHOE:
            //Pixel das item geben und dann in der welt l�schen
            pixel.item = SPRINGSHOE;
            items.removeElementAt(i);
            this.p.item = null;
            break;

        case ROCKET:
            //Pixel das item geben, den timer initialisieren und dann das item in der welt l�schen
            pixel.item = ROCKET;
            pixel.rocketTimer = System.currentTimeMillis();
            items.removeElementAt(i);
            this.p.item = null;
            break;
            
        case SHIELD:
            //Pixel das item geben, den timer initialisieren und dann das item in der welt l�schen
            pixel.item = SHIELD;
            pixel.shieldTimer = System.currentTimeMillis();
            items.removeElementAt(i);
            this.p.item = null;
            break;

        default:
            return;
        }
    }
}
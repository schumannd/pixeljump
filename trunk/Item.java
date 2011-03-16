import java.util.Random;
import java.util.Vector;

public class Item extends GameObject {

    private int x;
    private Platform p;
    public int type; // 0 = Feder, 1 = Trampolin, 2 = Springschuh, 3 = Rakete
    private Random r = new Random();

    public static final int NOITEM = -1;
    public static final int SPRING = 0;
    public static final int TRAMPOLINE = 1;
    public static final int SPRINGSHOE = 2;
    public static final int ROCKET = 3;
    public static final int SHIELD = 4;
    
    private static long shieldTimer;
    private static long rocketTimer;
    private static int shoeTimer = -1;


    public Item(Platform p, int type){
        super(Tools.itemImages[type], p.posX, p.posY);
        this.p = p;
        this.type = type;
        x = r.nextInt(30-Tools.itemImages[type].getWidth());
        posX = p.posX + x;

        defineReferencePixel(0, 16);
        setRefPixelPosition((int) posX,(int) posY);
    }
    
    
    public void activate(){
        switch (type) {
        case SPRINGSHOE:
            shoeTimer = 6;
            this.p.item = null;
            break;

        case ROCKET:
            rocketTimer = System.currentTimeMillis();
            this.p.item = null;
            break;
            
        case SHIELD:
            shieldTimer = System.currentTimeMillis();
            this.p.item = null;
            break;

        default:
            return;
        }
    }
    
    public static boolean isShieldActive() {
        if (shieldTimer == 0)
            return false;
        if ((System.currentTimeMillis() - shieldTimer) < 5000)
            return true;
        else {
            shieldTimer = 0;
            return false;
        }
    }
    
    public static boolean isRocketActive() {
        if (rocketTimer == 0)
            return false;
        if ((System.currentTimeMillis() - rocketTimer) < 2000)
            return true;
        else {
            rocketTimer = 0;
            return false;
        }
    }
    
    public static boolean isShoeActive(boolean jump) {
        if (jump)
            shoeTimer--;
        return shoeTimer >= 0;
    }
    
    public static double getJumheightMulti(int type) {
        if (type == SPRING || (type == SPRINGSHOE && isShoeActive(false)))
            return 1.33;
        if (type == TRAMPOLINE)
            return 2.25;
        Debug.add("FUUUUUUU");
        return -9001;
    }
}
import java.util.Random;

public class Item extends GameObject {

    private final int offset;
    private final Platform p;
    public final int type;
    private static final Random r = new Random();

    public static final int SPRING = 0;
    public static final int TRAMPOLINE = 1;
    public static final int SPRINGSHOE = 2;
    public static final int ROCKET = 3;
    public static final int SHIELD = 4;
    
    private static long shieldTimer = 0;
    public static long rocketTimer = 0;
    public static int shoeTimer = 0;


    public Item(Platform p, int type){
        super(Tools.itemImages[type], p.posX, p.posY);
        this.p = p;
        this.type = type;
        offset = r.nextInt(30-Tools.itemImages[type].getWidth());
        posX = p.posX + offset;

        defineReferencePixel(0, 16);
        setRefPixelPosition((int) posX,(int) posY);
    }
    
    
    public void activate(Pixel p){
        switch (type) {
        case SPRINGSHOE:
            p.setImage(Tools.pixelImages[5], Tools.pixelImages[5].getWidth(), Tools.pixelImages[5].getHeight());
            p.defineReferencePixel(0, p.getHeight()-1);
            p.pictureActive = 5;
            shoeTimer = 1000;
            this.p.item = null;
            break;
        case ROCKET:
            p.setImage(Tools.pixelImages[3], Tools.pixelImages[3].getWidth(), Tools.pixelImages[3].getHeight());
            p.defineReferencePixel(0, p.getHeight()-1);
            p.pictureActive = 3;
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
    
    public static void reset(){
        shoeTimer = 0;
        rocketTimer = 0;
        shieldTimer = 0;
    }
}

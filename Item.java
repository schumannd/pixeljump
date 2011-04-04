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
    private static long rocketTimer = 0;
    private static int shoeTimer = 0;

    /**
     * Konstruktor der Klasse Item
     * @param p die Plattform auf der das item liegt
     * @param type die art des items (s. konstanten)
     */
    public Item(Platform p, int type){
        super(Tools.itemImages[type], p.posX, p.posY);
        this.p = p;
        this.type = type;
        offset = r.nextInt(30-Tools.itemImages[type].getWidth());
        posX = p.posX + offset;

        defineReferencePixel(0, this.getHeight());
        setRefPixelPosition((int) posX,(int) posY);
    }
    
    /**
     * initialisiert die itemtimer und löscht die abhängigkeit zu der plattform
     * wird nur beim kontakt mit einem item benötigt
     */
    public void activate(){
        switch (type) {
        case SPRINGSHOE:
            shoeTimer = 6;
            this.p.item = null;
            break;
        case ROCKET:
            if(isShoeActive(false))
                shoeTimer = 0;
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
    /**
     * überprüft, ob ein schild aktiv ist
     * @return ist ein Schild aktiv? -> true
     */
    public static boolean isShieldActive() {
        //if (shieldTimer == 0)
            //return false;
        if ((System.currentTimeMillis() - shieldTimer) < 5000)
            return true;
        return false;
    }
    
    /**
     * überprüft, ob eine rakete aktiv ist
     * @return ist eine rakete aktiv? -> true
     */
    public static boolean isRocketActive() {
//        if (rocketTimer == 0)
//            return false;
        if ((System.currentTimeMillis() - rocketTimer) < 2000)
            return true;
        return false;
    }
    
    /**
     * überprüft, ob ein jumpshoe aktiv ist
     * @param jump fand eine kollision mit einer plattform statt?
     * @return ist ein jumpshoe aktiv? -> true
     */
    public static boolean isShoeActive(boolean jump) {
        if (jump)
            return --shoeTimer >= 0;
        return shoeTimer > 0;
    }
    
    /**
     * gibt den item-bedingten multiplikator abhängig vom itemtype aus
     * @param type der itemtyp
     * @return der multiplikator
     */
    public static double getJumheightMulti(int type) {
        if (type == SPRING || type == SPRINGSHOE)
            return 1.33;
        if (type == TRAMPOLINE)
            return 2.25;
        Debug.add("FUUUUUUU");
        return -9001;
    }
    
    /** 
     * setzt die timer zurück
     */
    public static void reset(){
        shoeTimer = 0;
        rocketTimer = 0;
        shieldTimer = 0;
    }
}


public class Platform extends GameObject{
    /** Der Typ der Plattform. **/
    private final int type;
    /** Das Item der Plattform, null wenn es kein Item besitzt **/
    public Item item = null;
    
    public static final int NORMAL = 0;
    public static final int BREAK = 1;
    public static final int FAKE = 2;
    
    /**
     * Konstruktor fuer Platformen aller Art
     * @param type = Typ der Platform
     * @param x  x-Koordinate der Platform
     * @param y  y-Koordinate der Platform
     */
    public Platform(int type, double x, double y) {
        super(Tools.platImages[type], x, y);
        this.type = type;
    }
    
    /**
     * Returned den Typ.
     * @return = Typ der Platform
     */
    public int getType() {
        return type;
    }
    
    /**
     * Bewegt die Platform um die angegebene Distanz nach unten.
     * @param dist = zu bewegende Distanz in pixeln
     */
    public void moveDown(double dist) {
        super.moveDown(dist);
        if (item != null) {
            item.moveDown(dist);
        }
    }
}

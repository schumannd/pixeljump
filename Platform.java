
public class Platform extends GameObject{
    
    private final int type;
    public Item item = null;
    
    public static final int NORMAL = 0;
    public static final int BREAK = 1;
    public static final int FAKE = 2;
    
    /*
     * Konstrukter fuer Platformen aller Art
     *
     * @param type = Typ der Platform
     * @param coordinates = koordinaten der Platform
     */
    public Platform(int type, double x, double y) {
        super(Tools.platImages[type], x, y);
        this.type = type;
    }
    /*
     * Get-Methode fuer den typ
     *
     * @return = Typ der Platform
     */
    
    public int getType() {
        return type;
    }
    /*
     * Methode bewegt die Platform nach unten
     * @param dist = zu bewegende Distanz in pixeln
     */
    public void moveDown(double dist) {
        super.moveDown(dist);
        if (item != null) {
            item.moveDown(dist);
        }
    }
}

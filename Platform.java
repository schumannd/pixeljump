
public class Platform extends GameObject{
    
    private final int type;
    public Item item = null;
    
    public static final int NORMAL = 0;
    public static final int BREAK = 1;
    public static final int FAKE = 2;
    
    private final boolean moves;
    
    private final double [][] coords;
    private int targetCoord = 0;
    private final double SPEED = 10;
    private double distToTarget = 0;
    
    /*
     * Konstrukter fuer Platformen aller Art
     *
     * @param type = Typ der Platform
     * @param coordinates = koordinaten der Platform
     */
    public Platform(int type, double [][] coordinates) {
        super(Tools.platImages[type], coordinates[0][0], coordinates[0][1]);
        this.coords = coordinates;
        if (coordinates.length > 1) {
            moves = true;
            targetCoord = 1;
            double distX = posX - coords[targetCoord][0];
            double distY = posY - coords[targetCoord][1];
            distToTarget = Math.sqrt(distX*distX + distY*distY);
        }
        else
            moves = false;
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

    /*
     * Methode bewegt bewegliche Platformen zur seite.
     * @param time = Zeit, die sich dei Platform bewegen soll?
     */
    public void moveSide(double time) {
        if (!moves)
            return;
        
        double distX = posX - coords[targetCoord][0];
        double distY = posY - coords[targetCoord][1];
        posX += distX * (SPEED/distToTarget);
        posY += distY * (SPEED/distToTarget);
        
        distToTarget = Math.sqrt(distX*distX + distY*distY);
        
        setRefPixelPosition((int) posX, (int) posY);
    }

    /*
     * Methode gibt den Abstand zum Zielpunkt der seitwärtsbewegung an
     * @param time = ?
     */
    private double distToNextCoord() {
        double distX = posX - coords[targetCoord][0];
        double distY = posY - coords[targetCoord][1];
        return Math.sqrt(distX*distX + distY*distY);
    }
}

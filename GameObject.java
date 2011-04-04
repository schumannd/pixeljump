import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;


public abstract class GameObject extends Sprite {
    /** Die x-Koordinate dieses GameObjects. **/
    protected double posX;
    /** Die y-Koordinate dieses GameObjects. **/
    protected double posY;
    
    /** Breite der Canvas. **/
    protected static int width;
    /** Hoehe der Canvas. **/
    protected static int height;
    
    /**
     * Erstellt ein GameObject mit dem angegebenen Bild an den angegebenen
     * Koordinaten.
     * @param image Das Bild des GameObjects
     * @param x x-Koordinate des GameObjects
     * @param y y-Koordinate des GameObjects
     */
    public GameObject(Image image, double x, double y) {
        super(image);
        posX = x;
        posY = y;
       
        setRefPixelPosition((int) posX, (int) posY);
    }

    /**
     * Bewegt dieses GameObject um die angegebene Distanz nach unten.
     */
    public void moveDown(double dist) {
        posY += dist;
        setRefPixelPosition((int) posX, (int) posY);
    }
    
    /**
     * @return true, wenn sich dieses GameObject auf dem Bildschirm befindet.
     */
    public boolean isOnScreen() {
        if (this.posX > width ||
            this.posY > height ||
            this.posX + getWidth() < 0 ||
            this.posY + getHeight() < 0)
            return false;
        return true;
    }
    
    /**
     * Initialisiert die Bildschirmgroesse
     * @param width die bildschirmbreite
     * @param height die bildschirmhoehe
     */
    public static void init(int width, int height) {
        GameObject.width = width;
        GameObject.height = height;
    }
    
    public double getPosY(){
        return posY;
    }
    
    public double getPosX(){
        return posX;
    }
}

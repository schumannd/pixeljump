import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;


public abstract class GameObject extends Sprite {
    protected double posX;
    protected double posY;
    
    protected static int width;
    protected static int height;

    public GameObject(Image image, double x, double y) {
        super(image);
        posX = x;
        posY = y;
       
        setRefPixelPosition((int) posX, (int) posY);
    }

    /**
     * moves the GameObject down
     */
    public void moveDown(double dist) {
        posY += dist;
        setRefPixelPosition((int) posX, (int) posY);
    }
    
    public double getPosY(){
        return posY;
    }
    public double getPosX(){
        return posX;
    }
    public void setPosX(double x) {
        posX = x;
    }
    /**
     * @return true, wenn sich das object auf dem bildschirm befindet
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
     * initialisiert die bildschirmgröße
     * @param width die bildschirmbreite
     * @param height die bildschirmhöhe
     */
    public static void init(int width, int height) {
        GameObject.width = width;
        GameObject.height = height;
    }
}

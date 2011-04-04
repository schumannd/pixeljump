import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;

public class Background2D {
    private double posY;
    private final TiledLayer landscape;
    
    /**
     * Erstellt den Hintergrund
     * @param height Die Hoehe des Canvas
     */
    public Background2D(int height) {
        int anzahl = (height / 10) + 1;
        Image img = Tools.backgroundImage;
        landscape = new TiledLayer(1, anzahl, img, img.getWidth(), img.getHeight());
        posY = -height%10;
        landscape.setPosition(0, (int) posY );

        for (int i = 0; i < anzahl; i++) {
            landscape.setCell(0, i, 1);
        }
    }
    
    /**
     * Painted den Hintergrund.
     * @param g
     */
    public void paint(Graphics g) {
        landscape.paint(g);
    }
    
    /**
     * Bewegt den Hintergrund um die angegebene Entfernung in Pixeln nach unten.
     * @param dist Die Entfernung.
     */
    public void moveDown(double dist) {
        posY += dist / 4;
        if (posY > 0) {
            posY %= 10;
            posY -= 10;
        }
        landscape.setPosition(landscape.getX(), (int) posY);
    }
}

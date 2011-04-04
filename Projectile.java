
public class Projectile extends GameObject {
    /** Gesamtgeschwindigkeit des Projektils **/
    private double speed = -50;
    /** Geschwindigkeit des Projektils in x-Richtung **/
    private double speedX;
    /** Geschwindigkeit des Projektils in y-Richtung **/
    private double speedY;
    
    /**
     * Erstellt ein Projektil an den angegebenen Koordinaten.
     * @param x Die x-Koordinate des Projektils.
     * @param y Die x-Koordinate des Projektils.
     * @param addSpeed Zusaetzliche Geschwindigkeit. Wenn sich Pixelman schnell
     *          bewegt, ist das Projektil entsprechend schneller.
     */
    public Projectile(double x, double y, double addSpeed){
        super(Tools.projectileImage, x, y);
        speed += addSpeed;
    }
    
    /**
     * Bewegt das Projektil fuer Dauer der angegebenen Zeit..
     * @param time Die Zeit, um
     */
    public void move(double time) {
        posY += speedY * time;
        posX += speedX * time;
        setRefPixelPosition((int) posX, (int) posY);
    }
    
    /**
     * Setzt die Richtung des Projektils.
     * @param xdist der x-Teil der neuen Richtung.
     * @param ydist der y-Teil der neuen Richtung.
     */
    public void setDirection (double xdist, double ydist) {
        double dist = Math.sqrt(xdist*xdist+ydist*ydist);
        double fraction = speed / dist;
        speedX = xdist * fraction;
        speedY = ydist * fraction;
    }
}

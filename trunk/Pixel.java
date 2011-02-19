import java.util.*;

public class Pixel
{

    public double posX;
    public double posY;
    public double speedX = 0;
    public double speedY = 0;

    /**
     * Konstruktor für Objekte der Klasse Platform
     */
    public Pixel(double x, double y)
    {
        posX = x;
        posY = y;
    }
    
    
    public void move(int width, int height, Vector platforms, int ms) {
        
        double fraction = 15*ms/1000.0d;
        //die diesen zug zurueckzulegende distanz.
        double moveY = speedY*fraction;
        double moveX = speedX*fraction;
        
        
        if (!collisionDetection(platforms, moveX, moveY)) {
            //wenn kollision stattdfand, wurde posY schonvon der kollisionsberechnung neu gesetzt
            posY += moveY;
        }
        posX += moveX;
          
        //linke wand
        if (posX < 0) {
            posX = 0;
            speedX = 0;
        }

        //rechte wand
        else if (posX > width - 1) {
            posX = width - 1;
            speedX = 0;
        }
        
        
        speedY += 2*fraction;
    }
    
    public boolean collisionDetection(Vector platforms, double moveX, double moveY) {
        // Kollisionen interessieren nicht, wenn pixel nach oben fliegt.
        if (speedY > 0) {
           for (int i = 0; i < platforms.size(); i++) {
                Platform p = (Platform) platforms.elementAt(i);
                //y-Distanz von pixel zu plattform
                double distanceY = p.posY - posY;
                //wenn die plattform ueberm pixel ist oder die distanz 
                //groesser als die zurueckzulegende strecke ist, abbruch.
                if (distanceY < 0 || distanceY > moveY)
                    continue;
                //der bruchteil der zeit dieser runde, nach dem die kollsion stattfinden wird
                double fraction2 = distanceY /moveY;
                //die x-distanz, die bis zum zeitpunkt der kollision zurueckgelegt werden wird
                double distToCollisionX = moveX * fraction2;
                
                //die position den pixel bei der kollision
                double newPosX = posX + distToCollisionX;
                //position bei der kollision ist gar nicht auf der plattform, also keine kollision, abbruch
                if (newPosX <= p.posX || newPosX >= (p.posX + p.size))
                    continue;
                
                //bewege pixel bis zur kollision und dann um die verbleibende zeit in die neue richtung
                posY = posY + moveY * fraction2 - moveY * (1-fraction2);
                //neue geschwindigkeit
                speedY = -20;
                //kollision wurde gefunden.
                return true;
            }
        }
        return false;
    }

}

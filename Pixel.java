import java.util.*;

public class Pixel {
    
    public double posX;
    public double posY;
    public double speedX = 0;
    public double speedY = 0;
    final int JUMPSPEED = -20;
    public int score = 0;

    /**
     * Konstruktor für Objekte der Klasse Platform
     */
    public Pixel(double x, double y) {
        posX = x;
        posY = y;
    }
    
    
    public void accelerate(int leftright, int ms) {
        double fraction = 15*ms/1000.0d;
        
        int speedlimit = 10;
        int acceleration = 2;
        if(speedX*leftright < 0)
            speedX /= 4/fraction;
        speedX+= leftright*fraction*acceleration;
        if (speedX > speedlimit)
            speedX = speedlimit;
        if (speedX < -speedlimit)
            speedX = -speedlimit;
        
        if (leftright == 0)
            speedX /= 2/fraction;
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
        if (posX < 0)
            posX = width;
        

        //rechte wand
        else if (posX > width - 1)
            posX = 0;
        
        
        //Gavitation.
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
                //der bruchteil der zeit dieses frames, nach dem 
                //der pixel auf der hoehe der plattform angekommen is
                double fraction2 = distanceY/moveY;
//                Debug.add(fraction2);
                //die position des pixels, wenn er auf der hoehe der plattform ankommt
                double newPosX = posX + moveX * fraction2;
                //die position ist nicht auf der plattform, also keine kollision, abbruch
                if (newPosX <= p.posX || newPosX >= (p.posX + p.size))
                    continue;
                // lösche platform ohne kollision wenn fake
                if(p.type == 2) {
                    platforms.removeElementAt(i);
                    continue;
                }
                //bewege pixel bis zur kollision und dann um die verbleibende zeit in die neue richtung
                posY = posY + moveY * fraction2;//
                posY += moveY/speedY*JUMPSPEED * (1-fraction2);
                //loesche Platform wenn breakable, dann kollision
                if(p.type == 1) {
                    platforms.removeElementAt(i);
                }
                
                
                //neue geschwindigkeit
                speedY = JUMPSPEED;
                //eine weitere kollision kann nicht gefunden werden, daher kompletter abbruch.
                return true;
            }
        }
        return false;
    }
    
    
    
}
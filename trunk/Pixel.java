import java.util.Vector;

public class Pixel extends GameObject {
    
    private double speedX = 0;
    private double speedY = 0;
    private final int JUMPSPEED = -20;
    private final int SPEEDLIMIT = 10;
    private final int ACCELERATION = 2;
    private final int GRAVITY = 2;
    private final int shotOriginX;
    private final int shotOriginY;

    
    public Pixel(double x, double y) {
        super(Tools.pixelImage, x, y);
        shotOriginX = getWidth()/2;
        shotOriginY = 0;
        defineReferencePixel(0, Tools.pixelImage.getHeight()-1);
        speedY = JUMPSPEED*2.1;
    }
    
    
    public void accelerate(int leftright, double time) {
        
        //bremsen wenn nichts gedrueckt wurde
        if (leftright == 0 && speedX != 0) {
            if (Math.abs(speedX) < time * ACCELERATION*2)
                speedX = 0;
            else
                speedX -= time * ACCELERATION * 2 * ((speedX < 0) ? -1 : 1);
        }
        else if (leftright != 0) {
            if(speedX * leftright < 0)
                speedX += leftright * time * ACCELERATION;
            speedX += leftright * time * ACCELERATION;
            if (speedX > SPEEDLIMIT)
                speedX = SPEEDLIMIT;
            else if (speedX < -SPEEDLIMIT)
                speedX = -SPEEDLIMIT;
        }
    }
    
    public void move(Vector platforms, Vector items, double time) {
        if(Item.isRocketActive()){
            //speedY auf raketenspeed
            speedY = JUMPSPEED*3;
            posY += speedY*time;
        }
        else {
            itemCollDetec(items);
            //diese methode berechnet auch die neue posY
            platformCollDetec(platforms, time);
        }
        posX += speedX*time;
        
        //linke wand
        if (posX+getWidth()/2 < 0)
            posX = width-getWidth()/2;
        //rechte wand
        else if (posX+getWidth()/2 > width)
            posX = 0;
        
        this.setRefPixelPosition((int) posX, (int) posY);
        //Gavitation.
        speedY += GRAVITY*time;
    }
    
    
    public boolean monsterCollision(Vector monsters) {
        if(Item.isRocketActive() || Item.isShieldActive())
            return false;
        for(int i = 0; i < monsters.size(); i++){
            Monster m = (Monster) monsters.elementAt(i);
            if (this.collidesWith(m, false))
                return true;
        }
        return false;
    }
    
    
    private void itemCollDetec(Vector items){
        for(int i = 0; i < items.size(); i++){
            Item it = (Item) items.elementAt(i);
            if(this.collidesWith(it, false)){
                if ((it.type == Item.SPRING || it.type == Item.TRAMPOLINE))
                    if (speedY > 0) {
                        speedY = JUMPSPEED * Item.getJumheightMulti(it.type);
                        SoundManager.playSound(SoundManager.JUMP);
                    }
                    else //ignorieren, wenn pixel nach oben fliegt
                        return;
                else if (it.type == Item.SPRINGSHOE && speedY <= 0)
                    return; //ignoriere springschuhe wenn pixel nach oben fliegt
                else {
                    it.activate();
                    items.removeElementAt(i);
                }
                return;
            }
        }
    }

    private boolean platformCollDetec(Vector platforms, double time){
        double moveY = GRAVITY*time*time/2 + speedY*time;
        // Kollisionen interessieren nicht, wenn pixel nach oben fliegt.
        if (speedY > 0) {
           for (int i = 0; i < platforms.size(); i++) {
                Platform p = (Platform) platforms.elementAt(i);
                //y-Distanz von pixel zu plattform
                double distanceY = p.posY-1 - posY;
                //wenn die plattform ueber dem pixel ist oder die distanz
                //groesser als die zurueckzulegende strecke ist, abbruch.
                if (distanceY < 0 || distanceY > moveY)
                    continue;
                //die zeit, nach der der pixel auf der hoehe der plattform angekommen ist
                double time2 = -(speedY/2)+Math.sqrt((speedY*speedY/4)+distanceY);
                //die position des pixels, wenn er auf der hoehe der plattform ankommt
                double newPosX = posX + speedX * time2;
                // +13 und +3 fuer die positionen der beine
                if (newPosX+28  <= p.posX || newPosX +3 >= (p.posX + p.getWidth()))
                    //die position ist nicht auf der plattform, also keine kollision, abbruch
                    continue;
                if(p.getType() == Platform.FAKE) {
                    platforms.removeElementAt(i);
                    continue;
                }
                //jetzt hat eine ernstzunehmende kollision stattgefunden.
                SoundManager.playSound(SoundManager.JUMP);
                if(p.getType() == Platform.BREAK)
                    platforms.removeElementAt(i);

                //bewege pixel bis zur kollision
                posY += distanceY;
                
                //neue geschwindigkeit
                if (Item.isShoeActive(true))
                    speedY = JUMPSPEED * Item.getJumheightMulti(Item.SPRINGSHOE);
                else
                    speedY = JUMPSPEED;
                
                //verbleibende zeit = gesamtzeit - zeit bis zur kollision
                double time3 = time-time2;
                posY += GRAVITY*time3*time3/2 + speedY*time3;
                speedY += GRAVITY*time3;
                //eine weitere kollision kann nicht gefunden werden, daher kompletter abbruch.
                return true;
            }
        }
        //keine kollision.
        posY += moveY;
        return false;
    }
    
    public void shoot(Arena arena) {
        SoundManager.playSound(SoundManager.SHOOT);
        arena.shoot(posX + shotOriginX, posY + shotOriginY, speedY);
    }
}

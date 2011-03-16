import java.util.Vector;

public class Pixel extends GameObject {
    
    private double speedX = 0;
    private double speedY = 0;
    private final int JUMPSPEED = -20;
    private final int SPEEDLIMIT = 10;
    private final int ACCELERATION = 2;
    private int shotOriginX;
    private int shotOriginY;

    
    public Pixel(double x, double y) {
        super(Tools.pixelImage, x, y);
        shotOriginX = getWidth()/2;
        shotOriginY = 0;
        defineReferencePixel(0, Tools.pixelImage.getHeight()-1);
    }
    
    
    public void accelerate(int leftright, int ms) {
        double fraction = 15*ms/1000.0d;
        
        if(speedX*leftright < 0)
            speedX /= 4/fraction;
        speedX+= leftright*fraction*ACCELERATION;
        if (speedX > SPEEDLIMIT)
            speedX = SPEEDLIMIT;
        else if (speedX < -SPEEDLIMIT)
            speedX = -SPEEDLIMIT;
        
        if (leftright == 0)
            speedX /= 2/fraction;
    }
    
    public void move(int width, Vector platforms, Vector items, int ms) {
        double fraction = 15*ms/1000.0d;
        //die diesen zug zurueckzulegende distanz.
        double moveY = speedY*fraction;
        double moveX = speedX*fraction;
        
        if(Item.isRocketActive()){
            //speedY auf raketenspeed
            speedY = JUMPSPEED*3;
            moveY = speedY*fraction;
            posY += moveY;
        }
        else if (!collisionDetection(platforms, items, moveX, moveY)) {
            //wenn kollision stattdfand, wurde posY schonvon der kollisionsberechnung neu gesetzt
            moveY = speedY*fraction;
            posY += moveY; 
        }
        posX += moveX;
        
        //linke wand
        if (posX < 0){
            posX = width;
        }
        //rechte wand
        else if (posX > width){
            posX = 0;
        }
        
        this.setRefPixelPosition((int) posX, (int) posY);
        //Gavitation.
        speedY += 2*fraction;
    }
    
    
    public boolean monsterCollision(Vector monsters) {
        if(Item.isRocketActive() || Item.isShieldActive()){ return false; }
        for(int i = 0; i < monsters.size(); i++){
            Monster m = (Monster) monsters.elementAt(i);
            if (this.collidesWith(m, false))
                return true;
        }
        return false;
    }


    public boolean collisionDetection(Vector platforms, Vector items, double moveX, double moveY){
        itemCollDetec(items);
        return platformCollDetec(platforms, moveX, moveY);
    }

    private void itemCollDetec(Vector items){
        for(int i = 0; i < items.size(); i++){
            Item it = (Item) items.elementAt(i);
            if(this.collidesWith(it, false)){
                if ((it.type == Item.SPRING || it.type == Item.TRAMPOLINE))
                    if (speedY > 0)
                        speedY = JUMPSPEED * Item.getJumheightMulti(it.type);
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

    private boolean platformCollDetec(Vector platforms, double moveX, double moveY){
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
                //der bruchteil der zeit dieses frames, nach dem
                //der pixel auf der hoehe der plattform angekommen is
                double fraction2 = distanceY/moveY;
                //die position des pixels, wenn er auf der hoehe der plattform ankommt
                double newPosX = posX + moveX * fraction2;
                // +13 und +3 fuer die positionen der beine
                if (newPosX+13  <= p.posX || newPosX +3 >= (p.posX + p.getWidth()))
                    //die position ist nicht auf der plattform, also keine kollision, abbruch
                    continue;
                SoundManager.playSound(SoundManager.JUMP);
                // loesche platform ohne kollision wenn fake
                if(p.type == Platform.FAKE) {
                    platforms.removeElementAt(i);
                    continue;
                }
                if(p.type == Platform.BREAK)
                    platforms.removeElementAt(i);

                //bewege pixel bis zur kollision
                posY = posY + moveY * fraction2;
                //neue geschwindigkeit
                if (Item.isShoeActive(true))
                    speedY = JUMPSPEED * Item.getJumheightMulti(Item.SPRINGSHOE);
                else
                    speedY = JUMPSPEED;
                //eine weitere kollision kann nicht gefunden werden, daher kompletter abbruch.
                return true;
            }
        }
        return false;
    }
    
    public void shoot(Arena arena) {
        SoundManager.playSound(SoundManager.SHOOT);
        arena.shoot(posX + shotOriginX, posY + shotOriginY);
    }
}
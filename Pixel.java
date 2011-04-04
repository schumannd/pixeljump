import java.util.Vector;

import javax.microedition.lcdui.game.Sprite;

public class Pixel extends GameObject {
    
    private double speedX = 0;
    private double speedY = 0;
    private final int JUMPSPEED = -20;
    private final int SPEEDLIMIT = 10;
    private final int ACCELERATION = 2;
    private final int GRAVITY = 2;
    private final int shotOriginX = 0;
    private final int shotOriginY = 0;
    
    private final int PIC_NORMAL = 0;
    private final int PIC_ROCKET = 1;
    private final int PIC_SPRINGSHOE = 2;
    private int pictureActive = PIC_NORMAL;

    private boolean looksRight = true;

    /**
     * Erstellt neuen Pixel an der Position (x/y).
     */
    public Pixel(double x, double y) {
        super(Tools.pixelImage, x, y);
        this.setImage(Tools.pixelImage, 48, 50);
        defineReferencePixel(Tools.pixelImage.getWidth() / 6, Tools.pixelImage.getHeight() - 1);
        speedY = JUMPSPEED*2;
    }
    
    
    /**
     * Beschleunigt den Pixel bei Tastendruck, bzw bremst, wenn nichts 
     * gedrueckt wurde.
     * @param leftright -1, 1 oder 0 wenn links, rechts oder nichts gedrueckt wurde
     */
    public void accelerate(int leftright, double time) {
        //Wenn pixel sich nach links bewegt, aber noch nach rechts guckt
        if(speedX < 0 && looksRight) {
            setTransform(Sprite.TRANS_MIRROR);
            looksRight = false;
        }
      //Wenn pixel sich nach rechts bewegt, aber noch nach links guckt
        else if(speedX > 0  && !looksRight) {
            setTransform(Sprite.TRANS_NONE);
            looksRight = true;
        }
        //bremsen wenn nichts gedrueckt wurde
        if (leftright == 0 && speedX != 0) {
            if (Math.abs(speedX) < time * ACCELERATION * 2)
                speedX = 0;
            else
                speedX -= time * ACCELERATION * 2 * ((speedX < 0) ? -1 : 1);
        }
        else if (leftright != 0) {
            //doppelte Beschleunigung, falls z.B. rechts gedrueckt wurde, Pixel
            //aber noch nach links fliegt.
            if(speedX * leftright < 0)
                speedX += leftright * time * ACCELERATION;
            speedX += leftright * time * ACCELERATION;
            //Einhaltung des Speedlimits
            if (speedX > SPEEDLIMIT)
                speedX = SPEEDLIMIT;
            else if (speedX < -SPEEDLIMIT)
                speedX = -SPEEDLIMIT;
        }
    }
    
    /**
     * Bewegt den Pixel um die angegebene Zeit. Ueberprueft Kollisionen mit 
     * Plattformen und Items und sorgt fuer die Gravitation.
     * @param platforms Vektor aller Plattformen.
     * @param items Vektor aller Items.
     * @param time Die Dauer dieses Frames
     */
    public void move(Vector platforms, Vector items, double time) {
        if(Item.isRocketActive()){
            //speedY auf raketenspeed
            speedY = JUMPSPEED * 3;
            posY += speedY * time;
        }
        else {
            itemCollDetec(items);
            //diese methode berechnet auch die neue posY
            platformCollDetec(platforms, time);
        }
        posX += speedX * time;
        
        //linke wand
        if (posX < 0)
            posX = width;
        //rechte wand
        else if (posX > width)
            posX = 0;
        
        this.setRefPixelPosition((int) posX, (int) posY);
        //Gavitation.
        speedY += GRAVITY * time;
    }
    
    /**
     * Ueberprueft, ob Pixel mit einem der angegebenen Monster kollidiert.
     * @param monsters Vektor aller Monster.
     * @return true, wenn das der Fall ist, sonst false.
     */
    public boolean monsterCollision(Vector monsters) {
        //Keine Monster da oder Raketen aktiv, Kollision daher unmoeglich.
        if(monsters.size() == 0 || Item.isRocketActive())
            return false;
        for(int i = 0; i < monsters.size(); i++){
            Monster m = (Monster) monsters.elementAt(i);
            if (this.collidesWith(m, false)) {
                //Wenn pixel nach oben fliegt und das Schild nicht aktiv ist, game over.
                if(speedY < 0 && !Item.isShieldActive())
                    return true;
                else { //Pixel fiel nach unten und springt jetzt vom monster ab
                    speedY = JUMPSPEED;
                    monsters.removeElement(m);
                    return false;
                }
            }
        }
        return false;
    }
    
    /**
     * Ueberprueft, ob Pixel mit einem der angegebenen Items kollidiert, und
     * aktiviert ggf. das Item.
     * @param items Vektor aller Items.
     */
    private void itemCollDetec(Vector items){
        for(int i = 0; i < items.size(); i++){
            Item it = (Item) items.elementAt(i);
            if(this.collidesWith(it, false)){
                switch (it.type){
                case Item.SPRING:
                case Item.TRAMPOLINE:
                    if (speedY > 0) { //ignorieren, falls Pixel nach oben fliegt
                        speedY = JUMPSPEED * Item.getJumheightMulti(it.type);
                        SoundManager.playSound(SoundManager.JUMP);
                    }
                    break;
                case Item.SPRINGSHOE:
                    if (speedY > 0) { //ignorieren, falls Pixel nach oben fliegt
                        it.activate();
                        setImage(PIC_SPRINGSHOE);
                        items.removeElementAt(i);
                    }
                    break;
                case Item.ROCKET:
                    it.activate();
                    setImage(PIC_ROCKET);
                    items.removeElementAt(i);
                    break;
                case Item.SHIELD:
                    it.activate();
                    items.removeElementAt(i);
                    break;
                }
                return;
            }
        }
    }
    
    /**
     * Ueberprueft, ob Pixel mit den angegebenen Plattformen kollidiert, und
     * berechnet entsprechend die neue y-Position des Pixels.
     * @param platforms Vektor aller Plattformen.
     * @param time Die Dauer dieses Frames, benoetigt zu korrekten collision-response
     */
    private void platformCollDetec(Vector platforms, double time){
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
                if (newPosX + 12  <= p.posX || newPosX - 13 >= (p.posX + p.getWidth()))
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
                return;
            }
        }
        //keine kollision.
        posY += moveY;
    }
    
    /**
     * Gibts einen Schuss ab und spielt den Sound ab.
     * @param arena Die Arena, die den Schuss "weiterverwaltet"
     */
    public void shoot(Arena arena) {
        SoundManager.playSound(SoundManager.SHOOT);
        arena.shoot(posX + shotOriginX, posY + shotOriginY, speedY);
    }
    
    /**
     * Setzt das Frame dieses Sprites auf das angegebene. Wird benutzt, um z.B.
     * das Bild des Pixels mit der Rakete oder den Springschuhen anzuzeigen.
     * @param img Index des neuen Bildes des Pixels.
     */
    public void setImage(int img) {
        this.setFrame(img);
        defineReferencePixel(getWidth() / 2, getHeight()-1);
        pictureActive = img;
    }
    
    /**
     * Setzt das Bild des Pixels auf das normale Bild, falls Raketen oder
     * Springschuhe abgelaufen sind.
     */
    public void resetImage(){
        if(pictureActive != PIC_NORMAL && !Item.isRocketActive() && !Item.isShoeActive(false))
            this.setImage(PIC_NORMAL);

    }
}

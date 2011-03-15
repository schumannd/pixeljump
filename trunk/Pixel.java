import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Pixel extends GameObject {
    
    private double speedX = 0;
    public double speedY = 0;
    public final int JUMPSPEED = -20;
    private final int SPEEDLIMIT = 10;
    private final int ACCELERATION = 2;
    public int score = 0;
    private int shotOriginX;
    private int shotOriginY;
    
    public int item = -1;
    public int shoeTimer = 6;
    public long rocketTimer = 0;
    public long shieldTimer = 0;

    
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
        
        if(item == Item.SHIELD && (System.currentTimeMillis() - shieldTimer) > 5000)
            item = Item.NOITEM;
        if(item == Item.ROCKET){
            //nach 2 s rocket zur�cksetzen
            if((System.currentTimeMillis() - rocketTimer) > 2000){
                item = Item.NOITEM;
                rocketTimer = 0;
            }
            else{
                //speedY auf raketenspeed
                speedY = JUMPSPEED*3;
                moveY = speedY*fraction;
                posY += moveY;
            }
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
        speedY = -20; //2*fraction;
    }
    
    
    public boolean monsterCollision(Vector monsters) {
        if(item == Item.ROCKET || item == Item.SHIELD){ return false; }
        for(int i = 0; i < monsters.size(); i++){
            Monster m = (Monster) monsters.elementAt(i);
            if (this.collidesWith(m, false))
                return true;
        }
        return false;
    }


    public boolean collisionDetection(Vector platforms, Vector items, double moveX, double moveY){
        if(item == Item.NOITEM || item == Item.SHIELD){
            itemCollDetec(items);
        }
        return platformCollDetec(platforms, moveX, moveY);
    }

    private boolean itemCollDetec(Vector items){
        for(int i = 0; i < items.size(); i++){
            Item it = (Item) items.elementAt(i);
            if(this.collidesWith(it, false)){
                it.causeEffect(items, i);
                return true;
            }


        }
        return false;

    }

    private boolean platformCollDetec(Vector platforms, double moveX, double moveY){
        // Kollisionen interessieren nicht, wenn pixel nach oben fliegt.
        if (speedY > 0) {
           for (int i = 0; i < platforms.size(); i++) {
                Platform p = (Platform) platforms.elementAt(i);
                //y-Distanz von pixel zu plattform
                double distanceY = p.posY - posY;
                //wenn die plattform ueber dem pixel ist oder die distanz
                //groesser als die zurueckzulegende strecke ist, abbruch.
                if (distanceY < 0 || distanceY > moveY)
                    continue;
                //der bruchteil der zeit dieses frames, nach dem
                //der pixel auf der hoehe der plattform angekommen is
                double fraction2 = distanceY/moveY;
                //die position des pixels, wenn er auf der hoehe der plattform ankommt
                double newPosX = posX + moveX * fraction2;
                //die position ist nicht auf der plattform, also keine kollision, abbruch
                // fuer die positionen der beine
                if (newPosX+13  <= p.posX || newPosX +3 >= (p.posX + p.size))
                    continue;
                // loesche platform ohne kollision wenn fake
                if(p.type == Platform.FAKE) {
                    platforms.removeElementAt(i);
                    continue;
                }

                //bewege pixel bis zur kollision und dann um die verbleibende zeit in die neue richtung
                posY = posY + moveY * fraction2;//
                if(item == Item.NOITEM || item == Item.SHIELD){
                    posY += (moveY/speedY)*JUMPSPEED * (1-fraction2);
                }
                else if(item == Item.SPRINGSHOE){
                    posY += (moveY/speedY)*JUMPSPEED * 1.33 * (1-fraction2);
                }
                //loesche Platform wenn breakable, dann kollision
                if(p.type == 1) {
                    platforms.removeElementAt(i);
                }
                SoundManager.playSound(SoundManager.JUMP);
                //neue geschwindigkeit
                switch(item){
                    case Item.NOITEM:
                    case Item.SHIELD:
                        speedY = JUMPSPEED;
                        break;
                       
                    case Item.SPRINGSHOE:
                        //wenn spingshoes, dann 6 mal wie auf federn springen
                        speedY = JUMPSPEED * 1.33;
                        shoeTimer--;
                        if(shoeTimer <= 0){
                            //item, shoeTimer zur�cksetzen
                            item = Item.NOITEM;
                            shoeTimer = 6;
                        }
                        break;
                        
                        default: break;

                  }
                //eine weitere kollision kann nicht gefunden werden, daher kompletter abbruch.
                return true;
            }
        }
        return false;
    }
    
    
    public void moveDown (double dist) {
        super.moveDown(dist);
        score += dist;
    }
    
    
    public void shoot(Arena arena) {
        SoundManager.playSound(SoundManager.SHOOT);
        arena.shoot(posX + shotOriginX, posY + shotOriginY);
    }
    
    
    public void paintScore(Graphics g, int width) {
        g.drawString("Score: "+Integer.toString(score), width-65, 15, Graphics.TOP | Graphics.LEFT);
    }
}
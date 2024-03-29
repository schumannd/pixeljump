import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Arena {
    public final Vector monsters = new Vector();
    public final Vector invisibleM = new Vector();
    private final Vector projectiles = new Vector();
    private final int height;
    
    
    public Arena (int height) {
        this.height = height;
    }
    
    public void shoot(double x, double y, double speed) {
        Projectile p = new Projectile(x, y, speed);
        p.posX -= p.getWidth()/2;
        p.posY -= p.getHeight();
        //autoaim
        if (monsters.size() > 0) {
            //finde das naheste monster
            int nearestMonster = -1;
            double nearestMonsterDist = 9001;
            for (int i = 0; i < monsters.size(); i++) {
                Monster m = getMonster(i);
                if (!m.isOnScreen())
                    continue;
                double xdist = x-(m.posX + m.getWidth()/2);
                double ydist = y-(m.posY + m.getHeight()/2);
                double dist = Math.sqrt(xdist*xdist+ydist*ydist);
                if (dist < nearestMonsterDist) {
                    nearestMonster = i;
                    nearestMonsterDist = dist;
                }
            }
            //setze die richtung des projektils
            Monster m = getMonster(nearestMonster);
            double xdist = x-(m.posX + m.getWidth()/2);
            double ydist = y-(m.posY + m.getHeight()/2);
            p.setDirection(xdist, ydist);
        }
        else //kein monster da, nach oben schiessen
            p.setDirection(0, 1);
        
        projectiles.addElement(p);
    }
    
    /**
     * Ueberprueft, ob irgendwelche Kollisionen zwischen Monstern und 
     * Projektilen stattfinden und entfernt ggf. das entsprechende Monster und
     * das Projektil.
     */
    public void monsterProjectileCollision() {
        for(int i = 0; i < monsters.size(); i++){
            Monster m = getMonster(i);
            for (int j = 0; j < projectiles.size(); j++) {
                Projectile p = getProjectile(j);
                if (m.collidesWith(p, false)) {
                    monsters.removeElementAt(i);
                    projectiles.removeElementAt(j);
                    i--;
                    break;
                }
            }
        }
    }
    
    /**
     * Bewegt die Projektile um die gegebene Zeit und loescht sie, falls sie
     * nicht mehr auf dem Bildschirm sind.
     * @param time
     */
    public void move(double time) {
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = getProjectile(i);
            p.move(time);
            if (!p.isOnScreen())
                projectiles.removeElementAt(i);
        }
    }
    
    /**
     * Bewegt alle Monster und Projektile um die gegebene Distanz nach unten.
     * @param dist die Distanz.
     */
    public void moveDown(double dist) {
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = getMonster(i);
            m.moveDown(dist);
            if (m.posY > height) {
                monsters.removeElementAt(i);
                i--;
            }
        }
        for (int i = 0; i < projectiles.size(); i++) {
            getProjectile(i).moveDown(dist);
        }
    }
    
    /**
     * Fuegt das angegebene Monster zum Monster-Vector hinzu.
     * @param m Das hinzuzufuegende Monster.
     */
    public void addVisibleM(int x, int y, int type) {
        monsters.addElement(new Monster(x, y, type));
    }
    
    /**
     * Fuegt ein neues Monster mit den angegebenen Koordinaten und mit dem
     * angegebenen Typ zum Vector der Monster oberhalb des Bildschirms hinzu.
     * @param x x-Koordinate des Monsters (sollte negativ sein).
     * @param y y-Koordinate des Monsters.
     * @param type Typ des Monsters.
     */
    public void addInvisibleM(int x, int y, int type) {
        invisibleM.addElement(new Monster(x, y, type));
    }

    public Monster getMonster(int i){
        return (Monster) monsters.elementAt(i);
    }

    public Monster getInvisibleM(int i){
        return (Monster) invisibleM.elementAt(i);
    }
    
    public Projectile getProjectile(int i){
        return (Projectile) projectiles.elementAt(i);
    }
    
    /**
     * Painted alle Monster und Projektile.
     * @param g
     */
    public void paint(Graphics g) {
        for (int i = 0; i < monsters.size(); i++) {
            getMonster(i).paint(g);
        }
        for (int i = 0; i < projectiles.size(); i++) {
            getProjectile(i).paint(g);
        }
    }
}

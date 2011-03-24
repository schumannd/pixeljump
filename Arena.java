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
        int nearestMonster = -1;
        double nearestMonsterDist = 9001;
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = getMonster(i);
            if (!m.isOnScreen())
                continue;
            double xdist = p.posX + p.getWidth()/2-(m.posX + m.getWidth()/2);
            double ydist = p.posY + p.getHeight()/2-(m.posY + m.getHeight()/2);
            double dist = Math.sqrt(xdist*xdist+ydist*ydist);
            if (dist < nearestMonsterDist) {
                nearestMonster = i;
                nearestMonsterDist = dist;
            }
        }
        if (nearestMonster != -1) {
           Monster m = getMonster(nearestMonster);
           double xdist = p.posX + p.getWidth()/2-(m.posX + m.getWidth()/2);
           double ydist = p.posY + p.getHeight()/2-(m.posY + m.getHeight()/2);
           p.setDirection(xdist, ydist);
        }
        else //kein monster da, nach oben schiessen
            p.setDirection(0, 1);
        
        projectiles.addElement(p);
    }
    
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
    
    public void move(double time) {
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = getProjectile(i);
            p.move(time);
            if (!p.isOnScreen())
                projectiles.removeElementAt(i);
        }
    }
    
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

    public void newMonster(int x, int y, int type) {
        monsters.addElement(new Monster(x, y, type));
    }
    public void newInvisibleM(int x, int y, int type) {
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
    
    public void paint(Graphics g) {
        for (int i = 0; i < monsters.size(); i++) {
            getMonster(i).paint(g);
        }
        for (int i = 0; i < projectiles.size(); i++) {
            getProjectile(i).paint(g);
        }
    }
}

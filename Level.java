import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Level {
    private int diff;
    public Vector platforms = new Vector();
    public Vector items = new Vector();
    public Vector monsters = new Vector();
    public Vector projectiles = new Vector();
    private int width;
    private int height;
    private Random r = new Random();
    private double num = 0;
    private double highest;
    
    public Level(int d, int w, int h) {
        diff = d;
        width = w;
        height = h;
        highest = height/2;
        createLvl(0);
    }
    
    private void createLvl(int rand) {
        if(rand == 0) {
            monsterChance();
            solvable();
            for(int i = diff; i > num; i--)
                easier();
        }
        else {
            addLvl(rand);
        }
    }
    
    public void addLvl(int nr) {
        
        }
    
    private void monsterChance() {
        for(int i = 0; i < 5+num-diff; i++)
            if(r.nextDouble() < 0.5 && i< 11) 
                monsters.addElement(new Monster(r.nextInt(width),
                                                r.nextInt(height)-height, 0));
    }
    
    private void solvable() {
        //Anfangsplatform
        if(num == 0)
            addNewPlat(width / 2 - 15, height -30, 0);

        for (int i = 0; i < 10; i++) {
            int x = r.nextInt(width - 30);
            int y = -i*92;
            //Platformen werden nicht ueber dem Bildschirm erzeugt
            if(num == 0)
                y +=height;
            int type = r.nextInt(2);

            int item = addNewPlat(x, y, type);
            if(item > 1)
                i++;
            
        }
    }
    
    private void easier() {
        for (int i = 0; i < 10; i++) {
            int x = r.nextInt(width - 30);
            int y = r.nextInt(92)-91*(i+1);
            //Platformen werden nicht ueber dem Bildschirm erzeugt
            if(num == 0)
                y +=height;
            int type = r.nextInt(3);

            addNewPlat(x, y, type);
        }
    }
    

    private int addNewPlat(int x, int y, int type){
        Platform plat = new Platform(x, y, type);
        int retItem = 0;
        platforms.addElement(plat);

        if(r.nextDouble() < 0.1 && (type == Platform.NORMAL || type == Platform.MOVE)){
            retItem++;
            int itemType = 0;
            int n = r.nextInt(100);
            if(80 < n )
                itemType++;
            Item item = new Item(plat, itemType);
            plat.item = item;
            items.addElement(item);
        }
        return retItem;
    }
    
    public void shoot(double x, double y) {
        Projectile p = new Projectile(x, y);
        p.posX -= p.getWidth()/2;
        //autoaim
        int nearestMonster = -1;
        double nearestMonsterDist = -1;
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = getMonster(i);
            double xdist = p.posX + p.getWidth()-(m.posX + m.getWidth());
            double ydist = p.posY + p.getHeight()-(m.posY + m.getHeight());
            double dist = Math.sqrt(xdist*xdist+ydist*ydist);
            if (dist > nearestMonsterDist) {
                nearestMonster = i;
                nearestMonsterDist = dist;
            }
        }
        if (nearestMonsterDist > 0) {
           Monster m = getMonster(nearestMonster);
           double xdist = p.posX + p.getWidth()-(m.posX + m.getWidth());
           double ydist = p.posY + p.getHeight()-(m.posY + m.getHeight());
           p.setDirection(xdist, ydist);
        }
        else 
            p.setDirection(0, -1);
        
        
        projectiles.addElement(p);
    }
    
    public void monsterProjectileCollision() {
        for(int i = 0; i < monsters.size(); i++){
            Monster m = getMonster(i);
            for (int j = 0; j < projectiles.size(); j++) {
                Projectile p = getProjectile(j);
                if (m.collidesWith(p, false)) {
                    monsters.removeElementAt(i);
                    i--;
                }
            }
        }
    }
    
    public void move(int ms) {
        for (int i = 0; i < platforms.size(); i++) {
            getPlat(i).moveSide(ms);
        }
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = getProjectile(i);
            p.move(ms);
            if (p.posY < 0)
                projectiles.removeElementAt(i);
        }
    }
    
    public void moveDown(double dist) {
        highest += dist;
        for (int i = 0; i < platforms.size(); i++) {
            Platform p = getPlat(i);
            p.moveDown(dist);
            
            if (p.posY > height) {
                items.removeElement(p.item);
                platforms.removeElementAt(i);
                i--;
            }
        }
        if(highest > 920-height/2 + 920*num*2.5) {
            num += 0.4;
            createLvl(0);
        }
        for (int i = 0; i < monsters.size(); i++) {
            getMonster(i).moveDown(dist);
        }
        for (int i = 0; i < projectiles.size(); i++) {
            getProjectile(i).moveDown(dist);
        }
    }
    
    public Platform getPlat(int i){
        return (Platform) platforms.elementAt(i);
    }
    
    public Item getItem(int i){
        return (Item) items.elementAt(i);
    }
    
    public Monster getMonster(int i){
        return (Monster) monsters.elementAt(i);
    }
    
    public Projectile getProjectile(int i){
        return (Projectile) projectiles.elementAt(i);
    }
    
    public void paintPlatAndItems(Graphics g) {
        for (int i = 0; i < platforms.size(); i++) {
            getPlat(i).paint2(g);
        }
        for (int i = 0; i < monsters.size(); i++) {
            getMonster(i).paint(g);
        }
        for (int i = 0; i < projectiles.size(); i++) {
            getProjectile(i).paint(g);
        }
    }
}
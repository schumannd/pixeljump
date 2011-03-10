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
    private int num = 0;
    private double highest;
    
    public Level(int d, int w, int h) {
        diff = d;
        width = w;
        height = h;
        highest = height/2;
        createLvl();
    }
    
    private void createLvl() {
        monsters.addElement(new Monster(20, -40, 0));
        solvable();
        for(int i = diff; i > num; i--)
            easier();
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
            int type = 0;

            addNewPlat(x, y, type);
        }
    }
    
    private void easier() {
        for (int i = 0; i < 10; i++) {
            int x = r.nextInt(width - 30);
            int y = r.nextInt(92)-91*(i+1);
            //Platformen werden nicht ueber dem Bildschirm erzeugt
            if(num == 0)
                y +=height;
            int type = r.nextInt(2)+1;

            addNewPlat(x, y, type);
        }
    }

    private void addNewPlat(int x, int y, int type){
        Platform plat = new Platform(x, y, type);
        platforms.addElement(plat);

        if(r.nextDouble() < 0.6 && (type == Platform.NORMAL || type == Platform.MOVE)){
            int itemType = 0;
            int n = r.nextInt(100);
            if(80 < n )
                itemType++;
            Item item = new Item(plat, itemType);
            plat.item = item;
            items.addElement(item);
        }
    }
    
    public void shoot(double x, double y) {
        Projectile p = new Projectile(x, y);
        p.posX -= p.getWidth()/2;
        //autoaim
        int nearestMonster = 0;
        
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
        if(highest > 920-height/2 + 920*num) {
            num += 1;
            createLvl();
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
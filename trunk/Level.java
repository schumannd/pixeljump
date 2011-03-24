import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Level {
    private int diff;
    public Vector platforms = new Vector();
    public Vector visiblePlat = new Vector();
    public Vector items = new Vector();
    private int width;
    private int height;
    private Random r = new Random();
    public int num = 0;
    private double lastSolvable = 0;
    private double lastPlat = 0;
    private double lastMonster = 0;
    private double highest;
    Arena arena;
    
    public Level(int d, int w, int h, Arena arena) {
        diff = d;
        width = w;
        height = h;
        highest = height/2;
        this.arena = arena;
    }

    private void createMonsters() {
        int pixel = 0;
        int yVal;
        while(pixel < 1000) {
            if(num+diff <=90)
                yVal = r.nextInt(5000-(num+diff)*50)+1;
            //falls lvl ueber 90
            else {
                yVal = r.nextInt(500)+1;
            }
            if(pixel + yVal <= 1000)
                arena.newInvisibleM(r.nextInt(width - 30), yVal, 0);
            pixel += yVal;
        }
    }
    
    private void createPlats() {
        int pixel = 0;
        while(pixel < 1000) {
            int yVal = r.nextInt((num+diff)*4+20)+1;
            if(pixel + yVal <= 1000)
                addPlat(r.nextInt(width - 30), yVal, r.nextInt(3));
            //falls Platform ueber 1000sten Pixel
            else {
                addPlat(r.nextInt(width - 30), 1000-pixel, r.nextInt(3));
            }
            pixel += yVal;
        }
    }
    
    private void addPlat(int x, int y, int type){
        Platform plat = new Platform(x, y, type, 1, 0);

        platforms.addElement(plat);
    }
    

    private void addVisPlat(int x, int y, int type){
        Platform plat = new Platform(x, y, type, 1, 0);

        visiblePlat.addElement(plat);
//        GameMain.b3d.addPlatform(x, y);

        if(r.nextDouble() < 0.2 && (type == Platform.NORMAL || type == Platform.MOVE)){

            int itemType = Item.SPRING;
            int n = r.nextInt(100);
            if(80 < n )
                itemType = Item.TRAMPOLINE;
            if(n < 60)
                itemType = Item.SHIELD;
            if(n < 20)
                itemType = Item.SPRINGSHOE;
            if(n < 10)
                itemType = Item.ROCKET;
            
            Item item = new Item(plat, itemType);
            plat.item = item;
            items.addElement(item);
        }
    }
    
    public void moveDown(double dist) {
        
        highest += dist;
        //Abstand zur letzten essentiellen Platform
        lastSolvable += dist;
        //Abstand zur letzten  Platform
        lastPlat += dist;
        //Abstand zum letzten  Monster
        lastMonster += dist;
        
        //sichtbare platformen bewegen
        for (int i = 0; i < visiblePlat.size(); i++) {
            Platform p = getVisPlat(i);
            p.moveDown(dist);
            
            if (p.posY > height) {
                items.removeElement(p.item);
                visiblePlat.removeElementAt(i);
                i--;
            }

        }
        if(highest >= 1000*num+height/2) {
            createPlats();
            createMonsters();
            num++;
        }
        
        //letze essentielle Platfrom zu weit weg?
        if(lastSolvable >= 75) {
            addVisPlat(r.nextInt(width - 30), 0, 0);
            lastSolvable = 0;

        }

        //platformen oben erscheinen lassen
        while(platforms.size() > 0 && getPlat(0).getPosY() <= lastPlat) {
            lastPlat = 0;
            addVisPlat((int)getPlat(0).getPosX(), -getPlat(0).getHeight(), getPlat(0).getType());
            platforms.removeElementAt(0);
        }
        //Monster oben erscheinen lassen
        while(arena.invisibleM.size() > 0 && arena.getInvisibleM(0).getPosY() <= lastMonster) {
            lastMonster = 0;
            arena.newMonster((int)arena.getInvisibleM(0).getPosX(), -arena.getInvisibleM(0).getHeight(),
                            arena.getInvisibleM(0).getType());
            arena.invisibleM.removeElementAt(0);
        }

    }
    
    public Platform getVisPlat(int i){
        return (Platform) visiblePlat.elementAt(i);
    }
    
    public Platform getPlat(int i){
        return (Platform) platforms.elementAt(i);
    }
    
    public Item getItem(int i){
        return (Item) items.elementAt(i);
    }
    
    public void move(int ms) {
        for (int i = 0; i < visiblePlat.size(); i++) {
            getVisPlat(i).moveSide(ms);
        }
    }
    
    public void paintPlatAndItems(Graphics g) {
        for (int i = 0; i < visiblePlat.size(); i++) {
            getVisPlat(i).paint(g);
        }
        for (int i = 0; i < items.size(); i++) {
            getItem(i).paint(g);
        }
    }
}

import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Level {
    private int diff;
    public Vector platforms = new Vector();
    public Vector items = new Vector();
    private int width;
    private int height;
    private Random r = new Random();
    private double num = 0;
    private double highest;
    Arena arena;
    
    public Level(int d, int w, int h, Arena arena) {
        diff = d;
        width = w;
        height = h;
        highest = height/2;
        this.arena = arena;
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
    
    private void addLvl(int nr) {
        
        }
    
    private void monsterChance() {
        for(int i = 0; i < 5+num-diff; i++)
            if(r.nextDouble() < 0.5 && i< 11)
                arena.newMonster(r.nextInt(width), r.nextInt(height)-height, 0);
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
            int type = r.nextInt(3);

            addNewPlat(x, y, type);
        }
    }
    

    private void addNewPlat(int x, int y, int type){
        Platform plat = new Platform(x, y, type);

        platforms.addElement(plat);

        if(r.nextDouble() < 1 && (type == Platform.NORMAL || type == Platform.MOVE)){

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
    
    public void move(int ms) {
        for (int i = 0; i < platforms.size(); i++) {
            getPlat(i).moveSide(ms);
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
    }
    
    public Platform getPlat(int i){
        return (Platform) platforms.elementAt(i);
    }
    
    public Item getItem(int i){
        return (Item) items.elementAt(i);
    }
    
    public void paintPlatAndItems(Graphics g) {
        for (int i = 0; i < platforms.size(); i++) {
            getPlat(i).paint(g);
        }
        for (int i = 0; i < items.size(); i++) {
            getItem(i).paint(g);
        }
    }
}
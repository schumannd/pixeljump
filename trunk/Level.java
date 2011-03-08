import java.util.*;

import javax.microedition.lcdui.Graphics;
import java.util.Random;

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
    
    public Platform getPlat(int i){
        return (Platform) platforms.elementAt(i);
    }
    
    public Item getItem(int i){
        return (Item) items.elementAt(i);
    }
    
    public Monster getMonster(int i){
        return (Monster) monsters.elementAt(i);
    }
    
    
    public int getSize(){
        return platforms.size();
    }

    
    public void monsterProjectileCollision(Vector monsters) {
        for(int i = 0; i < monsters.size(); i++){
            Monster m = (Monster) monsters.elementAt(i);
            for (int j = 0; j < projectiles.size(); j++) {
                Projectile p = (Projectile) projectiles.elementAt(j);
                if (m.collidesWith(p, false)) {
                    monsters.removeElementAt(i);
                    i--;
                }
            }
        }
    }
    
    
    private void createLvl() {
        monsters.addElement(new Monster(20, 20, 0));
        solvable();
        for(int i = diff; i > num; i--)
            easier();
        
    }
    
    public void move(int ms) {
        for (int i = 0; i < getSize(); i++) {
            Platform p = getPlat(i);
            p.moveSide(ms);
        }
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = (Projectile) projectiles.elementAt(i);
            p.move(ms);
            if (p.posY < 0)
                projectiles.removeElementAt(i);
        }
    }
    
    public void moveDown(double dist) {
        highest += dist;
        for (int i = 0; i < getSize(); i++) {
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
            Monster m = getMonster(i);
            m.moveDown(dist);
        }
        
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = (Projectile) projectiles.elementAt(i);
            p.moveDown(dist);
        }
    }
    
    private void solvable() {
        //Anfangsplatform
        if(num == 0)
            addNewPlat(width / 2 - 15, height -30, 0);

        for (int i = 0; i < 10; i++) {
            int x = r.nextInt(width - 30);
            int y = -i*92;
            //Platformen werden nicht über dem Bildschirm erzeugt
            if(num == 0)
                y +=height;
            int type = 0;

            addNewPlat(x, y, type);
        }
    }
    
    private void easier() {
        for (int i = 0; i < 10; i++) {
            int x = r.nextInt(width - 30);
            int y = r.nextInt(920)-919;
            //Platformen werden nicht über dem Bildschirm erzeugt
            if(num == 0)
                y +=height;
            int type = r.nextInt(2)+1;

            addNewPlat(x, y, type);
        }
    }

    private void addNewPlat(int x, int y, int type){
        Platform plat = new Platform(x, y, 30, type);
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
    
    public void paintPlatAndItems(Graphics g) {
        for (int i = 0; i < getSize(); i++) {
            getPlat(i).paint2(g);
        }
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = getMonster(i);
            m.paint(g);
        }
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = (Projectile) projectiles.elementAt(i);
            p.paint(g);
        }
    }
    
    public void paintHeight(Graphics g) {
        g.drawString("Height: "+Double.toString(Math.floor(highest)), 15, 15, Graphics.TOP | Graphics.LEFT);
    }

}
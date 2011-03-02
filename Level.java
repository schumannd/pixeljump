import java.util.*;

public class Level {
    
    public int lvl;
    public Vector platforms = new Vector();
    private int width;
    private int height;
    Random r = new Random();
    
    
    public Level(int l, int w, int h) {
        lvl = l;
        width = w;
        height = h;
        createLvl(lvl);
    }
    
    public Platform getPlat(int i){
        return (Platform) platforms.elementAt(i);
    }
    
    public int getSize(){
        return platforms.size();
    }
    
    /** 
     * Loescht alle Plattformen, die sich unter dem Bildschirm befinden.
     */
    private void deletePlatforms() {
        for (int i = 0; i < getSize(); i++) {
            Platform p = (Platform) getPlat(i);
            if (p.posY > height)
                platforms.removeElementAt(i);
        }
    }
    
    private void createLvl(int lvl) {
        solvable();
        for(int i = 0; i < lvl; i++)
            easier();
    }
    
    public void move(double dist) {
        //Wenn der Pixel ueber der Mitte ist, bewege alle Plattformen und den Pixel entsprchend.
        
        for (int i = 0; i < getSize(); i++) {
            Platform p = (Platform) getPlat(i);
            p.posY += dist;
            //TODO: hier deletePlatforms inlinen
            
        }
        deletePlatforms();
    }
    
    private void solvable() {
        //mittige Plattform, sodass man nicht gleich zu Beginn runterfaellt
        platforms.addElement(new Platform(width / 2 - 15, height - 30, 30, 1));
        
        for (int i = 1; i <= 20; i++) {
            int x = r.nextInt(width - 30);
            int y = height-30-i*91;
            platforms.addElement(new Platform(x, y, 30, r.nextInt(2)));
        }
    }
    
    private void easier() {
        for (int i = 0; i < 10; i++) {
            int x = r.nextInt(width - 30);
            int y = r.nextInt(height+1820)-1820;
            platforms.addElement(new Platform(x, y, 30, r.nextInt(3)));
        }
    }

}
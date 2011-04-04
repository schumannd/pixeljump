import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Level {
    public int diff;
    private final Vector invisPlats = new Vector();
    public final Vector visiblePlats = new Vector();
    public final Vector items = new Vector();
    private final int width;
    private final int height;
    private final Random r = new Random();
    private double lastSolvable = 0;
    private double lastPlat = 0;
    private double lastMonster = 0;
    private double highest;
    private final Arena arena;
    
    /*
     * Klasse des Hauptlevels
     * alle 1000 Pixel entsteht ein neues Level
     * 
     * @param d = Startlevel, also Schwierigkeitsstufe
     * @param w = Bildschirmbreite
     * @param h = Bildschirmhoehe
     * @param arena = uebergabe der Arena
     * 
     * */

    public Level(int d, int w, int h, Arena arena) {
        diff = d;
        width = w;
        height = h;
        highest = 1000;
        this.arena = arena;
    }

    /*
     * Methode zum bewegen des Levels
     *
     * @param dist = Anzahl der Pixel um die das Level bewegt wird
     */
    public void moveDown(double dist) {
        //fortschritt im Jetzigen Level
        highest += dist;
        //Abstand zur letzten essentiellen Platform
        lastSolvable += dist;
        //Abstand zur letzten  Platform
        lastPlat += dist;
        //Abstand zum letzten  Monster
        lastMonster += dist;

        //sichtbare platformen bewegen
        for (int i = 0; i < visiblePlats.size(); i++) {
            Platform p = getVisPlat(i);
            p.moveDown(dist);
            //Platformen löschen, die unten raus sind
            if (p.posY > height) {
                items.removeElement(p.item);
                visiblePlats.removeElementAt(i);
                i--;
            }

        }
        //random level erzeugen (einmal alle 1000 pixel)
        if(highest >= 1000) {
            createPlats();
            createMonsters();
            diff++;
            highest = 0;
        }
        //level solvable machen (jeden move)
        if(lastSolvable >= 75) {
            addVisPlat(r.nextInt(width - 30), 0, 0);
            lastSolvable = 0;

        }

        //platformen oben erscheinen lassen
        while(invisPlats.size() > 0 && getInvisPlat(0).getPosY() <= lastPlat) {
            lastPlat = 0;
            Platform p = getInvisPlat(0);
            addVisPlat((int)p.getPosX(), -p.getHeight(), p.getType());
            invisPlats.removeElementAt(0);
        }
        //Monster oben erscheinen lassen
        while(arena.invisibleM.size() > 0 && arena.getInvisibleM(0).getPosY() <= lastMonster) {
            lastMonster = 0;
            Monster m = arena.getInvisibleM(0);
            arena.newMonster((int)m.getPosX(), -m.getHeight(), m.getType());
            arena.invisibleM.removeElementAt(0);
        }

    }
    /*
     * Methode zum erstellen der Monster fuer ein Level
     * (alle 1000 Pixel
     */
    private void createMonsters() {
        int pixel = 0;
        int yVal;
        while(pixel < 1000) {
            //In Level 1-90
            if(diff <=90)
                //zufaelliger abstand zum naechsten Monster, je
                //hoeher das level, desto oefter kommen Monster
                yVal = r.nextInt(5000-diff*50)+1;
            //falls lvl ueber 90, kein negativer Parameter fuer nextInt()
            else {
                yVal = r.nextInt(500)+1;
            }
            //falls yVal < 1000 wird ein Monster erstellt
            if(pixel + yVal <= 1000)
                arena.newInvisibleM(r.nextInt(width - 30), yVal, 0);
            pixel += yVal;
        }
    }

    /*
     * Methode zum erstellen der Platformen fuer ein Level
     * (alle 1000 Pixel
     */
    private void createPlats() {
        int pixel = 0;
        while(pixel < 1000) {
            //random abstand zur naechsten Platform
            //Abstand jedes Level um ø 2 pixel groesser
            int yVal = r.nextInt(diff*4+20)+1;
            try{
            Platform p = (Platform) visiblePlats.elementAt(0);
            if(yVal < p.getHeight())
                yVal = p.getHeight();

            }
            catch(Exception e) {}
            int type = r.nextInt(3);
            if(pixel + yVal <= 1000)
                addInvisPlat(r.nextInt(width - 30), yVal, type);
            //falls Platform ueber 1000sten Pixel
            else {
                addInvisPlat(r.nextInt(width - 30), 1000-pixel, type);
            }
            pixel += yVal;
        }
    }

    /*
     * Methode zum Hinzufuegen von Platformen zum Vektor fuer sichtbare Platformen
     * @param x = X-Koordinate auf dem Bildschirm
     * @param y = Y-Koordinate auf dem Bildschirm
     * @param type = Platformtyp (0=Normal, 1=Zerbrechlich,...)
     */

    private void addVisPlat(int x, int y, int type){
        Platform plat = new Platform(type, new double[][]{{x, y}});

        visiblePlats.addElement(plat);
//        GameMain.b3d.addPlatform(x, y);

        if(r.nextDouble() < 0.2 && (type == Platform.NORMAL)){

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

    /*
     * Methode zum Hinzufuegen von Platformen zum Vektor fuer unsichtbare Platformen
     * @param x = X-Koordinate auf dem Bildschirm
     * @param y = Abstand zur naechsten Platform
     * @param type = Platformtyp (0=Normal, 1=Zerbrechlich,...)
     */

    private void addInvisPlat(int x, int y, int type){
        Platform plat = new Platform(type, new double[][]{{x, y}});

        invisPlats.addElement(plat);
    }

    /*
     * Methode gibt Platformen, die sich auf dem Bildschirm befinden zurueck
     * @param i = index
     * @return = gesuchte Platform
     */
        
    public Platform getVisPlat(int i){
        return (Platform) visiblePlats.elementAt(i);
    }
    /*
     * Methode gibt Platformen, die sich nicht auf dem Bildschirm befinden zurueck
     * @param i = index
     * @return = gesuchte Platform
     */
    
    public Platform getInvisPlat(int i){
        return (Platform) invisPlats.elementAt(i);
    }

    /*
     * Methode gibt Items aus dem Vektor zurueck
     * @param i = index
     * @return = gescuhtes Item
     */
    public Item getItem(int i){
        return (Item) items.elementAt(i);
    }
    /*
     * Methode bewegt bewegliche Platformen zur seite.
     * @param time = Zeit, die sich dei Platform bewegen soll?
     */
    
    public void move(double time) {
        for (int i = 0; i < visiblePlats.size(); i++) {
            getVisPlat(i).moveSide(time);
        }
    }
    /*
     * Methode Zeichnet Platformen und Items
     * @param g = Grafikobjekt uebergeben
     */
    
    public void paintPlatAndItems(Graphics g) {
        for (int i = 0; i < visiblePlats.size(); i++) {
            getVisPlat(i).paint(g);
        }
        for (int i = 0; i < items.size(); i++) {
            getItem(i).paint(g);
        }
    }
}

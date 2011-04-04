import java.util.*;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.GameCanvas;

class GameMain extends GameCanvas {

    private int gameState;
    private int statuslinks = 0;
    private int statusrechts = 0;
    private Timer mainTimer;
    private Pixel pixel;
    private Background2D b2d;
//    public static Background3D b3d;
    private Level level;
    private Arena arena;
    private int score = 0;
    public Highscore highscore;
    private MainMIDlet midlet;

    
    private final int FPS = 40;
    private final int GS_GAMEOVER = 3;
    private final int GS_GAME = 1;
    private final int GS_START = 0;   

    /*
     * Konstrukter des Spiels
     * Extends GameCanvas
     */
    public GameMain() {
        super(false);
    }

    /*
     * Initialisierungsmethode des Programms
     *
     * param m = Hauptmidlet wird uebergeben
     */
    public void init(MainMIDlet m) {
        midlet = m;
        b2d = new Background2D(getWidth(), getHeight());
        SoundManager.init();
        highscore = new Highscore();
        GameObject.init(getWidth(), getHeight());
        
        initNewGame();
    }

    /*
     * Methode zum zeichen des Spielgeschehens
     *
     * @param g = Grafikobjekt wird uebergeben
     */
    public void paint(Graphics g) {
         b2d.paint(g);
        switch (gameState) {
        case GS_START:
            g.drawString("press start!", getWidth() / 2, getHeight() / 2,
                    Graphics.BASELINE | Graphics.HCENTER);
            break;
        case GS_GAME:
            level.paintPlatAndItems(g);
            arena.paint(g);
            pixel.paint(g);
            if(Item.isShieldActive()){
                //die Koordinaten der mitte des pixels
                int xMPixel = (int) pixel.getPosX();
                int yMPixel = (int) pixel.getPosY() - pixel.getHeight()/2;
                //die koordinaten (x und y, weil quadratisch) des Mittelpunkts des Schilds
                int mShield = Tools.itemImages[5].getWidth()/2;
                //den Schild mittig auf den Pixel zeichnen
                g.drawImage(Tools.itemImages[5], xMPixel - mShield, yMPixel - mShield, 0);
            }
                
            //Aktuelles Level links oben und aktueller Score rechts oben
            g.drawString("Score: " + score, getWidth()-65, 15, Graphics.TOP | Graphics.LEFT);
            g.drawString("Lvl: " + level.diff, 15, 15, Graphics.TOP | Graphics.LEFT);
            break;
        case GS_GAMEOVER:
            g.drawString("GAME OVER", getWidth() / 2, getHeight() * 3 / 5,Graphics.BASELINE | Graphics.HCENTER);
            g.drawString("Score: "+Integer.toString(score), getWidth() / 2, getHeight() / 2 +15,
                    Graphics.BASELINE | Graphics.HCENTER);
            highscore.paintHighscores(g);
            if(((String) highscore.data.elementAt(highscore.nameIndex)).equals("YOU"))
                g.drawString("Press DOWN to enter your Name!", 10, getHeight() - 20 , 0);
            break;
        }
        Debug.print(g);
    }

    /*
     * Hauptmethode fuer das Gameplay
     *
     * @param ms = Millisekunden die seit letztem Aufruf vergangen sind
     */
    private void doGamePlay(int ms) {
        double time  = 15*ms/1000.d;
        //Handling von rechts/links-Tasten
        int keycode = getKeyStates();
        int leftright = 0;
        if ((keycode & LEFT_PRESSED) != 0) {
            leftright = -1;
            pixel.setTransform(Sprite.TRANS_MIRROR); // bei Pfeil-Nach-Links-Taste wird der Pixel nach links gespiegelt
            if(leftright == -1 && statuslinks == 0) { // wenn links-gedrückt und nicht schon links
             
            statuslinks = 1;
            statusrechts = 0;}
            }

            
        if ((keycode & RIGHT_PRESSED) != 0){
            leftright = 1;
           
            pixel.setTransform(Sprite.TRANS_NONE);
            if(leftright == 1 && statusrechts == 0) {
            
            statusrechts = 1;
            statuslinks = 0;
            }
        }

        pixel.accelerate(leftright, time);
        
        pixel.resetImage();

        pixel.move(level.visiblePlats, level.items, time);
        level.move(time);
        arena.move(time);
        arena.monsterProjectileCollision();
        //gameover weil mit monster kollidiert

        if (pixel.monsterCollision(arena.monsters)) {
            SoundManager.deathm();
            gameOver();
            return;
        }
        
        //gameover weil unten rausgefallen
        if(pixel.posY > getHeight()) {            
            SoundManager.death();
            gameOver();
            return;
        }

        //Abstand des Pixels zu Bildschirmmitte.
        double dist = getHeight() / 2 - pixel.getPosY();

        //Wenn der Pixel ueber der Mitte ist, bewege alle Plattformen und den Pixel entsprchend.
        if (dist > 0) {
            b2d.moveDown(dist);
            level.moveDown(dist);
            arena.moveDown(dist);
            pixel.moveDown(dist);
            score += dist;
        }
    }

    /*
     * Methode zum starten des Timer
     */
    public void startTimer() {
        final int ms = 1000/FPS;
        stopTimer();
        mainTimer = new Timer();
        TimerTask pt = new TimerTask() {
            public final void run() {
                doGamePlay(ms);
                repaint();
            }
        };
        mainTimer.schedule(pt, 0, ms);
    }
    /*
     * Methode zum stoppen des Timers
     */
    public void stopTimer() {
        if (mainTimer != null)
            mainTimer.cancel();
        mainTimer = null;
    }
    /*
     * Initialisierungsmethode fuer ein neues Spiel
     */
    public void initNewGame() {
        pixel = new Pixel(getWidth() / 2, getHeight()-1);
        arena = new Arena(getHeight());
        level = new Level(0,getWidth(), getHeight(), arena);
        SoundManager.start();
        Item.reset();
        score = 0;
        gameState = GS_GAME;
    }
    
    /**
     * Beendet das Spiel, sorgt fuer die Anzeige der Highscores und ggf. fuer 
     * die Eingabe des Spielernamens.
     * 
     * Wenn ein neuer Highscore erreicht wurde, wird zunaechst YOU als Spielername
     * in die Liste eingetragen. Der Spieler kann dann durch druecken von DOWN
     * seinen eigenen Namen eingeben.
     */
    public void gameOver(){
        stopTimer();
        gameState = GS_GAMEOVER;
        if(highscore.isNewHighscore(score)){
            highscore.addScore(score, "YOU");
            //Eingabe des Spielernamens wenn DOWN gedrueckt wurde
            while((getKeyStates() & DOWN_PRESSED) == 0) {
                repaint();
            }
            midlet.setSpielerName();
        }
        else
            midlet.showHighscore();
    }
    /*
     * Methode fuers schiessen
     */
    protected void keyPressed(int keyCode) {
        if (keyCode == getKeyCode(UP) && gameState == GS_GAME)
            pixel.shoot(arena);
    }
}
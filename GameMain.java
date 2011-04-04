import java.util.*;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

class GameMain extends GameCanvas {

    private int gameState;
    private Timer mainTimer;
    private Pixel pixel;
    private Background2D b2d;
    private Level level;
    private Arena arena;
    private int score = 0;
    public Highscore highscore;
    private MainMIDlet midlet;
    
    private final int FPS = 40;
    private final int GS_GAMEOVER = 3;
    private final int GS_GAME = 1;
    private final int GS_START = 0;   

    /**
     * Konstrukter des Spiels
     * Extends GameCanvas
     */
    public GameMain() {
        super(false);
    }

    /**
     * Initialisierungsmethode des Programms
     *
     * param m = Hauptmidlet wird uebergeben
     */
    public void init(MainMIDlet m) {
        midlet = m;
        b2d = new Background2D(getHeight());
        SoundManager.init();
        highscore = new Highscore();
        GameObject.init(getWidth(), getHeight());
        
        initNewGame();
    }

    /**
     * Zeichnet den Startscreen, das Spielgeschehen oder die Highscores.
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
            
            highscore.paintHighscores(g, score);
            
            break;
        }
        Debug.print(g);
    }

    /**
     * Hauptmethode. Fragt Benutzereingaben ab, startet Kollisionsberechnungen etc.
     *
     * @param ms = Millisekunden die seit letztem Aufruf vergangen sind
     */
    private void doGamePlay(int ms) {
        double time  = 15*ms/1000.d;
        //Handling von rechts/links-Tasten
        int keycode = getKeyStates();
        int leftright = 0;
        if ((keycode & LEFT_PRESSED) != 0)
            leftright = -1;
        if ((keycode & RIGHT_PRESSED) != 0)
            leftright = 1;

        pixel.accelerate(leftright, time);
        
        pixel.resetImage();

        pixel.move(level.visiblePlats, level.items, time);
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

    /**
     * Startet den Timer.
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
    
    /**
     * Stoppt den Timer.
     */
    public void stopTimer() {
        if (mainTimer != null)
            mainTimer.cancel();
        mainTimer = null;
    }
    
    /**
     * Initialisiert ein neues Spiel.
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
        midlet.startAllowed = false;
        gameState = GS_GAMEOVER;
        if(highscore.isNewHighscore(score)){
            highscore.addScore(score, "YOU");
            //Eingabe des Spielernamens wenn DOWN gedrueckt wurde
            while((getKeyStates() & DOWN_PRESSED) == 0) {
                repaint();
            }
            midlet.setSpielerName();
            midlet.startAllowed = true;
        }
        else{
            midlet.showHighscore();
            midlet.startAllowed = true;
        }
    }
    
    /**
     * Veranlasst bei Druecken der UP-Taste einen Schuss
     */
    protected void keyPressed(int keyCode) {
        if (keyCode == getKeyCode(UP) && gameState == GS_GAME)
            pixel.shoot(arena);
    }
}
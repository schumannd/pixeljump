import java.util.*;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

class GameMain extends GameCanvas {

    private int gameState;
    private Timer mainTimer;
    private Pixel pixel;
    private Background2D b2d;
//    public static Background3D b3d;
    private Level level;
    private Arena arena;
    private int score = 0;
    private boolean enterName = false;
    Highscore highscore;
    MainMIDlet midlet;
    
    private final int FPS = 40;

    public GameMain() {
        super(false);
        gameState = 0;
    }

    public void init(MainMIDlet m) {
        midlet = m;
//        b3d = new Background3D(getWidth(), getHeight());
        b2d = new Background2D(getWidth(), getHeight());
        SoundManager.init();
        initNewGame();
        highscore = new Highscore();
        highscore.init(getHeight(), getWidth());
        
        GameObject.init(getWidth(), getHeight());
    }

    public void paint(Graphics g) {
         b2d.draw(g);
//        b3d.paint(g);
        switch (gameState) {
        case 0: //startbildschirm
            g.drawString("press start!", getWidth() / 2, getHeight() / 2,
                    Graphics.BASELINE | Graphics.HCENTER);
            break;
        case 1: //im spiel
            level.paintPlatAndItems(g);
            arena.paint(g);
            pixel.paint(g);
            if(Item.isShieldActive())
                g.drawImage(Tools.itemImages[5],(int) pixel.getPosX() - 25,(int) pixel.getPosY() - 60, 0);
            
            g.drawString("Score: "+Integer.toString(score), getWidth()-65, 15, Graphics.TOP | Graphics.LEFT);
                g.drawString("Lvl: "+Integer.toString(level.diff), 15, 15, Graphics.TOP | Graphics.LEFT);
            break;
        case 3: //gameover
            g.drawString("GAME OVER", getWidth() / 2, getHeight() * 3 / 5,Graphics.BASELINE | Graphics.HCENTER);
            g.drawString("Score: "+Integer.toString(score), getWidth() / 2, getHeight() / 2 +15,
                    Graphics.BASELINE | Graphics.HCENTER);
            highscore.showHighscores(g);
            if(((String) highscore.data.elementAt(highscore.nameIndex)).equals("YOU"))
                g.drawString("Press DOWN to enter your Name!", 10, getHeight() - 20 , 0);
            break;
        }
        Debug.print(g);
    }


    private void doGamePlay(int ms) {
        double time  = 15*ms/1000.d;
        int keycode = getKeyStates();
        int leftright = 0;
        if ((keycode & LEFT_PRESSED) != 0)
            leftright = -1;
        if ((keycode & RIGHT_PRESSED) != 0)
            leftright = 1;
        pixel.accelerate(leftright, time);
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
    
    public void stopTimer() {
        if (mainTimer != null)
            mainTimer.cancel();
        mainTimer = null;
    }

    public void initNewGame() {
        pixel = new Pixel(getWidth() / 2, getHeight()-1);
        arena = new Arena(getHeight());
//        b3d.removeAll();
        level = new Level(0,getWidth(), getHeight(), arena);
        SoundManager.start();
        Item.reset();
        score = 0;
        gameState = 1;
    }

    public void gameOver(){
        gameState = 3;
        stopTimer();
        if(highscore.isNewHighscore(score)){
            highscore.addScore(score, "YOU");
            while(!enterName) {
                repaint();
            }
            midlet.setSpielerName();
            enterName = false;
        }
        else
            midlet.showHighscore();
        
    }
    
    
    protected void keyPressed( int keyCode ) {
        if (keyCode == getKeyCode(UP) && gameState == 1)
            pixel.shoot(arena);
        if (keyCode == getKeyCode(DOWN))
            enterName = true;

    }

    public int getScore() {
        return score;
    }


}
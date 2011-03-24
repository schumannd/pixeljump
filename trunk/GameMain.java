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
    Highscore highscore;
    
    private final int FPS = 40;

    public GameMain() {
        super(false);
        gameState = 0;
    }

    public void init() {
//        b3d = new Background3D();
//        b3d.init(getWidth(), getHeight());
        b2d = new Background2D(getWidth(), getHeight());
        SoundManager.init();
        initNewGame();
        highscore = new Highscore();
        
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
            
            g.drawString("Score: "+Integer.toString(score), getWidth()-65, 15, Graphics.TOP | Graphics.LEFT);
            break;
        case 3: //gameover
            g.drawString("GAME OVER", getWidth() / 2, getHeight() / 2,Graphics.BASELINE | Graphics.HCENTER);
            g.drawString("Score: "+Integer.toString(score), getWidth() / 2, getHeight() / 2 +15,
                    Graphics.BASELINE | Graphics.HCENTER);
            break;
        }
        Debug.print(g);
    }


    private void doGamePlay(int ms) {
        int keycode = getKeyStates();
        int leftright = 0;
        if ((keycode & LEFT_PRESSED) != 0)
            leftright = -1;
        if ((keycode & RIGHT_PRESSED) != 0)
            leftright = 1;
        pixel.accelerate(leftright, ms);
        pixel.move(level.visiblePlat, level.items, ms);
        level.move(ms);
        arena.move(ms);
        arena.monsterProjectileCollision();
        //gameover weil mit monster kollidiert
        if (pixel.monsterCollision(arena.monsters)) {
            gameState = 3;
            stopTimer();
            SoundManager.deathm();
            return;
        }
        
        //gameover weil unten rausgefallen
        if(pixel.posY > getHeight()) {
            gameState = 3;
            stopTimer();
            SoundManager.death();
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
        level = new Level(100,getWidth(), getHeight(), arena);
        SoundManager.start();
        Item.reset();
//        b3d.removeAll();
        score = 0;
        gameState = 1;
    }
    
    
    protected void keyPressed( int keyCode ) {
        if (keyCode == getKeyCode(UP))
            pixel.shoot(arena);
    }
}

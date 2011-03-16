import java.util.*;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

class GameMain extends GameCanvas {
    
    private int gameState;
    private Timer mainTimer;
    private Pixel pixel;
    private Background2D b2d;
    //private Background3D b3d;
    private Level l;
    private Arena arena;
    private int score = 0;
    
    private final int FPS = 40;

    public GameMain() {
        super(false);
        gameState = 0;
    }

    public void init() {
        initNewGame();
        
        new SoundManager().init();
        
        GameObject.init(getWidth(), getHeight());
    }

    public void paint(Graphics g) {
        //clear everything
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0, 0, 0);
        b2d.draw(g);

        switch (gameState) {
        case 0: //startbildschirm
            g.drawString("press start!", getWidth() / 2, getHeight() / 2,
                    Graphics.BASELINE | Graphics.HCENTER);
            break;
        case 1: //im spiel
            l.paintPlatAndItems(g);
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
        pixel.move(getWidth(), l.platforms, l.items, ms);
        l.move(ms);
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
        double dist = getHeight() / 2 - pixel.posY;
        //Wenn der Pixel ueber der Mitte ist, bewege alle Plattformen und den Pixel entsprchend.
        if (dist > 0) {
            b2d.moveDown(dist);
            l.moveDown(dist);
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
        pixel = new Pixel(getWidth() / 2, getHeight() / 2);
        SoundManager.start();
        arena = new Arena(getHeight());
        l = new Level(3,getWidth(), getHeight(), arena);
        b2d = new Background2D(getWidth(), getHeight());
        gameState = 1;
    }
    
    
    protected void keyPressed( int keyCode ) {
        if (keyCode == getKeyCode(UP))
            pixel.shoot(arena);
    }
}
import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.game.*;

class GameMain extends GameCanvas {
    
    private int gameState;
    private Timer mainTimer;
    private Pixel pixel;
    private Background2D b2d;
    //private Background3D b3d;
    private Level l;
    
    private final int FPS = 40;

    public GameMain(MainMIDlet midlet) {
        super(false);
        gameState = 0;
    }

    public void init() {
        initNewGame();
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
            pixel.paint(g);
            pixel.paintProjectiles(g);
            
            pixel.paintScore(g, getWidth());
            l.paintHeight(g);
            break;
        case 3: //gameover
            g.drawString("GAME OVER", getWidth() / 2, getHeight() / 2,Graphics.BASELINE | Graphics.HCENTER);
            g.drawString("Score: "+Integer.toString(pixel.score), getWidth() / 2, getHeight() / 2 +15,
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
        pixel.move(getWidth(), l.platforms, l.items, l.monsters, ms);
        if (pixel.monsterCollision(l.monsters)) {
            gameState = 3;
            stopTimer();
            return;
        }
        pixel.moveProjectiles(ms);
        
        //gameover
        if(pixel.posY > getHeight())
            gameState = 3;
        
        //Abstand des Pixels zu Bildschirmmitte.
        double dist = getHeight() / 2 - pixel.posY;
        //Wenn der Pixel ueber der Mitte ist, bewege alle Plattformen und den Pixel entsprchend.
        if (dist > 0) {
            b2d.setDist(dist);
            l.move(dist, ms);
            pixel.moveDown(dist);
            pixel.moveProjectilesDown(dist);
        }
        else {
            l.move(0, ms);
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
        Item.pixel = pixel;
        
        l = new Level(3,getWidth(), getHeight());
        b2d = new Background2D(getWidth(), getHeight());
        gameState = 1;
    }

    private void stopGame() {
        stopTimer();
        gameState = 0;
        repaint();
    }
    
    
    protected void keyPressed( int keyCode ) {
        if (keyCode == getKeyCode(UP))
            pixel.shoot();
    }
}
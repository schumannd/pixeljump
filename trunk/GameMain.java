import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.game.*;

class GameMain extends GameCanvas {
    
    private int gameState;
    private Timer mainTimer;
    private Pixel pixel;
    //private Vector platforms = new Vector();
    private Background2D b2d;
    //private Background3D b3d;
    private Level l;

    public GameMain(MainMIDlet midlet) {
        super(true);
        //b2d = new Background2D(getWidth(), getHeight());
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
        //b2d.draw(g);

        switch (gameState) {
        case 0:
            g.drawString("press start!", getWidth() / 2, getHeight() / 2,
                    Graphics.BASELINE | Graphics.HCENTER);
            break;
        case 1:
            for (int i = 0; i < l.getSize(); i++) {
                Platform p = (Platform) l.getPlat(i);
                //breakable == blau
                if( p.type == 1)
                    g.setColor(0, 0, 255);
                //fake == rot
                if(p.type == 2)
                    g.setColor(255, 0, 0);
                
                g.fillRect((int)p.posX, (int)p.posY, p.size, 3);
                g.setColor(0, 0, 0);
            }
            g.fillRect((int)pixel.posX-3, (int)pixel.posY-3, 3, 3);
            
            g.drawString("Score: "+Integer.toString(pixel.score), getWidth()-65, 15, Graphics.TOP | Graphics.LEFT);
            
            break;
        case 3:
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
        pixel.move(getWidth(), getHeight(), l.platforms, ms);
        
        
        if(pixel.posY > getHeight())
            gameState = 3;
        
        //Abstand des Pixels zu Bildschirmmitte.
        double dist = getHeight() / 2 - pixel.posY;
        //Wenn der Pixel ueber der Mitte ist, bewege alle Plattformen und den Pixel entsprchend.
        if (dist > 0) {
            l.move(dist);
            pixel.posY += dist;
            pixel.score += dist;
        }
        
    }
    
    public void startTimer() {
        final int ms = 40;
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
        
        l = new Level(5,getWidth(), getHeight());
        
        gameState = 1;
    }

    private void stopGame() {
        stopTimer();
        gameState = 0;
        repaint();
    }

    /*protected void keyPressed(int keyCode) {
        switch (keyCode) {
        case KEY_NUM4:
            pixel.speedX = -3;
            break;
        case KEY_NUM6:
            pixel.speedX = 3;
            break;
        }
    }*/

    /*protected void keyReleased(int keyCode) {

        switch (keyCode) {
        case KEY_NUM4:
            pixel.speedX = 0;
            break;
        case KEY_NUM6:
            pixel.speedX = 0;
            break;
        }
    }*/

    
}
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.lcdui.game.*;

class GameMain extends GameCanvas {

    
    private String text;
    private MainMIDlet midlet;
    private int gameState;
    
    private Timer mainTimer;

    private Pixel pixel;
    
    private Vector platforms = new Vector();

    public GameMain(MainMIDlet midlet) {
        super(true);
        this.midlet = midlet;
        gameState = 0;
        
        initNewGame();
    }

    public void paint() {
        Graphics g = getGraphics();
        //clear everything
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0, 0, 0);

        switch (gameState) {
        case 0:
            g.drawString("press start!", getWidth() / 2, getHeight() / 2,
                    Graphics.BASELINE | Graphics.HCENTER);
            break;
        case 1:
            for (int i = 0; i < platforms.size(); i++) {
                Platform p = (Platform) platforms.elementAt(i);
                //breakeable == blau
                if( p.type == 1)
                    g.setColor(0, 0, 255);
                //fake == rot
                if(p.type == 2)
                    g.setColor(255, 0, 0);
                
                g.fillRect((int)p.posX, (int)p.posY, p.size, 3);
                g.setColor(0, 0, 0);
            }
            g.fillRect((int)pixel.posX, (int)pixel.posY, 2, 2);
            
            break;
        case 3:
            g.drawString("game over", getWidth() / 2, getHeight() / 2,
                    Graphics.BASELINE | Graphics.HCENTER);
            break;
        }
        flushGraphics();
    }

    

    public void doGamePlay(int ms) {
        int keycode = getKeyStates();
        int leftright = 0;
        if ((keycode & LEFT_PRESSED) != 0)
            leftright = -1;
        if ((keycode & RIGHT_PRESSED) != 0)
            leftright = 1;
        
        pixel.accelerate(leftright);
        pixel.move(getWidth(), getHeight(), platforms, ms);
        
        
        if(pixel.posY > getHeight())
            gameState = 3;
        
        //Abstand des Pixels zu Bildschirmmitte.
        double dist = getHeight() / 2 - pixel.posY;
        //Wenn der Pixel ueber der Mitte ist, bewege alle Plattformen und den Pixel entsprchend.
        if (dist > 0) {
            for (int i = 0; i < platforms.size(); i++) {
                Platform p = (Platform) platforms.elementAt(i);
                p.posY += dist;
            }
            pixel.posY += dist;
        }
        deletePlatforms();
        //createNewPlatforms();
        
    }
    
    /** 
     * Loescht alle Plattformen, die sich unter dem Bildschirm befinden.
     */
    private void deletePlatforms() {
        for (int i = 0; i < platforms.size(); i++) {
            Platform p = (Platform) platforms.elementAt(i);
            if (p.posY > getHeight())
                platforms.removeElementAt(i);
        }
    }
    
    public void startTimer() {
        final int ms = 40;
        stopTimer();
        mainTimer = new Timer();
        TimerTask pt = new TimerTask() {
            public final void run() {
                doGamePlay(ms);
                paint();
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
        platforms.removeAllElements();
        //mittige Plattform, sodass man nicht gleich zu Beginn runterfaellt
        platforms.addElement(new Platform(getWidth() / 2 - 15, getHeight() - 30, 30, 1));
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            platforms.addElement(new Platform(r.nextInt(getWidth() - 30), r.nextInt(getHeight()+1000)-1000, 30, r.nextInt(3)));
        }
        
        gameState = 1;
    }

    private void stopGame() {
        stopTimer();
        gameState = 0;
        repaint();
    }

    protected void keyPressed(int keyCode) {
        /*switch (keyCode) {
        case KEY_NUM4:
            pixel.speedX = -3;
            break;
        case KEY_NUM6:
            pixel.speedX = 3;
            break;
        }*/
    }

    protected void keyReleased(int keyCode) {

        switch (keyCode) {
        case KEY_NUM4:
            pixel.speedX = 0;
            break;
        case KEY_NUM6:
            pixel.speedX = 0;
            break;
        }
    }

    
}
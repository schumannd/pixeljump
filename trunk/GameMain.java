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

    public GameMain(MainMIDlet midlet) {
        super(true);
        b2d = new Background2D(getWidth(), getHeight());
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
            for (int i = 0; i < l.getSize(); i++) {
                l.getPlat(i).paint(g);
                try{
                    l.getPlat(i).item.paint(g);
                }
                catch(Exception e) {}
            }
            pixel.paint(g);

            g.drawString("Score: "+Integer.toString(pixel.score), getWidth()-65, 15, Graphics.TOP | Graphics.LEFT);
            g.drawString("Height: "+Integer.toString(Level.highest), 15, 15, Graphics.TOP | Graphics.LEFT);
            
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
        pixel.move(getWidth(), getHeight(), l.platforms, ms);
        
        //gameover
        if(pixel.posY > getHeight())
            gameState = 3;
        
        //Abstand des Pixels zu Bildschirmmitte.
        double dist = getHeight() / 2 - pixel.posY;
        //Wenn der Pixel ueber der Mitte ist, bewege alle Plattformen und den Pixel entsprchend.
        if (dist > 0) {
            b2d.setDist(dist);
            l.move(dist, ms);
            pixel.posY += dist;
            pixel.setRefPixelPosition((int) pixel.posX, (int) pixel.posY);
            pixel.score += dist;
        }
        else {
            l.move(0, ms);
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
        Image img = null;
        try{
            img = Image.createImage("/res/pixelman.png");
        }catch(Exception e){}
        pixel = new Pixel(img, getWidth() / 2, getHeight() / 2);
        
        l = new Level(1,getWidth(), getHeight());
        
        gameState = 1;
    }

    private void stopGame() {
        stopTimer();
        gameState = 0;
        repaint();
    }
}
//test
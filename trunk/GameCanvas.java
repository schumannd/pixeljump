import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;

class GameCanvas extends Canvas implements CommandListener {

    private Command cmExit;
    private Command cmStart;
    private Command cmStop;
    private String text;
    private Main midlet;
    private int gameState;

    private Pixel pixel;
    
    private Vector platforms = new Vector();

    public GameCanvas(Main midlet) {

        this.midlet = midlet;
        gameState = 0;
        cmExit = new Command("Exit", Command.EXIT, 1);
        cmStart = new Command("Start", Command.SCREEN, 1);
        cmStop = new Command("Stop", Command.SCREEN, 2);
        addCommand(cmExit);
        addCommand(cmStart);
        addCommand(cmStop);
        setCommandListener(this);

        pixel = new Pixel(getWidth() / 2, getHeight() / 2);
        
        platforms.addElement(new Platform(getWidth() / 2 - 15, getHeight() - 30, 30));
        
        Random r = new Random();
        for (int i = 0; i < 15; i++) {
            platforms.addElement(new Platform(r.nextInt(getWidth() - 30), r.nextInt(getHeight() - 20), 30));
        }
        

    }

    protected void paint(Graphics g) {

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
                g.fillRect((int)p.posX, (int)p.posY, p.size, 3);
            }
            g.fillRect((int)pixel.posX, (int)pixel.posY, 2, 2);
            
            break;
        case 3:
            g.drawString("game over", getWidth() / 2, getHeight() / 2,
                    Graphics.BASELINE | Graphics.HCENTER);
            break;
        }
    }

    

    public void doGamePlay(int ms) {
        pixel.move(getWidth(), getHeight(), platforms, ms);
    }

    private void startGame() {

        pixel.posX = getWidth() / 2;
        pixel.posY = getHeight() / 2;
        pixel.speedY = 0;
        pixel.speedX = 0;
        gameState = 1;
        midlet.startTimer();
    }

    private void stopGame() {

        midlet.stopTimer();
        gameState = 0;
        repaint();
    }

    protected void keyPressed(int keyCode) {

        switch (keyCode) {
        case KEY_NUM4:
            pixel.speedX = -3;
            break;
        case KEY_NUM6:
            pixel.speedX = 3;
            break;
        }
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

    public void commandAction(Command c, Displayable d) {

        if (c == cmExit)
            midlet.exitMIDlet();
        else if (c == cmStart)
            startGame();
        else if (c == cmStop)
            stopGame();
    }
}
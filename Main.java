import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;

public class Main extends MIDlet {

    private Display display;
    private GameCanvas canvas;
    private Timer tm;

    public Main() {

        display = Display.getDisplay(this);
        canvas = new GameCanvas(this);

    }

    public void startApp() {

        display.setCurrent(canvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void exitMIDlet() {
        destroyApp(true);
        notifyDestroyed();
    }

    public void startTimer() {
        final int ms = 20;
        tm = new Timer();
        TimerTask pt = new TimerTask() {
            public final void run() {
                canvas.doGamePlay(ms);
                canvas.repaint();
            }
        };
        tm.schedule(pt, 0, ms);
    }

    public void stopTimer() {

        if (tm != null)
            tm.cancel();
        tm = null;
    }
}
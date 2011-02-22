import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;

public class MainMIDlet extends MIDlet implements CommandListener{

    private Command cmExit = new Command("Exit", Command.EXIT, 1);;
    private Command cmNewGame = new Command("New Game", Command.SCREEN, 1);
    private Command cmPause = new Command("Pause", Command.SCREEN, 2);
    private Command cmResume = new Command("Resume", Command.SCREEN, 2);
    
    
    private Display display;
    private GameMain canvas;

    public MainMIDlet() {
        display = Display.getDisplay(this);
        canvas = new GameMain(this);
        
        canvas.addCommand(cmExit);
        canvas.addCommand(cmNewGame);
        canvas.addCommand(cmPause);
        canvas.setCommandListener(this);
    }

    public void startApp() {
        display.setCurrent(canvas);
        canvas.startTimer();
    }

    public void pauseApp() {
        canvas.stopTimer();
    }

    public void destroyApp(boolean unconditional) {
    }

    public void exitMIDlet() {
        destroyApp(true);
        notifyDestroyed();
    }
    
    public void commandAction(Command c, Displayable d) {

        if (c == cmExit)
            exitMIDlet();
        else if (c == cmNewGame)
            canvas.initNewGame();
        else if (c == cmPause) {
            canvas.stopTimer();
            canvas.removeCommand(cmPause);
            canvas.addCommand(cmResume);
        }
        else if (c == cmResume) {
            canvas.startTimer();
            canvas.removeCommand(cmResume);
            canvas.addCommand(cmPause);
        }
    }

    
}
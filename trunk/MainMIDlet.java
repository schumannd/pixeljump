import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class MainMIDlet extends MIDlet implements CommandListener {

    private Command cmExit = new Command("Exit", Command.EXIT, 1);;
    private Command cmNewGame = new Command("New Game", Command.SCREEN, 1);
    private Command cmPause = new Command("Pause", Command.SCREEN, 2);
    private Command cmResume = new Command("Resume", Command.SCREEN, 2);
    private Command cmHighscore = new Command("Highscore", Command.SCREEN, 2);
    private Command cmOk = new Command("OK", Command.OK, 0);
    
    
    public TextBox pixelName;
    private Display display;
    private GameMain canvas;

    public MainMIDlet() {
        display = Display.getDisplay(this);
        canvas = new GameMain();
        pixelName = new TextBox("Name: ", "Bitte deinen Namen eingeben!", 30, TextField.ANY);
        Debug.canvas = canvas;

        canvas.addCommand(cmExit);
        canvas.addCommand(cmNewGame);
        canvas.addCommand(cmPause);
        canvas.addCommand(cmHighscore);
        canvas.setCommandListener(this);

        pixelName.addCommand(cmOk);
        pixelName.setCommandListener(this);

        display.setCurrent(canvas);
        Tools.init();
        canvas.init(this);
    }

    public void startApp() {
        display.setCurrent(canvas);
        Tools.init();
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
        else if (c == cmNewGame) {
            canvas.initNewGame();
            canvas.startTimer();
        }
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
        else if (c == cmHighscore) {
            canvas.gameOver();
        }
        else if (c == cmOk) {
            canvas.highscore.addScore(canvas.getScore(), pixelName.getString());
            display.setCurrent(canvas);

        }
    }

    public void setSpielerName() {
        display.setCurrent(pixelName);
    }
}

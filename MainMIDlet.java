import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class MainMIDlet extends MIDlet implements CommandListener, ItemCommandListener{

    private Command cmExit = new Command("Exit", Command.EXIT, 1);;
    private Command cmNewGame = new Command("New Game", Command.SCREEN, 1);
    private Command cmHighscore = new Command("Highscore", Command.SCREEN, 2);
    private Command cmPause = new Command("Pause", Command.SCREEN, 2);
    private Command cmResume = new Command("Resume", Command.SCREEN, 2);
    private Command cmOk = new Command("OK", Command.OK, 0);
    
    
    private TextField pixelName;
    private Form highscores;
    private Display display;
    private GameMain canvas;

    public MainMIDlet() {
        display = Display.getDisplay(this);
        canvas = new GameMain();
        pixelName = new TextField("Bitte deinen Namen eingeben!","" , 7, TextField.ANY);
        highscores = new Form("Highscore");
        Debug.canvas = canvas;

        canvas.addCommand(cmExit);
        canvas.addCommand(cmNewGame);
        canvas.addCommand(cmPause);
        canvas.addCommand(cmHighscore);
        canvas.setCommandListener(this);

        pixelName.addCommand(cmOk);
        pixelName.setItemCommandListener(this);

        highscores.append(pixelName);

        display.setCurrent(canvas);
        Tools.init();
        canvas.init(this);
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
       
    }

    public void commandAction(Command c, javax.microedition.lcdui.Item item) {
        if (c == cmOk) {
            canvas.highscore.data.setElementAt(pixelName.getString(), canvas.highscore.nameIndex);
            canvas.highscore.saveScore();
            display.setCurrent(canvas);
        }
    }

    public void setSpielerName() {
        display.setCurrent(highscores);
    }

    public void showHighscore() {
        display.setCurrent(canvas);
    }
}
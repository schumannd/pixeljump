import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;

public class Main extends MIDlet {

   private Display display;
   private GameCanvas canvas;
   private Timer tm;
   private PlayTimer pt;

public Main() {

   display=Display.getDisplay(this);
   canvas=new GameCanvas(this);
   
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

   tm=new Timer();
   pt=new PlayTimer(canvas);
   tm.schedule(pt,0,65);
   }

public void stopTimer() {

   if (tm!=null)
       tm.cancel();
   tm=null;
   }
}
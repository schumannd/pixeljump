import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;

class PlayTimer extends TimerTask {

  private GameCanvas canvas;

public PlayTimer (GameCanvas canvas) {

   this.canvas=canvas;
   }

public final void run() {

   canvas.doGamePlay();
   canvas.repaint();
   }
}
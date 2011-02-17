import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;

public class FirstGame extends MIDlet {

   private Display display;
   private GameCanvas canvas;
   private Timer tm;
   private PlayTimer pt;

public FirstGame() {

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

class GameCanvas extends Canvas implements CommandListener {

  private Command cmExit;
  private Command cmStart;
  private Command cmStop;
  private String text;
  private FirstGame midlet;
  private int gameState;

  private int playerPos;
  private int ballX,ballY;
  private int deltaX,deltaY;
  private int move;

public GameCanvas(FirstGame midlet) {

   this.midlet=midlet;
   gameState=0;
   cmExit=new Command("Exit",Command.EXIT, 1);
   cmStart=new Command("Start",Command.SCREEN, 1);
   cmStop=new Command("Stop",Command.SCREEN, 2);
   addCommand(cmExit);
   addCommand(cmStart);
   addCommand(cmStop);
   setCommandListener(this);
   }

protected void paint(Graphics g) {

   g.setColor(255,255,255);
   g.fillRect(0,0,getWidth(),getHeight());
   g.setColor(0,0,0);

   switch (gameState) {
      case 0: g.drawString("press start!",getWidth()/2,getHeight()/2,Graphics.BASELINE|Graphics.HCENTER);
              break;
      case 1: g.fillRect(getWidth()-5,playerPos,3,20);
              g.fillRect(ballX,ballY,2,2);
              break;
      case 3: g.drawString("game over",getWidth()/2,getHeight()/2,Graphics.BASELINE|Graphics.HCENTER);
              break;
      }
   }

public void doGamePlay() {

   ballX+=deltaX;
   ballY+=deltaY;

   playerPos+=move;

   if (playerPos<0) {
       playerPos=0;
       move=0;
       }
   if (playerPos>getHeight()-20) {
       playerPos=getHeight()-20;
       move=0;
       }
   if (ballX<0) {
       ballX=0;
       deltaX*=-1;
       }
   else if (ballX>getWidth()-1) {
       gameState=3;
       midlet.stopTimer();
       repaint();
       }
   if (ballY<0) {
       ballY=0;
       deltaY*=-1;
       }
   else if (ballY>getHeight()-1) {
       ballY=getHeight()-1;
       deltaY*=-1;
       }

   if ((ballX>getWidth()-7)&&(ballX<=getWidth()-4)) {
       if ((ballY>playerPos)&&(ballY<playerPos+20)) {
           deltaX*=-1;
           int ang=(ballY-playerPos)/3;
           deltaY=ang-3;
           }
       }
   }

private void startGame() {

   ballX=getWidth()-5;
   ballY=getHeight()/2;
   playerPos=(getHeight()-20)/2;
   deltaX=-2;
   deltaY=0;
   move=0;
   gameState=1;
   midlet.startTimer();
   }

private void stopGame() {

   midlet.stopTimer();
   gameState=0;
   repaint();
   }

protected void keyPressed(int keyCode) {

   switch (keyCode) {
      case KEY_NUM2: move=-2;
                     break;
      case KEY_NUM8: move=2;
                     break;
      }
  }

protected void keyReleased(int keyCode) {

   switch (keyCode) {
      case KEY_NUM2: move=0;
                     break;
      case KEY_NUM8: move=0;
                     break;
      }
  }

public void commandAction(Command c, Displayable d) {

   if (c==cmExit)
       midlet.exitMIDlet();
   else if (c==cmStart)
       startGame();
  else if (c==cmStop)
       stopGame();
   }
} 
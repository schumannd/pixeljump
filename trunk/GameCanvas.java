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


  private Pixel player;
   
  private Platform p1;
  private Platform p2;

public GameCanvas(Main midlet) {

   this.midlet=midlet;
   gameState=0;
   cmExit=new Command("Exit",Command.EXIT, 1);
   cmStart=new Command("Start",Command.SCREEN, 1);
   cmStop=new Command("Stop",Command.SCREEN, 2);
   addCommand(cmExit);
   addCommand(cmStart);
   addCommand(cmStop);
   setCommandListener(this);
   
   player = new Pixel(getWidth()/2,getHeight()/2);
   
   }

protected void paint(Graphics g) {

   g.setColor(255,255,255);
   g.fillRect(0,0,getWidth(),getHeight());
   g.setColor(0,0,0);
   
   p1 = new Platform((getWidth()-30)/2, 6, 30);
   p2 = new Platform((getWidth()-100)/2, 20, 30);

   switch (gameState) {
      case 0: g.drawString("press start!",getWidth()/2,getHeight()/2,Graphics.BASELINE|Graphics.HCENTER);
              break;
      case 1: g.fillRect(p1.posX,getHeight()-p1.posY,p1.size,3);
              g.fillRect(p2.posX,getHeight()-p2.posY,p2.size,3);
              g.fillRect(player.posX,player.posY,2,2);
              break;
      case 3: g.drawString("game over",getWidth()/2,getHeight()/2,Graphics.BASELINE|Graphics.HCENTER);
              break;
      }
   }

public boolean onPlat() {
    if (player.posY>getHeight()- p1.posY && player.posX>=p1.posX && player.posX<= (p1.posX+p1.size) ||
        player.posY>getHeight()- p2.posY && player.posX>=p2.posX && player.posX<= (p2.posX+p2.size))
        return true;
        return false;
    }
   
public void doGamePlay() {

   player.posX+=player.deltaX;
   player.posY+=player.deltaY;


   if (player.posX<0) {
       player.posX=0;
       player.deltaX=0;
       }
       
   else if (player.posX>getWidth()-1) {
       player.posX=getWidth()-1;
       player.deltaX=0;
       }
       
   if (onPlat()) {
       
       player.posY=getHeight()- p1.posY;
       player.deltaY = -20;
       }
       
   if (player.posY>getHeight()-1) {
       gameState=3;
       midlet.stopTimer();
       repaint();
       }
       
   else  {
       player.deltaY+=2;
       
       }

   }

private void startGame() {

   player.posX=getWidth()/2;
   player.posY=getHeight()/2;
   player.deltaY=0;
   player.deltaX=0;
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
      case KEY_NUM4: player.deltaX+=-3;
                     break;
      case KEY_NUM6: player.deltaX+=3;
                     break;
      }
  }

protected void keyReleased(int keyCode) {

   switch (keyCode) {
      case KEY_NUM4: player.deltaX=0;
                     break;
      case KEY_NUM6: player.deltaX=0;
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
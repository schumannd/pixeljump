import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.*;
import java.util.Random;
/**
 *
 * @author Malte
 */


public class Item extends Sprite {

    private int x;
    private double posX;
    public double posY;
    Platform p;
    int type; // 0 = Feder
    Random r = new Random();

    public static final int FEATHER = 0;


    public Item(Platform p,Image img, int type){
        super(img, 16, 16);
        x = r.nextInt(30);
        this.p = p;
        posX = p.posX + x;
        posY = p.posY;
        setRefPixelPosition((int) posX,(int) posY);
    }

    public int getItemX(){
        return x;
    }

    public void causeEffect(){
        switch (type) {
            case 0:

        }
    }
    public void updatePos() {
        posY = p.posY;
        posX = p.posX + x;
        setRefPixelPosition((int) posX, (int) posY);
    }

}
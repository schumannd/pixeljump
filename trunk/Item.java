import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.*;
import java.util.Random;
/**
 *
 * @author Malte
 */
public class Item extends Sprite {

    private int x;
    int type; // 0 = Feder
    Random r = new Random();

    public Item(Image img){
        super(img, 16, 16);
        x = r.nextInt(30);
    }

    public int getItemX(){
        return x;
    }
}
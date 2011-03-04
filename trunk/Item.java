import java.util.Random;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class Item extends Sprite {

    private int x;
    private double posX;
    public double posY;
    private Platform p;
    private int type; // 0 = Feder, 1 = Trampolin
    private Random r = new Random();
    public static Pixel pixel;

    public static final int SPRING = 0;


    public Item(Platform p, Image img, int type){
        super(img, img.getWidth(), img.getHeight());
        this.p = p;
        posX = p.posX + x;
        posY = p.posY;
        this.type = type;
        x = r.nextInt(30);
        setRefPixelPosition((int) posX,(int) posY);
    }
    
    
    public int getItemX(){
        return x;
    }
    
    
    public void causeEffect(){
        switch (type) {
            case 0: pixel.speedY = -27;
                    break;
            case 1: pixel.speedY = -45;
                    break;

        }
    }
    
    
    public void updatePos() {
        posY = p.posY;
        posX = p.posX + x;
        setRefPixelPosition((int) posX, (int) posY);
    }
}
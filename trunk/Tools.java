import javax.microedition.lcdui.Image;


public class Tools {
    public static Image pixelImage;
    public static Image projectileImage;
    public static Image backgroundImage;
    public static Image[] platImages = new Image[3];
    public static Image[] itemImages = new Image[6];
    public static Image[] monsterImages = new Image[1];
    
    /**
     * Laedt alle benoetigten Bilder.
     */
    public static void init() {            
        //Notwendig, damit sowohl bluej als auch eclipse/netbeans den
        //res-Ordner finden.
        String res;
        try{
            pixelImage = Image.createImage("/pixelman_sprite.png");
            res = "";
        }catch(Exception e){res = "/res";}
        
        try{
            pixelImage = Image.createImage(res+"/pixelman_sprite.png");
            
            projectileImage = Image.createImage(res+"/projectile.png");
            backgroundImage = Image.createImage(res+"/background.png");
            for (int i = 0; i < platImages.length; i++) {
                platImages[i] = Image.createImage(res+"/plattform"+i+".png");
            }
            for (int i = 0; i < itemImages.length; i++) {
                itemImages[i] = Image.createImage(res+"/item"+i+".png");
            }
            for (int i = 0; i < monsterImages.length; i++) {
                monsterImages[i] = Image.createImage(res+"/monster"+i+".png");
            }
        }catch(Exception e){}
    }
}

import javax.microedition.lcdui.Image;


public class Tools {
    
    public static Image pixelImage;
    public static Image projectileImage;
    public static Image backgroundImage;
    public static Image[] platImages = new Image[3];
    public static Image[] itemImages = new Image[5];
    public static Image[] monsterImages = new Image[1];
    
    public static String res;
    
    
    public static void init() {
        if (pixelImage != null)
            return; //wurde bereits initialisiert
        try{
            pixelImage = Image.createImage("/pixelman.png");
            res = "";
        }catch(Exception e){res = "/res";}
        try{
            
            pixelImage = Image.createImage(res+"/pixelman.png");
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

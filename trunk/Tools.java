import javax.microedition.lcdui.Image;


public class Tools {
    
    public static Image pixelImage;
    public static Image projectileImage;
    public static Image backgroundImage;
    public static Image[] platImages = new Image[3];
    public static Image[] itemImages = new Image[2];
    
    private static String res = null;
    
    
    public static void init() {
        try{
            pixelImage = Image.createImage("/pixelman.png");
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
            
        }catch(Exception e){}
    }
}

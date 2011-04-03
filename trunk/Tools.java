import javax.microedition.lcdui.Image;


public class Tools {
    
    public static Image[] pixelImages = new Image[6];
    public static Image projectileImage;
    public static Image backgroundImage;
    public static Image[] platImages = new Image[3];
    public static Image[] itemImages = new Image[6];
    public static Image[] monsterImages = new Image[1];
    
    private static String res;
    
    public static void init() {
        if (pixelImages[0] != null)
            return; //wurde bereits initialisiert
            
        //fix, damit sowohl bluej als auch eclipse die ressourcen finden
        try{
            pixelImages[0] = Image.createImage("/pixelman0.png");
            res = "";
        }catch(Exception e){res = "/res";}
        
        try{
            for (int i = 0; i < pixelImages.length; i++) {
                pixelImages[i] = Image.createImage(res+"/pixelman"+i+".png");
                
            }
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

import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;

public class Background2D {
    private Vector stars;
    private int height;
    private TiledLayer landscape;

    private Random r = new Random();

    public Background2D(int width, int height) {
        this.height = height;
        createLandscape();

//        stars = new Vector();7
//        for (int i = 0; i < 200; i++) {
//            stars.addElement(new Star(r.nextInt(width), r.nextInt(height), r.nextInt(2)+1));
//        }
    }

    public void draw(Graphics g) {
        landscape.paint(g);

//        for(int i = 0; i < stars.size(); i++) {
//            Star star = (Star) stars.elementAt(i);
//            g.fillRect((int)star.x, (int)star.y, (int)star.z, (int)star.z);
//        }
    }

    public void setDist(double dist) {
        landscape.setPosition(landscape.getX(), landscape.getY() + (int) (dist + 0.5));
//        for(int i = 0; i < stars.size(); i++) {
//            Star star = (Star) stars.elementAt(i);
//            star.move(dist);
//            if (star.y > height)
//                star.y = 0;
//        }
    }

    private void createLandscape() {
        Image img = Tools.backgroundImage;
        landscape = new TiledLayer(1, 1, img, img.getWidth(), img.getHeight());
        landscape.setCell(0, 0, 1);
        landscape.setPosition(0, -1000 + height);
//        int[] map = {
//                1
//        };
//        for (int i = 0; i < map.length; i++) {
//            int column = i % 4;
//            int row = i / 4;
//            landscape.setCell(column, row, map[i]);
//        }
    }
    
    
//    private class Star {
//        double x;
//        double y;
//        double z;
//
//        public Star (double x, double y, double z) {
//            this.x = x;
//            this.y = y;
//            this.z = z;
//        }
//
//
//        public void move(double d) {
//            this.y += d*z/3;
//        }
//    }
}

public class Platform {
    
    public int type; // 0 = normal, 1 = breaks
    public double posX;
    public double posY;
    public int size;
    
    
    public Platform(double x, double y, int l, int t) {
        posX = x;
        posY = y;
        size = l;
        type = t;
    }

}
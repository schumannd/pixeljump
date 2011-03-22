
public class Monster extends GameObject{
    private int type;
    
    public Monster(double x, double y, int t) {
        super(Tools.monsterImages[t], x, y);
        defineReferencePixel(0, Tools.monsterImages[t].getHeight()-1);
        type = t;
    }
}


public class Monster extends GameObject{
    private int type;
    
    public Monster(double x, double y, int t) {
        super(Tools.monsterImages[t], x, y);
        type = t;
    }
}

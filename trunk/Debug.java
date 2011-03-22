import java.util.Vector;
import javax.microedition.lcdui.Graphics;

import javax.microedition.lcdui.game.GameCanvas;

public class Debug {
    /** How many Strings are printed. **/
    private final static int capacity = 10;
    /** List of all Strings that should be printed. **/
    private static Vector strings = new Vector();
    
    public static GameCanvas canvas;
    
    /** Adds a String to the list. **/
    public static void add(String s) {
        if (s == null)
            add("string is null");
        addRepaint(s);
    }
    
    /** Adds a number of any type to the list. **/
    public static void add(double d) {
        addRepaint(Double.toString(d));
    }
    
    /** Adds a boolean as "true" or "false" to the list. **/
    public static void add(boolean b) {
        if(b)
            addRepaint("true");
        else
            addRepaint("false");
    }
    
    /** Adds the output from the given objects toString() to the list. **/
    public static void add(Object o) {
        if (o == null)
            add("object is null");
        addRepaint(o.toString());
    }
    
    private static void addRepaint(String s) {
        strings.addElement(s);
        canvas.repaint();
    }
    
    /** Removes the oldest elements until the list is not bigger than capacity. **/
    private static void truncate() {
        while(strings.size() > capacity)
            strings.removeElementAt(0);
    }
    
    /** Truncates the list and prints the elements one below the other,
     * beginning with the oldest element in the upper left corner of the screen. **/
    public static void print(Graphics g) {
        truncate();
        for (int i = 0; i < strings.size(); i++) {
            String s = (String) strings.elementAt(i);
            g.drawString(s, 0, i*10, 0);
        }
    }
}

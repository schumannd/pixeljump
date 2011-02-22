import java.util.*;
import javax.microedition.lcdui.*;

public class Debug {
    /** How many Strings are printed. **/
    public static int capacity = 10;
    /** List of all Strings that should be printed. **/
    private static Vector strings = new Vector();
    
    /** Adds a String to the list. **/
    public static void add(String s) {
        strings.addElement(s);
    }
    
    /** Adds a number of any type to the list. **/
    public static void add(double d) {
        strings.addElement(Double.toString(d));
    }
    
    /** Adds a boolean as "true" or "false" to the list. **/
    public static void add(boolean b) {
        if(b)
            strings.addElement("true");
        else
            strings.addElement("false");
    }
    
    /** Adds the output from the given objects toString() to the list. **/
    public static void add(Object o) {
        strings.addElement(o.toString());
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

import java.util.*;
/**
 * Beschreiben Sie hier die Klasse Platform.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */

public class Platform
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen

    public int type; // 0 = normal, 1 = breaks
    public double posX;
    public double posY;
    public int size;
    

    /**
     * Konstruktor für Objekte der Klasse Platform
     */
    public Platform(double x, double y, int l, int t)
    {
        posX = x;
        posY = y;
        size = l;
        type = t;
    }

}
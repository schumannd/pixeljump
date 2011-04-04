import java.io.*;
import javax.microedition.media.*;

public class SoundManager {
    /* Um einen neuen Sound hinzuzufuegen, muss der Dateiname zu filenames[]
     * und eine Konstante mit dem naechsthoeheren Wert hinzugefuegt werden. */
    public static final int MUSIC = 0;
    public static final int JUMP = 1;
    public static final int SHOOT = 2;
    public static final int DIE = 3;
    public static final int DIEM = 4;
    
    /** Array der Dateinamen aller Audiodateien **/
    private static final String[] filenames = { "giana.mp3", "jumpsound0.wav", 
        "shootsound0.wav", "die.mp3", "diem.mp3" };
    /** Array aller Player-Objekte **/
    private static final Player[] players = new Player[filenames.length];
    
    
    /**
     * Erstellt und prefetched ein Player-Objekt fuer jede der Audiodateien in 
     * filenames[].
     */
    public static void init() {
        //Notwendig, damit sowohl bluej als auch eclipse/netbeans den
        //res-Ordner finden.
        String res = "";
        SoundManager sm = new SoundManager();
        if (sm.getClass().getResourceAsStream("/" + filenames[0]) == null) 
            res = "/res";
        //Erstelle und prefetche alle Player-Objekte.
        try {
            for (int i = 0; i < players.length; i++) {
                InputStream is = sm.getClass().getResourceAsStream(
                        res + "/" + filenames[i]);
                String type = null;
                if (filenames[i].endsWith(".wav")) type = "audio/x-wav";
                else if (filenames[i].endsWith(".mp3")) type = "audio/mpeg";
                else if (filenames[i].endsWith(".mid")) type = "audio/midi";
                players[i] = Manager.createPlayer(is, type);
                players[i].prefetch();
            }
        players[MUSIC].setLoopCount(10);
            
        } catch (IOException e) {
        } catch (MediaException e) {
        }
    }
    
    /**
     * Stoppt die Musik und spielt den Todessound ab.
     */
    public static void death(){
        stopSound(MUSIC);
        playSound(DIE);
    }
    
    /**
     * Stoppt die Musik und spielt den Monster-Todessound ab.
     */
    public static void deathm(){
        stopSound(MUSIC);
        playSound(DIEM);
    }
    
    /**
     * Stoppt den Todessound und startet die Musik.
     */
    public static void start(){
        stopSound(DIE);
        //playSound(MUSIC);
    }
    
    /**
     * Spielt den angegebenen Sound ab.
     * @param which Welcher Sound abgespielt werden soll.
     */
    public static void playSound(int which) {
        try {
            players[which].setMediaTime(0);
            players[which].start(); 
        } catch (MediaException e) { 
        } catch (ArrayIndexOutOfBoundsException e) { 
        } catch (NullPointerException e) {  }
    }
    
    /**
     * Stoppt den angegebenen Sound.
     * @param which Welcher Sound gestoppt werden soll.
     */
    public static void stopSound(int which) {
        try {
            players[which].stop();
        } catch (MediaException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (NullPointerException e) {}
    }
}

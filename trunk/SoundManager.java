import java.io.*;
import javax.microedition.media.*;

public class SoundManager {
    /* neuen ton hinzufuegen: konstante hinzufuegen, bei new Player[x] das x 
     * hochzaehlen, und dateinamen zu filenames hinzufuegen
     */
    public static final int MUSIC = 0;
    public static final int JUMP = 1;
    public static final int SHOOT = 2;
    
    private static final Player[] players = new Player[3];
    private String[] filenames = { "music.mid", "jumpsound0.wav", "shootsound0.wav" };
    
    public void init() {
        String res = "/res";

        try {
            for (int i = 0; i < players.length; i++) {
                InputStream is = getClass().getResourceAsStream(
                        res + "/" + filenames[i]);
                String type = null;
                if (filenames[i].endsWith(".wav")) type = "audio/x-wav";
                if (filenames[i].endsWith(".mid")) type = "audio/midi";
                players[i] = Manager.createPlayer(is, type);
                players[i].prefetch();
            }
            players[MUSIC].start();
        } catch (IOException e) {
        } catch (MediaException e) {
        }
    }
    
    
    public static void playSound(int which) {
        try {
            players[which].start();
        } catch (MediaException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (NullPointerException e) {}
    }
}

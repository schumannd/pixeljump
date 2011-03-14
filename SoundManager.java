import java.io.*;
import javax.microedition.media.*;

public class SoundManager {
    /* neuen ton hinzufuegen: konstante hinzufuegen, bei new Player[x] das x 
     * hochzaehlen, und dateinamen zu filenames hinzufuegen
     */
    public static final int MUSIC = 0;
    public static final int JUMP = 1;
    public static final int SHOOT = 2;
    public static final int DIE = 3;
    public static final int DIEM = 4;
    
    private static final Player[] players = new Player[5];
    private static String[] filenames = { "giana.mp3", "jumpsound0.wav", "shootsound0.wav", "die.mp3", "diem.mp3" };
    
    public void init() {
        String res = "";
        if (getClass().getResourceAsStream("/" + filenames[0]) == null) 
            res = "/res";

        try {
            for (int i = 0; i < players.length; i++) {
                InputStream is = getClass().getResourceAsStream(
                        res + "/" + filenames[i]);
                String type = null;
                if (filenames[i].endsWith(".wav")) type = "audio/x-wav";
                if (filenames[i].endsWith(".mp3")) type = "audio/mpeg";
                players[i] = Manager.createPlayer(is, type);
                players[i].prefetch();
            }
        players[MUSIC].setLoopCount(10);
            
        } catch (IOException e) {
        } catch (MediaException e) {
        }
    }
    public static void death(){
        stopSound(MUSIC);
        playSound(DIE);
    }
    public static void deathm(){
        stopSound(MUSIC);
        playSound(DIEM);
    }
    public static void start(){
        stopSound(DIE);
        playSound(MUSIC);
    }

    public static void playSound(int which) {
        try {
            players[which].setMediaTime(0);
            players[which].start();
        } catch (MediaException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (NullPointerException e) {}
    }

    public static void stopSound(int which) {
        try {
            players[which].stop();
        } catch (MediaException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (NullPointerException e) {}
    }
}

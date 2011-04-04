import java.io.*;
import javax.microedition.media.*;

public class SoundManager {
    /* neuen ton hinzufuegen: konstante hinzufuegen
     * und dateinamen zu filenames hinzufuegen
     */
    public static final int MUSIC = 0;
    public static final int JUMP = 1;
    public static final int SHOOT = 2;
    public static final int DIE = 3;
    public static final int DIEM = 4;
    
    private static final String[] filenames = { "giana.mp3", "jumpsound0.wav", 
        "shootsound0.wav", "die.mp3", "diem.mp3" };
    private static final Player[] players = new Player[filenames.length];
    
    
    public static void init() {
        String res = "";
        SoundManager sm = new SoundManager();
        if (sm.getClass().getResourceAsStream("/" + filenames[0]) == null) 
            res = "/res";

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
        //playSound(MUSIC);
    }

    public static void playSound(int which) {
        try {
            Debug.add(players[which].setMediaTime(0));
            players[which].prefetch();
            players[which].start();
            //Debug.add(players[which].getMediaTime());
        } catch (MediaException e) { 
        } catch (ArrayIndexOutOfBoundsException e) { 
        } catch (NullPointerException e) {  }
    }

    public static void stopSound(int which) {
        try {
            players[which].stop();
        } catch (MediaException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (NullPointerException e) {}
    }
}

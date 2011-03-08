
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.media.*;


public class SoundManager {
    private static Player jumpPlayer;
    private static Player musicPlayer;
    
    public void init() {
        try {
            InputStream is = getClass().getResourceAsStream("/res/jumpsound.wav");
            jumpPlayer = Manager.createPlayer(is, "audio/x-wav");
            jumpPlayer.prefetch();
            is = getClass().getResourceAsStream("/res/music.mid");
            musicPlayer = Manager.createPlayer(is, "audio/midi");
            musicPlayer.prefetch();
            musicPlayer.start();
        } catch (IOException e) {
        } catch (MediaException e) {
        }
    }

    public static void playJumpSound() {
        try {
            jumpPlayer.start();
        } catch (MediaException e) { }
    }
    
    
}

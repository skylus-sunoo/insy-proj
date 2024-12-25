/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.util;

/**
 *
 * @author admin
 */
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundUtil {

    public static final String SOUND_SCANNED = "scanned.wav";
//    public static final String SOUND_ALERT = "alert.wav";
//    public static final String SOUND_CLICK = "click.wav";
    
    public static void playSound(String soundConstant) {
        try {
            URL soundFile = SoundUtil.class.getResource("/ProjectINSY/resources/sounds/" + soundConstant);
            if (soundFile == null) {
                System.out.println("Sound file not found: " + soundConstant);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            // Get a clip to play the sound
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            
            while (!clip.isRunning()) {
                Thread.sleep(10);
            }
            while (clip.isRunning()) {
                Thread.sleep(10);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace(System.out);
        }
    }
}

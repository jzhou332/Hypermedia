// Java program to play an Audio
// file using Clip Object
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
public class SimpleAudioPlayer {
    // to store current position
    Long currentFrame;
    Clip clip;
    // current status of clip
    String status;
    public  AudioInputStream audioInputStream;

    // constructor to initialize streams and clip
    public SimpleAudioPlayer(String audioPath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // create AudioInputStream object
        audioInputStream =
                AudioSystem.getAudioInputStream(new File(audioPath));
        // create clip reference
        clip = AudioSystem.getClip();
        // open audioInputStream to the clip
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.stop();
        currentFrame = 0L;
    }


    // Method to play the audio
    public void play() {
        //start the clip
//        clip.start();

//        status = "play";
    }
    // Method to pause the audio
    public void pause() {
//        if (status.equals("paused")) {
//            System.out.println("audio is already paused");
//            return;
//        }
        this.currentFrame =
                this.clip.getMicrosecondPosition();
        clip.stop();
//        status = "paused";
    }
    // Method to resume the audio
    public void resumeAudio() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
//        if (status.equals("play")) {
//            System.out.println("Audio is already " +
//                    "being played");
//            return;
//        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        clip.start();
    }
    // Method to restart the audio
    public void restart() throws IOException, LineUnavailableException,
            UnsupportedAudioFileException {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
//        this.play();
//        clip.stop();
    }
    // Method to stop the audio
    public void stop() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        clip.stop();
        clip.close();
    }
    // Method to jump over a specific part
    public void jump(long c) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (c > 0 && c < clip.getMicrosecondLength()) {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            clip.stop();
            System.out.println("c: " + c);
            System.out.println(clip.getMicrosecondLength());
        } else {
            System.out.println("didn't jump");
        }
    }
    // Method to reset audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
            LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(
                new File(Itimer_test.audioPath).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
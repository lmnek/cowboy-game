package cvut.gartnkry.view.assets;

import cvut.gartnkry.control.files.ResourcesUtils;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Random;

public class StepSoundsPlayer implements Runnable {
    private ArrayList<Media> sounds;
    private Random random;
    private static boolean stop;
    private static boolean playing;

    public StepSoundsPlayer() {
        stop = false;
        sounds = new ArrayList<>();
        random = new Random();
        for (int i = 1; i < 10; i++) {
            Media sound = ResourcesUtils.loadMedia("Steps/step" + i + ".mp3");
            sounds.add(sound);
        }
    }

    @Override
    public void run() {
        while (!stop) {
            System.out.print("");
            while (playing) {
                MediaPlayer mediaPlayer = new MediaPlayer(sounds.get(random.nextInt(9)));
                mediaPlayer.play();
                mediaPlayer.setVolume(0.1);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static synchronized public void play() {
        playing = true;
    }

    static synchronized public void stop() {
        playing = false;
    }

    public static void shutdown() {
        stop = true;
    }
}

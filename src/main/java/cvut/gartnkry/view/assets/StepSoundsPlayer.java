package cvut.gartnkry.view.assets;

import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.control.files.ResourcesUtils;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class responsible for playing walking/stepping sounds of a player.
 * Is supposed to be running on its own thread.
 */
public class StepSoundsPlayer implements Runnable {
    private static StepSoundsPlayer instance = new StepSoundsPlayer();
    private final ArrayList<Media> sounds;
    private final Random random;
    private static boolean stop;
    private volatile boolean playing;
    private final static int SOUNDS_COUNT = 10;

    public static StepSoundsPlayer getInstance() {
        return instance;
    }

    /**
     * Class constructor.
     * Init attributes and load stepping sounds.
     */
    public StepSoundsPlayer() {
        stop = false;
        sounds = new ArrayList<>();
        random = new Random();
        for (int i = 1; i < SOUNDS_COUNT; i++) {
            Media sound = ResourcesUtils.loadMedia("Steps/step" + i + ".mp3");
            sounds.add(sound);
        }
    }

    /**
     * Run method that plays stepping sounds when player is walking.
     */
    @Override
    public void run() {
        while (!stop) {
            // Wait while not walking
            synchronized (this) {
                while (!playing) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // play random step sound
            MediaPlayer mediaPlayer = new MediaPlayer(sounds.get(random.nextInt(SOUNDS_COUNT - 1)));
            mediaPlayer.play();
            mediaPlayer.setVolume(0.1);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Start playing the stepping sounds.
     * ~ notify thread
     */
    public void play() {
        AppLogger.fine(() -> "Play stepping sound.");
        synchronized (this) {
            notify();
            playing = true;
        }
    }

    /**
     * Start playing the stepping sounds.
     * ~ sets thread in blocking state
     */
    public void stop() {
        AppLogger.fine(() -> "Stop stepping sound.");
        playing = false;
    }

    /**
     * Ask to shut down the thread.
     */
    public static void shutdown() {
        stop = true;
    }
}

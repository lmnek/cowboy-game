package cvut.gartnkry.view.assets;

import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.control.files.ResourcesUtils;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Enum for playing sounds in needed moments.
 */
public enum Sound {
    WIND("wind", true, 0.2),
    GRUNT("grunt", false, 0.1),
    ITEM("pickup", false, 0.15),
    GUN("gun", false, 0.2),
    BRIDGE("bridge", false, 0.3),
    FALL("fall", false, 0.3),
    BOTTLE("bottle", false, 0.05),
    DEATH("death", false, 0.2),
    ENTITY_HIT("entity", false, 0.1),
    VOID_OPEN("void", false, 0.08),
    GHOST_HIT("ghost1", false, 0.2),
    GHOST_DEATH("ghost2", false, 0.1);

    private final String filename;
    private final Media media;
    private final double volume;

    /**
     * Enum constructor.
     * Loads sounds and starts playing sounds that are set on repeat.
     * @param filename name of the file without .wav
     * @param repeat whether to repeat the audio in the background
     * @param volume volume of the sound
     */
    Sound(String filename, boolean repeat, double volume) {
        this.filename = filename;
        this.volume = volume;
        media = ResourcesUtils.loadMedia(filename + ".wav");
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        if (repeat) {
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
            mediaPlayer.play();
        }
        mediaPlayer.setVolume(volume);
    }

    /**
     * Play the sound with MediaPlayer.
     */
    public void play() {
        AppLogger.finer(() -> "Playing sound: " + filename);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }
}



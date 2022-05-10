package cvut.gartnkry.view.assets;

import cvut.gartnkry.control.files.ResourcesUtils;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public enum Sound {
    WIND("wind", true, true, 0.2),
    GRUNT("grunt", false, false, 0.1),
    ITEM("pickup", false, false, 0.15),
    GUN("gun", false, false, 0.25),
    BRIDGE("bridge", false, false, 0.3),
    FALL("fall", false, false, 0.5),
    ENTITY("entity", false, false, 0.1),
    BOTTLE("bottle", false, false, 0.05),
    DEATH("death", false, false, 0.2),
    VOID("void", false, false, 0.08);

    private final Media media;
    private final double volume;

    Sound(String filename, boolean repeat, boolean start, double volume) {
        this.volume = volume;
        media = ResourcesUtils.loadMedia(filename + ".wav");
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        if (repeat) {
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        }
        if (start) {
            mediaPlayer.play();
        }
        mediaPlayer.setVolume(volume);
    }

    public void play() {
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }
}



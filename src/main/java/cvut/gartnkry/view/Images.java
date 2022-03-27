package cvut.gartnkry.view;

import cvut.gartnkry.Settings;
import javafx.scene.image.Image;

public enum Images {
    SAND1("sand_1.png"),
    SAND2("sand_2.png"),
    PLAYER("player.png");

    String filename;
    Image image;

    private Images(String filename) {
        this.filename = filename;
    }

    /**
     *  Load and scale Images from resource folder
     */
    public void loadImage() {
        // TODO: find a better way to scale image + smooth true/false ?
        Image unscaled_image = new Image(filename);
        image = new Image(filename, unscaled_image.getWidth() * Settings.SCALE, unscaled_image.getHeight() * Settings.SCALE, false, false);
    }

    public Image getImage() {
        return image;
    }
}

package cvut.gartnkry.view.assets;

import javafx.scene.image.Image;

/**
 * Enum for storing images, that are being used by entities, props and items.
 * </p>
 * Every constant has folder and filename attribute.
 * At the start of loading the game, all images are loaded from these attributes.
 * Images are also being scaled to current window size.
 */
public enum ImageAsset {
    PLAYER_DEFAULT("Player", "player_default"),
    CACTUS("Props", "cactus");

    private final Image image;

    /**
     * Enum constructor.
     * Load scaled image from /resources + folder + filename.
     * @param folder path to folder with the image
     * @param filename filename of the image
     */
    ImageAsset(String folder, String filename) {
        image = AssetsUtils.loadAsset(folder, filename); // load image
    }

    /**
     * @return scaled image
     */
    public Image getImage() {
        return image;
    }
}

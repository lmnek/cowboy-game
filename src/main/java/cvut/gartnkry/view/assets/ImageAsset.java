package cvut.gartnkry.view.assets;

import javafx.scene.image.Image;

public enum ImageAsset {
    PLAYER_DEFAULT("Player", "player_default"),
    CACTUS("Props", "cactus");

    private final String folder;
    private final String filename;
    private Image image;

    private ImageAsset(String folder, String filename) {
        this.folder = folder;
        this.filename = filename;

        // load image
        image = AssetsUtils.loadAsset(folder, filename);
    }

    public Image getImage() {
        return image;
    }
}

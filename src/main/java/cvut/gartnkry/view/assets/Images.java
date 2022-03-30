package cvut.gartnkry.view.assets;

import javafx.scene.image.Image;

public enum Images {
    PLAYER_DEFAULT("Player", "player_no_gun1"),
    CACTUS("", "cactus");

    private final String folder;
    private final String filename;
    private Image image;

    private Images(String folder, String filename) {
        this.folder = folder;
        this.filename = filename;
    }

    public Image getImage() {
        return image;
    }

    public void loadImage(){ this.image = AssetsUtils.loadAsset(folder, filename);};
}

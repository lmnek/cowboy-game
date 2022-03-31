package cvut.gartnkry.view.assets;

import javafx.scene.image.Image;

public class Tile {
     private final boolean hasHitbox;
     private final Image image;

    public Tile(String filename, boolean hasHitbox) {
        this.hasHitbox = hasHitbox;
        // load image
        this.image = AssetsUtils.loadAsset("Tiles", filename);
    }

    public Image getImage() {
        return image;
    }

    public boolean hasHitbox() {
        return hasHitbox;
    }
}
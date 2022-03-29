package cvut.gartnkry.view.assets;

import javafx.scene.image.Image;

public class Tile{
    private final boolean hasHitbox;
    private final int code;
    private Image image;

    public Tile(String filename, boolean hasHitboxes, int code) {
        this.hasHitbox = hasHitboxes;
        this.code = code;
        this.image = AssetsUtils.loadAsset("a", filename);
    }
}

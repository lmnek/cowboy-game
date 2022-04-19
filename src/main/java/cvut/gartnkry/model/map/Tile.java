package cvut.gartnkry.model.map;

import cvut.gartnkry.ResourcesUtils;
import javafx.scene.image.Image;

/**
 * Tile is used to hold image of the tile and other information
 * - e.g. whether the tile has hitbox/collision.
 */
public class Tile{
     private final String name;
     private final boolean hasHitbox;
     private final Image image;

    /**
     * Class constructor.
     * It also loads the image.
     * @param filename filename in resources/Maps/Tiles/ from which to load image
     * @param hasHitbox - boolean value whether the Tile has a hitbox
     */
    public Tile(String filename, boolean hasHitbox) {
        name = filename;
        this.hasHitbox = hasHitbox;
        this.image = ResourcesUtils.loadAsset("Maps/Tiles", filename); // load image
    }

    /**
     * @return scaled image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @return boolean value whether the Tile has a hitbox
     */
    public boolean hasHitbox() {
        return hasHitbox;
    }

    public String getName() {
        return name;
    }
}
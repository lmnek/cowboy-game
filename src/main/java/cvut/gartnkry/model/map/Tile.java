package cvut.gartnkry.model.map;

import cvut.gartnkry.control.files.ResourcesUtils;
import javafx.scene.image.Image;

/**
 * Tile is used to hold image of the tile, code, name and whether it has a hitbox.
 */
public class Tile {
    private final String code;
    private final String name;
    private final boolean hasHitbox;
    private final Image image;

    /**
     * Class constructor.
     * Sets attributes and loads an image.
     *
     * @param filename  filename in resources/Maps/Tiles/ from which to load image
     * @param code code of the Tile used in the CSV tilemap
     * @param hasHitbox boolean value whether the Tile has a hitbox
     */
    public Tile(String filename, String code, boolean hasHitbox) {
        this.code = code;
        name = filename;
        this.hasHitbox = hasHitbox;
        this.image = ResourcesUtils.loadAsset("Maps/Tiles/" + filename); // load image
    }

    public Image getImage() {
        return image;
    }

    public boolean hasHitbox() {
        return hasHitbox;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
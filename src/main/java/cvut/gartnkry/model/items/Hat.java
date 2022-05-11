package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.files.ResourcesUtils;
import javafx.scene.image.Image;

// Could add health
/**
 * Hat Item for player to wear on hat.
 * The hat is being rendered on top of player sprite.
 */
public class Hat extends Item {
    public static final Image SIDE = ResourcesUtils.loadAsset("Player/hat_side");
    public static final Image FRONT = ResourcesUtils.loadAsset("Player/hat_front");

    /**
     * Class constructor
     *
     * @param json JsonObject with item data
     */
    public Hat(JsonObject json) {
        super(json);
    }

    @Override
    public void parseJson(JsonObject json) {
    }

    @Override
    public boolean use() {
        return false;
    }
}

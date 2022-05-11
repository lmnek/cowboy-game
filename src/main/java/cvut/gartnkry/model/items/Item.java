package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.view.assets.AssetsManager;
import javafx.scene.image.Image;

/**
 * Abstract Item class. It is used as a template for other items.
 */
public abstract class Item {
    private final String name;
    private final Image image;

    /**
     * Class constructor - set name and load image.
     * @param json JsonObject with item data
     */
    public Item(JsonObject json) {
        name = json.get("name").getAsString();
        image = AssetsManager.getImage(json.get("name").getAsString());
        parseJson(json);
    }

    /**
     * Abstract method - every item has its attributes to get from json.
     * @param json JsonObject with item data
     */
    abstract public void parseJson(JsonObject json);

    /**
     * Use item.
     * @return whether the item should be removed after usage
     */
    public abstract boolean use();

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public boolean is(Class inClass) {
        return getClass().equals(inClass);
    }
}

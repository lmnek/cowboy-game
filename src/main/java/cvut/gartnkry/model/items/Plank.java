package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;

/**
 * Plank item. It is required for building a bridge.
 */
public class Plank extends Item{

    /**
     * Class constructor
     * @param json JsonObject with item data
     */
    public Plank(JsonObject json) {
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

package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.view.assets.AssetsManager;
import javafx.scene.image.Image;


public abstract class Item {
    private final String name;
    private final Image image;

    public Item(JsonObject json) {
        name = json.get("name").getAsString();
        image = AssetsManager.getImage(json.get("name").getAsString());
        parseJson(json);
    }

    abstract public void parseJson(JsonObject json);

    // GET POPIS - mnozstvi, lvl, ...

    //

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}

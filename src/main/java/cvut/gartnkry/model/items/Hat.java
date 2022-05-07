package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.ResourcesUtils;
import javafx.scene.image.Image;

public class Hat extends Item{
    public static final Image SIDE = ResourcesUtils.loadAsset("Player/hat_side");
    public static final Image FRONT = ResourcesUtils.loadAsset("Player/hat_front");

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

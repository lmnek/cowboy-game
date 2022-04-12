package cvut.gartnkry.model;

import com.google.gson.JsonObject;
import cvut.gartnkry.ResourcesUtils;
import cvut.gartnkry.view.assets.ImageAsset;

public class Prop {
    private Sprite sprite;
    private String name;

    Prop(JsonObject propData){
        name = propData.get("name").getAsString();
        ImageAsset ia = ImageAsset.getFromName(name);
        sprite = new Sprite(ia.getImage(),
                ResourcesUtils.pointFromJson(propData));
    }

    public Sprite getSprite() {
        return sprite;
    }
}

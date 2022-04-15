package cvut.gartnkry.model;

import com.google.gson.JsonObject;
import cvut.gartnkry.ResourcesUtils;
import cvut.gartnkry.view.assets.AssetsManager;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Prop {
    protected Sprite sprite;
    protected Rectangle hitbox;
    private String name;

    public Prop(JsonObject data, Image defaultImage){
        name = data.get("name").getAsString();
        Point2D coords = ResourcesUtils.pointFromJson(data);
        sprite = new Sprite(defaultImage, coords);
        hitbox = AssetsManager.getHitbox(name);
        if(hitbox != null){
            hitbox.setY(coords.getY() + hitbox.getY());
            hitbox.setX(coords.getX() + hitbox.getX());
        }
        System.out.println(name + "," +hitbox);
    }

    /**
     * @return Sprite object containing image and entities coordinates
     */
    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}

package cvut.gartnkry.model;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.collisions.HitboxInfo;
import cvut.gartnkry.view.assets.AssetsManager;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Prop {
    protected Sprite sprite;
    protected HitboxInfo hitboxInfo;
    private final String name;

    public Prop(JsonObject data, Image defaultImage) {
        name = data.get("name").getAsString();
        sprite = new Sprite(defaultImage, data.get("coordX").getAsDouble(), data.get("coordY").getAsDouble());
        hitboxInfo = AssetsManager.getHitboxInfo(name);
    }

    /**
     * @return Sprite object containing image and entities coordinates
     */
    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getHitboxRec() {
        return getHitboxRec(0, 0);
    }

    public Rectangle getHitboxRec(double velocityX, double velocityY) {
        return new Rectangle(hitboxInfo.getX() + sprite.getX() + velocityX,
                hitboxInfo.getY() + sprite.getY() + velocityY,
                hitboxInfo.getWidth(), hitboxInfo.getHeight());
    }

    public HitboxInfo getHitboxInfo() {
        return hitboxInfo;
    }
}

package cvut.gartnkry.model;

import com.google.gson.JsonObject;
import cvut.gartnkry.ResourcesUtils;
import cvut.gartnkry.view.assets.AssetsManager;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Prop {
    protected Sprite sprite;
    protected HitboxInfo hitboxInfo;
    private String name;

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

    public Rectangle getHitboxRect() {
        return getHitboxRect(0, 0);
    }

    public Rectangle getHitboxRect(double velocityX, double velocityY) {
        if (hitboxInfo != null) {
            return new Rectangle(hitboxInfo.getX() + sprite.getX() + velocityX,
                    hitboxInfo.getY() + sprite.getY() + velocityY,
                    hitboxInfo.getWidth(), hitboxInfo.getHeight());
        }
        return null;
    }

    public HitboxInfo getHitboxInfo() {
        return hitboxInfo;
    }
}

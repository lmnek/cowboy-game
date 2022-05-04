package cvut.gartnkry.model;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.collisions.HitboxInfo;
import cvut.gartnkry.view.assets.AssetsManager;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Prop {
    protected Sprite sprite;
    protected HitboxInfo hitboxInfo;
    protected final String name;
    private boolean active;

    public Prop(JsonObject data, Image image) {
        this.name = data.get("name").getAsString();
        sprite = new Sprite(image, data.get("x").getAsDouble(), data.get("y").getAsDouble());
        hitboxInfo = AssetsManager.getHitboxInfo(name);
    }

    public Prop(JsonObject data) {
        this(data, AssetsManager.getImage(data.get("name").getAsString()));
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }
}

package cvut.gartnkry.model;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.collisions.HitboxInfo;
import cvut.gartnkry.view.assets.AssetsManager;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 * Class used for displaying sprites in a given position on the ground and for interacting with other game objects.
 */
public class Prop {
    protected Sprite sprite;
    protected HitboxInfo hitboxInfo;
    protected String name;
    protected boolean active;

    /**
     * Class constructor.
     * Loads position and name from Json data.
     * Sets Image and hitbox info from asset configuration file.
     *
     * @param data  JsonObject containing prop data
     * @param image default prop image
     */
    public Prop(JsonObject data, Image image) {
        this.name = data.get("name").getAsString();
        sprite = new Sprite(image, data.get("x").getAsDouble(), data.get("y").getAsDouble());
        hitboxInfo = AssetsManager.getHitboxInfo(name);
    }

    /**
     * Class constructor.
     * Load position and name from Json data.
     * Set Image corresponding to the name.
     * Also sets hitbox info from asset configuration file.
     *
     * @param data JsonObject containing prop data
     */
    public Prop(JsonObject data) {
        this(data, AssetsManager.getImage(data.get("name").getAsString()));
    }

    /**
     * Compute where the prop will be in the next tick, according to the velocities and return its hitbox.
     *
     * @return hitbox Rectangle
     */
    public Rectangle getHitboxRec(double velocityX, double velocityY) {
        return new Rectangle(hitboxInfo.getX() + sprite.getX() + velocityX,
                hitboxInfo.getY() + sprite.getY() + velocityY,
                hitboxInfo.getWidth(), hitboxInfo.getHeight());
    }

    public Rectangle getHitboxRec() {
        return getHitboxRec(0, 0);
    }

    public Sprite getSprite() {
        return sprite;
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

    public void update() {
    }
}

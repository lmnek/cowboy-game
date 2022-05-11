package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.control.collisions.HitboxInfo;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.view.assets.AssetsManager;
import cvut.gartnkry.view.assets.Sound;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 * Parent class other entities.
 * Such entities could be player or enemies.
 * It extends Prop and also adds entity hitbox and health functionality.
 */
public abstract class Entity extends Prop {
    private final HitboxInfo entityHitboxInfo;
    protected int health;
    protected int damage;
    protected Sound deathSound;
    protected Sound damageSound;

    /**
     * Class constructor.
     *
     * @param entityData   JsonObject with entity data
     * @param defaultImage default image
     */
    protected Entity(JsonObject entityData, Image defaultImage) {
        super(entityData, defaultImage);
        health = entityData.get("health").getAsInt();
        entityHitboxInfo = AssetsManager.getEntityHitboxInfo(name);
    }

    /**
     * Update entity state in a single tick.
     */
    public abstract void update();

    /**
     * Decrease health of entity by damage points
     * @param damagePoints point as int
     */
    public void damage(int damagePoints) {
        // Decrease health
        health -= damagePoints;
        if (health < 0) {
            health = 0;
        }
        // Play damage sound
        if (health == 0) {
            AppLogger.fine(() -> "Entity died: " + name);
            deathSound.play();
        } else {
            damageSound.play();
        }
    }

    /**
     * Return hitbox corresponding to current position.
     * @return hitbox as Rectangle
     */
    public Rectangle getEntityHitboxRec() {
        return new Rectangle(entityHitboxInfo.getX() + sprite.getX(),
                entityHitboxInfo.getY() + sprite.getY(),
                entityHitboxInfo.getWidth(), entityHitboxInfo.getHeight());
    }

    public boolean isDead() {
        return health == 0;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }
}

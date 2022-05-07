package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.collisions.HitboxInfo;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.view.assets.AssetsManager;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 * Parent class for inherited entities.
 * Such entities could be player or enemies.
 */
public abstract class Entity extends Prop {
    private final HitboxInfo entityHitboxInfo;
    private final int max_health;
    private int health;
    protected int damage;

    protected Entity(JsonObject entityData, Image defaultImage) {
        super(entityData, defaultImage);
        health = entityData.get("health").getAsInt();
        max_health = entityData.get("maxHealth").getAsInt();
        if (health > max_health) {
            health = max_health;
        }
        entityHitboxInfo = AssetsManager.getEntityHitboxInfo(name);
    }

    public abstract void update();

    public void damage(int damagePoints) {
        addToHealth(-damagePoints);
    }

    public void heal(int healPoints) {
        addToHealth(healPoints);
    }

    private void addToHealth(int inc) {
        health += inc;
        if (health < 0) {
            health = 0;
        } else if (health > max_health) {
            health = max_health;
        }
    }

    public boolean isDead() {
        return health == 0;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return max_health;
    }

    public Rectangle getEntityHitboxRec() {
        return new Rectangle(entityHitboxInfo.getX() + sprite.getX(),
                entityHitboxInfo.getY() + sprite.getY(),
                entityHitboxInfo.getWidth(), entityHitboxInfo.getHeight());
    }

    public int getDamage() {
        return  damage;
    }
}

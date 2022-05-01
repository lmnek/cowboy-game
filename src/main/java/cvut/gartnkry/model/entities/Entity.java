package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.view.assets.PlayerAnimation;
import javafx.scene.image.Image;

/**
 * Parent class for inherited entities.
 * Such entities could be player or enemies.
 */
public class Entity extends Prop {
    protected final int max_health;
    private int health;
    protected PlayerAnimation animation;

    protected Entity(JsonObject entityData, Image defaultImage) {
        super(entityData, defaultImage);
        health = entityData.get("health").getAsInt();
        max_health = entityData.get("max_health").getAsInt();
        if (health > max_health) {
            health = max_health;
        }
    }

    public void decreaseHealth(int damagePoints) {
        health -= damagePoints;
        if (health < 0) {
            health = 0;
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

    public void collisionSetX(double x) {
        sprite.setX(sprite.getX() - x);
    }

    public void collisionSetY(double y) {
        sprite.setY(sprite.getY() - y);
    }
}

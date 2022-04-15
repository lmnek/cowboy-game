package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.model.Sprite;
import javafx.scene.image.Image;

/**
 * Parent class for inherited entities.
 * Such entities could be player or enemies.
 */
public class Entity extends Prop {
    protected static int MAX_HEALTH;
    protected int health;

    protected Entity(JsonObject entityData, Image defaultImage) {
        super(entityData, defaultImage);
        health = entityData.get("health").getAsInt();
        if (health > MAX_HEALTH) {
            health = MAX_HEALTH;
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

    public void collisionSetX(double x) {
        sprite.setX(sprite.getX() - x);
    }

    public void collisionSetY(double y) {
        sprite.setY(sprite.getY() - y);
    }
}

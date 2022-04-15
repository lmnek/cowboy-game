package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Prop;
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

    protected void addToHitbox(double velocityX, double velocityY){
        hitbox.setX(hitbox.getX() + velocityX * Settings.SCALE);
        hitbox.setY(hitbox.getY() + velocityY * Settings.SCALE);
    }
}

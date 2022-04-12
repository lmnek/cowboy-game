package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.ResourcesUtils;
import cvut.gartnkry.model.Sprite;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * Parent class for inherited entities.
 * Such entities could be player or enemies.
 */
public abstract class Entity {
    protected Sprite sprite;
    protected int health;
    protected int maxHealth;


    protected Entity(JsonObject entityData, int maxHealth, Image defaultImage) {
        sprite = new Sprite(defaultImage, ResourcesUtils.pointFromJson(entityData));

        this.maxHealth = maxHealth;
        health = entityData.get("health").getAsInt();
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public abstract void update();

    /**
     * @return Sprite object containing image and entities coordinates
     */
    public Sprite getSprite() {
        return sprite;
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

}

package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.Data;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.view.assets.ImageAsset;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.HashMap;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.D;

/**
 * Parent class for inherited entities.
 * Such entities could be player or enemies.
 */
public abstract class Entity {
    protected Sprite sprite;
    protected int health;
    protected int maxHealth;

    protected Entity(JsonObject entityData, int maxHealth, Image defaultImage) {
        Point2D coords = new Point2D(entityData.get("positionX").getAsDouble(),
                entityData.get("positionY").getAsDouble());
        sprite = new Sprite(defaultImage, coords);

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

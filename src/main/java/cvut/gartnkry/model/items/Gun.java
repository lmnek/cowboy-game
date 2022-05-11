package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.entities.Bullet;
import cvut.gartnkry.view.assets.Sound;

import static cvut.gartnkry.control.Settings.SCALE;

public class Gun extends Item {
    private double bulletVelocity;
    private int damage;
    private int fireRate;

    /**
     * Class constructor
     * @param json JsonObject with item data
     */
    public Gun(JsonObject json) {
        super(json);
    }

    /**
     * Shoot from the gun ~ create a bullet
     * @param dirX direction of bullet in X axis
     * @param dirY direction of bullet in Y axis
     * @param X bullet X starting coordinate
     * @param Y bullet Y starting coordinate
     * @return new Bullet object
     */
    public Bullet shoot(int dirX, int dirY, double X, double Y) {
        Sound.GUN.play();
        return new Bullet(dirX, dirY, X, Y, this);
    }

    @Override
    public void parseJson(JsonObject json) {
        this.bulletVelocity = json.get("bulletVelocity").getAsDouble() * SCALE;
        this.fireRate = json.get("fireRate").getAsInt();
        this.damage = json.get("damage").getAsInt();
    }

    @Override
    public boolean use() {
        return false;
    }

    public int getFireRate() {
        return fireRate;
    }

    public int getDamage() {
        return damage;
    }

    public Double getBulletVelocity() {
        return bulletVelocity;
    }
}

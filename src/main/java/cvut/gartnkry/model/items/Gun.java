package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.entities.Bullet;

import static cvut.gartnkry.control.Settings.SCALE;

public class Gun extends Item {
    private double bulletVelocity;
    private int damage;
    private int fireRate;

    public Gun(JsonObject json) {
        super(json);
    }

    public Bullet shoot(int dirX, int dirY, double X, double Y) {
        return new Bullet(dirX, dirY, X, Y, bulletVelocity, this);
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

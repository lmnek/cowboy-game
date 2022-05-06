package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.entities.Bullet;

public class Gun extends Item {
    private double bulletSpeed;
    public int fireRate;

    public Gun(JsonObject json) {
        super(json);
    }

    public Bullet shoot(){
        return new Bullet(0, 0);
    }

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    public int getFireRate() {
        return fireRate;
    }

    @Override
    public void parseJson(JsonObject json) {
        this.bulletSpeed = json.get("bulletSpeed").getAsDouble();
        this.fireRate = json.get("fireRate").getAsInt();
    }
}

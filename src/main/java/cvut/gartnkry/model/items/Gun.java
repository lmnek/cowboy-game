package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.entities.Bullet;

public class Gun extends Item {
    private double bulletVelocity;
    public int fireRate;

    public Gun(JsonObject json) {
        super(json);
    }

    public Bullet shoot(int dirX, int dirY, double X, double Y){
        return new Bullet(dirX, dirY, X, Y, bulletVelocity);
    }

    public int getFireRate() {
        return fireRate;
    }

    @Override
    public void parseJson(JsonObject json) {
        this.bulletVelocity = json.get("bulletVelocity").getAsDouble();
        this.fireRate = json.get("fireRate").getAsInt();
    }
}

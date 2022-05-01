package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;

public class Gun extends Item {
    private double bulletSpeed;
    public double fireRate;
    public int bulletSize;

    public Gun(JsonObject json) {
        super(json);
    }

    public double getBulletSpeed() {
        return bulletSpeed;
    }

    public double getFireRate() {
        return fireRate;
    }

    public int getBulletSize() {
        return bulletSize;
    }

    @Override
    public void parseJson(JsonObject json) {
//        this.bulletSpeed = json.get("bulletSpeed").getAsDouble();
//        this.fireRate = json.get("fireRate").getAsDouble();
//        this.bulletSize = json.get("bulletSize").getAsInt();
    }
}

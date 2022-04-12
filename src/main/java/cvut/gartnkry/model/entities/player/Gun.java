package cvut.gartnkry.model.entities.player;

public class Gun {
    private double bulletSpeed;
    public double fireRate;
    public int bulletSize;

    // default gun
    public Gun(){
        this.bulletSpeed = 10;
        this.fireRate = 10;
        this.bulletSize = 1;
    }

    public Gun(double bulletSpeed, double fireRate, int bulletSize) {
        this.bulletSpeed = bulletSpeed;
        this.fireRate = fireRate;
        this.bulletSize = bulletSize;
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
}

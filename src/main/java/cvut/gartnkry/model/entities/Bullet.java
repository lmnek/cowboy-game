package cvut.gartnkry.model.entities;

import cvut.gartnkry.control.Settings;
import cvut.gartnkry.model.items.Gun;
import javafx.scene.shape.Rectangle;

public class Bullet {
    private final Gun gun;
    private final double velocityX;
    private final double velocityY;
    private double X;
    private double Y;

    public Bullet(int directionX, int directionY, double X, double Y, double velocity, Gun gun){
        this.velocityX = directionX * velocity;
        this.velocityY = directionY * velocity;
        this.X = X;
        this.Y = Y;
        this.gun = gun;
    }

    public void update(){
        X += velocityX;
        Y += velocityY;
    }

    public Rectangle getRectangle() {
        return new Rectangle(X, Y, Settings.SCALE, Settings.SCALE);
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public Gun getGun() {
        return gun;
    }
}

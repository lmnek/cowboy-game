package cvut.gartnkry.model.entities;

import cvut.gartnkry.control.Settings;
import cvut.gartnkry.model.items.Gun;
import javafx.scene.shape.Rectangle;

/**
 * Class for single bullet shot by a pistol.
 */
public class Bullet {
    private final Gun gun;
    private final double velocityX;
    private final double velocityY;
    private double X;
    private double Y;

    /**
     * Class constructor.
     * @param directionX direction of bullet in X axis
     * @param directionY direction of bullet in Y axis
     * @param X X starting coordinate
     * @param Y Y starting coordinate
     * @param gun Gun that shot the bullet
     */
    public Bullet(int directionX, int directionY, double X, double Y, Gun gun) {
        this.velocityX = directionX * gun.getBulletVelocity();
        this.velocityY = directionY * gun.getBulletVelocity();
        this.X = X;
        this.Y = Y;
        this.gun = gun;
    }

    public void update() {
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

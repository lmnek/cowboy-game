package cvut.gartnkry.model.entities;

import cvut.gartnkry.Settings;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

public class Bullet {
    private final double velocityX;
    private final double velocityY;
    private double X;
    private double Y;

    public Bullet(int directionX, int directionY, double X, double Y, double velocity){
        this.velocityX = directionX * velocity;
        this.velocityY = directionY * velocity;
        this.X = X;
        this.Y = Y;
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
}

package cvut.gartnkry.model;

import cvut.gartnkry.control.Settings;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 * Class for keeping image of the sprite
 * and its coordinates on the map.
 */
public class Sprite {
    private double X;
    private double Y;
    private Image image;

    /**
     * Class constructor.
     *
     * @param image  loaded Image asset
     * @param coordX X coordinate
     * @param coordY Y coordinate
     */
    public Sprite(Image image, double coordX, double coordY) {
        this.image = image;
        this.X = coordX * Settings.SCALE;
        this.Y = coordY * Settings.SCALE;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public void setX(double coordX) {
        this.X = coordX;
    }

    public void setY(double coordY) {
        this.Y = coordY;
    }

    /**
     * @return X coordinate in the center of the sprite
     */
    public double getXCenter() {
        return X + image.getWidth() / 2;
    }

    /**
     * @return Y coordinate in the center of the sprite
     */
    public double getYCenter() {
        return Y + image.getHeight() / 2;
    }

    public void addXYScaled(double x, double y) {
        addXY(x * Settings.SCALE, y * Settings.SCALE);
    }

    public void addXY(double x, double y) {
        X += x;
        Y += y;
    }

    public Rectangle getImageRect() {
        return new Rectangle(X, Y, image.getWidth(), image.getHeight());
    }

}

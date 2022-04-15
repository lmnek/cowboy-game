package cvut.gartnkry.model;

import cvut.gartnkry.Settings;
import javafx.scene.image.Image;

/**
 * Class for keeping image of this sprite
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
     * @param coords coordinates on the map
     */
    public Sprite(Image image, double coordX, double coordY) {
        this.image = image;
        this.X = coordX;
        this.Y = coordY;
    }

    /**
     * @return Image asset of this sprite.
     */
    public Image getImage() {
        return image;
    }

    /**
     * New image is being set when animation
     * frames are being switched.
     *
     * @param image new Image
     */
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
     * @return double value of X coordinate in the center of the sprite
     */
    public double getXCenter() {
        return X + image.getWidth() / 2;
    }

    /**
     * @return double value of Y coordinate in the center of the sprite
     */
    public double getYCenter() {
        return Y + image.getHeight() / 2;
    }

    /**
     * @param x double value to add to x coordinate
     * @param y double value to add to y coordinate
     */
    public void addXY(double x, double y) {
        X += x * Settings.SCALE;
        Y += y * Settings.SCALE;
    }
}

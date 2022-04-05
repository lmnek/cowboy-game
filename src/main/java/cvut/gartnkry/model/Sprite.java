package cvut.gartnkry.model;

import cvut.gartnkry.Settings;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * Class for keeping image of this sprite
 * and its coordinates on the map.
 */
public class Sprite {
    private Point2D coords;
    private Image image;

    /**
     * Class constructor.
     * @param image loaded Image asset
     * @param coords coordinates on the map
     */
    public Sprite(Image image, Point2D coords) {
        this.image = image;
        this.coords = coords;
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
     * @param image new Image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    public double getX() {
        return coords.getX();
    }

    public double getY() {
        return coords.getY();
    }

    /**
     * @return double value of X coordinate in the center of the sprite
     */
    public double getXCenter() {
        return coords.getX() + image.getWidth() / 2;
    }

    /**
     * @return double value of Y coordinate in the center of the sprite
     */
    public double getYCenter() {
        return coords.getY() + image.getHeight() / 2;
    }

    /**
     * @param x double value to add to x coordinate
     * @param y double value to add to y coordinate
     */
    public void addXY(double x, double y) {
        coords = coords.add(x * Settings.SCALE, y * Settings.SCALE);
    }
}

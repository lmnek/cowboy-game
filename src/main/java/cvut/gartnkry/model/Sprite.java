package cvut.gartnkry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cvut.gartnkry.Settings;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 * Class for keeping image of this sprite
 * and its coordinates on the map.
 */
public class Sprite {
    private double x;
    private double y;
    @JsonIgnore
    private Image image;


    public Sprite(){
    }

    /**
     * Class constructor.
     *
     * @param image loaded Image asset
     */
    public Sprite(Image image, double coordX, double coordY) {
        this.image = image;
        this.x = coordX * Settings.SCALE;
        this.y = coordY * Settings.SCALE;
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
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double coordX) {
        this.x = coordX;
    }

    public void setY(double coordY) {
        this.y = coordY;
    }

    /**
     * @return double value of X coordinate in the center of the sprite
     */
    public double getXCenter() {
        return x + image.getWidth() / 2;
    }

    /**
     * @return double value of Y coordinate in the center of the sprite
     */
    public double getYCenter() {
        return y + image.getHeight() / 2;
    }

    /**
     * @param x double value to add to x coordinate
     * @param y double value to add to y coordinate
     */
    public void addXYScaled(double x, double y) {
        addXY(x * Settings.SCALE, y * Settings.SCALE);
    }

    public void addXY(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public Rectangle getImageRect() {
        return new Rectangle(x, y, image.getWidth(), image.getHeight());
    }

}

package cvut.gartnkry.model;

import cvut.gartnkry.Settings;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * Class for keeping image and its coordinates
 */
public class Sprite {
    private Point2D coords;
    private Image image;

    public Sprite(Point2D coords) {
        this(null, coords);
    }

    public Sprite(Image image, Point2D coords) {
        this.image = image;
        this.coords = coords;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public double getX() {
        return coords.getX();
    }

    public double getXCenter() {
        return coords.getX() + image.getWidth() / 2;
    }

    public double getY() {
        return coords.getY();
    }

    public double getYCenter() {
        return coords.getY() + image.getHeight() / 2;
    }

    public void addXY(double x, double y) {
        coords = coords.add(x * Settings.SCALE, y * Settings.SCALE);
    }

    public Point2D getCoords() {
        return coords;
    }
}

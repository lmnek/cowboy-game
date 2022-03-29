package cvut.gartnkry.model;

import cvut.gartnkry.Settings;
import cvut.gartnkry.view.assets.Images;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 *
 *  Class for keeping image and its coordinates
 */
public class Sprite {
    private Point2D coords;
    private Image image;

    public Sprite(Image image, Point2D coords) {
        this.image = image;
        this.coords = coords;
    }

    public Image getImage() {
        return image;
    }
    public void setImage(Image image){
        this.image = image;
    }

    public double getX() {
        return coords.getX();
    }

    public double getY(){
        return  coords.getY();
    }

    public void addXY(double x, double y) {
        coords = coords.add(x * Settings.SCALE, y * Settings.SCALE);
    }
}

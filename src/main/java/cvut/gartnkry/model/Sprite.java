package cvut.gartnkry.model;

import cvut.gartnkry.view.Images;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 *
 * Class for keeping image and its coordinates
 */
public class Sprite {
    private Point2D coords;
    private final Images imageData;

    public Sprite(Images image, Point2D coords) {
        this.imageData = image;
        this.coords = coords;
    }

    public Image getImage() {
        //TODO: refactor enum ???
        return imageData.getImage();
    }

    public double getX() {
        return coords.getX();
    }

    public double getY(){
        return  coords.getY();
    }
}

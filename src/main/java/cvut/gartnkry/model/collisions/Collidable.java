package cvut.gartnkry.model.collisions;

import javafx.scene.shape.Rectangle;

public interface Collidable {
    public Rectangle getHitboxRec();
    public Rectangle getHitboxRec(double velocityX, double velocityY);
}

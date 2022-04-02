package cvut.gartnkry.model.entities;

import cvut.gartnkry.Data;
import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.view.assets.Animation;
import cvut.gartnkry.view.assets.ImageAsset;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.*;

public class Player extends Entity {

    private final Map<KeyCode, Integer> directions;
    private final double sidewaysVelocity;

    private Animation animation;
    private int tickCounter;
    private int previousDirectionX;
    private int previousDirectionY;

    public Player(Data data, Sprite sprite) {
        super(sprite);

        directions = new HashMap<>();
        directions.put(W, 0);
        directions.put(A, 0);
        directions.put(S, 0);
        directions.put(D, 0);

        sidewaysVelocity = Math.sqrt(0.5); // compute in advance
        tickCounter = 0;
        previousDirectionX = previousDirectionY = 1;
        animation = null;

        //this.data = data;
    }

    public void onKeyPressed(KeyCode code) {
        directions.replace(code, 1);
    }

    public void onKeyReleased(KeyCode code) {
        directions.replace(code, 0);
    }

    /**
     * Update coordinates of player's sprite.
     * Includes swapping frames for animation.
     */
    public void update() {

        int directionX = directions.get(D) - directions.get(A);
        int directionY = directions.get(S) - directions.get(W);

        boolean changedDirection = !(previousDirectionX == directionX && previousDirectionY == directionY);
        previousDirectionX = directionX;
        previousDirectionY = directionY;

        // update sprite coords only when moving
        if (directionX != 0 || directionY != 0) {
            ++tickCounter;

            // first frame from new animation
            if (changedDirection && setAnimation(directionX, directionY)) {
                tickCounter = animation.getTicksPerFrame() - 3;
                sprite.setImage(animation.getFirstFrame());
            }
            // next frame in animation
            else if (tickCounter >= animation.getTicksPerFrame()) {
                sprite.setImage(animation.getNextFrame());
                tickCounter = 0;
            }

            // compute velocities
            double velocityX, velocityY;
            velocityX = directionX;
            velocityY = directionY;
            // fixes faster movement sideways -> pythagoras theorem
            if (directionX != 0 && directionY != 0) {
                velocityX *= sidewaysVelocity;
                velocityY *= sidewaysVelocity;
            }
            // move sprite
            sprite.addXY(velocityX * Settings.PLAYER_SPEED, velocityY * Settings.PLAYER_SPEED);

        } else if (changedDirection) {
            sprite.setImage(ImageAsset.PLAYER_DEFAULT.getImage());
            animation = null;
        }
    }

    // Set new animation, if it is necessary
    // Return boolean whether new animation was set
    private boolean setAnimation(int directionX, int directionY) {
        Animation an1 = chooseAnimationX(directionX);
        Animation an2 = chooseAnimationY(directionY);

        if (animation == null // starting to move
                || (an1 != animation && an2 != animation)) { // completely change directions
            animation = (an1 == null) ? an2 : an1; // new animation
            return true;
        }
        return false;
    }

    private Animation chooseAnimationX(int directionX) {
        if (directionX == 1) {
            return Animation.PLAYER_RIGHT;
        } else if (directionX == -1) {
            return Animation.PLAYER_LEFT;
        }
        return null;
    }

    private Animation chooseAnimationY(int directionY) {
        if (directionY == 1) {
            return Animation.PLAYER_DOWN;
        } else if (directionY == -1) {
            return Animation.PLAYER_UP;
        }
        return null;
    }
}

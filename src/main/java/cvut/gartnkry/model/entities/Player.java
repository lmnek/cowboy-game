package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.Settings;
import cvut.gartnkry.view.assets.Animation;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.*;

//TODO: go through JAVADOC when program is done

/**
 * Player class is responsible for:
 * <ul>
 * <li> Keeping player's data (position, velocity, players movement direction, ...)
 * <li> Handling keyboard input (WASD movement)
 * <li> Animating player's movement
 * </ul>
 * Keys for controlling player's movement direction are:
 * <ul>
 * <li> W - forward
 * <li> A - left
 * <li> S - bottom
 * <li> D - right
 * </ul>
 */
public class Player extends Entity {
    private static final int MAX_HEALTH = 10;

    private boolean moving;
    private Animation animation;
    private int tickCounter;
    private int previousDirectionX;
    private int previousDirectionY;

    private final Map<KeyCode, Integer> directions;
    private final double sidewaysVelocity;

    /**
     * Class constructor.
     * Load position of player from data and set a sprite.
     * Player is initialized not moving, with default static image and no animation.
     *
     * @param playerData data object loaded from input save file
     */
    public Player(JsonObject playerData) {
        super(playerData, MAX_HEALTH, Animation.PLAYER_DOWN.getDefaultImage());
        animation = Animation.PLAYER_DOWN;
        moving = false;

        directions = new HashMap<>();
        directions.put(W, 0);
        directions.put(A, 0);
        directions.put(S, 0);
        directions.put(D, 0);

        sidewaysVelocity = Math.sqrt(0.5); // compute in advance
        tickCounter = 0;
        previousDirectionX = previousDirectionY = 0;
    }

    /**
     * When key is pressed, player starts to move in the supposed direction.
     *
     * @param code code of key that is pressed
     */
    public void onKeyPressed(KeyCode code) {
        directions.replace(code, 1);
    }

    /**
     * When key is released, player stops to move in the supposed direction.
     *
     * @param code code of key that is released
     */
    public void onKeyReleased(KeyCode code) {
        directions.replace(code, 0);
    }

    /**
     * Update coordinates of player's sprite from direction of movement.
     * Includes swapping frames for animation.
     */
    @Override
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
            moving = true;

        } else if (changedDirection) {
            sprite.setImage(animation.getDefaultImage());
            moving = false;
        }
    }

    /**
     * Set new animation, if player is starting to move
     * or completely changed directions.
     *
     * @param directionX x direction movement
     * @param directionY y direction movement
     * @return boolean whether new animation was set
     */
    private boolean setAnimation(int directionX, int directionY) {
        Animation an1 = chooseAnimationX(directionX);
        Animation an2 = chooseAnimationY(directionY);

        // not moving or completely change directions
        if (!moving || (an1 != animation && an2 != animation)) {
            animation = (an1 == null) ? an2 : an1; // new animation
            return true;
        }
        return false;
    }

    /**
     * Choose animation from direction in X axis
     *
     * @param directionX
     * @return Animation object or null (if animation was not chosen)
     */
    private Animation chooseAnimationX(int directionX) {
        if (directionX == 1) {
            return Animation.PLAYER_RIGHT;
        } else if (directionX == -1) {
            return Animation.PLAYER_LEFT;
        }
        return null;
    }

    /**
     * Choose animation from direction in Y axis
     *
     * @param directionY
     * @return Animation object or null (if animation was not chosen)
     */
    private Animation chooseAnimationY(int directionY) {
        if (directionY == 1) {
            return Animation.PLAYER_DOWN;
        } else if (directionY == -1) {
            return Animation.PLAYER_UP;
        }
        return null;
    }
}

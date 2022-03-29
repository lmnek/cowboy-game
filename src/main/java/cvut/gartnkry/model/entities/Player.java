package cvut.gartnkry.model.entities;

import cvut.gartnkry.Data;
import cvut.gartnkry.Settings;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.view.assets.Animations;
import cvut.gartnkry.view.assets.Images;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.*;

public class Player extends Entity {

    private Map<KeyCode, Integer> directions;
    private double sidewaysVelocity;

    private Animations animation;
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
        previousDirectionX = previousDirectionY = 0;
        //this.data = data;
    }

    public void onKeyPressed(KeyCode code) {
        directions.replace(code, 1);
    }

    public void onKeyReleased(KeyCode code) {
        directions.replace(code, 0);
    }

    /**
     *  Update coordinates of player's sprite.
     *  Includes swapping frames for animation.
     */
    public void update() {

        int directionX = (directions.get(D) - directions.get(A));
        int directionY = (directions.get(S) - directions.get(W));

        boolean changedDirection = !(previousDirectionX == directionX && previousDirectionY == directionY);
        previousDirectionX = directionX;
        previousDirectionY = directionY;

        // update sprite coords only when moving
        if (directionX != 0 || directionY != 0) {
            ++tickCounter;

            // compute velocities
            double velocityX, velocityY;
            boolean sideways = directionX != 0 && directionY != 0;

            velocityX = directionX;
            velocityY = directionY;
            if (sideways) {
                // fixes faster movement sideways -> pythagoras theorem
                velocityX *= sidewaysVelocity;
                velocityY *= sidewaysVelocity;
            }

            // set image frame from animation
            if (changedDirection && !sideways) {
                // set new animation
                animation = chooseAnimation(directionX, directionY);
                sprite.setImage(animation.getFrame());
                tickCounter = 0;

            } else if (tickCounter >= animation.getTicksPerFrame()) {
                // next frame
                sprite.setImage(animation.getNextFrame());
                System.out.println(tickCounter);
                tickCounter = 0;
            }

            sprite.addXY(velocityX * Settings.PLAYER_SPEED, velocityY * Settings.PLAYER_SPEED); // move sprite
        } else if (changedDirection) {
            sprite.setImage(Images.PLAYER_DEFAULT.getImage());
        }
    }

    private Animations chooseAnimation(int directionX, int directionY) {
        if (directionX > 0) {
            return Animations.PLAYER_RIGHT;
        } else if (directionX < 0) {
            return Animations.PLAYER_LEFT;
        }
        if (directionY > 0) {
            return Animations.PLAYER_DOWN;
        } else if (directionY < 0) {
            return Animations.PLAYER_UP;
        }
        return null;         //TODO: return
    }

}

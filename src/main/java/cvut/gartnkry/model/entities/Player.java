package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.items.Inventory;
import cvut.gartnkry.model.items.Item;
import cvut.gartnkry.view.assets.PlayerAnimation;
import javafx.scene.input.KeyCode;

import java.util.*;

import static cvut.gartnkry.Settings.PLAYER_MAX_VELOCITY;
import static cvut.gartnkry.Settings.PLAYER_TICKS_TO_ACCELERATE;
import static java.lang.Math.max;
import static java.lang.Math.min;
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
    private int tickCounter;

    //private HashMap<KeyCode, Boolean> keyPressed;
    private final Map<KeyCode, Integer> directions;

    private double velocityX, velocityY;
    private final double maxSidewaysVel;
    private final double maxVel;
    private final double addVel;
    private final double addSidewaysVel;

    private final Inventory inventory;
    private final LinkedList<Bullet> bullets;

    /**
     * Class constructor.
     * Load position of player from data and set a sprite.
     * Player is initialized not moving, with default static image and no animation.
     *
     * @param playerData data object loaded from input save file
     */
    public Player(JsonObject playerData) {
        super(playerData, null);

        animation = PlayerAnimation.PLAYER_DOWN;
        tickCounter = 0;
        bullets = new LinkedList<>();

        velocityX = velocityY = 0;
        maxVel = PLAYER_MAX_VELOCITY;
        maxSidewaysVel = Math.sqrt(maxVel * maxVel / 2);
        addVel = maxVel / PLAYER_TICKS_TO_ACCELERATE;
        addSidewaysVel = maxSidewaysVel / PLAYER_TICKS_TO_ACCELERATE;

        KeyCode[] keys = new KeyCode[]{W, A, S, D};
        directions = new HashMap<>(keys.length);
        for (KeyCode k : keys) {
            directions.put(k, 0);
        }

        inventory = new Inventory(playerData.get("inventory").getAsJsonArray());
        sprite.setImage(PlayerAnimation.PLAYER_DOWN.getDefaultImage());
    }

    public void pickupItem(Item item) {
        //
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

    public void update() {
        setSpriteImage();
        sprite.addXY(velocityX, velocityY);
//        if (gun != null) {
//            handleShooting();
//        }
    }

    private void handleShooting() {
    }

    public int getDirectionX() {
        return directions.get(D) - directions.get(A);
    }

    public int getDirectionY() {
        return directions.get(S) - directions.get(W);
    }

    public void computeVelocities() {
        double add_tmp, max_vel;
        // fixes faster movement sideways -> pythagoras theorem
        if (velocityX != 0 && velocityY != 0) {
            max_vel = maxSidewaysVel;
            add_tmp = addSidewaysVel;
        } else {
            max_vel = maxVel;
            add_tmp = addVel;
        }
        int directionX = getDirectionX();
        int directionY = getDirectionY();
        velocityX = getSingleVelocity(directionX, velocityX, add_tmp, max_vel);
        velocityY = getSingleVelocity(directionY, velocityY, add_tmp, max_vel);
    }

    private double getSingleVelocity(int direction, double velocity, double add_tmp, double max_vel) {
        // accelerate to max velocity
        if (direction != 0) {
            velocity = max(min(max_vel, velocity + (add_tmp * direction)), -max_vel);
        }
        // decelerate to zero
        else if (velocity > 0) {
            velocity = max(velocity - add_tmp, 0);
        } else if (velocity < 0) {
            velocity = min(velocity + add_tmp, 0);
        }
        return velocity;
    }

    private void setSpriteImage() {
        ++tickCounter;
        PlayerAnimation tmp_animation = getAnimation();
        if (tmp_animation != null) {
            if (tmp_animation != animation) {
                tickCounter = animation.getTicksPerFrame() - 3;
                animation = tmp_animation;
                sprite.setImage(animation.getFirstFrame());
            } else if (tickCounter >= animation.getTicksPerFrame()) {
                sprite.setImage(animation.getNextFrame());
                tickCounter = 0;
            }
        } else {
            sprite.setImage(animation.getDefaultImage());
        }
    }

    private PlayerAnimation getAnimation() {
        int directionX = getDirectionX();
        if (directionX == 1) {
            return PlayerAnimation.PLAYER_RIGHT;
        } else if (directionX == -1) {
            return PlayerAnimation.PLAYER_LEFT;
        }
        int directionY = getDirectionY();
        if (directionY == 1) {
            return PlayerAnimation.PLAYER_DOWN;
        } else if (directionY == -1) {
            return PlayerAnimation.PLAYER_UP;
        }
        return null;
    }

    public LinkedList<Bullet> getBullets() {
        return bullets;
    }


    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
}

package cvut.gartnkry.model.entities.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cvut.gartnkry.model.entities.Entity;
import cvut.gartnkry.view.assets.Animation;
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
    private static final int MAX_HEALTH = 10;

    private Animation animation;
    private int tickCounter;



    private HashMap<KeyCode, Boolean> keyPressed;
    private final Map<KeyCode, Integer> directions;

    double velocityX, velocityY;
    private final double maxSidewaysVel;
    private final double maxVel;
    private final double addVel;
    private final double addSidewaysVel;

    private Gun gun;
    private boolean hasHat;
    private final LinkedList<Bullet> bullets;

    /**
     * Class constructor.
     * Load position of player from data and set a sprite.
     * Player is initialized not moving, with default static image and no animation.
     *
     * @param playerData data object loaded from input save file
     */
    public Player(JsonObject playerData) {
        super(playerData, MAX_HEALTH, Animation.PLAYER_DOWN.getDefaultImage());
        parseJson(playerData);

        animation = Animation.PLAYER_DOWN;
        tickCounter = 0;
        bullets = new LinkedList<>();


        maxVel = PLAYER_MAX_VELOCITY;
        maxSidewaysVel = Math.sqrt(maxVel * maxVel / 2);
        addVel = maxVel / PLAYER_TICKS_TO_ACCELERATE;
        addSidewaysVel = maxSidewaysVel / PLAYER_TICKS_TO_ACCELERATE;


        velocityX = velocityY = 0;


        KeyCode[] keys = new KeyCode[]{W, A, S, D};
        directions = new HashMap<>(keys.length);
        for (KeyCode k : keys) {
            directions.put(k, 0);
        }
    }

    private void parseJson(JsonObject playerData) {
        // parse gun
        if(playerData.get("gun") != null){
            JsonObject gunJson = playerData.get("gun").getAsJsonObject();
            gun = new Gun(gunJson.get("bulletSpeed").getAsDouble(),
                    gunJson.get("fireRate").getAsDouble(),
                    gunJson.get("bulletSize").getAsInt());
        }else{
            gun = null;
        }
        // parse inventory
        for (JsonElement el : playerData.get("inventory").getAsJsonArray()) {
            pickupItem(el.getAsString());
        }
    }

    public void pickupItem(String itemName) {
        switch (itemName) {
            case "gun":
                gun = new Gun();
                break;
            case "hat":
                hasHat = true;
                break;
        }
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


    @Override
    public void update() {
        updateMovement();
        if (gun != null) {
            handleShooting();
        }
    }

    private void handleShooting() {
    }


    /**
     * Update coordinates of player's sprite from direction of movement.
     * Includes swapping frames for animation.
     */
    private void updateMovement() {
        int directionX = directions.get(D) - directions.get(A);
        int directionY = directions.get(S) - directions.get(W);

//        System.out.println(directionX + "   " + directionY);

        setSpriteImage(directionX, directionY);

        double add_tmp, max_vel;
        // fixes faster movement sideways -> pythagoras theorem
        if (velocityX != 0 && velocityY != 0) {
            max_vel = maxSidewaysVel;
            add_tmp = addSidewaysVel;
        }else{
            max_vel = maxVel;
            add_tmp = addVel;
        }

        velocityX = getVelocity(directionX, velocityX, add_tmp, max_vel);
        velocityY = getVelocity(directionY, velocityY, add_tmp, max_vel);

       // System.out.println(velocityX + "    " + velocityY);

        // move sprite
        sprite.addXY(velocityX, velocityY);
    }

    public double getVelocity(int direction, double velocity,double add_tmp, double max_vel){
        // accelerate to max velocity
        if(direction != 0){
            velocity = max(min(max_vel, velocity + (add_tmp * direction)), -max_vel);
        }
        // decelerate to zero
        else if (velocity > 0) {
            velocity = max(velocity - add_tmp, 0);
        }
        else if(velocity < 0){
            velocity = min(velocity + add_tmp, 0);
        }
        return velocity;
    }

    private void setSpriteImage(int directionX, int directionY) {
        ++tickCounter;
        Animation tmp_animation = getAnimation(directionX, directionY);
        if(tmp_animation != null){
            if(tmp_animation != animation){
                tickCounter = animation.getTicksPerFrame() - 3;
                animation = tmp_animation;
                sprite.setImage(animation.getFirstFrame());
            }else if(tickCounter >= animation.getTicksPerFrame()){
                sprite.setImage(animation.getNextFrame());
                tickCounter = 0;
            }
        }else{
            sprite.setImage(animation.getDefaultImage());
        }
    }

    private Animation getAnimation(int directionX, int directionY) {
        if (directionX == 1) {
            return Animation.PLAYER_RIGHT;
        } else if (directionX == -1) {
            return  Animation.PLAYER_LEFT;
        }
        if (directionY == 1) {
            return Animation.PLAYER_DOWN;
        } else if (directionY == -1) {
            return Animation.PLAYER_UP;
        }
        return null;
    }

    public LinkedList<Bullet> getBullets() {
        return bullets;
    }
}

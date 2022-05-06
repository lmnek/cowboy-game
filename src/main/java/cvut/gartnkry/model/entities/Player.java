package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.KeysEventHandler;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.model.items.Gun;
import cvut.gartnkry.model.items.Inventory;
import cvut.gartnkry.model.items.PropItem;
import cvut.gartnkry.view.assets.PlayerAnimation;

import java.util.*;

import static cvut.gartnkry.Settings.*;
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
    private double velocityX, velocityY;
    private final double maxSidewaysVel;
    private final double maxVel;
    private final double addVel;
    private final double addSidewaysVel;
    private boolean moving;

    private final Inventory inventory;
    private final LinkedList<Bullet> bullets;

    private boolean invincible;
    private final int invincibleInterval = 30;
    private int invincibleCounter;
    private int fireRateMax = 100;
    private int fireRateCounter;

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
        bullets = new LinkedList<>();

        velocityX = velocityY = 0;
        maxVel = PLAYER_MAX_VELOCITY;
        maxSidewaysVel = Math.sqrt(maxVel * maxVel / 2);
        addVel = maxVel / PLAYER_TICKS_TO_ACCELERATE;
        addSidewaysVel = maxSidewaysVel / PLAYER_TICKS_TO_ACCELERATE;

        inventory = new Inventory(playerData.get("inventory").getAsJsonArray());
        sprite.setImage(PlayerAnimation.PLAYER_DOWN.getDefaultImage());
    }

    public void pickupItem(PropItem prop) {
        inventory.pickup(prop);
    }

    public void update() {
        sprite.addXYScaled(velocityX, velocityY);
        invincibleTick();

        int dirX = KeysEventHandler.getDirection(D, A);
        int dirY = KeysEventHandler.getDirection(S, W);
        int shootX = KeysEventHandler.getDirection(RIGHT, LEFT);
        int shootY = KeysEventHandler.getDirection(DOWN, UP);
        boolean shooting = inventory.gunSelected() && (shootX != 0 || shootY != 0);
        ++tickCounter;
        if (shooting) {
            setShootingSprite(dirX, dirY, shootX, shootY);
        } else {
            setWalkingSprite(dirX, dirY);
        }

        handleShooting(shootX, shootY, shooting);
    }

    private void invincibleTick() {
        if (invincible) {
            ++invincibleCounter;
            if (invincibleCounter == invincibleInterval) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    private void handleShooting(int shootX, int shootY, boolean shooting) {
        if (fireRateCounter != fireRateMax) {
            ++fireRateCounter;
        } else if (shooting) {
            Gun gun = (Gun) inventory.getSelectedItem();
            gun.shoot();
            fireRateMax = gun.getFireRate();
            fireRateCounter = 0;
            System.out.println(shootX + " " + shootY);
        }
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
        velocityX = getSingleVelocity(KeysEventHandler.getDirection(D, A), velocityX, add_tmp, max_vel);
        velocityY = getSingleVelocity(KeysEventHandler.getDirection(S, W), velocityY, add_tmp, max_vel);
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

    private void setWalkingSprite(int dirX, int dirY) {
        PlayerAnimation tmp_animation = getAnimation(dirX, dirY);
        if ((moving = tmp_animation != null)) {
            setSpriteImage(tmp_animation);
        } else {
            sprite.setImage(animation.getDefaultImage());
        }
    }

    private void setShootingSprite(int dirX, int dirY, int shootX, int shootY) {
        PlayerAnimation tmp_animation = getAnimation(shootX, shootY);
        if (tmp_animation != null) {
            setSpriteImage(tmp_animation);
        }
        if (!(moving = getAnimation(dirX, dirY) != null)) {
            sprite.setImage(animation.getDefaultImage());
        }
    }

    private void setSpriteImage(PlayerAnimation tmp_animation) {
        if (tmp_animation != animation) {
            tickCounter = animation.getTicksPerFrame() - 3;
            animation = tmp_animation;
            sprite.setImage(animation.getFirstFrame());
        } else if (tickCounter >= animation.getTicksPerFrame()) {
            sprite.setImage(animation.getNextFrame());
            tickCounter = 0;
        }
    }

    private PlayerAnimation getAnimation(int dirX, int dirY) {
        if (dirX == 1) {
            return PlayerAnimation.PLAYER_RIGHT;
        } else if (dirX == -1) {
            return PlayerAnimation.PLAYER_LEFT;
        }
        if (dirY == 1) {
            return PlayerAnimation.PLAYER_DOWN;
        } else if (dirY == -1) {
            return PlayerAnimation.PLAYER_UP;
        }
        return null;
    }

    @Override
    public void decreaseHealth(int damagePoints) {
        if (!invincible) {
            super.decreaseHealth(damagePoints);
            invincible = true;
        }
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

    public Inventory getInventory() {
        return inventory;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public Sprite getHatSprite() {
        return animation.getHatSprite(moving);
    }

    public boolean hasHat() {
        return inventory.hasHat();
    }
}

package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.control.KeysEventHandler;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.model.items.Gun;
import cvut.gartnkry.model.items.Inventory;
import cvut.gartnkry.model.items.PropItem;
import cvut.gartnkry.view.UI;
import cvut.gartnkry.view.assets.PlayerAnimation;
import cvut.gartnkry.view.assets.Sound;
import cvut.gartnkry.view.assets.StepSoundsPlayer;

import java.util.*;

import static cvut.gartnkry.control.Settings.*;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static javafx.scene.input.KeyCode.*;

/**
 * Player class is responsible for:
 * <ul>
 * <li> Keeping player's data (position, velocity, players movement direction, ...)
 * <li> Handling keyboard input (walking and shooting)
 * <li> Animating player's movement (changing sprite images)
 * </ul>
 * Keys for controlling player's movement:
 * <ul>
 * <li> W - forward
 * <li> A - left
 * <li> S - bottom
 * <li> D - right
 * </ul>
 * Keys for controlling shooting from a gun: Left, Right, Up, Down arrow keys
 */
public class Player extends Entity {
    private PlayerAnimation animation;
    private int animationCounter;
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
    private final int maxHealth;
    private int invincibleCounter;
    private int fireRateMax = 100;
    private int fireRateCounter;

    /**
     * Class constructor.
     * Load Player's position, inventory and health from Json data.
     *
     * @param playerData player Json Object from save file
     */
    public Player(JsonObject playerData) {
        super(playerData, null);

        maxHealth = playerData.get("maxHealth").getAsInt();
        if (health > maxHealth) {
            health = maxHealth;
        }

        deathSound = Sound.DEATH;
        damageSound = Sound.GRUNT;
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

    /**
     * Add item to player's inventory and remove prop item from the ground.
     * If the inventory slot is full, drop item in the slot.
     *
     * @param propItem to pick up
     */
    public void pickupItem(PropItem propItem) {
        AppLogger.info(() -> "Picking up item: " + propItem.getName());
        inventory.pickup(propItem);
        Sound.ITEM.play();
    }

    /**
     * Update player's sprite corresponding to keyboard input.
     * Change position according to computed velocity.
     * Handle shooting if Gun is selected in inventory.
     */
    public void update() {
        if (!isDead()) {
            sprite.addXYScaled(velocityX, velocityY);
            invincibleTick();

            ++animationCounter;
            int dirX = KeysEventHandler.getDirection(D, A);
            int dirY = KeysEventHandler.getDirection(S, W);
            int shootX = KeysEventHandler.getDirection(RIGHT, LEFT);
            int shootY = shootX == 0 ? KeysEventHandler.getDirection(DOWN, UP) : 0;
            boolean shooting = inventory.gunSelected() && (shootX != 0 || shootY != 0);
            boolean prevMoving = moving;
            if (shooting) {
                setShootingSprite(dirX, dirY, shootX, shootY);
            } else {
                setWalkingSprite(dirX, dirY);
            }
            handleShooting(shootX, shootY, shooting);

            if (prevMoving != moving) {
                if (moving) {
                    StepSoundsPlayer.getInstance().play();
                } else {
                    StepSoundsPlayer.getInstance().stop();
                }
            }
        }
    }

    /**
     * Handle invincibility state after being hit.
     */
    private void invincibleTick() {
        if (invincible) {
            ++invincibleCounter;
            if (invincibleCounter == invincibleInterval) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    /**
     * Handle interval for shooting and create new bullet if needed.
     *
     * @param shootX   direction of shooting in X axis
     * @param shootY   direction of shooting in Y axis
     * @param shooting bool whether player is shooting right now
     */
    private void handleShooting(int shootX, int shootY, boolean shooting) {
        if (fireRateCounter != fireRateMax) {
            ++fireRateCounter;
        } else if (shooting) {
            AppLogger.finer(() -> "New bullet created.");
            Gun gun = (Gun) inventory.getSelectedItem();
            bullets.add(gun.shoot(shootX, shootY, sprite.getX() + animation.getShootX(), sprite.getY() + animation.getShootY()));
            fireRateMax = gun.getFireRate();
            fireRateCounter = 0;
        }
    }


    /**
     * Compute X and Y velocity from keyboard input and direction of movement.
     */
    public void computeVelocities() {
        double tmpAdd, tmpMaxVel;
        // fixes faster movement sideways -> pythagoras theorem
        if (velocityX != 0 && velocityY != 0) {
            tmpMaxVel = maxSidewaysVel;
            tmpAdd = addSidewaysVel;
        } else {
            tmpMaxVel = maxVel;
            tmpAdd = addVel;
        }
        // compute velocities
        velocityX = getSingleVelocity(KeysEventHandler.getDirection(D, A), velocityX, tmpAdd, tmpMaxVel);
        velocityY = getSingleVelocity(KeysEventHandler.getDirection(S, W), velocityY, tmpAdd, tmpMaxVel);
    }

    /**
     * Compute single velocity.
     */
    private double getSingleVelocity(int direction, double velocity, double tmpAdd, double tmpMaxVel) {
        // accelerate to max velocity
        if (direction != 0) {
            velocity = max(min(tmpMaxVel, velocity + (tmpAdd * direction)), -tmpMaxVel);
        }
        // decelerate to zero
        else if (velocity > 0) {
            velocity = max(velocity - tmpAdd, 0);
        } else if (velocity < 0) {
            velocity = min(velocity + tmpAdd, 0);
        }
        return velocity;
    }


    /**
     * Set sprite image for player - frame from animation or default image.
     */
    private void setWalkingSprite(int dirX, int dirY) {
        PlayerAnimation tmp_animation = getAnimation(dirX, dirY);
        if ((moving = tmp_animation != null)) {
            setSpriteImage(tmp_animation);
        } else {
            sprite.setImage(animation.getDefaultImage());
        }
    }

    /**
     * Set sprite image for player with a gun.
     */
    private void setShootingSprite(int dirX, int dirY, int shootX, int shootY) {
        // Player will be turned in the direction of shooting
        // (even if walking backwards)
        PlayerAnimation tmp_animation = getAnimation(shootX, shootY);
        if (tmp_animation != null) {
            setSpriteImage(tmp_animation);
        }
        if (!(moving = getAnimation(dirX, dirY) != null)) {
            sprite.setImage(animation.getDefaultImage());
        }
    }

    private void setSpriteImage(PlayerAnimation tmp_animation) {
        // Animation changed?
        if (tmp_animation != animation) {
            // Set new animation
            animationCounter = animation.getTicksPerFrame() - 3;
            animation = tmp_animation;
            sprite.setImage(animation.getFirstFrame());
        }
        // Check interval for new frame
        else if (animationCounter >= animation.getTicksPerFrame()) {
            // Set new animation frame
            sprite.setImage(animation.getNextFrame());
            animationCounter = 0;
        }
    }

    /**
     * Get animation from direction of player's movement.
     */
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

    /**
     * Damage player (~decrease his health) if he is not in invincible state.
     *
     * @param damagePoints Amount of points to remove from player's health.
     */
    @Override
    public void damage(int damagePoints) {
        if (!invincible) {
            super.damage(damagePoints);
            invincible = true;
            UI.getInstance().drawHearts(this);
            if (isDead()) {
                Sound.FALL.play();
                StepSoundsPlayer.getInstance().stop();
            }
        }
    }

    /**
     * Heal player (~increase his health).
     * Could from usage of item like Bottle.
     *
     * @param healPoints Amount of points to add to player's health.
     */
    public void heal(int healPoints) {
        health += healPoints;
        if (health > maxHealth) {
            health = maxHealth;
        }
        UI.getInstance().drawHearts(this);
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

    public int getMaxHealth() {
        return maxHealth;
    }
}

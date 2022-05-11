package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.view.assets.Animation;
import cvut.gartnkry.view.assets.AssetsManager;
import cvut.gartnkry.view.assets.Sound;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

import static cvut.gartnkry.control.Settings.SCALE;

/**
 * Void enemy class inheriting from Entity.
 * Void can't change position and stays in the same place.
 * It can be either non activated and invisible or activated and deal damage when stepped on.
 * Void is activated when player is nearby.
 */
public class Void extends Entity {
    private boolean activated;
    private Animation animation;
    private int anCounter;
    private int currentFrame;
    private final Rectangle activateRec;

    /**
     * Class constructor.
     * @param entityData JsonObject with entity data
     */
    public Void(JsonObject entityData) {
        super(entityData, Animation.VOID_OPEN.getFrame(0));
        damage = AssetsManager.getDamage(name);
        activated = entityData.get("activated").getAsBoolean();
        if (activated) {
            animation = Animation.VOID;
        }
        activateRec = new Rectangle(sprite.getX() - 10 * SCALE, sprite.getY() - 10 * SCALE,
                sprite.getImage().getWidth() + 20 * SCALE, sprite.getImage().getHeight() + 20 * SCALE);
        deathSound = Sound.VOID_OPEN;
        damageSound = Sound.ENTITY_HIT;
    }

    /**
     * Set sprite image to void according to the current state and animation.
     */
    @Override
    public void update() {
        if (active && activated) {
            ++anCounter;
            if (anCounter >= animation.getTicksPerFrame()) {
                anCounter = 0;
                if (++currentFrame == animation.getFrameCount()) {
                    currentFrame = 0;
                    // Void fully opened -> set normal animation
                    if (animation == Animation.VOID_OPEN) {
                        animation = Animation.VOID;
                    }
                }
                sprite.setImage(animation.getFrame(currentFrame));
            }
        }
    }

    /**
     * Set activated to true.
     * Void begins to open with an animation and starts to deal damage.
     */
    public void activate() {
        AppLogger.fine(() -> "Void activated.");
        activated = true;
        animation = Animation.VOID_OPEN;
    }

    public boolean isActivated() {
        return activated;
    }

    public Bounds getActivateBounds() {
        return activateRec.getBoundsInParent();
    }
}

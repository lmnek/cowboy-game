package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.Settings;
import cvut.gartnkry.view.assets.Animation;
import cvut.gartnkry.view.assets.AssetsManager;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

import java.util.BitSet;

import static cvut.gartnkry.Settings.SCALE;

public class Void extends Entity {
    private boolean activated;
    private Animation animation;
    private int anCounter;
    private int currentFrame;
    private Rectangle activateRec;

    public Void(JsonObject entityData) {
        super(entityData, Animation.VOID_OPEN.getFrame(0));
        damage = AssetsManager.getDamage(name);
        activated = entityData.get("activated").getAsBoolean();
        activateRec = new Rectangle(sprite.getX() - 10 * SCALE, sprite.getY() - 10 * SCALE,
                sprite.getImage().getWidth() + 20 * SCALE, sprite.getImage().getHeight() + 20 * SCALE);
    }

    @Override
    public void update() {
        if (active && activated) {
            ++anCounter;
            if (anCounter >= animation.getTicksPerFrame()) {
                anCounter = 0;
                if (++currentFrame == animation.getFrameCount()) {
                    currentFrame = 0;
                    if (animation == Animation.VOID_OPEN) {
                        animation = Animation.VOID;
                    }
                }
                sprite.setImage(animation.getFrame(currentFrame));
            }
        }
    }

    public void activate() {
        activated = true;
        animation = Animation.VOID_OPEN;
    }

    @Override
    public void damage(int damagePoints) {
        // invincible
    }

    public boolean isActivated() {
        return activated;
    }

    public Bounds getActivateBounds() {
        return activateRec.getBoundsInParent();
    }

    public Rectangle getRec(){
       return activateRec;
    }
}

package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.Data;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.view.assets.Animation;
import cvut.gartnkry.view.assets.ImageAsset;
import javafx.scene.image.Image;

import java.util.HashMap;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.D;

public class Ghost extends Entity{
    private static final int MAX_HEALTH = 5;

    private Animation animation;

    public Ghost(JsonObject entityData) {
        super(entityData, MAX_HEALTH, Animation.GHOST.getDefaultImage());
        animation = Animation.GHOST;
    }

    @Override
    public void update() {

    }
}

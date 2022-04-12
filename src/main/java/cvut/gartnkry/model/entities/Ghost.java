package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.view.assets.Animation;

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

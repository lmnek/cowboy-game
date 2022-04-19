package cvut.gartnkry.model.entities;

import com.google.gson.JsonObject;
import cvut.gartnkry.view.assets.Animation;

public class Ghost extends Entity{
    private static final int MAX_HEALTH = 5;

    public Ghost(JsonObject entityData) {
        super(entityData, Animation.GHOST.getDefaultImage());
        animation = Animation.GHOST;
    }

    public void update() {

    }
}

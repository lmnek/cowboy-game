package cvut.gartnkry.model.entities;

import cvut.gartnkry.Data;
import cvut.gartnkry.model.Sprite;

public  class Entity {
    private final Sprite sprite;

    public Entity(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

}

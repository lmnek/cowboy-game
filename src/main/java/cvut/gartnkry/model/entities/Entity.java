package cvut.gartnkry.model.entities;

import cvut.gartnkry.model.Sprite;

/**
 *  Parent class for inherited entities.
 *  Such entities could be player or enemies.
 */
public  class Entity {
    protected Sprite sprite;

    /**
     * Class constructor.
     * @param sprite sprite of the entity
     */
    public Entity(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * @return Sprite object containing image and entities coordinates
     */
    public Sprite getSprite() {
        return sprite;
    }

}

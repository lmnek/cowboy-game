package cvut.gartnkry.control.collisions;

/**
 * Class for saving data of a hitbox.
 * Hitbox can be:
 * <ul>
 * <li> walking hitbox - for collided with and blocking player's movement
 * <li> entity hitbox - mainly used for combat
 * </ul>
 */
public class HitboxInfo {
    // offset from top right corner of a sprite
    private final int x_offset;
    private final int y_offset;
    // size of the hitbox
    private final int width;
    private final int height;

    /**
     * Class constructor.
     */
    public HitboxInfo(int x_offset, int y_offset, int width, int height) {
        this.x_offset = x_offset;
        this.y_offset = y_offset;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x_offset;
    }

    public int getY() {
        return y_offset;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

package cvut.gartnkry.control.collisions;

public class HitboxInfo {
    private final int x_offset;
    private final int y_offset;
    private final int width;
    private final int height;

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

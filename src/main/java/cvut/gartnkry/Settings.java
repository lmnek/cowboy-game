package cvut.gartnkry;

public class Settings {

    private Settings(){}

    public static final String TITLE = "RPG";

    public static final int SCALE = 6; // scale sprites
    public static final int TILES_COUNT_WIDTH = 16;
    public static final int TILES_COUNT_HEIGHT = 9;
    public static final String ASSETS_FILE_FORMAT = ".png";

    public static final long LOOP_INTERVAL = 10_000_000; // in nanoseconds

    public static final double PLAYER_MAX_VELOCITY = 0.2 * SCALE;
    public static final int PLAYER_TICKS_TO_ACCELERATE = 10;


    public static final boolean DRAW_HITBOXES = false;
    public static final double HITBOX_PADDING = 0.01 * SCALE;
}

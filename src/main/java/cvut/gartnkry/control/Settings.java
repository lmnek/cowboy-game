package cvut.gartnkry.control;

import java.util.logging.Level;

public class Settings {
    public static final String SAVE_FILENAME = "save3.json";

    public static final Level logLevel = Level.ALL;
    public static final Level consoleLogLevel = Level.FINE;
    public static final Level fileLogLevel = Level.INFO;

    public static final String TITLE = "RPG";

    public static final int SCALE = 6; // scale sprites
    public static final int TILES_COUNT_WIDTH = 16;
    public static final int TILES_COUNT_HEIGHT = 9;
    public static final String ASSETS_FILE_FORMAT = ".png";

    public static final long LOOP_INTERVAL = 10_000_000; // in nanoseconds

    public static final double PLAYER_MAX_VELOCITY = 1.2;
    public static final int PLAYER_TICKS_TO_ACCELERATE = 10;
    public static final int INVENTORY_SIZE = 5;


    public static final boolean DRAW_HITBOXES = false;
    public static final double HITBOX_PADDING = 0.01 * SCALE;

    public static final int CACTUS_DAMAGE = 1;
    public static final double MAX_GHOST_VELOCITY = SCALE / 2;
}

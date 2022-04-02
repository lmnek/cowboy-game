package cvut.gartnkry;

public class Settings {

    private Settings(){}

    public static final String TITLE = "RPG";

    public static final int SCALE = 4; // scale sprites
    public static final int TILES_COUNT_WIDTH = 15;
    public static final int TILES_COUNT_HEIGHT = 10;
    public static final String ASSETS_FILE_FORMAT = ".png";

    public static final long LOOP_INTERVAL = 10_000_000; // in nanoseconds
    public static final double PLAYER_SPEED = 1;
}

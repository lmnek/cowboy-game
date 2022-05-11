package cvut.gartnkry.view.assets;

import cvut.gartnkry.control.files.ResourcesUtils;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.model.items.Hat;
import javafx.scene.image.Image;

import java.util.HashMap;

import static cvut.gartnkry.control.Settings.SCALE;

/**
 * Enum for storing and handling player movement animation.
 * Player animation can be in four direction and with or without a gun.
 */
public enum PlayerAnimation {
    PLAYER_DOWN("forward", 5, 9, 4, 10),
    PLAYER_UP("backwards", 7, 6, 10, 7),
    PLAYER_LEFT("left", 4, 10, -1, 9),
    PLAYER_RIGHT("right", 4, 10, 12, 9);

    private final int frameCount;
    private final int ticksPerFrame;
    private final int shootX;
    private final int shootY;

    private final HashMap<Boolean, Frame[]> frames;
    private final HashMap<Boolean, Frame> defaultFrame;
    private int currentFrame;

    private static boolean gunSelected = false;

    /**
     * Inner private class for storing Image and whether the player
     * in this is image is lower or not (used for rendering hat on players head).
     */
    private static class Frame {
        public final Image image;
        public final boolean lower;

        private Frame(String path) {
            image = ResourcesUtils.loadAsset(path);
            lower = image.getPixelReader().getArgb((int) (image.getWidth() / 2), 0) == 0;
        }
    }

    /**
     * Enum constructor.
     * @param filename prefix of the file in the resources/Player/ folder
     * @param frameCount how many frames are in this animation
     * @param ticksPerFrame speed of the animation
     * @param shootX X coordinates of the gun barrel
     * @param shootY Y coordinated of the gun barrel
     */
    PlayerAnimation(String filename, int frameCount, int ticksPerFrame, int shootX, int shootY) {
        this.frameCount = frameCount;
        this.ticksPerFrame = ticksPerFrame;
        this.shootX = shootX * SCALE;
        this.shootY = shootY * SCALE;
        frames = new HashMap<>();
        frames.put(true, loadFrames("Player/player_gun_" + filename));
        frames.put(false, loadFrames("Player/player_" + filename));
        defaultFrame = new HashMap<>();
        defaultFrame.put(true, new Frame("Player/player_gun_" + filename + "_default"));
        defaultFrame.put(false, new Frame("Player/player_" + filename + "_default"));
        currentFrame = 0;
    }

    /**
     * Load all frames from given the path.
     */
    private Frame[] loadFrames(String path) {
        Frame[] tmpFrames = new Frame[frameCount];
        for (int i = 0; i < frameCount; i++) {
            tmpFrames[i] = new Frame(path + (i + 1));
        }
        return tmpFrames;
    }


    /**
     * @return Image of the next frame
     */
    public Image getNextFrame() {
        if (++currentFrame == frameCount) {
            currentFrame = 0;
        }
        return frames.get(gunSelected)[currentFrame].image;
    }

    /**
     * Get hat sprite for rendering on top of the player.
     * @param moving is the player moving?
     * @return Hat sprite with the correct coordinates
     */
    public Sprite getHatSprite(boolean moving) {
        int y = -2 + (((moving && frames.get(gunSelected)[currentFrame].lower) || (!moving && defaultFrame.get(gunSelected).lower)) ? 1 : 0);
        if (this == PlayerAnimation.PLAYER_DOWN || this == PlayerAnimation.PLAYER_UP) {
            return new Sprite(Hat.FRONT, -0.5, y);
        }
        return new Sprite(Hat.SIDE, 1, y);
    }

    public int getTicksPerFrame() {
        return ticksPerFrame;
    }

    public static void setGunSelected(boolean _gunSelected) {
        gunSelected = _gunSelected;
    }

    public Image getDefaultImage() {
        return defaultFrame.get(gunSelected).image;
    }

    public Image getFirstFrame() {
        return frames.get(gunSelected)[(currentFrame = 0)].image;
    }

    public int getShootX() {
        return shootX;
    }

    public int getShootY() {
        return shootY;
    }
}

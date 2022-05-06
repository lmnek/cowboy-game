package cvut.gartnkry.view.assets;

import cvut.gartnkry.control.ResourcesUtils;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.model.items.Hat;
import cvut.gartnkry.model.items.Item;
import javafx.scene.image.Image;

import java.util.HashMap;


public enum PlayerAnimation {
    PLAYER_DOWN("forward", 5, 9, new int[]{3, 4}),
    PLAYER_UP("backwards", 7, 6, new int[]{5, 6}),
    PLAYER_LEFT("left", 4, 10, new int[]{2, 3}),
    PLAYER_RIGHT("right", 4, 10, new int[]{2, 3});

    // GHOST("Enemies", "ghost", 7, 5);

    private static boolean gunSelected = false;

    private final int frameCount;
    private final int ticksPerFrame;
    private final int[] lowerFrames;

    private final HashMap<Boolean, Frame[]> frames;
    private final HashMap<Boolean, Frame> defaultFrame;
    private int currentFrame;


    private class Frame {
        public final Image image;
        public final boolean lower;

        private Frame(String path) {
            image = ResourcesUtils.loadAsset(path);
            lower = image.getPixelReader().getArgb((int) (image.getWidth() / 2), 0) == 0;
            System.out.println(path + " " + lower);
        }
    }

    PlayerAnimation(String filename, int frameCount, int ticksPerFrame, int[] lowerFrames) {
        this.frameCount = frameCount;
        this.ticksPerFrame = ticksPerFrame;
        this.lowerFrames = lowerFrames;
        frames = new HashMap<>();
        frames.put(true, loadFrames("Player/player_gun_" + filename));
        frames.put(false, loadFrames("Player/player_" + filename));
        defaultFrame = new HashMap<>();
        defaultFrame.put(true, new Frame("Player/player_gun_" + filename + "_default"));
        defaultFrame.put(false, new Frame("Player/player_" + filename + "_default"));
        currentFrame = 0;
    }


    private Frame[] loadFrames(String path) {
        Frame[] tmpFrames = new Frame[frameCount];
        for (int i = 0; i < frameCount; i++) {
            tmpFrames[i] = new Frame(path + (i + 1));
        }
        return tmpFrames;
    }

    public static void setGunSelected(Item item) {
        gunSelected = item != null && item.getName().equals("Gun");
    }

    public Image getCurrentFrame() {
        return frames.get(gunSelected)[currentFrame].image;
    }

    public Image getDefaultImage() {
        return defaultFrame.get(gunSelected).image;
    }

    public Image getFirstFrame() {
        return frames.get(gunSelected)[(currentFrame = 0)].image;
    }

    public Image getNextFrame() {
        if (++currentFrame == frameCount) {
            currentFrame = 0;
        }
        return frames.get(gunSelected)[currentFrame].image;
    }

    public int getTicksPerFrame() {
        return ticksPerFrame;
    }

    public Sprite getHatSprite(boolean moving) {
        int y = -2 + (((moving && frames.get(gunSelected)[currentFrame].lower) || (!moving && defaultFrame.get(gunSelected).lower)) ? 1 : 0);
        if (this == PlayerAnimation.PLAYER_DOWN || this == PlayerAnimation.PLAYER_UP) {
            return new Sprite(Hat.FRONT, -0.5, y);
        }
        return new Sprite(Hat.SIDE, 1, y);
    }
}

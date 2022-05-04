package cvut.gartnkry.view.assets;

import cvut.gartnkry.control.ResourcesUtils;
import javafx.scene.image.Image;


public enum PlayerAnimation {
    PLAYER_DOWN("forward", 5, 9),
    PLAYER_UP("backwards", 7, 6),
    PLAYER_LEFT("left", 4, 10),
    PLAYER_RIGHT("right", 4, 10);

    // GHOST("Enemies", "ghost", 7, 5);

    private static boolean gunSelected = false;

    private final int frameCount;
    private final int ticksPerFrame;

    private final Image[] noGunframes;
    private final Image[] gunFrames;
    private final Image noGunDefaultImage;
    private final Image gunDefaultImage;
    private int currentFrame;

    PlayerAnimation(String filename, int frameCount, int ticksPerFrame) {
        this.frameCount = frameCount;
        this.ticksPerFrame = ticksPerFrame;
        noGunDefaultImage = ResourcesUtils.loadAsset("Player/player_" + filename + "_default");
        gunDefaultImage = ResourcesUtils.loadAsset("Player/player_gun_" + filename + "_default");
        noGunframes = loadAnimation("Player/player_" + filename);
        gunFrames = loadAnimation("Player/player_gun_" + filename);
        currentFrame = 0;
    }

    private Image[] loadAnimation(String path) {
        Image[] tmpFrames = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            tmpFrames[i] = ResourcesUtils.loadAsset(path + (i + 1));
        }
        return tmpFrames;
    }

    public static void setGunSelected(boolean _gunSelected) {
        gunSelected = _gunSelected;
    }

    public Image getDefaultImage() {
        return gunSelected ? gunDefaultImage : noGunDefaultImage;
    }

    public Image getCurrentFrame() {
        return getFrames(gunSelected)[currentFrame];
    }

    public Image getFirstFrame() {
        return getFrames(gunSelected)[(currentFrame = 0)];
    }

    public Image getNextFrame() {
        Image image = getFrames(gunSelected)[currentFrame];
        currentFrame++;
        if (currentFrame == frameCount) {
            currentFrame = 0;
        }
        return image;
    }

    public int getTicksPerFrame() {
        return ticksPerFrame;
    }

    private Image[] getFrames(boolean hasGun) {
        return hasGun ? gunFrames : noGunframes;
    }
}

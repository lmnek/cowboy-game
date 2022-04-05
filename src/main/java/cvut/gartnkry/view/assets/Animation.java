package cvut.gartnkry.view.assets;

import javafx.scene.image.Image;


public enum Animation {
    PLAYER_DOWN("Player", "player_forward", 5, 9),
    PLAYER_UP("Player", "player_backwards", 7, 6),
    PLAYER_LEFT("Player", "player_left", 4, 10),
    PLAYER_RIGHT("Player", "player_right", 4, 10);

    private final int frameCount;
    private final int ticksPerFrame;

    private final Image[] frames;
    private int currentFrame;

    Animation(String folder, String filename, int frameCount, int ticksPerFrame) {
        this.frameCount = frameCount;
        this.ticksPerFrame = ticksPerFrame;
        frames = new Image[frameCount];
        currentFrame = 0;

        // load animation
        for (int i = 0; i < frameCount; i++) {
            frames[i] = AssetsUtils.loadAsset(folder, filename + (i + 1));
        }
    }

    public Image getCurrentFrame() {
        return frames[currentFrame];
    }

    public Image getFirstFrame(){
        return frames[(currentFrame = 0)];
    }

    public Image getNextFrame() {
        Image image = frames[currentFrame];
        currentFrame++;
        if (currentFrame == frameCount) {
            currentFrame = 0;
        }
        return image;
    }

    public int getTicksPerFrame() {
        return ticksPerFrame;
    }
}

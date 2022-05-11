package cvut.gartnkry.view.assets;

import cvut.gartnkry.control.files.ResourcesUtils;
import javafx.scene.image.Image;

/**
 * Enum for storing, loading and managing animations.
 */
public enum Animation {
    GHOST("Entities", "ghost", 7, 5),
    VOID("Entities", "void", 3, 7),
    VOID_OPEN("Entities", "void_open", 25, 4);

    private final int frameCount;
    private final int ticksPerFrame;
    private final Image[] frames;

    /**
     * Enum constructor - for init and loading the frames.
     * @param folder Folder of the frames
     * @param filename Prefix of the frame images
     * @param frameCount how many frames in this animation
     * @param ticksPerFrame speed of the animation
     */
    Animation(String folder, String filename, int frameCount, int ticksPerFrame) {
        this.frameCount = frameCount;
        this.ticksPerFrame = ticksPerFrame;
        frames = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = ResourcesUtils.loadAsset(folder + "/" + filename + (i + 1));
        }
    }

    public Image getFrame(int index) {
        return frames[index];
    }

    public int getTicksPerFrame() {
        return ticksPerFrame;
    }

    public int getFrameCount() {
        return frames.length;
    }

}


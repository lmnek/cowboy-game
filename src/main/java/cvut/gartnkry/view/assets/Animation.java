package cvut.gartnkry.view.assets;

import cvut.gartnkry.control.ResourcesUtils;
import javafx.scene.image.Image;


public enum Animation {
    GHOST("Entities", "ghost", 7, 5),
    VOID("Entities", "void", 3, 7),
    VOID_OPEN("Entities", "void_open", 25, 2);

    private final int frameCount;
    private final int ticksPerFrame;
    private Image[] frames;

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


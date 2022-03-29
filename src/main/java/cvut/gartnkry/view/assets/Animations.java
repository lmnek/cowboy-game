package cvut.gartnkry.view.assets;

import javafx.scene.image.Image;

public enum Animations {
    PLAYER_DOWN("Player", "player_forward", 4, 10),
    PLAYER_UP("Player", "player_no_gun", 1, 15),
    PLAYER_LEFT("Player", "player_left", 2, 15),
    PLAYER_RIGHT("Player", "player_right", 2, 15);;

    String folder;
    String filename;
    int frameCount;
    int ticksPerFrame;

    private Image[] frames;
    private int currentFrame;

    private Animations(String folder,String filename, int frameCount, int ticksPerFrame) {
        this.folder = folder;
        this.filename = filename;
        this.frameCount = frameCount;
        this.ticksPerFrame = ticksPerFrame;
        frames = new Image[frameCount];
        currentFrame = 0;
    }

    public void loadAnimation() {
        for (int i = 0; i < frameCount; i++) {
            frames[i] = AssetsUtils.loadAsset(folder, filename + (i + 1));
        }
    }

    public Image getFrame() {
        return frames[currentFrame];
    }

    public Image getNextFrame() {
        Image image = frames[currentFrame];
        currentFrame++;
        if(currentFrame == frameCount){
            currentFrame = 0;
        }
        return image;
    }

    public int getTicksPerFrame(){
        return ticksPerFrame;
    }
}

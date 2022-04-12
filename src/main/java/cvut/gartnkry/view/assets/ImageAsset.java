package cvut.gartnkry.view.assets;

import cvut.gartnkry.ResourcesUtils;
import javafx.scene.image.Image;

/**
 * Enum for storing images, that are being used by entities, props and items.
 * </p>
 * Every constant has folder and filename attribute.
 * At the start of loading the game, all images are loaded from these attributes.
 * Images are also being scaled to current window size.
 */
public enum ImageAsset {
    CACTUS("Props", "cactus"),
    SKULL("Props", "skull"),
    CROSS("Props", "cross"),
    TREE("Props", "tree")
    // GUNS("Items", )
    ;

    // inventory items...

    private final Image image;

    /**
     * Enum constructor.
     * Load scaled image from /resources + folder + filename.
     * @param folder path to folder with the image
     * @param filename filename of the image
     */
    ImageAsset(String folder, String filename) {
        image = ResourcesUtils.loadAsset(folder, filename); // load image
    }

    /**
     * @return scaled image
     */
    public Image getImage() {
        return image;
    }
    
    public static ImageAsset getFromName(String name){
        for (ImageAsset ia :
                ImageAsset.values()) {
            if(ia.name().toLowerCase().equals(name)){
                return ia;
            }
        }
        return null;
    }
}

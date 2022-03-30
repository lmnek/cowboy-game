package cvut.gartnkry.view.assets;

import cvut.gartnkry.Settings;
import javafx.scene.image.Image;

/**
 *  Set of static methods that can be used by classes working with resources/assets
 */
public class AssetsUtils {
    /**
     * Load, scale and return given image/asset
     * @param path path of the image in resources folder
     * @param filename filename of the image
     * @return Instance of loaded Image
     */
    public static Image loadAsset( String path, String filename) {
        String fullpath = "/" + (path == "" ? "" : path + "/") + filename + Settings.ASSETS_FILE_FORMAT; // build relative path
        String url = AssetsUtils.class.getResource(fullpath).toString(); // get url
        // TODO: find a better way to scale image + smooth true/false ?
        // load image and scale it
        Image unscaled_image = new Image(url);
        return new Image(url, unscaled_image.getWidth() * Settings.SCALE, unscaled_image.getHeight() * Settings.SCALE, false, false);
    }
}

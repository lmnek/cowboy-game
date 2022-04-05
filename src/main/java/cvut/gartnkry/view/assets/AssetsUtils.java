package cvut.gartnkry.view.assets;

import cvut.gartnkry.Settings;
import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *  Set of static methods that can be used by classes working with resources/assets.
 */
public class AssetsUtils {
    /**
     * Load, scale and return given image/asset.
     * @param folder path of the image in resources folder
     * @param filename filename of the image
     * @return Instance of loaded Image
     */
    public static Image loadAsset( String folder, String filename) {
        String fullpath = "/" + (folder.isEmpty() ? "" : folder + "/") + filename + Settings.ASSETS_FILE_FORMAT; // build relative path
        String url = AssetsUtils.class.getResource(fullpath).toString(); // get url
        // TODO: find a better way to scale image + add try catch
        // load image and scale it
        Image unscaled_image = new Image(url);
        return new Image(url, unscaled_image.getWidth() * Settings.SCALE, unscaled_image.getHeight() * Settings.SCALE, false, true);
    }

    /**
     * @param path path in resources to the folder to read
     * @return Buffered Reading in given folder
     */
    public static BufferedReader getResourcesReader(String path){
        System.out.println(path);
       return new BufferedReader(new InputStreamReader(
                AssetsUtils.class.getResourceAsStream(path)));
    }
}

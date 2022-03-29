package cvut.gartnkry.view.assets;

import cvut.gartnkry.Settings;
import javafx.scene.image.Image;

public class AssetsUtils {

    public static Image loadAsset( String path, String filename) {
        // build relative path
        String fullpath = "/" + (path == "" ? "" : path + "/") + filename + Settings.ASSETS_FILE_FORMAT;
        // get url
        String url = AssetsUtils.class.getResource(fullpath).toString();
        // TODO: find a better way to scale image + smooth true/false ?
        // load image and scale it
        Image unscaled_image = new Image(url);
        return new Image(url, unscaled_image.getWidth() * Settings.SCALE, unscaled_image.getHeight() * Settings.SCALE, false, false);
    }
}

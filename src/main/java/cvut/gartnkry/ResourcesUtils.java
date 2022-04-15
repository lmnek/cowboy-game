package cvut.gartnkry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  Set of static methods that can be used by classes working with resources/assets.
 */
public class ResourcesUtils {
    /**
     * Load, scale and return given image/asset.
     * @param folder path of the image in resources folder
     * @param filename filename of the image
     * @return Instance of loaded Image
     */
    public static Image loadAsset( String folder, String filename) {
        String fullpath = "/" + (folder.isEmpty() ? "" : folder + "/") + filename + Settings.ASSETS_FILE_FORMAT; // build relative path
        String url = ResourcesUtils.class.getResource(fullpath).toString(); // get url
        // TODO: find a better way to scale image + add try catch
        // load image and scale it
        Image unscaled_image = new Image(url);
        return new Image(url, unscaled_image.getWidth() * Settings.SCALE, unscaled_image.getHeight() * Settings.SCALE, false, true);
    }

    /**
     * @param path path in resources to the folder to read
     * @return Buffered Reading in given folder
     */
    public static BufferedReader getReader(String path){
       return new BufferedReader(new InputStreamReader(
                ResourcesUtils.class.getResourceAsStream("/" + path)));
    }

    public static JsonElement readJsonFile(String path){
        JsonParser parser = new JsonParser();
        JsonElement returnElement = null;
        try(BufferedReader br = ResourcesUtils.getReader(path)){
            returnElement = parser.parse(br);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return returnElement;
    }

    public static Point2D pointFromJson(JsonObject json){
        return new Point2D(json.get("coordX").getAsDouble(),
                json.get("coordY").getAsDouble());
    }

}

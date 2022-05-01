package cvut.gartnkry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Set of static methods that can be used by classes working with resources/assets.
 */
public class ResourcesUtils {
    /**
     * Load, scale and return given image/asset.
     *
     * @return Instance of loaded Image
     */
    public static Image loadAsset(String path) {
        return loadAsset(path, 1);
    }

    public static Image loadAsset(String path, double scale) {
        String fullpath = "/" + path + Settings.ASSETS_FILE_FORMAT; // build relative path
        String url = ResourcesUtils.class.getResource(fullpath).toString(); // get url
        // TODO: find a better way to scale image + add try catch
        // load image and scale it
        Image unscaled_image = new Image(url);
        return new Image(url, unscaled_image.getWidth() * Settings.SCALE * scale,
                unscaled_image.getHeight() * Settings.SCALE * scale, false, true);
    }

    /**
     * @param path path in resources to the folder to read
     * @return Buffered Reading in given folder
     */
    public static BufferedReader getReader(String path) {
        return new BufferedReader(new InputStreamReader(
                ResourcesUtils.class.getResourceAsStream("/" + path)));
    }

    public static JsonElement readJsonFile(String path) {
        JsonParser parser = new JsonParser();
        JsonElement returnElement = null;
        try (BufferedReader br = ResourcesUtils.getReader(path)) {
            returnElement = parser.parse(br);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnElement;
    }

    public static Object loadReflection(JsonObject json, String packageName) {
        if (packageName != "") {
            packageName += ".";
        }
        try {
            Constructor itemConstructor = Class.forName(
                            "cvut.gartnkry.model." + packageName + json.get("name").getAsString())
                    .getConstructor(JsonObject.class);
            Object[] parameters = {json};
            return itemConstructor.newInstance(parameters);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}

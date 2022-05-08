package cvut.gartnkry.control;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cvut.gartnkry.AppController;
import cvut.gartnkry.Settings;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 * Set of static methods that can be used by classes working with resources/assets.
 */
public class ResourcesUtils {

    private static final Logger LOG = Logger.getLogger(AppController.class.getName());

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
        LOG.info("Loading asset: " + fullpath);
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
        String path = "cvut.gartnkry.model." + packageName + json.get("name").getAsString();
        LOG.info("Loading object with reflection: " + path);
        try {
            Constructor constructor = Class.forName(path).getConstructor(JsonObject.class);
            Object[] parameters = {json};
            return constructor.newInstance(parameters);
        } catch (Exception e) {
            LOG.severe("Reflection failed.");
            e.printStackTrace();
        }
        return null;
    }

    public static Font loadFont(String fontName, int size) {
        Font font = null;
        InputStream fontStream = ResourcesUtils.class.getResourceAsStream("/Fonts/" + fontName);
        if (fontStream != null) {
            font = Font.loadFont(fontStream, size);
            try {
                fontStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return font;
    }
}

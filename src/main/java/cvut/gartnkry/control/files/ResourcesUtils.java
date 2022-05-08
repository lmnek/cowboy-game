package cvut.gartnkry.control.files;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.control.Settings;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Objects;

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
        AppLogger.fine(() -> "Loading asset: " + fullpath);
        String url = Objects.requireNonNull(ResourcesUtils.class.getResource(fullpath)).toString(); // get url
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
                Objects.requireNonNull(ResourcesUtils.class.getResourceAsStream("/" + path))));
    }

    public static JsonElement readJsonFile(String path) {
        AppLogger.fine(() -> "Loading JSON file: " + path);
        JsonParser parser = new JsonParser();
        JsonElement returnElement = null;
        try (BufferedReader br = ResourcesUtils.getReader(path)) {
            returnElement = parser.parse(br);
        } catch (IOException e) {
            AppLogger.severe(() -> "Loading JSON file failed: " + e.getMessage());
        }
        return returnElement;
    }

    public static Object loadReflection(JsonObject json, String packageName) {
        if (!packageName.equals("")) {
            packageName += ".";
        }
        String path = "cvut.gartnkry.model." + packageName + json.get("name").getAsString();
        AppLogger.fine(() -> "Loading object with reflection: " + path);
        try {
            Constructor<?> constructor = Class.forName(path).getConstructor(JsonObject.class);
            Object[] parameters = {json};
            return constructor.newInstance(parameters);
        } catch (Exception e) {
            AppLogger.severe(() -> "Reflection failed: " + e.getMessage());
        }
        return null;
    }

    public static Font loadFont(String fontName, int size) {
        AppLogger.fine(() -> "Loading Font: " + fontName);
        Font font = null;
        InputStream fontStream = ResourcesUtils.class.getResourceAsStream("/Fonts/" + fontName);
        if (fontStream != null) {
            font = Font.loadFont(fontStream, size);
            try {
                fontStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AppLogger.info(() -> "Font failed to load.");
        }
        return font;
    }
}

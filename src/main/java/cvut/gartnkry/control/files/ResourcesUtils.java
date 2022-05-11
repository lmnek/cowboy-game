package cvut.gartnkry.control.files;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cvut.gartnkry.control.AppLogger;
import cvut.gartnkry.control.Settings;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.Objects;

/**
 * Set of static methods that can be used for working with files/assets in resources.
 */
public class ResourcesUtils {

    /**
     * Load and return given image.
     * @param path path in resources to the file
     * @return Instance of loaded Image
     */
    public static Image loadAsset(String path) {
        return loadAsset(path, 1);
    }

    /**
     * Load, scale and return given image.
     * @param path path in resources to the file
     * @param scale scaling for image (1 ~ no scaling)
     * @return Instance of loaded Image
     */
    public static Image loadAsset(String path, double scale) {
        String fullpath = "/" + path + Settings.ASSETS_FILE_FORMAT; // build relative path
        AppLogger.fine(() -> "Loading asset: " + fullpath);
        String url = Objects.requireNonNull(ResourcesUtils.class.getResource(fullpath)).toString(); // get url
        Image unscaled_image = new Image(url);
        return new Image(url, unscaled_image.getWidth() * Settings.SCALE * scale,
                unscaled_image.getHeight() * Settings.SCALE * scale, false, true);
    }

    /**
     * Get reader in resources.
     * @param path path in resources to the file
     * @return Buffered Reader in given file
     */
    public static BufferedReader getReader(String path) {
        return new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(ResourcesUtils.class.getResourceAsStream("/" + path))));
    }

    /**
     * Get Output Stream in resources.
     * @param path path in resources to the file
     * @return Buffered Output Stream in given file
     */
    public static BufferedOutputStream getOutputStream(String path) {
        try {
            return new BufferedOutputStream(new FileOutputStream(
                    (new File(Objects.requireNonNull(ResourcesUtils.class.getResource("/" + path)).toURI()))));
        } catch (Exception e) {
            AppLogger.exception(() -> "Failed to get writer: " + path, e);
        }
        return null;
    }

    /**
     * Open and parse Json file.
     * @param path path in resources to the Json file
     * @return JsonElement of whole Json file
     */
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

    /**
     * Search all classes from json name and initialize it.
     * It is used for props and items.
     * Constructor has json as a parameter.
     * @param json Json of the prop/item
     * @param packageName name of the package in cvut.gartnkry.model
     * @return Initialized object
     */
    public static Object loadReflection(JsonObject json, String packageName) {
        if (!packageName.equals("")) {
            packageName += ".";
        }
        String path = "cvut.gartnkry.model." + packageName + json.get("name").getAsString();
        AppLogger.info(() -> "Loading object with reflection: " + path);
        try {
            Constructor<?> constructor = Class.forName(path).getConstructor(JsonObject.class);
            Object[] parameters = {json};
            return constructor.newInstance(parameters);
        } catch (Exception e) {
            AppLogger.severe(() -> "Reflection failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * Load font.
     * @param fontName name of the font file in resources/Fonts
     * @param size Size of the font
     * @return loaded Font
     */
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

    /**
     * Load sound as Media object.
     * @param filename name of the .wav file in resources/Sounds
     * @return Media object
     */
    public static Media loadMedia(String filename) {
        AppLogger.fine(() -> "Loading sound: " + filename);
        return new Media(Objects.requireNonNull(ResourcesUtils.class.getResource("/Sounds/" + filename)).toExternalForm());
    }
}

package cvut.gartnkry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cvut.gartnkry.control.ResourcesUtils;
import cvut.gartnkry.model.Model;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;


/**
 * Class for loading saves from JSON files
 */
public class Data {
    private JsonObject json;
    private static ObjectMapper objectMapper = new ObjectMapper();

    public Data(String filename) {
        loadSave(filename);
    }

    public static Model loadModel(String s) throws IOException {
        System.out.println(ResourcesUtils.getReader(s).lines().collect(Collectors.joining()));
         //Test test = objectMapper.readValue(ResourcesUtils.getReader("test.json").lines().collect(Collectors.joining()), Test.class);
        return objectMapper.readValue(ResourcesUtils.getReader(s).lines().collect(Collectors.joining()), Model.class);
        //return null;
    }

    private void loadSave(String filename) {
        json = ResourcesUtils.readJsonFile(filename).getAsJsonObject();
    }

    public static void saveSave(Model model) {
        // todo: save...
    }


    public String getMapFilename() {
        return json.get("map").getAsString();
    }

    public JsonArray getArrayData(String dataName) {
        return json.get(dataName).getAsJsonArray();
    }
}

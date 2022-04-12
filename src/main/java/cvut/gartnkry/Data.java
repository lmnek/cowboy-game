package cvut.gartnkry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cvut.gartnkry.model.Model;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *  Class for loading saves from JSON files
 */
public class Data {
    private JsonObject json;

    public Data(String filename) {
        loadFromJSON(filename);
    }

    private void loadFromJSON(String filename) {
        JsonParser parser = new JsonParser();
        BufferedReader br = ResourcesUtils.getReader(filename);
        json = parser.parse(br).getAsJsonObject();
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToJSON(Model model){
        // todo: save...
    }

    public JsonArray getEntitiesData(){
        return json.get("entities").getAsJsonArray();
    }

    public String getMapFilename(){
        return json.get("map").getAsString();
    }

    public JsonArray getPropsData() {return  json.get("props").getAsJsonArray();}
}

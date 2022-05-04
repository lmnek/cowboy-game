package cvut.gartnkry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cvut.gartnkry.control.ResourcesUtils;
import cvut.gartnkry.model.Model;


/**
 *  Class for loading saves from JSON files
 */
public class Data {
    private JsonObject json;

    public Data(String filename) {
        loadSave(filename);
    }

    private void loadSave(String filename) {
        json = ResourcesUtils.readJsonFile(filename).getAsJsonObject();
    }

    public static void saveSave(Model model){
        // todo: save...
    }


    public String getMapFilename(){
        return json.get("map").getAsString();
    }

    public JsonArray getArrayData(String dataName){
        return json.get(dataName).getAsJsonArray();
    }
}

package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;

public class Hat extends Item{
    public Hat(JsonObject json) {
        super(json);
    }

    @Override
    public void parseJson(JsonObject json) {

    }
}

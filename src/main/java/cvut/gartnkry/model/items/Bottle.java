package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;

public class Bottle extends Item{

    public Bottle(JsonObject json) {
        super(json);
    }

    @Override
    public void parseJson(JsonObject json) {

    }
}

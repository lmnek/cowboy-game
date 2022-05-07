package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;

public class Wood extends Item{
    public Wood(JsonObject json) {
        super(json);
    }

    @Override
    public void parseJson(JsonObject json) {

    }

    @Override
    public boolean use() {

        return false;
    }
}

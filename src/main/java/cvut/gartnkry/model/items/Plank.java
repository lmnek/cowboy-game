package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;

public class Plank extends Item{
    public Plank(JsonObject json) {
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

package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.Model;

public class Bottle extends Item{

    public Bottle(JsonObject json) {
        super(json);
    }

    @Override
    public void parseJson(JsonObject json) {

    }

    @Override
    public boolean use() {
        Model.getInstance().getPlayer().heal(1);
        return true;
    }
}

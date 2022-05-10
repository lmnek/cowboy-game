package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.Model;
import cvut.gartnkry.view.assets.Sound;

public class Bottle extends Item {
    private int heal;

    public Bottle(JsonObject json) {
        super(json);
    }

    @Override
    public void parseJson(JsonObject json) {
        heal = json.get("heal").getAsInt();
    }

    @Override
    public boolean use() {
        Model.getInstance().getPlayer().heal(heal);
        Sound.BOTTLE.play();
        return true;
    }

    public int getHeal() {
        return heal;
    }
}

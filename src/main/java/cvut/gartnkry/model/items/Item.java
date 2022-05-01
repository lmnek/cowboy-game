package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.model.Sprite;
import cvut.gartnkry.view.assets.AssetsManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class Item {
    private Sprite sprite;
    private Prop prop;

    public Item(JsonObject json) {
        // pokud existuje pozice nastav ji
        prop = null;
        if(json.get("x") != null){
            prop = new Prop(json);
        }
        parseJson(json);
    }

    abstract public void parseJson(JsonObject json);

    // GET POPIS - mnozstvi, lvl, ...

    //

    public Sprite getSprite() {
        return sprite;
    }

    public Prop getProp() {
        return prop;
    }
}

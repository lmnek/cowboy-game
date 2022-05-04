package cvut.gartnkry.model.items;

import com.google.gson.JsonObject;
import cvut.gartnkry.control.ResourcesUtils;
import cvut.gartnkry.model.Prop;
import cvut.gartnkry.view.assets.AssetsManager;

public class PropItem extends Prop {
    private Item item;

    public PropItem(JsonObject data) {
        super(data);
        item = (Item) ResourcesUtils.loadReflection(data, "items");
    }

    public Item getItem() {
        return item;
    }

    public void setNewItem(Item newItem) {
        item = newItem;
        sprite.setImage(item.getImage());
        hitboxInfo = AssetsManager.getHitboxInfo(item.getName());
    }
}

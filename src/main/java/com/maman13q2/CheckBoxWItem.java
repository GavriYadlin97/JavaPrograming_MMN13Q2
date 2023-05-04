package com.maman13q2;

import javafx.scene.control.CheckBox;

public class CheckBoxWItem extends CheckBox {
    private Item item;

    public CheckBoxWItem(Item item) {
        super(item.getDescription());
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }
}

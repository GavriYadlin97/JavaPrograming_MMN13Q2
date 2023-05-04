package com.maman13q2;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboBoxWItem extends ComboBox<Integer> {
    private Item item;

    public ComboBoxWItem(ObservableList<Integer> list, Item item) {
        super(list);
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }
}

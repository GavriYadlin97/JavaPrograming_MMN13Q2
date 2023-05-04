package com.maman13q2;

public class OrderedItem extends Item {
    private int quantity;

    public OrderedItem(Item item, int quantity) {
        super(item);
        this.quantity = quantity;
    }
}

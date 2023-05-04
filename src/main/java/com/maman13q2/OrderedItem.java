package com.maman13q2;

public class OrderedItem extends Item implements Comparable<OrderedItem> {
    private int quantity;

    public OrderedItem(Item item, int quantity) {
        super(item);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public int compareTo(OrderedItem o) {
        if (super.getDescription().equals(o.getDescription()) && this.quantity == o.getQuantity() && super.getPrice() == o.getPrice() && super.getType() == o.getType())
            return 0;
        return 1;
    }
}

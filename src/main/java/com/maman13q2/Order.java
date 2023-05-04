package com.maman13q2;

import java.util.ArrayList;

public class Order {

    private ArrayList<OrderedItem> order;

    public Order() {
        this.order = new ArrayList<>();
    }

    public boolean addToOrder(Item item, int quantity) {
        return this.order.add(new OrderedItem(item, quantity));
    }

    public boolean removeFromOrder(Item item, int quantity) {
        return this.order.remove(new OrderedItem(item, quantity));
    }
}

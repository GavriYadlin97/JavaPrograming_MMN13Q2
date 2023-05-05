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
        OrderedItem toRemove;
        for (OrderedItem checker : order) {
            if (checker.compareTo(new OrderedItem(item, quantity)) == 0) {
                this.order.remove(checker);
                return true;
            }
        }
        return false;
    }

    public int getTotal() {
        int total = 0;
        for (OrderedItem item : order) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder newStr = new StringBuilder();
        newStr.append("                  Item                  | Price| Quantity| Total\n");
        newStr.append("----------------------------------------------------------------\n");
        for (OrderedItem item : order) {
            newStr.append(String.format("%-40s|%5d$|%9d|%5d$\n", item.getDescription(), item.getPrice(), item.getQuantity(), item.getQuantity() * item.getPrice()));
        }
        newStr.append(String.format("Total:%57d$",this.getTotal()));
        return newStr.toString();
    }
}

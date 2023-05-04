package com.maman13q2;

public class Item {
    private String description;
    private int price;
    private TypeOfItem type;

    public Item(String description, int price, TypeOfItem type) throws IllegalArgumentException {
        if (type == null)
            throw new IllegalArgumentException();
        this.description = description;
        this.price = price;
        this.type = type;
    }

    public Item(Item item) {
        this.type = item.type;
        this.price = item.price;
        this.description = item.description;
    }

    public int getPrice() {
        return this.price;
    }

    public TypeOfItem getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }
}

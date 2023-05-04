package com.maman13q2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class Menu {

    private ArrayList<Item> menu;
    private Iterator<Item> itr;

    public Menu() {
        this.menu = new ArrayList<>();
        itr = null;
    }

    public boolean addToMenu(Item item) {
        return this.menu.add(item);
    }

    public boolean removeFromMenu(Item item) {
        return this.menu.remove(item);
    }

    public Item getNext() {
        if (itr == null) //initialize
            itr = this.menu.iterator();
        if (itr.hasNext())
            return itr.next();
        itr = this.menu.iterator(); //reset
        return null;
    }

}

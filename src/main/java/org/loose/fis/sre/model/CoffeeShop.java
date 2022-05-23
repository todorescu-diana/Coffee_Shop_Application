package org.loose.fis.sre.model;

import org.dizitart.no2.objects.Id;

public class CoffeeShop {
    @Id
    private String name;
    private String owner;
    private CoffeeShopMenuItem[] menuItems;
    private int menuItemsNumber = 0;

    public CoffeeShop(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public CoffeeShop() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoffeeShopMenuItem[] getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(CoffeeShopMenuItem[] arr, int arrLength) {
        menuItems = new CoffeeShopMenuItem[arrLength];
        int count = 0;
        for(CoffeeShopMenuItem item : arr) {
            if(count < 100) menuItems[count++] = item;
        }
    }

    public String getOwner() {return owner;}

    public void setOwner(String owner) {this.owner = owner;}

    public int getMenuItemsNumber() {return this.menuItemsNumber;}
    public void setMenuItemsNumber(int number) {this.menuItemsNumber = number;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoffeeShopMenuItem item = (CoffeeShopMenuItem) o;

        if (name != null ? !name.equals(item.getName()) : item.getName() != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        return result;
    }
}

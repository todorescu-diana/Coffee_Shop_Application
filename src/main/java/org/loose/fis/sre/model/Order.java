package org.loose.fis.sre.model;

import java.util.Objects;

public class Order {
    private CoffeeShopMenuItem[] items;
    private int itemNumber = 0;
    private String orderDate;

    public Order(CoffeeShopMenuItem[] items, String orderDate) {
        this.items = items;
        this.orderDate = orderDate;
    }

    public Order() {
    }

    public CoffeeShopMenuItem[] getItems() {
        return items;
    }

    public void setItems(CoffeeShopMenuItem[] newItems, int arrLength) {
        items = new CoffeeShopMenuItem[arrLength];
        int count = 0;
        for(CoffeeShopMenuItem item : newItems) {
            {items[count++] = item;System.out.println("item: "+ item);}
        }

        itemNumber = arrLength;
    }

    public void addItem(CoffeeShopMenuItem item, Order targetOrder) {
        CoffeeShopMenuItem[] newItems = new CoffeeShopMenuItem[targetOrder.getItemNumber() + 1];
        int count = 0;

        if (targetOrder.getItemNumber() > 0) {
            for (CoffeeShopMenuItem shopItem : targetOrder.getItems()) {
                newItems[count++] = shopItem;
            }
        }

        newItems[count] = item;

        this.setItems(newItems, count+1);
    }

    public void removeItem(CoffeeShopMenuItem item, Order targetOrder) {
        CoffeeShopMenuItem[] newItems = new CoffeeShopMenuItem[targetOrder.getItemNumber() - 1];
        int count = 0;
        int occurenceCount = 0;

        if (targetOrder.getItemNumber() > 0) {
            for (CoffeeShopMenuItem shopItem : targetOrder.getItems()) {
                if(Objects.equals(item.getName(), shopItem.getName()) && occurenceCount == 0) {
                    occurenceCount++;
                }
                else {
                    newItems[count++] = shopItem;
                }
            }
        }

        targetOrder.setItems(newItems, count);
    }

    public int getItemNumber() {
        return this.itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String  getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}

package org.loose.fis.sre.model;

import org.dizitart.no2.objects.Id;

public class CoffeeShopMenuItem {
    @Id
    private String name;
    private String description;
    private String drinkVolume;

    private float price;

    public CoffeeShopMenuItem(String name, String description, String drinkVolume, float price) {
        this.name = name;
        this.description = description;
        this.drinkVolume = drinkVolume;
        this.price = price;
    }

    public CoffeeShopMenuItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDrinkVolume() {
        return drinkVolume;
    }

    public void setDrinkVolume(String drinkVolume) {
        this.drinkVolume = drinkVolume;
    }

    public float getPrice() { return price; }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoffeeShopMenuItem item = (CoffeeShopMenuItem) o;

        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        if (description != null ? !description.equals(item.description) : item.description != null) return false;
        return drinkVolume != null ? drinkVolume.equals(item.drinkVolume) : item.drinkVolume == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ?  description.hashCode() : 0);
        result = 31 * result + (drinkVolume != null ? drinkVolume.hashCode() : 0);
        return result;
    }
}

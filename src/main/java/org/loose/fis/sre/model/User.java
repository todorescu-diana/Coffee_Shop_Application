package org.loose.fis.sre.model;

import org.dizitart.no2.objects.Id;

public class User {
    @Id
    private String username;
    private String password;
    private String role;

    private Order[] orderList;
    private int orderNumber = 0;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password, String role, Order[] orderList) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.orderList = orderList;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Order[] getOrderList() {
        return orderList;
    }

    public void setOrderList(Order[] arr, int arrLength) {
        orderList = new Order[arrLength];
        int count = 0;
        for(Order order : arr) {
            orderList[count++] = order;
        }

        orderNumber++;
    }

    public void addOrderToOrderList(Order order) {
        Order[] newOrders = new Order[this.getOrderNumber() + 1];
        int count = 0;

        if (this.getOrderNumber() > 0) {
            for (Order orderItem : this.orderList) {
                newOrders[count++] = orderItem;
            }
        }

        newOrders[count] = order;

        this.setOrderList(newOrders, count + 1);
    }


    public int getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        return role != null ? role.equals(user.role) : user.role == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}

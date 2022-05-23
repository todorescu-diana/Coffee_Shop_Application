package org.loose.fis.sre.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.loose.fis.sre.model.CoffeeShopMenuItem;
import org.loose.fis.sre.model.Order;
import org.loose.fis.sre.services.CoffeeShopMenuItemService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static org.loose.fis.sre.controllers.LoginController.getCurrentUser;

public class TodaysOrdersController {
    @FXML
    private VBox verticalBoxContainer;

    private String[] distinctItems;

    public void initialize () {

        verticalBoxContainer.setSpacing(30);
        verticalBoxContainer.setStyle("-fx-padding: 20 0 30 3");

        // System.out.println(getCurrentUser().getOrderNumber());
        if(getCurrentUser().getOrderNumber() > 0 ) {
            for(Order order: getCurrentUser().getOrderList()) {
                if(Objects.equals(order.getOrderDate(), new SimpleDateFormat("dd-MM-yyyy").format(new Date()))) createNewOrderContainer(order);
            }
        }
    }

    public void createNewOrderContainer(Order order) {

        int distinctCount = 0;
        distinctItems = new String[order.getItemNumber()];

        HBox newHBox = new HBox(50);
        newHBox.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc; -fx-background-radius: 5px; -fx-padding: 10 10 10 10");

        VBox newVBoxTitles = new VBox(10);
        VBox newVBoxInfo = new VBox(10);

        HBox container = new HBox();

        VBox itemsVBox = new VBox();
        //System.out.println("order items number: " + order.getItemNumber());
        for(CoffeeShopMenuItem item : order.getItems()) {
            boolean isDistinct = true;

            if(distinctCount > 0) {
                for(String itemName : distinctItems) {
                    if(Objects.equals(itemName, item.getName())) isDistinct = false;
                }
            }

            if(isDistinct) {
                int itemCount = 0;

                for (CoffeeShopMenuItem i : order.getItems()) {
                    if (Objects.equals(i.getName(), item.getName())) itemCount++;
                }

                Text itemText = new Text(item.getName() + " x " + String.valueOf(itemCount));

                itemsVBox.getChildren().add(itemText);
                distinctItems[distinctCount++] = item.getName();
            }
        }

        Text priceField = new Text(String.valueOf(order.getOrderPrice()));

        newVBoxTitles.getChildren().addAll(new Text("Items ordered:"), new Text("Order price:"));
        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().addAll(itemsVBox, priceField);

        newHBox.getChildren().addAll(newVBoxTitles, newVBoxInfo);
        container.getChildren().addAll(newHBox);

        verticalBoxContainer.getChildren().add(container);
    }
}
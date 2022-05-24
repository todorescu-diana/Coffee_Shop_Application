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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.loose.fis.sre.model.CoffeeShopMenuItem;
import org.loose.fis.sre.model.Order;
import org.loose.fis.sre.services.CoffeeShopMenuItemService;

import java.io.IOException;
import java.util.Objects;

import static org.loose.fis.sre.controllers.LoginController.getCurrentUser;

public class OrderListClientController {
    @FXML
    private VBox verticalBoxContainer;

    private String[] distinctItems;

    public void initialize () {


        verticalBoxContainer.setSpacing(30);
        verticalBoxContainer.setStyle("-fx-padding: 20 0 30 9");

//        System.out.println(getCurrentUser().getOrderNumber());

        if(getCurrentUser().getOrderNumber() > 0 ) {
            for(Order order: getCurrentUser().getOrderList()) {
                createNewOrderContainer(order);
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

        Text coffeeShopNameField = new Text(order.getCoffeeShopName());
        coffeeShopNameField.setFill(Color.web("#ffffcc"));

        VBox itemsVBox = new VBox();
//        System.out.println("order items number: " + order.getItemNumber());
        if(order.getItemNumber() > 0) {
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
                    itemText.setFill(Color.web("#ffffcc"));

                    itemsVBox.getChildren().add(itemText);
                    distinctItems[distinctCount++] = item.getName();
                }
            }
        }

        Text priceField = new Text(String.valueOf(order.getOrderPrice()));
        priceField.setFill(Color.web("#ffffcc"));

        Text nameText = new Text("Coffee Shop Name:");
        nameText.setFill(Color.web("#ffffcc"));
        Text itemsText = new Text("Items ordered:");
        itemsText.setFill(Color.web("#ffffcc"));
        Text priceText = new Text("Order price");
        priceText.setFill(Color.web("#ffffcc"));

        newVBoxTitles.getChildren().addAll(nameText, itemsText, priceText);

        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().addAll(coffeeShopNameField, itemsVBox, priceField);

        newHBox.getChildren().addAll(newVBoxTitles, newVBoxInfo);

        container.getChildren().addAll(newHBox);

        verticalBoxContainer.getChildren().add(container);
    }

    public void handleBackButtonPress(javafx.event.ActionEvent event) throws IOException {
        Stage currentStage = (Stage) verticalBoxContainer.getScene().getWindow();
        currentStage.close();

        Parent coffeeShopMenuClient = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("coffeeShopList.fxml")));
        Scene newScene = new Scene(coffeeShopMenuClient);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(newScene);
        newStage.show();
    }
}
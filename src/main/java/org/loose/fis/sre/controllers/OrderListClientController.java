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

        HBox newHBox = new HBox();
        AnchorPane newPanelContent = new AnchorPane();

        VBox newVBoxTitles = new VBox();
        VBox newVBoxInfo = new VBox();

        Text coffeeShopNameField = new Text(order.getCoffeeShopName());

        VBox itemsVBox = new VBox();
//        System.out.println("order items number: " + order.getItemNumber());
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

        newVBoxTitles.getChildren().addAll(new Text("Coffee Shop Name:"), new Text("Items ordered:"), new Text("Order price:"));
        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().addAll(coffeeShopNameField, itemsVBox, priceField);

        newPanelContent.getChildren().addAll(newVBoxTitles, newVBoxInfo);
        TitledPane newTitledPane = new TitledPane(order.getCoffeeShopName(), newPanelContent);
        newTitledPane.setPrefWidth(241);
        newTitledPane.setPrefHeight(200);
        newTitledPane.expandedProperty().addListener((observable, wasExpanded, isExpanded) ->
                newTitledPane.setMinHeight(isExpanded ? 200 : Region.USE_PREF_SIZE));

        newHBox.getChildren().addAll(newTitledPane);

        verticalBoxContainer.getChildren().add(newHBox);
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
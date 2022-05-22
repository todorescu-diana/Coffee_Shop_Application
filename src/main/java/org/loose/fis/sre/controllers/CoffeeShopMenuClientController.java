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
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.model.CoffeeShop;
import org.loose.fis.sre.model.CoffeeShopMenuItem;
import org.loose.fis.sre.model.Order;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static org.loose.fis.sre.services.CoffeeShopService.getCoffeeShopsRepository;

public class CoffeeShopMenuClientController {
    @FXML
    private VBox verticalBoxContainer;

    @FXML
    private HBox hContainer;

    private static CoffeeShop currentCoffeeShop;

    private static Order currentOrder;

    @FXML
    private Button goToCheckoutButton;


    public void initialize () {
        String selectedCoffeeShop = CoffeeShopListController.getSelectedCoffeeShopName();
        ObjectRepository<CoffeeShop> coffeeShopsRepository = getCoffeeShopsRepository();

        int itemIndex = 0;

        for(CoffeeShop shop : coffeeShopsRepository.find()) {
            if(Objects.equals(shop.getName(), selectedCoffeeShop)) {
                currentCoffeeShop = shop;
//                for(CoffeeShopMenuItem item : currentCoffeeShop.getMenuItems()) {
//                    System.out.println(item.getPrice());
//                }
            }
        }

        if(currentCoffeeShop.getMenuItemsNumber() > 0) {
            for (CoffeeShopMenuItem item : currentCoffeeShop.getMenuItems()) {
                createNewItemContainer(item.getName(), item.getDescription(), item.getDrinkVolume(), item.getPrice(), item, itemIndex);
                itemIndex++;
            }
        }

        currentOrder = new Order();
    }

    @FXML
    private void onCheckoutPress (javafx.event.ActionEvent event) throws IOException {
        Stage currentStage = (Stage) verticalBoxContainer.getScene().getWindow();
        currentStage.close();
        Parent checkout = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("checkout.fxml")));

        Scene newScene = new Scene(checkout);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(newScene);
        newStage.show();
    }

    public void createNewItemContainer(String name, String description, String drinkVolume, double price, CoffeeShopMenuItem item, int itemIndex) {
        HBox newHBox = new HBox();
        AnchorPane newPanelContent = new AnchorPane();

        VBox newVBoxTitles = new VBox();
        VBox newVBoxInfo = new VBox();

        Text nameField = new Text(name);
        String nameFieldId = "coffeeNameField" + String.valueOf(itemIndex);
        nameField.setId(nameFieldId);
        Text descriptionField = new Text(description);
        Text drinkVolumeField = new Text(drinkVolume);
        Text priceField = new Text(String.valueOf(price));

        CoffeeShopMenuItem currentItem = item;

        newVBoxTitles.getChildren().addAll(new Text("Name:"), new Text("Description:"), new Text("Drink volume:"), new Text("Price:"));
        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().addAll(nameField, descriptionField, drinkVolumeField, priceField);

        newPanelContent.getChildren().addAll(newVBoxTitles, newVBoxInfo);
        TitledPane newTitledPane = new TitledPane(name, newPanelContent);
        newTitledPane.setPrefWidth(241);
        newTitledPane.setPrefHeight(200);
        newTitledPane.expandedProperty().addListener((observable, wasExpanded, isExpanded) ->
                newTitledPane.setMinHeight(isExpanded ? 200 : Region.USE_PREF_SIZE));

        AtomicInteger itemQuantity = new AtomicInteger();

        Text itemQuantityText = new Text(String.valueOf(itemQuantity.get()));
        Button newDecrementButton = new Button("-");
        String decrementButtonId = "decrementButton" + String.valueOf(itemIndex);
        newDecrementButton.setId(decrementButtonId);
        newDecrementButton.setOnAction((event) -> {
            if(currentOrder.getItemNumber() > 0 && Integer.parseInt(itemQuantityText.getText()) > 0) {
                currentOrder.removeItem(item, currentOrder);
                itemQuantity.getAndDecrement();
                itemQuantityText.setText(String.valueOf(itemQuantity.get()));

            }
        });
        Button newIncrementButton = new Button("+");
        String incrementButtonId = "incrementButton" + String.valueOf(itemIndex);
        newIncrementButton.setId(incrementButtonId);
        newIncrementButton.setOnAction((event) -> {
            currentOrder.addItem(item, currentOrder);
            itemQuantity.getAndIncrement();
            itemQuantityText.setText(String.valueOf(itemQuantity.get()));
        });
        newHBox.getChildren().addAll(newTitledPane, newIncrementButton, newDecrementButton, itemQuantityText);

        verticalBoxContainer.getChildren().add(newHBox);
    }

    public static Order getCurrentOrder() {
        return currentOrder;
    }

    public static CoffeeShop getCurrentCoffeeShop() {
        return currentCoffeeShop;
    }
}

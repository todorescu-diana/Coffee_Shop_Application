package org.loose.fis.sre.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
        verticalBoxContainer.setSpacing(30);
        verticalBoxContainer.setStyle("-fx-padding: 20 0 30 3");

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
        HBox newHBox = new HBox(50);
        newHBox.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc; -fx-background-radius: 5px; -fx-padding: 10 10 10 10");

        VBox newVBoxTitles = new VBox(10);
        VBox newVBoxInfo = new VBox(10);

        HBox container = new HBox(10);

        Text nameField = new Text(name);
        nameField.setFill(Color.web("#ffffcc"));
        String nameFieldId = "coffeeNameField" + String.valueOf(itemIndex);
        nameField.setId(nameFieldId);
        Text descriptionField = new Text(description);
        descriptionField.setFill(Color.web("#ffffcc"));
        Text drinkVolumeField = new Text(drinkVolume);
        drinkVolumeField.setFill(Color.web("#ffffcc"));
        Text priceField = new Text(String.valueOf(price));
        priceField.setFill(Color.web("#ffffcc"));

        CoffeeShopMenuItem currentItem = item;

        Text nameText = new Text("Name:");
        nameText.setFill(Color.web("#ffffcc"));
        Text descriptionText = new Text("Name:");
        descriptionText.setFill(Color.web("#ffffcc"));
        Text drinkVolumeText = new Text("Name:");
        drinkVolumeText.setFill(Color.web("#ffffcc"));
        Text priceText = new Text("Name:");
        priceText.setFill(Color.web("#ffffcc"));

        newVBoxTitles.getChildren().addAll(nameText, descriptionText, drinkVolumeText, priceText);
        newVBoxInfo.getChildren().addAll(nameField, descriptionField, drinkVolumeField, priceField);

        newHBox.getChildren().addAll(newVBoxTitles, newVBoxInfo);

        AtomicInteger itemQuantity = new AtomicInteger();

        Text itemQuantityText = new Text(String.valueOf(itemQuantity.get()));
        Button newDecrementButton = new Button("-");
        newDecrementButton.setShape(new Circle(14));
        newDecrementButton.setMinSize(28, 28);
        newDecrementButton.setMaxSize(28, 28);
        String decrementButtonId = "decrementButton" + String.valueOf(itemIndex);
        newDecrementButton.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc;");
        newDecrementButton.setId(decrementButtonId);
        newDecrementButton.setOnAction((event) -> {
            if(currentOrder.getItemNumber() > 0 && Integer.parseInt(itemQuantityText.getText()) > 0) {
                currentOrder.removeItem(item, currentOrder);
                itemQuantity.getAndDecrement();
                itemQuantityText.setText(String.valueOf(itemQuantity.get()));

            }
        });
        Button newIncrementButton = new Button("+");
        newIncrementButton.setShape(new Circle(14));
        newIncrementButton.setMinSize(28, 28);
        newIncrementButton.setMaxSize(28, 28);
        String incrementButtonId = "incrementButton" + String.valueOf(itemIndex);
        newIncrementButton.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc;");
        newIncrementButton.setId(incrementButtonId);
        newIncrementButton.setOnAction((event) -> {
            currentOrder.addItem(item, currentOrder);
            itemQuantity.getAndIncrement();
            itemQuantityText.setText(String.valueOf(itemQuantity.get()));
        });

        HBox textContainer = new HBox();
        textContainer.setStyle("-fx-padding: 5 0 0 0");
        textContainer.getChildren().add(itemQuantityText);

        container.getChildren().addAll(newHBox, newDecrementButton, textContainer, newIncrementButton);

        verticalBoxContainer.getChildren().add(container);
    }

    public static Order getCurrentOrder() {
        return currentOrder;
    }

    public static CoffeeShop getCurrentCoffeeShop() {
        return currentCoffeeShop;
    }
}

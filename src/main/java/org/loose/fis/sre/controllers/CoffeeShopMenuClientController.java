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
import org.dizitart.no2.exceptions.InvalidIdException;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.exceptions.MenuItemAlreadyExistsException;
import org.loose.fis.sre.model.CoffeeShop;
import org.loose.fis.sre.model.CoffeeShopMenuItem;
import org.loose.fis.sre.model.Order;
import org.loose.fis.sre.services.CoffeeShopMenuItemService;

import java.io.IOException;
import java.util.Objects;

import static org.loose.fis.sre.controllers.LoginController.getCurrentUser;
import static org.loose.fis.sre.services.CoffeeShopService.getCoffeeShopsRepository;

public class CoffeeShopMenuClientController {
    @FXML
    private VBox verticalBoxContainer;

    @FXML
    private HBox hContainer;

    private static CoffeeShop currentCoffeeShop;

    private Order currentOrder;

    @FXML
    private Button goToCheckoutButton;


    public void initialize () {
        String selectedCoffeeShop = CoffeeShopListController.getSelectedCoffeeShopName();
        ObjectRepository<CoffeeShop> coffeeShopsRepository = getCoffeeShopsRepository();

        for(CoffeeShop shop : coffeeShopsRepository.find()) {
            if(Objects.equals(shop.getName(), selectedCoffeeShop)) {
                currentCoffeeShop = shop;
            }
        }

        if(currentCoffeeShop.getMenuItemsNumber() > 0) {
            for (CoffeeShopMenuItem item : currentCoffeeShop.getMenuItems()) {
                createNewItemContainer(item.getName(), item.getDescription(), item.getDrinkVolume(), item);
            }
        }

        currentOrder = new Order();
    }

    @FXML
    private void onCheckoutPress (javafx.event.ActionEvent event) {
        Stage currentStage = (Stage) verticalBoxContainer.getScene().getWindow();
        currentStage.close();
        Parent checkout = null;
        try {
            checkout = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("checkout.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene newScene = new Scene(checkout);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(newScene);
        newStage.show();
    }

    public void createNewItemContainer(String name, String description, String drinkVolume, CoffeeShopMenuItem item) {
        HBox newHBox = new HBox();
        AnchorPane newPanelContent = new AnchorPane();

        VBox newVBoxTitles = new VBox();
        VBox newVBoxInfo = new VBox();

        Text nameField = new Text(name);
        Text descriptionField = new Text(description);
        Text drinkVolumeField = new Text(drinkVolume);

        CoffeeShopMenuItem currentItem = item;

        newVBoxTitles.getChildren().addAll(new Text("Name:"), new Text("Description:"), new Text("Drink volume:"));
        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().addAll(nameField, descriptionField, drinkVolumeField);

        newPanelContent.getChildren().addAll(newVBoxTitles, newVBoxInfo);
        TitledPane newTitledPane = new TitledPane(name, newPanelContent);
        newTitledPane.setPrefWidth(241);
        newTitledPane.setPrefHeight(200);
        newTitledPane.expandedProperty().addListener((observable, wasExpanded, isExpanded) ->
                newTitledPane.setMinHeight(isExpanded ? 200 : Region.USE_PREF_SIZE));

        Button newDecrementButton = new Button("-");
        newDecrementButton.setOnAction((event) -> {
            if(currentOrder.getItemNumber() > 0) currentOrder.removeItem(item, currentOrder);
        });
        Button newIncrementButton = new Button("+");
        newIncrementButton.setOnAction((event) -> {
            currentOrder.addItem(item, currentOrder);
        });
        newHBox.getChildren().addAll(newTitledPane, newDecrementButton, newIncrementButton);

        verticalBoxContainer.getChildren().add(newHBox);
    }
}

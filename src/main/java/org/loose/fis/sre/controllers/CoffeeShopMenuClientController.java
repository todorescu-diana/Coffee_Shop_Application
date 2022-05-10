package org.loose.fis.sre.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.dizitart.no2.exceptions.InvalidIdException;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.exceptions.MenuItemAlreadyExistsException;
import org.loose.fis.sre.model.CoffeeShop;
import org.loose.fis.sre.model.CoffeeShopMenuItem;
import org.loose.fis.sre.services.CoffeeShopMenuItemService;

import java.util.Objects;

import static org.loose.fis.sre.services.CoffeeShopService.getCoffeeShopsRepository;

public class CoffeeShopMenuClientController {
    @FXML
    private VBox verticalBoxContainer;

    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField drinkVolumeField;


    public void initialize () {
        String selectedCoffeeShop = CoffeeShopListController.getSelectedCoffeeShopName();
        CoffeeShop currentCoffeeShop;
        ObjectRepository<CoffeeShop> coffeeShopsRepository = getCoffeeShopsRepository();

        for(CoffeeShop shop : coffeeShopsRepository.find()) {
            if(Objects.equals(shop.getName(), selectedCoffeeShop)) {
                currentCoffeeShop = shop;
            }
        }

       // for(CoffeeShopMenuItem item : menuItemsRepository.find()) {
         //   createNewItemContainer(item.getName(), item.getDescription(), item.getDrinkVolume());
       // }
    }

    public void createNewItemContainer(String name, String description, String drinkVolume) {
        HBox newHBox = new HBox();
        AnchorPane newPanelContent = new AnchorPane();

        VBox newVBoxTitles = new VBox();
        VBox newVBoxInfo = new VBox();

        Text nameField = new Text(name);
        Text descriptionField = new Text(description);
        Text drinkVolumeField = new Text(drinkVolume);

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

        });
        Button newIncrementButton = new Button("+");
        newIncrementButton.setOnAction((event) -> {

        });
        newHBox.getChildren().addAll(newTitledPane, newDecrementButton, newIncrementButton);

        verticalBoxContainer.getChildren().add(newHBox);
    }
}

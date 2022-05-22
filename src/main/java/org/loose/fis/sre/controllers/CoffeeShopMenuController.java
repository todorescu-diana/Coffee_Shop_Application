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
import org.loose.fis.sre.services.CoffeeShopMenuItemService;

import java.io.IOException;
import java.util.Objects;

import static org.loose.fis.sre.controllers.LoginController.getCurrentUser;
import static org.loose.fis.sre.services.CoffeeShopService.getCoffeeShopsRepository;

public class CoffeeShopMenuController {
    @FXML
    private VBox verticalBoxContainer;

    private static CoffeeShop currentCoffeeShop;

    public void initialize () {
        ObjectRepository<CoffeeShop> coffeeShopsRepository = getCoffeeShopsRepository();

        for(CoffeeShop shop : coffeeShopsRepository.find()) {
            if(Objects.equals(shop.getOwner(), getCurrentUser().getUsername())) {
               currentCoffeeShop = shop;
            }
        }
        if(currentCoffeeShop.getMenuItemsNumber() > 0) {
            for(CoffeeShopMenuItem item : currentCoffeeShop.getMenuItems()) {
                createNewItemContainer(item.getName(), item.getDescription(), item.getDrinkVolume(), item.getPrice());
            }
        }
    }

    public static CoffeeShop getCurrentCoffeeShop() {return currentCoffeeShop;}

    public void createNewItemContainer(String name, String description, String drinkVolume, double price) {
        HBox newHBox = new HBox();
        AnchorPane newPanelContent = new AnchorPane();

        VBox newVBoxTitles = new VBox();
        VBox newVBoxInfo = new VBox();

        Text nameField = new Text(name);
        nameField.setId("nameField");
        Text descriptionField = new Text(description);
        descriptionField.setId("descriptionField");
        Text drinkVolumeField = new Text(drinkVolume);
        drinkVolumeField.setId("drinkVolumeField");
        Text priceField = new Text(String.valueOf(price));
        priceField.setId("priceField");

        newVBoxTitles.getChildren().addAll(new Text("Name:"), new Text("Description:"), new Text("Drink volume:"), new Text("Price:"));
        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().addAll(nameField, descriptionField, drinkVolumeField, priceField);

        newPanelContent.getChildren().addAll(newVBoxTitles, newVBoxInfo);
        TitledPane newTitledPane = new TitledPane(name, newPanelContent);
        newTitledPane.setPrefWidth(241);
        newTitledPane.setPrefHeight(200);
        newTitledPane.expandedProperty().addListener((observable, wasExpanded, isExpanded) ->
                newTitledPane.setMinHeight(isExpanded ? 200 : Region.USE_PREF_SIZE));

        Button newEditButton = new Button("Edit");
        newEditButton.setId("editItemButton");
        newEditButton.setOnAction((event) -> {
            createNewEditableItemContainer(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText(), Double.parseDouble(priceField.getText()));
            verticalBoxContainer.getChildren().remove(newHBox);
        });
        Button newDeleteButton = new Button("Delete");
        newDeleteButton.setOnAction((event) -> {
            CoffeeShopMenuItemService.removeMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
            verticalBoxContainer.getChildren().remove(newHBox);
        });
        newHBox.getChildren().addAll(newTitledPane, newEditButton, newDeleteButton);

        newHBox.setId("vboxchild");

        verticalBoxContainer.getChildren().add(newHBox);
    }

    public void createNewEditableItemContainer() {
        HBox newHBox = new HBox();
        AnchorPane newPanelContent = new AnchorPane();

        VBox newVBoxTitles = new VBox();
        VBox newVBoxInfo = new VBox();

        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        TextField drinkVolumeField = new TextField();
        TextField priceField = new TextField();
        nameField.setId("editableNameField");
        descriptionField.setId("editableDescriptionField");
        drinkVolumeField.setId("editableDrinkVolumeField");
        priceField.setId("editablePriceField");

        Text itemMessage = new Text("");
        itemMessage.setId("itemMessage");

        Button addNewItemButton = new Button("Add New Item");
        addNewItemButton.setId("addNewItemButton");
        addNewItemButton.setOnAction((event) -> {
            try {
                if(Objects.equals(nameField.getText(), "")) itemMessage.setText("Menu item name cannot be empty.");
                else if(priceField.getText().contains("-") || Objects.equals(priceField.getText(), "0") || Objects.equals(priceField.getText(), "0.0")) itemMessage.setText("Price has to be a number greater than 0.");
                else {
                    try {
                        Double.parseDouble(priceField.getText());
                        CoffeeShopMenuItemService.addMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText(), Double.parseDouble(priceField.getText()));
                        createNewItemContainer(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText(), Double.parseDouble(priceField.getText()));
                        verticalBoxContainer.getChildren().remove(newHBox);
                    }
                    catch(NumberFormatException ex) {
                        itemMessage.setText("Price has to be a number.");
                    }
                }
            } catch(InvalidIdException e) {
                itemMessage.setText("Menu item name cannot be empty.");
            } catch (MenuItemAlreadyExistsException e) {
                itemMessage.setText(e.getMessage());
            } catch(NumberFormatException ex) {
                itemMessage.setText("Price has to be a number.");
            }

        });

        newVBoxTitles.getChildren().addAll(new Text("Name:"), new Text("Description:"), new Text("Drink volume:"), new Text("Price:"), addNewItemButton, itemMessage);
        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().addAll(nameField, descriptionField, drinkVolumeField, priceField);

        newPanelContent.getChildren().addAll(newVBoxTitles, newVBoxInfo);
        TitledPane newTitledPane = new TitledPane("New Item", newPanelContent);
        newTitledPane.setPrefWidth(241);
        newTitledPane.setPrefHeight(200);
        newTitledPane.expandedProperty().addListener((observable, wasExpanded, isExpanded) ->
                newTitledPane.setMinHeight(isExpanded ? 200 : Region.USE_PREF_SIZE));

        Button newDeleteButton = new Button("Delete");
        newDeleteButton.setOnAction((event) -> {
                CoffeeShopMenuItemService.removeMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
                verticalBoxContainer.getChildren().remove(newHBox);
        });
        newHBox.getChildren().addAll(newTitledPane, newDeleteButton);

        verticalBoxContainer.getChildren().add(newHBox);
    }

    public void createNewEditableItemContainer(String nameFieldDefaultValue, String descriptionFieldDefaultValue, String drinkVolumeFieldDefaultValue, double priceFieldDefaultValue) {

        HBox newHBox = new HBox();
        AnchorPane newPanelContent = new AnchorPane();

        VBox newVBoxTitles = new VBox();
        VBox newVBoxInfo = new VBox();

        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        TextField drinkVolumeField = new TextField();
        TextField priceField = new TextField();
        nameField.setId("editableNameField");
        descriptionField.setId("editableDescriptionField");
        drinkVolumeField.setId("editableDrinkVolumeField");
        priceField.setId("editablePriceField");
        nameField.setText(nameFieldDefaultValue);
        descriptionField.setText(descriptionFieldDefaultValue);
        drinkVolumeField.setText(drinkVolumeFieldDefaultValue);
        priceField.setText(String.valueOf(priceFieldDefaultValue));

        Text itemMessage = new Text("");
        itemMessage.setId("itemMessage");

        Button addNewItemButton = new Button("Add New Item");
        addNewItemButton.setId("addNewItemButton");
        addNewItemButton.setOnAction((event) -> {
            try {
                if(Objects.equals(nameField.getText(), "")) itemMessage.setText("Menu item name cannot be empty.");
                else if(priceField.getText().contains("-") || Objects.equals(priceField.getText(), "0") || Objects.equals(priceField.getText(), "0.0")) itemMessage.setText("Price has to be a number greater than 0.");
                else {
                    try {
                        Double.parseDouble(priceField.getText());
                        CoffeeShopMenuItemService.modifyMenuItem(nameFieldDefaultValue, nameField.getText(), descriptionField.getText(), drinkVolumeField.getText(), Double.parseDouble(priceField.getText()));
                        createNewItemContainer(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText(), Double.parseDouble(priceField.getText()));
                        verticalBoxContainer.getChildren().remove(newHBox);
                    } catch(NumberFormatException ex) {
                        itemMessage.setText("Price has to be a number.");
                    }
                }
            } catch(InvalidIdException e) {
                itemMessage.setText("Menu item name cannot be empty.");
            } catch (MenuItemAlreadyExistsException e) {
                itemMessage.setText(e.getMessage());
            }
        });

        newVBoxTitles.getChildren().addAll(new Text("Name:"), new Text("Description:"), new Text("Drink volume:"), new Text("Price:"), addNewItemButton, itemMessage);
        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().addAll(nameField, descriptionField, drinkVolumeField, priceField);

        newPanelContent.getChildren().addAll(newVBoxTitles, newVBoxInfo);
        TitledPane newTitledPane = new TitledPane("New Item", newPanelContent);
        newTitledPane.setPrefWidth(241);
        newTitledPane.setPrefHeight(200);
        newTitledPane.expandedProperty().addListener((observable, wasExpanded, isExpanded) ->
                newTitledPane.setMinHeight(isExpanded ? 200 : Region.USE_PREF_SIZE));

        Button newDeleteButton = new Button("Delete");
        newDeleteButton.setOnAction((event) -> {
            CoffeeShopMenuItemService.removeMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
            verticalBoxContainer.getChildren().remove(newHBox);
        });
        newHBox.getChildren().addAll(newTitledPane, newDeleteButton);

        verticalBoxContainer.getChildren().add(newHBox);
    }

    public void handleGoToCoffeeShopMenu(javafx.event.ActionEvent event) throws IOException {
        Stage currentStage = (Stage) verticalBoxContainer.getScene().getWindow();
        currentStage.close();

        Parent coffeeShopList = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("coffeeShopMenu.fxml")));
        Scene newScene = new Scene(coffeeShopList);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(newScene);
        newStage.show();
    }

    public void handleGoToTodaysOrders(javafx.event.ActionEvent event) throws IOException {
        Stage currentStage = (Stage) verticalBoxContainer.getScene().getWindow();
        currentStage.close();

        Parent coffeeShopList = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("todaysOrders.fxml")));
        Scene newScene = new Scene(coffeeShopList);
        Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        newStage.setScene(newScene);
        newStage.show();
    }

    @FXML
    private void handleAddItemAction(javafx.event.ActionEvent event) {
        createNewEditableItemContainer();
    }
}

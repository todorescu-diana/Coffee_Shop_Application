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

        verticalBoxContainer.setSpacing(30);
        verticalBoxContainer.setStyle("-fx-padding: 20 0 30 3");

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
    public static void setCurrentCoffeeShop(CoffeeShop coffeeShop) {currentCoffeeShop = coffeeShop;}

    public void createNewItemContainer(String name, String description, String drinkVolume, double price) {
        HBox newHBox = new HBox(50);
        newHBox.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc; -fx-background-radius: 5px; -fx-padding: 10 10 10 10");

        VBox newVBoxTitles = new VBox(10);
        VBox newVBoxInfo = new VBox(10);

        HBox container = new HBox(5);

        Text nameField = new Text(name);
        nameField.setFill(Color.web("#ffffcc"));
        nameField.setId("nameField");
        Text descriptionField = new Text(description);
        descriptionField.setFill(Color.web("#ffffcc"));
        descriptionField.setId("descriptionField");
        Text drinkVolumeField = new Text(drinkVolume);
        drinkVolumeField.setFill(Color.web("#ffffcc"));
        drinkVolumeField.setId("drinkVolumeField");
        Text priceField = new Text(String.valueOf(price));
        priceField.setFill(Color.web("#ffffcc"));
        priceField.setId("priceField");

        Text nameText = new Text("Name:");
        nameText.setFill(Color.web("#ffffcc"));
        Text descriptionText = new Text("Description:");
        descriptionText.setFill(Color.web("#ffffcc"));
        Text drinkVolumeText = new Text("Drink volume:");
        drinkVolumeText.setFill(Color.web("#ffffcc"));
        Text priceText = new Text("Price:");
        priceText.setFill(Color.web("#ffffcc"));

        newVBoxTitles.getChildren().addAll(nameText, descriptionText, drinkVolumeText, priceText);
        newVBoxInfo.getChildren().addAll(nameField, descriptionField, drinkVolumeField, priceField);

        newHBox.getChildren().addAll(newVBoxTitles, newVBoxInfo);

        Button newEditButton = new Button("Edit");
        newEditButton.setId("editItemButton");
        newEditButton.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc;");
        newEditButton.setOnAction((event) -> {
            createNewEditableItemContainer(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText(), Double.parseDouble(priceField.getText()));
            verticalBoxContainer.getChildren().remove(container);
        });
        Button newDeleteButton = new Button("Delete");
        newDeleteButton.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc;");
        newDeleteButton.setOnAction((event) -> {
            CoffeeShopMenuItemService.removeMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
            verticalBoxContainer.getChildren().remove(container);
        });
        container.getChildren().addAll(newHBox, newEditButton, newDeleteButton);

        verticalBoxContainer.getChildren().add(container);

        container.setId("vboxchild");
    }

    public void createNewEditableItemContainer() {
        HBox newHBox = new HBox(50);
        newHBox.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc; -fx-background-radius: 5px; -fx-padding: 10 10 10 10");

        VBox newVBoxTitles = new VBox(20);
        VBox newVBoxInfo = new VBox(10);

        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        TextField drinkVolumeField = new TextField();
        TextField priceField = new TextField();
        nameField.setId("editableNameField");
        descriptionField.setId("editableDescriptionField");
        drinkVolumeField.setId("editableDrinkVolumeField");
        priceField.setId("editablePriceField");

        Text itemMessage = new Text("");
        itemMessage.setFill(Color.MAROON);
        itemMessage.setId("itemMessage");

        HBox container = new HBox(5);

        Button addNewItemButton = new Button("Add New Item");
        addNewItemButton.setId("addNewItemButton");
        addNewItemButton.setStyle("-fx-background-color: #ffffcc; -fx-text-fill: #800000;");
        addNewItemButton.setOnAction((event) -> {
            try {
                if(Objects.equals(nameField.getText(), "")) itemMessage.setText("Menu item name cannot be empty.");
                else if(priceField.getText().contains("-") || Objects.equals(priceField.getText(), "0") || Objects.equals(priceField.getText(), "0.0")) itemMessage.setText("Price has to be a number greater than 0.");
                else {
                    try {
                        Double.parseDouble(priceField.getText());
                        CoffeeShopMenuItemService.addMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText(), Double.parseDouble(priceField.getText()));
                        createNewItemContainer(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText(), Double.parseDouble(priceField.getText()));
                        verticalBoxContainer.getChildren().remove(container);
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

        Text nameText = new Text("Name:");
        nameText.setFill(Color.web("#ffffcc"));
        Text descriptionText = new Text("Description:");
        descriptionText.setFill(Color.web("#ffffcc"));
        Text drinkVolumeText = new Text("Drink volume:");
        drinkVolumeText.setFill(Color.web("#ffffcc"));
        Text priceText = new Text("Price:");
        priceText.setFill(Color.web("#ffffcc"));

        newVBoxTitles.getChildren().addAll(nameText, descriptionText, drinkVolumeText, priceText,  addNewItemButton);
        newVBoxInfo.getChildren().addAll(nameField, descriptionField, drinkVolumeField, priceField);

        newHBox.getChildren().addAll(newVBoxTitles, newVBoxInfo);

        Button newDeleteButton = new Button("Delete");
        newDeleteButton.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc;");
        newDeleteButton.setOnAction((event) -> {
                CoffeeShopMenuItemService.removeMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
                verticalBoxContainer.getChildren().remove(container);
        });

        container.getChildren().addAll(newHBox, newDeleteButton);

        verticalBoxContainer.getChildren().add(container);
    }

    public void createNewEditableItemContainer(String nameFieldDefaultValue, String descriptionFieldDefaultValue, String drinkVolumeFieldDefaultValue, double priceFieldDefaultValue) {

        HBox newHBox = new HBox(50);
        newHBox.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc; -fx-background-radius: 5px; -fx-padding: 10 10 10 10");

        VBox newVBoxTitles = new VBox(20);
        VBox newVBoxInfo = new VBox(10);

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
        itemMessage.setFill(Color.MAROON);
        itemMessage.setId("itemMessage");

        HBox container = new HBox(5);

        Button addNewItemButton = new Button("Add New Item");
        addNewItemButton.setId("addNewItemButton");
        addNewItemButton.setStyle("-fx-background-color: #ffffcc; -fx-text-fill: #800000;");
        addNewItemButton.setOnAction((event) -> {
            try {
                if(Objects.equals(nameField.getText(), "")) itemMessage.setText("Menu item name cannot be empty.");
                else if(priceField.getText().contains("-") || Objects.equals(priceField.getText(), "0") || Objects.equals(priceField.getText(), "0.0")) itemMessage.setText("Price has to be a number greater than 0.");
                else {
                    try {
                        Double.parseDouble(priceField.getText());
                        CoffeeShopMenuItemService.modifyMenuItem(nameFieldDefaultValue, nameField.getText(), descriptionField.getText(), drinkVolumeField.getText(), Double.parseDouble(priceField.getText()));
                        createNewItemContainer(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText(), Double.parseDouble(priceField.getText()));
                        verticalBoxContainer.getChildren().remove(container);
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

        Text nameText = new Text("Name:");
        nameText.setFill(Color.web("#ffffcc"));
        Text descriptionText = new Text("Description:");
        descriptionText.setFill(Color.web("#ffffcc"));
        Text drinkVolumeText = new Text("Drink volume:");
        drinkVolumeText.setFill(Color.web("#ffffcc"));
        Text priceText = new Text("Price:");
        priceText.setFill(Color.web("#ffffcc"));

        newVBoxTitles.getChildren().addAll(nameText, descriptionText, drinkVolumeText, priceText, addNewItemButton, itemMessage);
        newVBoxInfo.getChildren().addAll(nameField, descriptionField, drinkVolumeField, priceField);

        newHBox.getChildren().addAll(newVBoxTitles, newVBoxInfo);

        Button newDeleteButton = new Button("Delete");
        newDeleteButton.setStyle("-fx-background-color: #800000; -fx-text-fill: #ffffcc;");
        newDeleteButton.setOnAction((event) -> {
            CoffeeShopMenuItemService.removeMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
            verticalBoxContainer.getChildren().remove(container);
        });

        container.getChildren().addAll(newHBox, newDeleteButton);

        verticalBoxContainer.getChildren().add(container);
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

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
import org.loose.fis.sre.model.CoffeeShopMenuItem;
import org.loose.fis.sre.services.CoffeeShopMenuItemService;

import static org.loose.fis.sre.services.CoffeeShopMenuItemService.getMenuItemsRepository;

public class CoffeeShopMenuController {
    @FXML
    private VBox verticalBoxContainer;

    @FXML
    private Text itemMessage;

    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField drinkVolumeField;


    public void initialize () {
        ObjectRepository<CoffeeShopMenuItem> menuItemsRepository = getMenuItemsRepository();

        for(CoffeeShopMenuItem item : menuItemsRepository.find()) {
            createNewItemContainer(item.getName(), item.getDescription(), item.getDrinkVolume());
        }
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

        Button newEditButton = new Button("Edit");
        newEditButton.setOnAction((event) -> {
            createNewEditableItemContainer(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
            verticalBoxContainer.getChildren().remove(newHBox);
        });
        Button newDeleteButton = new Button("Delete");
        newDeleteButton.setOnAction((event) -> {
            CoffeeShopMenuItemService.removeMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
            verticalBoxContainer.getChildren().remove(newHBox);
        });
        newHBox.getChildren().addAll(newTitledPane, newEditButton, newDeleteButton);

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
        nameField.setId("nameField");
        nameField.setId("descriptionField");
        nameField.setId("drinkVolumeField");

        Text itemMessage = new Text("");
        itemMessage.setId("itemMessage");

        Button addNewItemButton = new Button("Add New Item");
        addNewItemButton.setOnAction((event) -> {
            try {
                CoffeeShopMenuItemService.addMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
                createNewItemContainer(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
                verticalBoxContainer.getChildren().remove(newHBox);
            } catch(InvalidIdException e) {
                itemMessage.setText("Menu item name cannot be empty.");
            } catch (MenuItemAlreadyExistsException e) {
                itemMessage.setText(e.getMessage());
            }
        });

        newVBoxTitles.getChildren().addAll(new Text("Name:"), new Text("Description:"), new Text("Drink volume:"), addNewItemButton, itemMessage);
        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().addAll(nameField, descriptionField, drinkVolumeField);

        newPanelContent.getChildren().addAll(newVBoxTitles, newVBoxInfo);
        TitledPane newTitledPane = new TitledPane("New Item", newPanelContent);
        newTitledPane.setPrefWidth(241);
        newTitledPane.setPrefHeight(200);
        newTitledPane.expandedProperty().addListener((observable, wasExpanded, isExpanded) ->
                newTitledPane.setMinHeight(isExpanded ? 200 : Region.USE_PREF_SIZE));

        Button newEditButton = new Button("Edit");
        Button newDeleteButton = new Button("Delete");
        newDeleteButton.setOnAction((event) -> {
                CoffeeShopMenuItemService.removeMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
                verticalBoxContainer.getChildren().remove(newHBox);
        });
        newHBox.getChildren().addAll(newTitledPane, newEditButton, newDeleteButton);

        verticalBoxContainer.getChildren().add(newHBox);
    }

    public void createNewEditableItemContainer(String nameFieldDefaultValue, String descriptionFieldDefaultValue, String drinkVolumeFieldDefaultValue) {
        HBox newHBox = new HBox();
        AnchorPane newPanelContent = new AnchorPane();

        VBox newVBoxTitles = new VBox();
        VBox newVBoxInfo = new VBox();

        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        TextField drinkVolumeField = new TextField();
        nameField.setId("nameField");
        nameField.setId("descriptionField");
        nameField.setId("drinkVolumeField");
        nameField.setText(nameFieldDefaultValue);
        descriptionField.setText(descriptionFieldDefaultValue);
        drinkVolumeField.setText(drinkVolumeFieldDefaultValue);

        Text itemMessage = new Text("");
        itemMessage.setId("itemMessage");

        Button addNewItemButton = new Button("Add New Item");
        addNewItemButton.setOnAction((event) -> {
            try {
                CoffeeShopMenuItemService.modifyMenuItem(nameFieldDefaultValue, nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
                createNewItemContainer(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
                verticalBoxContainer.getChildren().remove(newHBox);
            } catch(InvalidIdException e) {
                itemMessage.setText("Menu item name cannot be empty.");
            } catch (MenuItemAlreadyExistsException e) {
                itemMessage.setText(e.getMessage());
            }
        });

        newVBoxTitles.getChildren().addAll(new Text("Name:"), new Text("Description:"), new Text("Drink volume:"), addNewItemButton, itemMessage);
        newVBoxInfo.setLayoutX(104.0);
        newVBoxInfo.getChildren().addAll(nameField, descriptionField, drinkVolumeField);

        newPanelContent.getChildren().addAll(newVBoxTitles, newVBoxInfo);
        TitledPane newTitledPane = new TitledPane("New Item", newPanelContent);
        newTitledPane.setPrefWidth(241);
        newTitledPane.setPrefHeight(200);
        newTitledPane.expandedProperty().addListener((observable, wasExpanded, isExpanded) ->
                newTitledPane.setMinHeight(isExpanded ? 200 : Region.USE_PREF_SIZE));

        Button newEditButton = new Button("Edit");
        Button newDeleteButton = new Button("Delete");
        newDeleteButton.setOnAction((event) -> {
            CoffeeShopMenuItemService.removeMenuItem(nameField.getText(), descriptionField.getText(), drinkVolumeField.getText());
            verticalBoxContainer.getChildren().remove(newHBox);
        });
        newHBox.getChildren().addAll(newTitledPane, newEditButton, newDeleteButton);

        verticalBoxContainer.getChildren().add(newHBox);
    }

    @FXML
    private void handleAddItemAction(javafx.event.ActionEvent event) throws MenuItemAlreadyExistsException {
        createNewEditableItemContainer();
    }

}

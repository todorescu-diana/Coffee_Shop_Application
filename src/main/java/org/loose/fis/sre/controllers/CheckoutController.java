package org.loose.fis.sre.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.dizitart.no2.objects.ObjectRepository;
import org.loose.fis.sre.model.Card;
import org.loose.fis.sre.model.CoffeeShopMenuItem;
import org.loose.fis.sre.model.Order;
import org.loose.fis.sre.model.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static org.loose.fis.sre.controllers.CoffeeShopMenuClientController.getCurrentCoffeeShop;
import static org.loose.fis.sre.controllers.LoginController.getCurrentUser;
import static org.loose.fis.sre.services.CardService.addCard;
import static org.loose.fis.sre.services.CardService.getCardRepository;
import static org.loose.fis.sre.services.UserService.getUserRepository;
import static org.loose.fis.sre.services.UserService.modifyUser;

public class CheckoutController {
    @FXML
    private ChoiceBox payWith;
    @FXML
    private HBox hBox;
    @FXML
    private Button placeOrderButton;

    @FXML
    private Text insufficientFundsMessage;

    private String cardNumber;

    private User currentManager;
    private Card currentCard;

    public void initialize() {
        payWith.getItems().addAll("Cash", "Card");

        payWith.setOnAction((event) -> {
            if (Objects.equals(payWith.getValue(), "Card")) {
                Text itemMessage = new Text("Card number:");
                TextField cardNumberField = new TextField();
                cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
                    cardNumber = newValue;
                });
                hBox.getChildren().addAll(itemMessage, cardNumberField);
            }
        });
    }

    public void handlePlaceOrderPress(javafx.event.ActionEvent event) throws IOException {
        CoffeeShopMenuClientController.getCurrentOrder().calculateOrderPrice();
        CoffeeShopMenuClientController.getCurrentOrder().setOrderDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        CoffeeShopMenuClientController.getCurrentOrder().setCoffeeShopName(getCurrentCoffeeShop().getName());

        getCurrentUser().addOrderToOrderList(CoffeeShopMenuClientController.getCurrentOrder());

        for (User user : getUserRepository().find()) {
            if (Objects.equals(getCurrentCoffeeShop().getOwner(), user.getUsername())) currentManager = user;
        }

        currentManager.addOrderToOrderList(CoffeeShopMenuClientController.getCurrentOrder());

        modifyUser(getCurrentUser());
        modifyUser(currentManager);

//        for(Order o : getCurrentUser().getOrderList()) {
//            for(CoffeeShopMenuItem item : o.getItems()) {
//                System.out.println(item.getName());
//            }
//        }

        //System.out.println(CoffeeShopMenuClientController.getCurrentOrder().getCoffeeShopName());

        int found = 0;
        if (Objects.equals(payWith.getValue(), "Card")) {
            ObjectRepository<Card> cardRepository = getCardRepository();

            for (Card card : cardRepository.find()) {
                if (Objects.equals(card.getCardNumber(), cardNumber)) {
                    currentCard = card;
                    found = 1;
                }
            }
            if (found == 0) {
                insufficientFundsMessage.setText("Card not found.");
            }


//        System.out.println("CARD NUMBER: " + currentCard.getCardNumber());
//        System.out.println("CARD BALANCE: " + currentCard.getBalance());
            else {
                int oldBalance = currentCard.getBalance();
                if (oldBalance < CoffeeShopMenuClientController.getCurrentOrder().getOrderPrice()) {
                    insufficientFundsMessage.setText("Insufficient funds on card.");
//            System.out.println("OLD BALANCE: " + currentCard.getBalance());
//            System.out.println("ORDER PRICE: " + CoffeeShopMenuClientController.getCurrentOrder().getOrderPrice());
                } else {
                    currentCard.setBalance(oldBalance - CoffeeShopMenuClientController.getCurrentOrder().getOrderPrice());
//                System.out.println("NEW BALANCE: " + currentCard.getBalance());
                    Stage currentStage = (Stage) placeOrderButton.getScene().getWindow();
                    currentStage.close();

                    Parent orderPlacedScreen = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("orderPlacedScreen.fxml")));
                    Scene newScene = new Scene(orderPlacedScreen);
                    Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    newStage.setScene(newScene);
                    newStage.show();
                }
            }
        }
        else {
            Stage currentStage = (Stage) placeOrderButton.getScene().getWindow();
            currentStage.close();

            Parent orderPlacedScreen = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("orderPlacedScreen.fxml")));
            Scene newScene = new Scene(orderPlacedScreen);
            Stage newStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            newStage.setScene(newScene);
            newStage.show();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package creditcardvalidator;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author Spark
 */
public class ValidatorDesignController implements Initializable {

    @FXML
    private JFXTextField cardNumber;
    @FXML
    private JFXTextField cardHolder;
    @FXML
    private JFXTextField cardCVC;
    @FXML
    private JFXTextField cardExpiry;
    @FXML
    private Text cardStatus;
    @FXML
    private Pane inputPane;
    @FXML
    private Pane displayPane;
    @FXML
    private Text appName;
    @FXML
    private Text displayNumber;
    @FXML
    private Text displayHolder;
    @FXML
    private Text displayDate;
    @FXML
    private Text displayCVV;

    NumberValidator numberValidator1;
    NumberValidator numberValidator2;
    RequiredFieldValidator requiredFieldValidator1;
    RequiredFieldValidator requiredFieldValidator2;
    RequiredFieldValidator requiredFieldValidator3;
    RequiredFieldValidator requiredFieldValidator4;

    @FXML
    private void validateCard(ActionEvent event) {

        String cardNum = cardNumber.getText();

        String card[] = new String[cardNum.length()];
        if (cardNum.equals("") || cardHolder.getText().equals("") || cardCVC.getText().equals("") || cardExpiry.getText().equals("")) {
            cardStatus.setLayoutX(70);
            cardStatus.setText("Some fields are Empty!");
        } else if (validate(cardNum, card) == false) {
            cardStatus.setLayoutX(56);
            cardStatus.setText("This credit card is Invalid!");
        } else if (validate(cardNum, card) == true) {
            displayNumber.setText(cardNum);
            displayCVV.setText(cardCVC.getText());
            displayHolder.setText(cardHolder.getText());
            displayDate.setText(cardExpiry.getText());
            cardStatus.setText("");
            moveToNext();
        }
//        4847352989263094
    }

    public void moveToNext() {
        FadeTransition fade = new FadeTransition(Duration.millis(100));
        fade.setNode(inputPane);
        fade.setFromValue(1);
        fade.setToValue(0);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.7));
        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(0.5));
        translateTransition.setNode(inputPane);
        translateTransition2.setNode(appName);
        translateTransition.setToX(-200);
        translateTransition2.setToX(140);
        translateTransition.play();
        translateTransition2.play();
        translateTransition.setOnFinished((event) -> {
            displayPane.setVisible(true);
            fade.play();
        });
    }

    public static boolean validate(String cardNum, String[] cardArray) {
        char cardChar;
        int sumOfOdd;
        int sumOfEven;
        int checkSum;
        boolean flag = false;

        for (int i = 0; i < cardNum.length(); i++) {
            cardChar = cardNum.charAt(i);
            cardArray[i] = String.valueOf(cardChar);
        }

        sumOfOdd = sumOfOddPlaces(cardArray);

        sumOfEven = sumOfEvenPlaces(cardArray);

        checkSum = (sumOfEven + sumOfOdd);

        if (cardNum.length() == 16) {
            if (checkSum % 10 == 0) {
                flag = true;
            } else if (checkSum % 10 != 0) {
                flag = false;
            }
        } else {
            flag = false;
        }

        return flag;
    }

    public static int sumOfOddPlaces(String[] cards) {
        int sum = 0;
        int ind;
        int index = 0;
        String d;
        String c = "";
        for (String card : cards) {
            c += card;
            ind = c.lastIndexOf(card);
            while (ind % 2 != 0) {
                index = c.lastIndexOf(card);
                char num = c.charAt(ind);
                String sd = String.valueOf(num);
                int val = Integer.parseInt(sd);
                sum += val;

                break;
            }
        }
        return sum;
    }

    public static int sumOfEvenPlaces(String[] cards) {
        int sum = 0;
        int ind;
        int index = 0;
        int charSum1 = 0;
        int charSum2 = 0;
        String d;
        String c = "";
        for (String card : cards) {
            c += card;
            ind = c.lastIndexOf(card);
            while (ind % 2 == 0) {
                index = c.lastIndexOf(card);
                String num = String.valueOf(c.charAt(ind));
                int val = (Integer.parseInt(num) * 2);
                String sd = String.valueOf(val);
                while (sd.length() != 0) {
                    if (sd.length() > 1) {
                        int firstNum = Integer.parseInt(sd.substring(0, 1));
                        int secondNum = Integer.parseInt(sd.substring(1, 2));
                        charSum2 += firstNum + secondNum;
                    } else if (sd.length() <= 1) {
                        charSum1 += Integer.parseInt(sd);
                    }
                    break;
                }

                break;
            }
            sum = charSum1 + charSum2;
        }
        return sum;
    }

    private void setupValidations() {
        cardNumber.setOnKeyTyped((KeyEvent event1) -> {
            if (cardNumber.getText().length() > 15) {
                event1.consume();
            }
//            if (!cardNumber.getText().matches("(\\d)")) {
//                event1.consume();
//            }
        });

        cardCVC.setOnKeyTyped((KeyEvent event2) -> {
            if (cardCVC.getText().length() > 2) {
                event2.consume();
            }
//            if (!cardCVC.getText().matches("(//d)")) {
//                event2.consume();
//            }
        });

        requiredFieldValidator1 = new RequiredFieldValidator();
        requiredFieldValidator1.setIcon(new ImageView(getClass().getResource("images/warning.png").toString()));
        requiredFieldValidator1.setMessage("Empty Field");

        numberValidator1 = new NumberValidator();
        numberValidator1.setIcon(new ImageView(getClass().getResource("images/warning.png").toString()));
        numberValidator1.setMessage("Only Numbers Allowed");

        cardNumber.getValidators().add(requiredFieldValidator1);
        cardNumber.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                cardNumber.validate();
            }
        });
        cardNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
//                cardNumber.resetValidation();
            } else if (newValue.isEmpty()) {
                cardNumber.validate();
            }
        });
        cardNumber.getValidators().add(numberValidator1);

        cardNumber.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                cardNumber.validate();
            }
        });

        cardNumber.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("(\\d)")) {
                cardNumber.validate();
            }
        });

        requiredFieldValidator2 = new RequiredFieldValidator();
        requiredFieldValidator2.setIcon(new ImageView(getClass().getResource("images/warning.png").toString()));
        requiredFieldValidator2.setMessage("Empty Field");

        cardHolder.getValidators().add(requiredFieldValidator2);
        cardHolder.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                cardHolder.validate();
            }
        });
        cardHolder.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                cardHolder.resetValidation();
            } else if (newValue.isEmpty()) {
                cardHolder.validate();
            }
        });

        numberValidator2 = new NumberValidator();
        numberValidator2.setIcon(new ImageView(getClass().getResource("images/warning.png").toString()));
        numberValidator2.setMessage("Numbers Only");

        requiredFieldValidator3 = new RequiredFieldValidator();
        requiredFieldValidator3.setIcon(new ImageView(getClass().getResource("images/warning.png").toString()));
        requiredFieldValidator3.setMessage("Empty");

        cardCVC.getValidators().add(requiredFieldValidator3);
        cardCVC.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                cardCVC.validate();
            }
        });
        cardCVC.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
//                cardCVC.resetValidation();
            } else if (newValue.isEmpty()) {
                cardCVC.validate();
            }
        });

        cardCVC.getValidators().add(numberValidator2);

        cardCVC.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                cardCVC.validate();
            }
        });

        cardCVC.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("(\\d)")) {
                cardCVC.validate();
            }
        });

        requiredFieldValidator4 = new RequiredFieldValidator();
        requiredFieldValidator4.setIcon(new ImageView(getClass().getResource("images/warning.png").toString()));
        requiredFieldValidator4.setMessage("Empty");

        cardExpiry.getValidators().add(requiredFieldValidator4);
        cardExpiry.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                cardExpiry.validate();
            }
        });
        cardExpiry.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                cardExpiry.resetValidation();
            } else if (newValue.isEmpty()) {
                cardExpiry.validate();
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupValidations();
    }
}

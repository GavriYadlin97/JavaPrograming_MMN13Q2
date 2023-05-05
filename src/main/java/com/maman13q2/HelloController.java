package com.maman13q2;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

public class HelloController {
    @FXML
    private TitledPane appetizerPane;
    @FXML
    private TitledPane mainCoursePane;
    @FXML
    private TitledPane dessertPane;
    @FXML
    private TitledPane drinksPane;
    private Order order;
    private final ObservableList<Integer> quantity = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    @FXML
    void appetizerClicked(MouseEvent event) {
        mainCoursePane.setExpanded(false);
        dessertPane.setExpanded(false);
        drinksPane.setExpanded(false);
    }

    @FXML
    void mainCourseClicked(MouseEvent event) {
        appetizerPane.setExpanded(false);
        dessertPane.setExpanded(false);
        drinksPane.setExpanded(false);
    }

    @FXML
    void dessertClicked(MouseEvent event) {
        appetizerPane.setExpanded(false);
        mainCoursePane.setExpanded(false);
        drinksPane.setExpanded(false);
    }

    @FXML
    void drinksClicked(MouseEvent event) {
        appetizerPane.setExpanded(false);
        mainCoursePane.setExpanded(false);
        dessertPane.setExpanded(false);
    }

    @FXML
    void orderBtnClicked(ActionEvent event) {
        ButtonType confirmOrder = new ButtonType("Confirm Order");
        ButtonType updateOrder = new ButtonType("Update Order");
        ButtonType cancelOrder = new ButtonType("Cancel Order");

        Alert alert = new Alert(Alert.AlertType.INFORMATION, order.toString(), confirmOrder, updateOrder, cancelOrder);
        alert.setHeaderText("Please confirm Order.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent())  //if UpdateOrder, do nothing: let the user continue with the order as is
            if (result.get() == confirmOrder) {
                confirmOrder();
                cancelOrder();
            } else if (result.get() == cancelOrder)
                cancelOrder();
    }

    public void initialize() {
        order = new Order();
        Menu menu = readMenuFromFile();
        loadData(menu);
    }

    private void cancelOrder() {
        order = new Order();
        appetizerPane.getContent().setDisable(true);
        mainCoursePane.getContent().setDisable(true);
        dessertPane.getContent().setDisable(true);
        drinksPane.getContent().setDisable(true);
    }

    private void confirmOrder() {
        TextInputDialog inputDialog = new TextInputDialog();

        inputDialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(true);
        inputDialog.setContentText("Please enter your name");
        inputDialog.showAndWait();

        StringBuilder str = new StringBuilder(inputDialog.getEditor().getText());

        inputDialog.setContentText("Please enter your ID");
        inputDialog.getEditor().clear();
        inputDialog.showAndWait();

        str.append(inputDialog.getEditor().getText());

        createFile(str.toString());
    }

    private void createFile (String fileName) {
        try {
            Formatter output = new Formatter(fileName + ".txt");
            try {
                output.format(order.toString());
            } catch (NoSuchElementException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Couldn't write to file" + new File(fileName + ".txt").getAbsolutePath(), ButtonType.OK);
                alert.showAndWait();
            }
            output.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadData(Menu menu) {
        Item item;
        while ((item = menu.getNext()) != null) {
            setData(idGrid(item), item);
        }
    }

    private void setData(GridPane gridPane, Item item) {
        if (gridPane == null)
            return;
        CheckBoxWItem chkBox = new CheckBoxWItem(item);
        ComboBoxWItem comBox = new ComboBoxWItem(quantity, item);
        Label pricePerUnitLabel = new Label(item.getPrice() + "$");
        Label totalPrice = new Label("0$");
        //Add visual stuff into grid
        gridPane.addRow(gridPane.getRowCount(), chkBox, comBox, pricePerUnitLabel, totalPrice);
        //Visual adjustment
        setVisual(chkBox, comBox, totalPrice, pricePerUnitLabel);
        //Listeners
        setListenerChkBox(item, chkBox, comBox, totalPrice);
        setListenerComBox(item, chkBox, comBox, totalPrice);
        setListenerGridPane(gridPane, chkBox, comBox); //for clearing when canceling an order
    }

    private void setVisual(Node... nodes) {
        final int MARGIN = 5;
        for (Node node : nodes)
            GridPane.setMargin(node, new Insets(0, MARGIN, 0, MARGIN));
    }

    private void setListenerChkBox(Item item, CheckBoxWItem chkBox, ComboBoxWItem comBox, Label totalPrice) {
        chkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableVal, Boolean oldVal, Boolean newVal) {
                if (!newVal) {
                    try {
                        order.removeFromOrder(item, comBox.getValue());
                    } catch (NullPointerException ignored) {
                    }
                    comBox.setValue(null);
                    totalPrice.setText("0$");
                } else if (comBox.getValue() != null) {
                    totalPrice.setText(item.getPrice() * comBox.getValue() + "$");
                    order.addToOrder(item, comBox.getValue());
                }
            }
        });
    }

    private void setListenerComBox(Item item, CheckBoxWItem chkBox, ComboBoxWItem comBox, Label totalPrice) {
        comBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableVal, Integer oldVal, Integer newVal) {
                if (chkBox.isSelected()) {
                    totalPrice.setText(item.getPrice() * newVal + "$");
                    try {
                        order.removeFromOrder(item, oldVal);
                    } catch (NullPointerException ignored) {
                    } finally {
                        order.addToOrder(item, newVal);
                    }
                }
            }
        });
    }

    private void setListenerGridPane(GridPane gridPane, CheckBoxWItem chkBox, ComboBoxWItem comBox) {
        gridPane.disableProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                chkBox.setSelected(false);
                comBox.setValue(null);
                gridPane.setDisable(false);
            }
        });
    }

    private Menu readMenuFromFile() {
        Scanner input = readFile();
        Menu menu = new Menu();
        while (input.hasNextLine()) {
            String description = "", typeStr = "", price = "0";
            try {
                description = input.nextLine();
                typeStr = input.nextLine();
                price = input.nextLine();
            } catch (NoSuchElementException e) {
                corruptedDataMSG(input);
            }
            try {
                menu.addToMenu(new Item(description, Integer.parseInt(price), idType(typeStr)));
            } catch (IllegalArgumentException e) {
                corruptedDataMSG(input);
            }
        }
        closeFile(input);
        return menu;
    }

    private GridPane idGrid(Item item) {
        switch (item.getType()) {
            case Appetizer:
                return ((GridPane) (appetizerPane.getContent()));
            case MainCourse:
                return ((GridPane) (mainCoursePane.getContent()));
            case Dessert:
                return ((GridPane) (dessertPane.getContent()));
            case Drink:
                return ((GridPane) (drinksPane.getContent()));
            default:
                return null;
        }
    }

    private Scanner readFile() {
        try {
            return new Scanner(new File("menu.txt"));
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot find " + new File("menu.txt").getAbsolutePath() + "\nProgram cannot resume", ButtonType.OK);
            alert.showAndWait();
            System.exit(-1);
        }
        return null;
    }

    private TypeOfItem idType(String typeStr) {
        if (typeStr.equalsIgnoreCase("appetizer"))
            return TypeOfItem.Appetizer;
        else if (typeStr.equalsIgnoreCase("main course"))
            return TypeOfItem.MainCourse;
        else if (typeStr.equalsIgnoreCase("dessert"))
            return TypeOfItem.Dessert;
        else if (typeStr.equalsIgnoreCase("drink"))
            return TypeOfItem.Drink;
        else
            return null;
    }

    private void closeFile(Scanner input) {
        try {
            input.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot close " + new File("menu.txt").getAbsolutePath() + "\nProgram cannot resume", ButtonType.OK);
            alert.showAndWait();
            System.exit(-1);
        }
    }

    private void corruptedDataMSG(Scanner input) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Data in\"" + new File("menu.txt").getAbsolutePath() + "\" is corrupted\nProgram cannot resume", ButtonType.OK);
        alert.showAndWait();
        closeFile(input);
        System.exit(-1);
    }
}
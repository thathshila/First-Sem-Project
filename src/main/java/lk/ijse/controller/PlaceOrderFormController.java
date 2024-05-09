package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.model.*;
import lk.ijse.model.tm.CartTm;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lk.ijse.repository.*;

public class PlaceOrderFormController {

    @FXML
    private Button btnADD;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnPlaceOrder;


    @FXML
    private TableColumn<?, ?> colItemId;

    @FXML
    private TableColumn<?, ?> colItemName;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TextField txtContact;

    @FXML
    private JFXComboBox<String> combItemId;

    @FXML
    private JFXComboBox<String> combNIC;

    @FXML
    private JFXComboBox<String> combUserId;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<CartTm> tblPlaceOrder;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextArea txtAreaNetBalance;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtOrderId;

    @FXML
    private TextField txtQtyOnHand;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtItemName;

    private ObservableList<CartTm> obList = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        setDate();
        getCustomerNIC();
        getItemIds();
        getUserId();
        setCellValueFactory();
        getCurrentOrderId();
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        txtDate.setText(String.valueOf(now));
    }

    private void setCellValueFactory() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("ItemId"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("Total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
    }



    private void getCustomerNIC() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> nicList = CustomerRepo.getNIC();

            for (String nic : nicList) {
                obList.add(nic);
            }

            combNIC.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getItemIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> itemIdList = ItemRepo.getItemId();

            for (String itemId : itemIdList) {
                obList.add(itemId);
            }
            combItemId.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getUserId() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> userList = UserRepo.getIds();
            for (String id : userList) {
                obList.add(id);
            }
            combUserId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCurrentOrderId() throws SQLException {
        String currentId = OrderRepo.getCurrentId();

        String nextOrderId = generateNextOrderId(currentId);
        txtOrderId.setText(nextOrderId);
    }

    private String generateNextOrderId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("O");
            int idNum = Integer.parseInt(split[1]);
            return "0" + ++idNum;
        }
        return "01";
    }

    @FXML
    void btnADDOnAction(ActionEvent event) {
        String ItemId = combItemId.getValue();
        String ItemName = txtItemName.getText();
        int Quantity = Integer.parseInt(txtQuantity.getText());
        double Price = Double.parseDouble(txtPrice.getText());
        double Total = Quantity * Price;
        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int selectedIndex = tblPlaceOrder.getSelectionModel().getSelectedIndex();
                obList.remove(selectedIndex);

                tblPlaceOrder.refresh();
                calculateNetTotal();
            }
        });

        for (int i = 0; i < tblPlaceOrder.getItems().size(); i++) {
            if (ItemId.equals(colItemId.getCellData(i))) {

                CartTm tm = obList.get(i);
                Quantity += tm.getQuantity();
                Total = Quantity * Price;

                tm.setQuantity(Quantity);
                tm.setTotal(Total);

                tblPlaceOrder.refresh();

                calculateNetTotal();
                return;
            }
        }


        CartTm tm = new CartTm(ItemId, ItemName, Quantity, Price, Total, btnRemove);
        obList.add(tm);

        tblPlaceOrder.setItems(obList);
        calculateNetTotal();
        txtQuantity.setText("");

    }

    private void calculateNetTotal() {
        int netBalance = 0;
        for (int i = 0; i < tblPlaceOrder.getItems().size(); i++) {
            netBalance += (double) colTotal.getCellData(i);
        }
        txtAreaNetBalance.setText(String.valueOf(netBalance));
        }


    @FXML
        void btnBackOnAction (ActionEvent event) throws IOException {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            Stage stage = (Stage) rootNode.getScene().getWindow();

            stage.setScene(new Scene(anchorPane));
            stage.setTitle("Main Form");
            stage.centerOnScreen();
        }

        @FXML
        void btnNewOnAction (ActionEvent event) throws IOException {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"));
            Stage stage = (Stage) rootNode.getScene().getWindow();

            stage.setScene(new Scene(anchorPane));
            stage.setTitle("Customer Form");
            stage.centerOnScreen();
        }


        @FXML
        void combItemIdOnAction (ActionEvent event){
            String item_id = (String) combItemId.getValue();

            try {
                Item item = ItemRepo.searchItem(item_id);

                txtItemName.setText(item.getName());
                txtQtyOnHand.setText(String.valueOf(item.getQty()));
                txtPrice.setText(String.valueOf(item.getPrice()));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @FXML
        void combNICOnAction (ActionEvent event){
            String nic =  combNIC.getValue();

            try {
                Customer customer = CustomerRepo.SEARCHbyNic(nic);

                txtCustomerId.setText(customer.getCustomer_id());
                txtCustomerName.setText(customer.getCustomer_name());
                txtAddress.setText(customer.getAddress());
                txtContact.setText(String.valueOf(customer.getContact()));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @FXML
        void btnPlaceOrderOnAction (ActionEvent event){
            String orderId = txtOrderId.getText();
            Date date = Date.valueOf(LocalDate.now());
            double Price = Double.parseDouble(txtPrice.getText());
            String cusId = txtCustomerId.getText();
            String userId = combUserId.getValue();




            var order = new Order(orderId,date,Price,cusId,userId);

            List<OrderItem>  odList = new ArrayList<>();

            for (int i = 0; i < tblPlaceOrder.getItems().size(); i++){
                CartTm tm = obList.get(i);

                OrderItem od = new OrderItem(
                        orderId,
                        tm.getItemId(),
                        tm.getQuantity(),
                        tm.getPrice()
                );
                odList.add(od);
            }

            PlaceOrder po = new PlaceOrder(order, odList);
            try {
                boolean isPlaced = PlaceOrderRepo.placeOrder(po);
                if(isPlaced) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Order Placed!").show();
                }else {
                    new Alert(Alert.AlertType.WARNING, "Order Placed Unsuccessfully!").show();
                }
                }catch (SQLException e){
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

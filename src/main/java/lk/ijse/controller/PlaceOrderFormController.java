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
import lk.ijse.db.DbConnection;
import lk.ijse.model.*;
import lk.ijse.model.tm.CartTm;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

import lk.ijse.repository.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;

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
    private TextField txtItemId;
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
    public TextField txtNIC;

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

    @FXML
    private Label lblBalance;

    private ObservableList<CartTm> obList = FXCollections.observableArrayList();

    public void initialize() throws SQLException {
        setDate();
        getUserId();
        setCellValueFactory();
        getCurrentOrderId();
        setNIC();
        setItemName();
    }

    private void setNIC() throws SQLException {
        List<String> nic = CustomerRepo.getNIC();
        ObservableList<String> obList = FXCollections.observableArrayList();

        for (String n : nic){
            obList.add(n);
        }

        TextFields.bindAutoCompletion(txtNIC, obList);
    }

  public void   setItemName() throws SQLException {
      List<String> item = ItemRepo.getItem();
      ObservableList<String> obList = FXCollections.observableArrayList();

      for (String n : item){
          obList.add(n);
      }

      TextFields.bindAutoCompletion(txtItemName, obList);
  }
    private void getCurrentOrderId() {
        try {
            String currentId = OrderRepo.getCurrentId();

            String nextOrderId = generateNextOrderId(currentId);
            txtOrderId.setText(nextOrderId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextOrderId(String currentId) {
        if(currentId != null) {
            String[] split = currentId.split("O");
            int idNum = Integer.parseInt(split[1]);
            return "O" + ++idNum;
        }
        return "O1";
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

    @FXML
    void btnADDOnAction(ActionEvent event) {
        String ItemId = txtItemId.getText();
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
        lblBalance.setText(String.valueOf(netBalance));
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


    public void txtItemNameOnAction(ActionEvent actionEvent) throws IOException {
        String itemName = txtItemName.getText();

        try {
            Item item = ItemRepo.searchItem(itemName);
            txtItemId.setText(item.getItem_id());
            txtQtyOnHand.setText(String.valueOf(item.getQty()));
            txtPrice.setText(String.valueOf(item.getPrice()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void txtNICOnAction(ActionEvent actionEvent) throws IOException {
            String nic =  txtNIC.getText();

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
        void btnPlaceOrderOnAction (ActionEvent event) throws JRException, SQLException {
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
                    obList.clear();
                    tblPlaceOrder.setItems(obList);
                    calculateNetTotal();
                    makeOrderBill();
                    getCurrentOrderId();

                }else {
                    new Alert(Alert.AlertType.WARNING, "Order Placed Unsuccessfully!").show();
                }
            }catch (SQLException e){
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
            clearFields();
        }

    public void makeOrderBill() throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/ORDERBILL.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("OrderID",txtOrderId.getText());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint, false);
    }

    public void clearFields(){
        txtItemName.clear();
        txtOrderId.clear();
        txtQuantity.clear();
        txtPrice.clear();
        txtContact.clear();
        txtPrice.clear();
        txtAreaNetBalance.clear();
        txtDate.clear();
        txtNIC.clear();
        txtQtyOnHand.clear();
        txtCustomerName.clear();
        txtCustomerId.clear();
        combUserId.setValue("");
        txtAddress.clear();
        txtItemId.clear();
    }

}


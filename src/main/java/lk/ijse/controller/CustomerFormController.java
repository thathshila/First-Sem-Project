package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.DbConnection;
import lk.ijse.model.Customer;
import lk.ijse.model.tm.CustomerTm;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.repository.EmployeeRepo;
import lk.ijse.util.Regex;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;


public class CustomerFormController {

    public AnchorPane rootNode;
    @FXML
    private Button btnBACK;

    @FXML
    private Button btnCLEAR;

    @FXML
    private Button btnDELETE;

    @FXML
    private Button btnSAVE;

    @FXML
    private Button btnSEARCH;

    @FXML
    private Button btnUPDATE;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colCustomerId;

    @FXML
    private TableColumn<?, ?> colNIC;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableView<CustomerTm> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtNICNumber;

    public void initialize() throws SQLException {
        setDate();
        setCellValueFactory();
        loadAllCustomers();
        getCurrentCustomerId();
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        txtDate.setText(String.valueOf(now));
    }

    private void setCellValueFactory() {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("Customer_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Customer_name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("Nic"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
    }

    private void loadAllCustomers() {
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        try {
            List<Customer> customerList = CustomerRepo.getAll();
            for (Customer customer : customerList) {
                CustomerTm tm = new CustomerTm(
                        customer.getCustomer_id(),
                        customer.getCustomer_name(),
                        customer.getContact(),
                        customer.getAddress(),
                        customer.getNic(),
                        customer.getDate()
                );

                obList.add(tm);
            }

            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnBACKOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) rootNode.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Main Form");
        stage.centerOnScreen();
    }

    @FXML
    void btnCLEAROnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtCustomerId.setText("");
        txtCustomerName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        txtNICNumber.setText("");
        txtDate.setText("");
    }

    @FXML
    void btnDELETEOnAction(ActionEvent event) {
        String Customer_id = txtCustomerId.getText();

        try {
            boolean isDeleted = CustomerRepo.DELETE(Customer_id);
            initialize();
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "customer deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSAVEOnAction(ActionEvent event) {
        String Customer_id = txtCustomerId.getText();
        String Customer_name = txtCustomerName.getText();
        int Contact = Integer.parseInt(txtContact.getText());
        String Address = txtAddress.getText();
        String Nic = txtNICNumber.getText();
        Date date = Date.valueOf(LocalDate.now());

        try {
            if (isValied()) {
                boolean isSave = CustomerRepo.SAVE(Customer_id, Customer_name, Contact, Address,Nic, date);
                if (isSave) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer saved..", ButtonType.OK).show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Customer not saved..", ButtonType.OK).show();
                }
            }else {
                return;
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllCustomers();
    }

    @FXML
    void btnSEARCHOnAction(ActionEvent event) throws SQLException {
        String id = txtCustomerId.getText();

        Customer customer = CustomerRepo.SEARCH(id);
        if (customer != null) {
            txtCustomerId.setText(customer.getCustomer_id());
            txtCustomerName.setText(customer.getCustomer_name());
            txtContact.setText(String.valueOf(customer.getContact()));
            txtAddress.setText(customer.getAddress());
            txtNICNumber.setText(customer.getNic());
            LocalDate now = LocalDate.now();
            txtDate.setText(String.valueOf(now));
        } else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
    }

    @FXML
    void btnUPDATEOnAction(ActionEvent event) {
        String Customer_id = txtCustomerId.getText();
        String Customer_name = txtCustomerName.getText();
        int Contact = Integer.parseInt(txtContact.getText());
        String Address = txtAddress.getText();
        String Nic = txtNICNumber.getText();
        Date date = java.sql.Date.valueOf(txtDate.getText());

        Customer customer = new Customer(Customer_id, Customer_name, Contact, Address, Nic, date);

        try {
            boolean isUpdated = CustomerRepo.UPDATE(customer);
            if (isUpdated) {
                initialize();
                new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void getCurrentCustomerId() throws SQLException {
        String currentId = CustomerRepo.getCurrentId();

        String nextCustomerId = generateNextCustomerId(currentId);
        txtCustomerId.setText(nextCustomerId);

    }

    private String generateNextCustomerId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("C00");
            int idNum = Integer.parseInt(split[1]);
            return "C00" + ++idNum;
        }
        return "C001";
    }

    public void tblCustomerOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblCustomer.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtCustomerId.setText(colCustomerId.getCellData(index).toString());
        txtCustomerName.setText(colName.getCellData(index).toString());
        txtContact.setText(colContact.getCellData(index).toString());
        txtAddress.setText(colAddress.getCellData(index).toString());
        txtNICNumber.setText(colNIC.getCellData(index).toString());
        txtDate.setText(colDate.getCellData(index).toString());
    }

    public void txtCustomerIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.ID, txtCustomerId);
    }

    public void txtCustomerNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.NAME, txtCustomerName);
    }

    public void txtAddressOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.ADDRESS, txtAddress);
    }

    public void txtDateOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.DATE, txtDate);
    }

    public void txtContactOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.CONTACT, txtContact);
    }

    public void txtNICNumberOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.NIC, txtNICNumber);
    }

    public boolean isValied() {
        if (!Regex.setTextColor(lk.ijse.util.TextField.ID, txtCustomerId)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.NAME, txtCustomerName)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.ADDRESS, txtAddress)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.DATE, txtDate)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.CONTACT, txtContact)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.NIC, txtNICNumber)) return false;

        return true;
    }
    public void btnBillOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/reports/customer.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
       // data.put("CustomerID",txtCustomerId.getText());

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }
}





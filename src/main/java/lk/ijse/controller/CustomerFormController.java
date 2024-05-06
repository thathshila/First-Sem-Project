package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.model.Customer;
import lk.ijse.model.tm.CustomerTm;
import lk.ijse.repository.CustomerRepo;


import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;


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
        private TableColumn<?,?> colDate;

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

    public void initialize() {
        setDate();
        setCellValueFactory();
        loadAllCustomers();
    }
    private void setDate() {
        LocalDate now = LocalDate.now();
        txtDate.setText(String.valueOf(now));
    }

    private void setCellValueFactory() {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("Customer_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Customer_name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
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
                        customer.getAddress(),
                        customer.getContact(),
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
                if(isDeleted) {
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
            String Address = txtAddress.getText();
            String Contact = txtContact.getText();
            String Nic = txtNICNumber.getText();
            Date date = Date.valueOf(LocalDate.now());

            try {
                CustomerRepo.SAVE(Customer_id, Customer_name,Address,Contact,Nic, date);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }

            @FXML
        void btnSEARCHOnAction(ActionEvent event) throws SQLException {
            String id = txtCustomerId.getText();

            Customer customer = CustomerRepo.SEARCH(id);
            if (customer != null) {
                txtCustomerId.setText(customer.getCustomer_id());
                txtCustomerName.setText(customer.getCustomer_name());
                txtContact.setText(customer.getContact());
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
            String Contact = txtContact.getText();
            String Address = txtAddress.getText();
            String Nic = txtNICNumber.getText();
            Date date = Date.valueOf(LocalDate.now());

            Customer customer = new Customer(Customer_id,Customer_name, Contact,Address,Nic,date);

            try {
                boolean isUpdated = CustomerRepo.UPDATE(customer);
                if(isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "customer updated!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }


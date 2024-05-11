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
import lk.ijse.model.Supplier;
import lk.ijse.model.tm.SupplierTm;
import lk.ijse.repository.EmployeeRepo;
import lk.ijse.repository.SupplierRepo;
import lk.ijse.util.Regex;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SupplierFormController {

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
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colProductName;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableColumn<?, ?> colNIC;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableView<SupplierTm> tblSupplier;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtNIC;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtProductName;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtSupplierId;

    @FXML
    private TextField txtSupplierName;

    @FXML
    private TextField txtUnitPrice;

    public void initialize() throws SQLException {
        setDate();
        setCellValueFactory();
        loadAllSuppliers();
        getCurrentSupplierId();
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        txtDate.setText(String.valueOf(now));
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("Supplier_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Supplier_name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("NIC"));
    }

    private void loadAllSuppliers() {
        ObservableList<SupplierTm> spList = FXCollections.observableArrayList();

        try {
            List<Supplier> supplierList = SupplierRepo.getAll();
            for (Supplier supplier : supplierList) {
                SupplierTm tm = new SupplierTm(
                        supplier.getSupplier_id(),
                        supplier.getSupplier_name(),
                        supplier.getAddress(),
                        supplier.getContact(),
                        supplier.getQuantity(),
                        supplier.getPrice(),
                        supplier.getProductName(),
                        supplier.getDate(),
                        supplier.getNIC()
                );

                spList.add(tm);
            }

            tblSupplier.setItems(spList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCurrentSupplierId() throws SQLException {
        String currentId = SupplierRepo.getCurrentId();

        String nextSupplierId = generateNextSupplierId(currentId);
        txtSupplierId.setText(nextSupplierId);
    }

    private String generateNextSupplierId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("S00");
            int idNum = Integer.parseInt(split[1]);
            return "S00" + ++idNum;
        }
        return "S001";
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
        txtSupplierId.setText("");
        txtSupplierName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        txtNIC.setText("");
        txtQuantity.setText("");
        txtProductName.setText("");
        txtUnitPrice.setText("");
        txtDate.setText("");
    }

    @FXML
    void btnDELETEOnAction(ActionEvent event) {
        String Supplier_id = txtSupplierId.getText();

        try {
            boolean isDeleted = SupplierRepo.DELETE(Supplier_id);
            initialize();
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "supplier deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSAVEOnAction(ActionEvent event) {
        String Supplier_id = txtSupplierId.getText();
        String Supplier_name = txtSupplierName.getText();
        String Address = txtAddress.getText();
        int Contact = Integer.parseInt(txtContact.getText());
        int Quantity = Integer.parseInt(txtQuantity.getText());
        double Price = Double.parseDouble(txtUnitPrice.getText());
        String Product = txtProductName.getText();
        Date Date = java.sql.Date.valueOf(txtDate.getText());
        String Nic = txtNIC.getText();

        try {
            if (isValied()) {
                boolean isSave = SupplierRepo.SAVE(Supplier_id, Supplier_name, Address, Contact, Quantity, Price, Product, Date, Nic);
                if (isSave) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Employee saved..", ButtonType.OK).show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Employee not saved..", ButtonType.OK).show();
                }
            }else {
                return;
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllSuppliers();
    }

    @FXML
    void btnSEARCHOnAction(ActionEvent event) throws SQLException {
        String id = txtSupplierId.getText();

        Supplier supplier = SupplierRepo.SEARCH(id);
        if (supplier != null) {
            txtSupplierId.setText(supplier.getSupplier_id());
            txtSupplierName.setText(supplier.getSupplier_name());
            txtAddress.setText(supplier.getAddress());
            txtContact.setText(String.valueOf(supplier.getContact()));
            txtProductName.setText(supplier.getProductName());
            txtDate.setText(String.valueOf(supplier.getDate()));
            txtUnitPrice.setText(String.valueOf(supplier.getPrice()));
            txtQuantity.setText(String.valueOf(supplier.getQuantity()));
            txtNIC.setText(supplier.getNIC());

        } else {
            new Alert(Alert.AlertType.INFORMATION, "supplier not found!").show();
        }
    }

    @FXML
    void btnUPDATEOnAction(ActionEvent event) {
        String Supplier_id = txtSupplierId.getText();
        String Supplier_name = txtSupplierName.getText();
        String Address = txtAddress.getText();
        int Contact = Integer.parseInt(txtContact.getText());
        int Quantity = Integer.parseInt(txtQuantity.getText());
        double Price = Double.parseDouble(txtUnitPrice.getText());
        String Product = txtProductName.getText();
        Date Date = java.sql.Date.valueOf(txtDate.getText());
        String Nic = txtNIC.getText();

        Supplier supplier = new Supplier(Supplier_id, Supplier_name, Address, Contact, Quantity, Price, Product, Date, Nic);


        try {
            boolean isUpdated = SupplierRepo.UPDATE(supplier);
            if (isUpdated) {
                initialize();
                new Alert(Alert.AlertType.CONFIRMATION, "supplier updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void tblSupplierOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblSupplier.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtSupplierId.setText(colId.getCellData(index).toString());
        txtSupplierName.setText(colName.getCellData(index).toString());
        txtAddress.setText(colAddress.getCellData(index).toString());
        txtContact.setText(colContact.getCellData(index).toString());
        txtQuantity.setText(colQuantity.getCellData(index).toString());
        txtUnitPrice.setText(colUnitPrice.getCellData(index).toString());
        txtProductName.setText(colProductName.getCellData(index).toString());
        txtDate.setText(colDate.getCellData(index).toString());
        txtNIC.setText(colNIC.getCellData(index).toString());

    }

    public void txtAddressOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.ADDRESS, txtSupplierId);
    }

    public void txtProductNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.NAME, txtProductName);
    }

    public void txtQuantityOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.QUANTITY, txtQuantity);
    }

    public void txtUnitPriceOnKeyRelaesed(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.PRICE, txtUnitPrice);
    }

    public void txtNICOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.NIC, txtNIC);
    }

    public void txtContactOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.CONTACT, txtContact);
    }


    public void txtSupplierNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.NAME, txtSupplierName);
    }

    public void txtSupplierIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.ID, txtSupplierId);
    }

    public boolean isValied() {
        if (!Regex.setTextColor(lk.ijse.util.TextField.ID, txtSupplierId)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.NAME, txtSupplierName)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.ADDRESS, txtAddress)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.DATE, txtDate)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.CONTACT, txtContact)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.NIC, txtNIC)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.NAME, txtProductName)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.PRICE, txtUnitPrice)) return false;

        return true;
    }

}
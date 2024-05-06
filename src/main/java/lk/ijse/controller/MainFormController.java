package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainFormController {

    @FXML
    private AnchorPane anpmain1;

    @FXML
    private AnchorPane anpmain2;

    @FXML
    private AnchorPane anpmain3;

    @FXML
    private JFXButton btnCustomer;

    @FXML
    private JFXButton btnDashboard;

    @FXML
    private JFXButton btnEmployee;

    @FXML
    private JFXButton btnOrder;

    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private JFXButton btnSupplier;


    public void initialize() throws IOException {
        loadDashboardForm();
    }

    private void loadDashboardForm() throws IOException {
        AnchorPane dashboardPane = FXMLLoader.load(this.getClass().getResource("/view/DashboardForm.fxml"));


        anpmain1.getChildren().clear();
        anpmain1.getChildren().add(dashboardPane);
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) throws IOException {
        AnchorPane customerPane = FXMLLoader.load(this.getClass().getResource("/view/CustomerForm.fxml"));


        anpmain1.getChildren().clear();
        anpmain1.getChildren().add(customerPane);
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
        AnchorPane dashbordPane = FXMLLoader.load(this.getClass().getResource("/view/DashboardForm.fxml"));


        anpmain1.getChildren().clear();
        anpmain1.getChildren().add(dashbordPane);
    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) throws IOException {
        AnchorPane employeePane = FXMLLoader.load(this.getClass().getResource("/view/EmployeeForm.fxml"));


        anpmain1.getChildren().clear();
        anpmain1.getChildren().add(employeePane);
    }

    @FXML
    void btnOrderOnAction(ActionEvent event) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/OrderForm.fxml"));


        anpmain1.getChildren().clear();
        anpmain1.getChildren().add(orderPane);
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws IOException {
        AnchorPane placePane = FXMLLoader.load(this.getClass().getResource("/view/PlaceOrderForm.fxml"));


        anpmain1.getChildren().clear();
        anpmain1.getChildren().add(placePane);
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) throws IOException {
        AnchorPane supplierPane = FXMLLoader.load(this.getClass().getResource("/view/SupplierForm.fxml"));


        anpmain1.getChildren().clear();
        anpmain1.getChildren().add(supplierPane);
    }

}

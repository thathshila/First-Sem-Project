package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.db.DbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainFormController {

    @FXML
    private AnchorPane anpmain1;

    @FXML
    private AnchorPane anpmain2;

    @FXML
    private AnchorPane anpmain3;

    @FXML
    private Button btnCustomer;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnItem;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnPlaceOrder;

    @FXML
    private Button btnSupplier;

    @FXML
    private Label lblUser;

    private String user;
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
    void btnItemOnAction(ActionEvent event) throws IOException {
        AnchorPane orderPane = FXMLLoader.load(this.getClass().getResource("/view/ItemForm.fxml"));


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

    @FXML
    public void btnExitOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane supplierPane = FXMLLoader.load(this.getClass().getResource("/view/LoginForm.fxml"));


        anpmain1.getChildren().clear();
        anpmain1.getChildren().add(supplierPane);
    }


    private String getUserName(String id) throws SQLException {
        String sql = "SELECT User_name FROM User;";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("User_name");
        }
        return null;
    }


    String userId;

    public void setUserID(String userId) {
        this.userId = userId;

        try {

            user = getUserName(userId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setUserName(user);
    }

    private void setUserName(String user) {
        lblUser.setText(String.valueOf(user));
    }
}
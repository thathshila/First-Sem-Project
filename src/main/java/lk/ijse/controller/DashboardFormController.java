package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.DbConnection;
import lk.ijse.repository.OrderRepo;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;


public class DashboardFormController implements Initializable{

    public AnchorPane rootNode;
    public AnchorPane rootNode1;
    @FXML
    public Label lblCustomerCount;

    private int customerCount;

    @FXML
    private Label lblSupplierCount;

    @FXML
    private Label lblEmployeeCount;

    private  int employeeCount;
    private int supplierCount;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            customerCount = getCustomerCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setCustomerCount(customerCount);

        try {
            supplierCount = getSupplierCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setSupplierCount(supplierCount);

        try {
            employeeCount = getEmployeeCount();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setEmployeeCount(employeeCount);
    }


    private void setEmployeeCount(int employeeCount){
        lblEmployeeCount.setText(String.valueOf(employeeCount));
    }

    private void setCustomerCount(int customerCount) {
        lblCustomerCount.setText(String.valueOf(customerCount));
    }

    private void setSupplierCount(int supplierCount){ lblSupplierCount.setText(String.valueOf(supplierCount));}
    private int getCustomerCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS customer_count FROM Customer";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("customer_count");
        }
        return 0;
    }
    private int getSupplierCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS supplier_count FROM Supplier";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("supplier_count");
        }
        return 0;
    }
    private int getEmployeeCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS employee_count FROM Employee";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("employee_count");
        }
        return 0;
    }
}


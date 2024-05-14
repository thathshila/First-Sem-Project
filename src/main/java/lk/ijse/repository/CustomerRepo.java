package lk.ijse.repository;

import javafx.scene.control.Alert;
import lk.ijse.db.DbConnection;
import lk.ijse.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {
    public static List<Customer> getAll() throws SQLException {
        String sql = "SELECT * FROM Customer";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Customer> cusList = new ArrayList<>();

        while (resultSet.next()) {
            String Customer_id = resultSet.getString(1);
            String Customer_name = resultSet.getString(2);
            int Contact = Integer.parseInt(resultSet.getString(3));
            String Address = resultSet.getString(4);
            String Nic = resultSet.getString(5);
            Date date = Date.valueOf(resultSet.getString(6));

            Customer customer = new Customer(Customer_id, Customer_name, Contact, Address, Nic, date);

            cusList.add(customer);
        }
        return cusList;
    }

    public static boolean DELETE(String id) throws SQLException {
        String sql = "DELETE FROM Customer WHERE Customer_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }


    public static Customer SEARCH(String contact) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE Contact = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, contact);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String Customer_id = resultSet.getString(1);
            String Customer_name = resultSet.getString(2);
            int Contact = Integer.parseInt(resultSet.getString(3));
            String Address = resultSet.getString(4);
            String Nic = resultSet.getString(5);
            Date date = resultSet.getDate(6);

            Customer customer = new Customer(Customer_id, Customer_name, Contact, Address, Nic, date);
            return customer;
        }

        return null;
    }

    public static boolean UPDATE(Customer customer) throws SQLException {
        String sql = "UPDATE Customer SET Customer_name = ?, Contact = ?, Nic = ? , Address = ? , Date = ? WHERE Customer_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, customer.getCustomer_name());
        pstm.setObject(2, customer.getContact());
        pstm.setObject(3, customer.getAddress());
        pstm.setObject(4, customer.getNic());
        pstm.setObject(5, customer.getDate());
        pstm.setObject(6, customer.getCustomer_id());
        return pstm.executeUpdate() > 0;
    }

    public static boolean SAVE(String customerId, String customerName, int contact, String address, String nic, Date date) throws SQLException {
        String sql = "INSERT INTO Customer VALUES(?, ?, ?, ?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, customerId);
        pstm.setObject(2, customerName);
        pstm.setObject(3, contact);
        pstm.setObject(4, address);
        pstm.setObject(5, nic);
        pstm.setObject(6, date);

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getNIC() throws SQLException {
        String sql = "SELECT Nic FROM Customer";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> nicList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            nicList.add(id);
        }
        return nicList;

    }

    public static Customer SEARCHbyNic(String nic) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE Nic = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, nic);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String Customer_id = resultSet.getString(1);
            String Customer_name = resultSet.getString(2);
            int Contact = Integer.parseInt(resultSet.getString(3));
            String Address = resultSet.getString(4);
            String Nic = resultSet.getString(5);
            Date Date = java.sql.Date.valueOf(resultSet.getString(6));

            Customer customer = new Customer(Customer_id, Customer_name, Contact, Address, Nic, Date);

            return customer;
        }

        return null;
    }

    public static String getCurrentId() throws SQLException {


        String sql = "SELECT Customer_id FROM Customer ORDER BY Customer_id DESC LIMIT 1";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String customerId = resultSet.getString(1);
            return customerId;
        }
        return null;
    }
}

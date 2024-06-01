package lk.ijse.repository;

import javafx.scene.control.Alert;
import lk.ijse.db.DbConnection;
import lk.ijse.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static lk.ijse.util.TextField.NIC;

public class SupplierRepo {
    public static List<Supplier> getAll() throws SQLException {
        String sql = "SELECT * FROM Supplier";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Supplier> supList = new ArrayList<>();

        while (resultSet.next()) {
            String Supplier_id = resultSet.getString(1);
            String Supplier_name = resultSet.getString(2);
            String Address = resultSet.getString(3);
            int Contact = Integer.parseInt(resultSet.getString(4));
            int Quantity = Integer.parseInt(resultSet.getString(5));
            double Price = resultSet.getDouble(6);
            String Product = resultSet.getString(7);
            Date Date = resultSet.getDate(8);
            String Nic = resultSet.getString(9);

            Supplier supplier = new Supplier(Supplier_id, Supplier_name, Address, Contact, Quantity, Price, Product, Date,Nic);
            supList.add(supplier);
        }
        return supList;
    }

    public static boolean DELETE(String id) throws SQLException {
        String sql = "DELETE FROM Supplier WHERE Supplier_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static boolean SAVE(String supplier_id, String supplier_name, String address, int contact, int quantity, double price, String product, Date date, String nic) throws SQLException {
        String sql = "INSERT INTO Supplier VALUES(?, ?, ?, ?,?,?,?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, supplier_id);
        pstm.setObject(2, supplier_name);
        pstm.setObject(3, address);
        pstm.setObject(4, contact);
        pstm.setObject(5, quantity);
        pstm.setObject(6, price);
        pstm.setObject(7, product);
        pstm.setObject(8, date);
        pstm.setObject(9, nic);

        return pstm.executeUpdate() > 0;
    }

    public static Supplier SEARCH(String contact) throws SQLException {
        String sql = "SELECT * FROM Supplier WHERE Contact = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, contact);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String Supplier_id = resultSet.getString(1);
            String Supplier_name = resultSet.getString(2);
            String Address = resultSet.getString(3);
            int Contact = Integer.parseInt(resultSet.getString(4));
            int Quantity = Integer.parseInt(resultSet.getString(5));
            double Price = resultSet.getDouble(6);
            String ProductName = resultSet.getString(7);
            Date Date = resultSet.getDate(8);
            String Nic = resultSet.getString(9);

            Supplier supplier = new Supplier(Supplier_id, Supplier_name, Address, Contact, Quantity, Price, ProductName,Date, Nic);
            return supplier;
        }

        return null;
    }

    public static boolean UPDATE(Supplier supplier) throws SQLException {
        String sql = "UPDATE Supplier SET Supplier_name = ?, Address = ?, Contact = ? , Quantity = ? , Price = ? , ProductName = ? ,Date= ? ,NIC = ? WHERE Supplier_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, supplier.getSupplier_name());
        pstm.setObject(2, supplier.getAddress());
        pstm.setObject(3, supplier.getContact());
        pstm.setObject(4, supplier.getQuantity());
        pstm.setObject(5, supplier.getPrice());
        pstm.setObject(6, supplier.getProductName());
        pstm.setObject(7, supplier.getDate());
        pstm.setObject(8, supplier.getNIC());
        pstm.setObject(9,supplier.getSupplier_id());
        return pstm.executeUpdate() > 0;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT Supplier_id FROM Supplier ORDER BY Supplier_id DESC LIMIT 1";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String supplierId = resultSet.getString(1);
            return supplierId;
        }
        return null;
    }
}

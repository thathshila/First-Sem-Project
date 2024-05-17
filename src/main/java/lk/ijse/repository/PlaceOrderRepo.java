package lk.ijse.repository;


import lk.ijse.db.DbConnection;


import lk.ijse.model.OrderItem;
import lk.ijse.model.PlaceOrder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PlaceOrderRepo {

    public static boolean placeOrder(PlaceOrder po) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            System.out.println("connection = " + connection);
            boolean isOrderSaved = OrderRepo.save(po.getOrder());
            System.out.println("isOrderSaved = " + isOrderSaved);
            if (isOrderSaved) {

                boolean isQtyUpdated = false;

                for (OrderItem items : po.getOdList()) {
                    isQtyUpdated = ItemRepo.UpdateQty(items);

                }
                System.out.println("isQtyUpdated = " + isQtyUpdated);


                if (isQtyUpdated) {

                    boolean isOrderDetailSaved = false;

                    for (OrderItem items : po.getOdList()) {
                        isOrderDetailSaved = ItemRepo.SaveItem(items);
                    }
                    System.out.println("isOrderDetailSaved = " + isOrderDetailSaved);

                    if (isOrderDetailSaved) {
                        connection.commit();
                        return true;
                    } else {
                        connection.rollback();
                    }
                } else {
                    connection.rollback();
                }
            } else {
                connection.rollback();
            }

        } catch (Exception e) {
            connection.rollback();
            throw new RuntimeException(e);
        } finally {
            connection.setAutoCommit(true);
        }
        return false;
    }

   public static String calculateNetTotal(String orderId) {
        double netTotal = 0.0;

        String sql = "SELECT SUM(i.Price * oi.qty) " +
                "FROM Items i " +
                "JOIN Order_Item oi ON i.Item_id = oi.Item_id " +
                "WHERE oi.orderId = ?";


        try (PreparedStatement statement = DbConnection.getInstance().getConnection().prepareStatement(sql)) {
            statement.setString(1, orderId);


            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    double c = resultSet.getDouble(1);
                    netTotal = netTotal + c;
                }
                return String.valueOf((netTotal));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}





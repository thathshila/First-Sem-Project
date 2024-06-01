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
}





package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OrderRepo {

    public static boolean save(Order order) throws SQLException {
        String sql = "INSERT INTO Orders VALUES(?,?, ?,?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, order.getOrder_id());
        pstm.setString(2, String.valueOf(order.getDate()));
        pstm.setString(3, String.valueOf(order.getPrice()));
        pstm.setString(4, order.getCustomer_id());
        pstm.setString(5, order.getUser_id());

        return pstm.executeUpdate() > 0;

    }

public static String getCurrentId() throws SQLException {
    String sql = "SELECT Order_id FROM Orders ORDER BY Order_id DESC LIMIT 1";

    PreparedStatement pstm = DbConnection.getInstance().getConnection()
            .prepareStatement(sql);

    ResultSet resultSet = pstm.executeQuery();
    if(resultSet.next()) {
        String Order_id = resultSet.getString(1);
        return Order_id;
    }
    return null;
}
}




package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.OrderItem;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderItemRepo {

    public static boolean Save(List<OrderItem> odList) throws SQLException {
        for (OrderItem od : odList){
            boolean isSaved = Save(od);
            if(!isSaved){
                return false;
            }
        }
        return true;
    }
   private static boolean Save(OrderItem od) throws SQLException {
       String sql = "INSERT INTO Order_ITEM VALUES(?, ?, ?, ?)";

       PreparedStatement pstm = DbConnection.getInstance().getConnection()
               .prepareStatement(sql);

       pstm.setString(1, od.getOrder_id());
       pstm.setString(2, od.getItem_id());
       pstm.setInt(3, od.getQuantity());
       pstm.setDouble(4, od.getPrice());

       return pstm.executeUpdate() > 0;
   }
}

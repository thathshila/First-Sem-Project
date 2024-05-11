package lk.ijse.repository;

import javafx.scene.control.Alert;
import lk.ijse.db.DbConnection;
import lk.ijse.model.User;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepo {

    public static void RegisterNow(String userId, String userName, Date date, String password) throws SQLException {
        String sql = "INSERT INTO User VALUES (?, ? ,?, ? )";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, userId);
        pstm.setObject(2, userName);
        pstm.setObject(3, date);
        pstm.setObject(4, password);

        int effectedRows = pstm.executeUpdate();
        if (effectedRows > 0) {
            new Alert(Alert.AlertType.CONFIRMATION, "Register successfully!!!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Can't register this user").show();
        }

    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT User_id FROM User";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }
}


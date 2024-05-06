package lk.ijse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderTm {
    private  String Order_id;
    private Date date;
    private double Price;
    private String Customer_id;
    private String Payment_id;
    private String User_id;
}

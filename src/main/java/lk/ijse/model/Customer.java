package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private String Customer_id;
    private String Customer_name;
    private String Address;
    private String Contact;
    private String Nic;
    private Date Date;

}
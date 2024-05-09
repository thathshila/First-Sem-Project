package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Supplier {
    private String Supplier_id;
    private String Supplier_name;
    private String Address;
    private int Contact;
    private int Quantity;
    private double Price;
    private String ProductName;
    private Date Date;
    private String NIC;

}
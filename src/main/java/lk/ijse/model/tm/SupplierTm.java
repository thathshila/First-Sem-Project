package lk.ijse.model.tm;

import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SupplierTm {
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

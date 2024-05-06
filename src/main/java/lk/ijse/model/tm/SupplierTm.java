package lk.ijse.model.tm;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class SupplierTm {
    private String Supplier_id;
    private String Supplier_name;
    private String Address;
    private String Contact;
    private String Quantity;
    private double Price;
    private String ProductName;
    private Date Date;
    private String NIC;
}

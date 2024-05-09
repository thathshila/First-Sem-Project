package lk.ijse.model.tm;
import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CustomerTm {
        private String Customer_id;
        private String Customer_name;
        private int Contact;
        private String Address;
        private String Nic;
        private Date Date;
    }




package lk.ijse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeTm {
    private String Employee_id;
    private String Employee_name;
    private String Address;
    private String Contact;
    private Date Date;
    private double Salary;
    private String WorkingHours;
    private String Attendance;
    private String Position;

}

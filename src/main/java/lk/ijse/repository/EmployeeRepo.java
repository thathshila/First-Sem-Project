package lk.ijse.repository;


import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lk.ijse.db.DbConnection;

import lk.ijse.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {

    public static List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM Employee";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Employee> emList = new ArrayList<>();

        while (resultSet.next()) {
            String Employee_id = resultSet.getString(1);
            String Employee_name = resultSet.getString(2);
            String Address = resultSet.getString(3);
            int Contact = Integer.parseInt(resultSet.getString(4));
            Date Date = resultSet.getDate(5);
            double Salary = resultSet.getDouble(6);
            String WorkingHours = resultSet.getString(7);
            String Attendance = resultSet.getString(8);
            String Position = resultSet.getString(9);
            String userId = resultSet.getString(10);
            Employee employee = new Employee(Employee_id, Employee_name, Address, Contact, Date, Salary, WorkingHours, Attendance, Position, userId);
            emList.add(employee);
        }
        return emList;
    }

    public static boolean save(String employeeId, String employeeName, String address, int contact, Date date, double salary, String workingHours, String attendance, String position, String userId) throws SQLException {
        String sql = "INSERT INTO Employee VALUES(?, ?, ?, ?,?,?,?,?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, employeeId);
        pstm.setObject(2, employeeName);
        pstm.setObject(3, address);
        pstm.setObject(4, contact);
        pstm.setObject(5, date);
        pstm.setObject(6, salary);
        pstm.setObject(7, workingHours);
        pstm.setObject(8, attendance);
        pstm.setObject(9, position);
        pstm.setObject(10, userId);

        int effectedRows = pstm.executeUpdate();
        if (effectedRows > 0) {
            new Alert(Alert.AlertType.CONFIRMATION, "Employee save successfully!!!").show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Can't save this employee").show();
        }
        return false;
    }

    public static boolean DELETE(String employeeId) throws SQLException {
        String sql = "DELETE FROM Employee WHERE Employee_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, employeeId);

        return pstm.executeUpdate() > 0;
    }

    public static Employee SEARCH(String contact) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE Contact = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, contact);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String Employee_id = resultSet.getString(1);
            String Employee_name = resultSet.getString(2);
            String Address = resultSet.getString(3);
            int Contact = Integer.parseInt(resultSet.getString(4));
            Date date = resultSet.getDate(5);
            double Salary = Double.parseDouble(resultSet.getString(6));
            String Working_hours = resultSet.getString(7);
            String Attendance = resultSet.getString(8);
            String Position = resultSet.getString(9);
            String User_id = resultSet.getString(10);


            Employee employee = new Employee(Employee_id, Employee_name, Address, Contact, date, Salary, Working_hours, Attendance, Position, User_id);
            return employee;
        }
        return null;
    }

    public static String getCurrentId() throws SQLException {
        String sql = "SELECT Employee_id FROM Employee ORDER BY Employee_id DESC LIMIT 1";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String employeeId = resultSet.getString(1);
            return employeeId;
        }
        return null;
    }

    public static boolean UPDATE(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET Employee_name = ?, Address = ?,  Contact = ?, date = ?, Salary = ?,Working_hours = ?,Attendance =?, Position =?, User_id =? WHERE Employee_id = ? ";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, employee.getEmployee_name());
        pstm.setObject(2, employee.getAddress());
        pstm.setObject(3, employee.getContact());
        pstm.setObject(4, employee.getDate());
        pstm.setObject(5, employee.getSalary());
        pstm.setObject(6, employee.getWorkingHours());
        pstm.setObject(7, employee.getAttendance());
        pstm.setObject(8, employee.getPosition());
        pstm.setObject(9, employee.getUserId());
        pstm.setObject(10, employee.getEmployee_id());
        return pstm.executeUpdate() > 0;
    }
}



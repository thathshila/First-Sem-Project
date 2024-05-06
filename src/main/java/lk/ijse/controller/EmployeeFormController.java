package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.model.Customer;
import lk.ijse.model.Employee;
import lk.ijse.model.User;
import lk.ijse.model.tm.EmployeeTm;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.repository.EmployeeRepo;
import lk.ijse.repository.UserRepo;

import java.lang.String;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmployeeFormController {

    @FXML
    private Button btnBACK;

    @FXML
    private Button btnCLEAR;

    @FXML
    private Button btnDELETE;

    @FXML
    private Button btnSAVE;

    @FXML
    private Button btnSEARCH;

    @FXML
    private Button btnUPDATE;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colAttendance;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPosition;

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableColumn<?, ?> colWorkHours;

    @FXML
    private JFXComboBox<String > comUserId;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<EmployeeTm> tblEmployee;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtAttendance;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtEmployeeId;

    @FXML
    private TextField txtEmployeeName;

    @FXML
    private TextField txtPosition;

    @FXML
    private TextField txtSalary;

    @FXML
    private TextField txtWorkHours;

    public void initialize() {
        setCellValueFactory();
        loadAllEmployees();
        setDate();
       getUserIds();
    }

    private void getUserIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> userList = UserRepo.getIds();
            for (String id : userList){
                obList.add(id);
            }
            comUserId.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        txtDate.setText(String.valueOf(now));
    }
    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("Employee_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Employee_name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        colWorkHours.setCellValueFactory(new PropertyValueFactory<>("WorkingHours"));
        colAttendance.setCellValueFactory(new PropertyValueFactory<>("Attendance"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("Position"));
    }

    private void loadAllEmployees() {
        ObservableList<EmployeeTm>  emList = FXCollections.observableArrayList();

        try {
            List<Employee> employeesList = EmployeeRepo.getAll();
            for (Employee employee : employeesList) {
                EmployeeTm tm = new EmployeeTm(
                       employee.getEmployee_id(),
                        employee.getEmployee_name(),
                        employee.getAddress(),
                        employee.getContact(),
                        employee.getDate(),
                        employee.getSalary(),
                        employee.getWorkingHours(),
                        employee.getAttendance(),
                        employee.getPosition()
                );

                emList.add(tm);
            }

            tblEmployee.setItems(emList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void btnBACKOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"));
        Stage stage = (Stage) rootNode.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }

    @FXML
    void btnCLEAROnAction(ActionEvent event) {
        clearFields();
    }
    private void clearFields() {
        txtEmployeeId.setText("");
        txtEmployeeName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        txtDate.setText("");
        txtSalary.setText("");
        txtWorkHours.setText("");
        txtAttendance.setText("");
        txtPosition.setText("");
    }

    @FXML
    void btnDELETEOnAction(ActionEvent event) {
        String Employee_id = txtEmployeeId.getText();

        try {
            boolean isDeleted = EmployeeRepo.DELETE(Employee_id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Employee deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSAVEOnAction(ActionEvent event) {
        String Employee_id = txtEmployeeId.getText();
        String Employee_name = txtEmployeeName.getText();
        String Address =  txtAddress.getText();
        String Contact = txtContact.getText();
        Date date = java.sql.Date.valueOf(txtDate.getText());
        double Salary = Double.parseDouble(txtSalary.getText());
        String WorkingHours = txtWorkHours.getText();
        String Attendance = txtAttendance.getText();
        String Position = txtPosition.getText();
        String userId = comUserId.getValue();

        try {
            EmployeeRepo.save(Employee_id,Employee_name,Address,Contact,date,Salary,WorkingHours,Attendance,Position,userId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllEmployees();
    }

    @FXML
    void btnSEARCHOnAction(ActionEvent event) {

    }

    @FXML
    void btnUPDATEOnAction(ActionEvent event) {

    }
}

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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.model.Employee;
import lk.ijse.model.tm.EmployeeTm;
import lk.ijse.repository.EmployeeRepo;
import lk.ijse.repository.UserRepo;
import lk.ijse.util.Regex;
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
    private JFXComboBox<String> comUserId;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<EmployeeTm> tblEmployee;

    @FXML
    private TextField txtAddress;

    @FXML
    private ChoiceBox choiceAttendance;

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

    public void initialize() throws SQLException {
        setCellValueFactory();
        loadAllEmployees();
        setDate();
        getUserIds();
        getCurrentEmployeeId();


        ObservableList<String> attendanceType = FXCollections.observableArrayList("Present", "Absent");
        choiceAttendance.setItems(attendanceType);
    }
    private void getUserIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> userList = UserRepo.getIds();
            for (String id : userList) {
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
        ObservableList<EmployeeTm> emList = FXCollections.observableArrayList();

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
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) rootNode.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Main Form");
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
        choiceAttendance.setValue("");
        txtPosition.setText("");
        comUserId.setValue("");
    }

    @FXML
    void btnDELETEOnAction(ActionEvent event) {
        String Employee_id = txtEmployeeId.getText();

        try {
            boolean isDeleted = EmployeeRepo.DELETE(Employee_id);
            initialize();
            if (isDeleted) {
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
        String Address = txtAddress.getText();
        int Contact = Integer.parseInt(txtContact.getText());
        Date date = java.sql.Date.valueOf(txtDate.getText());
        double Salary = Double.parseDouble(txtSalary.getText());
        String WorkingHours = txtWorkHours.getText();
        String Attendance = (String) choiceAttendance.getValue();
        String Position = txtPosition.getText();
        String userId = comUserId.getValue();

        try {
            if (isValied()) {
                boolean isSave = EmployeeRepo.save(Employee_id, Employee_name, Address, Contact, date, Salary, WorkingHours, Attendance, Position, userId);
                if (isSave) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Employee saved..", ButtonType.OK).show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Employee not saved..", ButtonType.OK).show();
                }
            }else {
                return;
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllEmployees();
    }

    @FXML
    void btnSEARCHOnAction(ActionEvent event) throws SQLException {
        String id = txtEmployeeId.getText();

        Employee employee = EmployeeRepo.SEARCH(id);
        if (employee != null) {
            txtEmployeeId.setText(employee.getEmployee_id());
            txtEmployeeName.setText(employee.getEmployee_name());
            txtAddress.setText(employee.getAddress());
            txtContact.setText(String.valueOf(employee.getContact()));
            LocalDate now = LocalDate.now();
            txtDate.setText(String.valueOf(now));
            txtSalary.setText(String.valueOf(employee.getSalary()));
            txtWorkHours.setText(employee.getWorkingHours());
          //  choiceAttendance.setItems(employee.getAttendance());
            txtPosition.setText(employee.getPosition());
            // comUserId.setItems((ObservableList<String>) employee.getUserId());

        } else {
            new Alert(Alert.AlertType.INFORMATION, "Employee not found!").show();
        }
    }

    @FXML
    void btnUPDATEOnAction(ActionEvent event) {
        String Employee_id = txtEmployeeId.getText();
        String Employee_name = txtEmployeeName.getText();
        String Address = txtAddress.getText();
        int Contact = Integer.parseInt(txtContact.getText());
        Date date = java.sql.Date.valueOf(txtDate.getText());
        double Salary = Double.parseDouble(txtSalary.getText());
        String WorkingHours = txtWorkHours.getText();
        String Attendance = (String) choiceAttendance.getValue();
        String Position = txtPosition.getText();
        String User_id = comUserId.getValue();

        Employee employee = new Employee(Employee_id, Employee_name, Address, Contact, date, Salary, WorkingHours, Attendance, Position, User_id);

        try {
            boolean isUpdated = EmployeeRepo.UPDATE(employee);
            if (isUpdated) {
                initialize();
                new Alert(Alert.AlertType.CONFIRMATION, "employee updated!").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllEmployees();
    }
    private void getCurrentEmployeeId() throws SQLException {
        String currentId = EmployeeRepo.getCurrentId();

        String nextEmployeeId = generateNextEmployeeId(currentId);
        txtEmployeeId.setText(nextEmployeeId);
    }

    private String generateNextEmployeeId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("E00");
            int idNum = Integer.parseInt(split[1]);
            return "E00" + ++idNum;
        }
        return "E001";
    }

    public void tblEmployeeOnMouseClicked(MouseEvent mouseEvent) {
        Integer index = tblEmployee.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtEmployeeId.setText(colId.getCellData(index).toString());
        txtEmployeeName.setText(colName.getCellData(index).toString());
        txtAddress.setText(colAddress.getCellData(index).toString());
        txtContact.setText(colContact.getCellData(index).toString());
        txtDate.setText(colDate.getCellData(index).toString());
        txtSalary.setText(colSalary.getCellData(index).toString());
        txtWorkHours.setText(colWorkHours.getCellData(index).toString());
        choiceAttendance.setValue(colAttendance.getCellData(index).toString());
        txtPosition.setText(colPosition.getCellData(index).toString());
    }

   public void txtEmployeeIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.ID,txtEmployeeId);
    }

    public void txtEmployeeNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.NAME,txtEmployeeName);
    }

    public void txtWorkHoursOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.QUANTITY,txtWorkHours);
    }

    public void txtDateOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.DATE,txtDate);
        }

    public void txtAddressOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.ADDRESS,txtAddress);
    }

    public void txtSalaryOnKeyReleased(KeyEvent keyEvent){
        Regex.setTextColor(lk.ijse.util.TextField.PRICE,txtSalary);
    }

   public void txtContactOnKeyReleased(KeyEvent keyEvent){
       Regex.setTextColor(lk.ijse.util.TextField.CONTACT,txtContact);
   }

  public  boolean isValied(){
       if (!Regex.setTextColor(lk.ijse.util.TextField.ID, txtEmployeeId)) return false;
       if (!Regex.setTextColor(lk.ijse.util.TextField.NAME, txtEmployeeName)) return false;
       if (!Regex.setTextColor(lk.ijse.util.TextField.ADDRESS, txtAddress)) return false;
       if (!Regex.setTextColor(lk.ijse.util.TextField.DATE, txtDate)) return false;
       if (!Regex.setTextColor(lk.ijse.util.TextField.CONTACT, txtContact)) return false;
       if (!Regex.setTextColor(lk.ijse.util.TextField.PRICE, txtSalary)) return false;
       if (!Regex.setTextColor(lk.ijse.util.TextField.QUANTITY, txtWorkHours)) return false;

       return true;
   }
}


package lk.ijse.controller;

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
import lk.ijse.model.User;

import lk.ijse.repository.UserRepo;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RegisterFormController {

    @FXML
    private Button btnRegisterNow;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private TableColumn<?, ?> colPassword;

    @FXML
    private AnchorPane rootNode;


    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserId;

    @FXML
    private TextField txtUserName;

    public void initialize() {
        setDate();
      //  setCellValueFactory();
     //   loadAllUsers();
    }
    private void setDate() {
        LocalDate now = LocalDate.now();
        txtDate.setText(String.valueOf(now));
    }
  /*  private void setCellValueFactory() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("User_id"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("User_name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("Password"));
    }*/
   /* private void loadAllUsers() {
        ObservableList<UserTm> obList = FXCollections.observableArrayList();

        try {
            List<User> userList = UserRepo.getAll();
            for (User user : userList) {
                UserTm tm = new UserTm(
                        user.getUser_id(),
                        user.getUser_name(),
                        user.getDate(),
                        user.getPassword()
                );

                obList.add(tm);
            }

            tblRegister.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
    @FXML
    void btnRegisterNowOnAction(ActionEvent event) throws IOException {
        String User_id = txtUserId.getText();
        String User_name = txtUserName.getText();
        Date date = Date.valueOf(txtDate.getText());
        String Password = txtPassword.getText();
        try {
            UserRepo.RegisterNow(User_id, User_name,date,Password);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"));
        Stage stage = (Stage) rootNode.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }
    }



package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.DbConnection;
import lk.ijse.util.Regex;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    @FXML
    private Button btnLOGIN;

    @FXML
    private Button btnForgotpassword;

    @FXML
    private Button btnRegister;
    public TextField txtPassword;
    public TextField txtUsername;

    public AnchorPane rootNode;

    public AnchorPane rootNode1;

    String username;
    @FXML
    public  void btnLOGINOnAction(ActionEvent event) throws IOException{
        username = txtUsername.getText();
       String pw = txtPassword.getText();


        try {
            checkCredential(username, pw);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    private void checkCredential(String username, String pw) throws SQLException, IOException {
        String sql = "SELECT User_name,Password FROM User WHERE User_name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, username);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            if (isValied()) {
                String dbPw = resultSet.getString("password");

                if (pw.equals(dbPw)) {
                    navigateToTheDashboard();
                } else {
                    new Alert(Alert.AlertType.ERROR, "sorry! password is incorrect!").show();
                }
            } else {
                new Alert(Alert.AlertType.INFORMATION, "sorry! user name can't be find!").show();
            }
        }
    }

    private void navigateToTheDashboard() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/MainForm.fxml"));
        AnchorPane anchorPane = loader.load();

        MainFormController mainFormController =loader.getController();
        mainFormController.setUserID(username);

        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setScene(anchorPane.getScene());
        stage.centerOnScreen();
        stage.setTitle("Main Form");
        stage.show();
        rootNode.getScene().getWindow().hide();
}
    @FXML
   public void btnRegisterOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/RegisterForm.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Register Form");
    }

    public void userNameOnAction(ActionEvent actionEvent) {
        txtPassword.requestFocus();
    }

    public void passwordOnAction(ActionEvent actionEvent) throws IOException{
        btnLOGINOnAction(actionEvent);
    }

    public void btnForgotpasswordOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/RegisterForm.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Register Form");
    }

    public void txtUsernameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.USERNAME,txtUsername);
    }

    public void txtPasswordOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.PASSWORD,txtPassword);
    }

    public boolean isValied() {
        if (!Regex.setTextColor(lk.ijse.util.TextField.USERNAME, txtUsername)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.PASSWORD, txtPassword)) return false;

        return true;
    }
}



package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SupplierItemController {

    @FXML
    private JFXButton btnsave;

    @FXML
    private TableColumn<?, ?> coldate;

    @FXML
    private TableColumn<?, ?> colitemId;

    @FXML
    private TableColumn<?, ?> colqty;

    @FXML
    private TableColumn<?, ?> colsupplierId;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<?> tablesupplierItem;

    @FXML
    private TextField txtdate;

    @FXML
    private TextField txtitemId;

    @FXML
    private TextField txtqty;

    @FXML
    private TextField txtsupplierId;

    @FXML
    void btnsaveOnAction(ActionEvent event) {

    }

}

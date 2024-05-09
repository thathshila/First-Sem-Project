package lk.ijse.controller;

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
import lk.ijse.model.Item;
import lk.ijse.model.tm.ItemTm;
import lk.ijse.repository.ItemRepo;
import lk.ijse.util.Regex;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class ItemFormController {

    public AnchorPane rootNode;
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
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableView<ItemTm> tblItem;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtItemId;

    @FXML
    private TextField txtItemName;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtQuantity;

    public void initialize() throws SQLException {
        setCellValueFactory();
        loadAllItems();
        setDate();
        getCurrentItemId();
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        txtDate.setText(String.valueOf(now));
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("Item_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void loadAllItems() {
        ObservableList<ItemTm> obList = FXCollections.observableArrayList();

        try {
            List<Item> itemList = ItemRepo.getAll();
            for (Item item : itemList) {
                ItemTm tm = new ItemTm(
                        item.getItem_id(),
                        item.getName(),
                        item.getQty(),
                        item.getPrice(),
                        item.getDescription(),
                        item.getDate()
                );

                obList.add(tm);
            }

            tblItem.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnBACKOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/MainForm.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Main Form");
        stage.centerOnScreen();
//        stage.show();
    }

    private void getCurrentItemId() throws SQLException {
        String currentId = ItemRepo.getCurrentId();

        String nextItemId = generateNextItemId(currentId);
        txtItemId.setText(nextItemId);
    }

    private String generateNextItemId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("I00");  //" ", "2"
            int idNum = Integer.parseInt(split[1]);
            return "I00" + ++idNum;
        }
        return "I001";
    }

    @FXML
    void btnCLEAROnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtItemId.setText("");
        txtQuantity.setText("");
        txtItemName.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        txtDate.setText("");
    }

    @FXML
    void btnDELETEOnAction(ActionEvent event) {
        String itemId = txtItemId.getText();
        try {
            boolean isDeleted = ItemRepo.DELETE(itemId);
            initialize();
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSAVEOnAction(ActionEvent event) {
        String itemId = txtItemId.getText();
        String name = txtItemName.getText();
        int qty = Integer.parseInt(txtQuantity.getText());
        double price = Double.parseDouble(txtPrice.getText());
        String description = txtDescription.getText();
        Date date = Date.valueOf(LocalDate.now());

        try {
            ItemRepo.saveItem(itemId, name, qty, price, date, description);
            initialize();
        } catch (SQLException e) {
//            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnSEARCHOnAction(ActionEvent event) throws SQLException {
        String id = txtItemId.getText();
        Item item = ItemRepo.searchItem(id);
        if (item != null) {
            txtItemId.setText(item.getItem_id());
            txtQuantity.setText(String.valueOf(item.getQty()));
            txtItemName.setText(item.getName());
            txtPrice.setText(String.valueOf(item.getPrice()));
            //txtDate.setText(String.valueOf(item).getDate());
            txtDescription.setText(item.getDescription());
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Item not found!").show();
        }
    }

    @FXML
    void btnUPDATEOnAction(ActionEvent event) {
        String itemId = txtItemId.getText();
        String name = txtItemName.getText();
        int qty = Integer.parseInt(txtQuantity.getText());
        double price = Double.parseDouble(txtPrice.getText());
        String description = txtDescription.getText();
        Date date = Date.valueOf(LocalDate.now());

        Item item = new Item(itemId, name, qty, price, description, date);
        try {
            boolean isUpdated = ItemRepo.UPDATE(item);
            if (isUpdated) {
                initialize();
                new Alert(Alert.AlertType.CONFIRMATION, "item updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void mouseClickOnAction(MouseEvent mouseEvent) {
        Integer index = tblItem.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txtItemId.setText(colId.getCellData(index).toString());
        txtItemName.setText(colName.getCellData(index).toString());
        txtDate.setText(colDate.getCellData(index).toString());
        txtDescription.setText(colDescription.getCellData(index).toString());
        txtPrice.setText(colPrice.getCellData(index).toString());
        txtQuantity.setText(colQuantity.getCellData(index).toString());
    }


    public void txtItemIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.ID, txtItemId);
    }

    public void txtItemNameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.NAME, txtItemName);
    }

    public void txtQuantityOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.QUANTITY, txtQuantity);
    }

    public void txtPriceOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.PRICE, txtPrice);
    }

    public void txtDateOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(lk.ijse.util.TextField.DATE, txtDate);
    }

   /* public boolean isValied() {
        if (!Regex.setTextColor(TextField.ID, txtItemId)) return false;
        if (!Regex.setTextColor(TextField.NAME, txtItemName)) return false;
        if (!Regex.setTextColor(TextField.DATE, txtDate)) return false;
        if (!Regex.setTextColor(TextField.PRICE, txtPrice)) return false;
        if (!Regex.setTextColor(TextField.QUANTITY, txtQuantity)) return false;

        return true;
    }*/

}
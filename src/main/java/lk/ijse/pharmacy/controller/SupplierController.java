package lk.ijse.pharmacy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.pharmacy.bo.BOFactory;
import lk.ijse.pharmacy.bo.custom.SupplierBO;
import lk.ijse.pharmacy.dto.SupplierDTO;
import java.sql.SQLException;

public class SupplierController {

    @FXML private TextField txtId, txtName, txtContact, txtEmail;
    @FXML private Button btnSave, btnDelete, btnUpdate, btnReset;
    @FXML private TableView<SupplierDTO> tblSupplier;
    @FXML private TableColumn<SupplierDTO, Integer> colId;
    @FXML private TableColumn<SupplierDTO, String> colName, colEmail, colContact;

    ObservableList<SupplierDTO> supplierList = FXCollections.observableArrayList();

    // 1. BO Connection
    SupplierBO supplierBO = (SupplierBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.SUPPLIER);

    @FXML
    private void initialize() {
        loadAllSuppliers();
        tblSupplier.setItems(supplierList);
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNum"));

        tblSupplier.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) populateFields(newVal);
        });
    }

    @FXML
    private void handlePressEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String id = txtId.getText() == null ? "" : txtId.getText().trim();
            if (!id.matches("^\\d+$")) { new Alert(Alert.AlertType.WARNING, "Please enter a valid ID!").show(); return; }
            try {
                SupplierDTO supplierDTO = supplierBO.searchSupplier(Integer.parseInt(id)); // BO Call
                if (supplierDTO == null) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Not Found").showAndWait();
                    clearFields();
                    return;
                }
                populateFields(supplierDTO);
            } catch (Exception e) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Not Found").showAndWait();
                clearFields();
            }
        }
    }

    @FXML
    private void btnSaveOnAction(ActionEvent event) {
        String name = txtName.getText() == null ? "" : txtName.getText().trim();
        String contact = txtContact.getText() == null ? "" : txtContact.getText().trim();
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();

        if (name.isEmpty() || contact.isEmpty() || email.isEmpty()) return;
        if (!validateSupplierInput(name, contact, email)) return;

        for (SupplierDTO s : supplierList) {
            if (s.getContactNum().equals(contact) || s.getEmail().equalsIgnoreCase(email)) {
                new Alert(Alert.AlertType.WARNING, "Supplier exists!").show();
                return;
            }
        }

        SupplierDTO supplier = new SupplierDTO(0, name, email, contact);
        try {
            if (supplierBO.saveSupplier(supplier)) { // BO Call
                new Alert(Alert.AlertType.INFORMATION, "Supplier Saved Successfully!").show();
                loadAllSuppliers();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText().trim() == null ? "" : txtId.getText().trim();
        if (id.isEmpty() || !id.matches("^\\d+$")) return;

        boolean confirmed = lk.ijse.pharmacy.util.AlertUtil.showConfirmation("Confirm Deletion", "Are you sure you want to delete this supplier?", "delete-alert");
        if (!confirmed) return;

        try {
            if (supplierBO.deleteSupplier(Integer.parseInt(id))) { // BO Call
                new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted Successfully!").show();
                loadAllSuppliers();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Supplier ID not found!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText() == null ? "" : txtId.getText().trim();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String email = txtEmail.getText();

        if (!id.matches("^\\d+$")) return;
        if (!validateSupplierInput(name, contact, email)) return;

        boolean confirmed = lk.ijse.pharmacy.util.AlertUtil.showConfirmation("Confirm Update", "Are you sure you want to update?", "update-alert");
        if (!confirmed) return;

        SupplierDTO supplier = new SupplierDTO(Integer.parseInt(id), name, email, contact);
        try {
            if (supplierBO.updateSupplier(supplier)) { // BO Call
                new Alert(Alert.AlertType.INFORMATION, "Supplier Updated Successfully!").show();
                loadAllSuppliers();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Supplier ID not found!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private void loadAllSuppliers() {
        try {
            supplierList.clear();
            supplierList.setAll(supplierBO.getAllSuppliers()); // BO Call
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private boolean validateSupplierInput(String name, String contact, String email) {
        if (!name.matches("[A-Za-z .'-]{3,}")) return false;
        if (!contact.matches("^\\d{10}$")) return false;
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,10}$")) return false;
        return true;
    }

    @FXML void btnResetOnAction(ActionEvent event) { loadAllSuppliers(); clearFields(); }

    private void populateFields(SupplierDTO supplierDTO) {
        txtId.setText(String.valueOf(supplierDTO.getSupplierId()));
        txtName.setText(supplierDTO.getSupplierName());
        txtContact.setText(supplierDTO.getContactNum());
        txtEmail.setText(supplierDTO.getEmail());
    }

    private void clearFields() { txtId.clear(); txtName.clear(); txtContact.clear(); txtEmail.clear(); }
}
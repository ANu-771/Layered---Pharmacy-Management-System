package lk.ijse.pharmacy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.geometry.Side;
import lk.ijse.pharmacy.bo.BOFactory;
import lk.ijse.pharmacy.bo.custom.MedicineBO;
import lk.ijse.pharmacy.dto.MedicineDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicineController {

    @FXML
    private Button btnClear, btnDelete, btnSave, btnUpdate;
    @FXML
    private TableColumn<MedicineDTO, String> colBrand, colExpDate, colName;
    @FXML
    private TableColumn<MedicineDTO, Integer> colId, colQty;
    @FXML
    private TableColumn<MedicineDTO, Double> colPrice;
    @FXML
    private DatePicker dpExpDate;
    @FXML
    private TableView<MedicineDTO> tblMedicine;
    @FXML
    private TextField txtBrand, txtId, txtName, txtPrice, txtQty;

    private ObservableList<MedicineDTO> medicineList = FXCollections.observableArrayList();
    private List<String> allMedicineNames = new ArrayList<>();

    MedicineBO medicineBO = (MedicineBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.MEDICINE);

    @FXML
    private void initialize() {
        loadAllMedicines();
        setupTable();
        loadMedicineNames();
        setupAutoSuggestion();

        tblMedicine.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) populateFields(newVal);
        });

        txtPrice.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*(\\.\\d*)?")) txtPrice.setText(oldV);
        });
        txtQty.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) txtQty.setText(oldV);
        });
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtName.getText().trim();
        String brand = txtBrand.getText().trim();
        String priceText = txtPrice.getText().trim();
        String qtyText = txtQty.getText().trim();
        LocalDate localExpDate = dpExpDate.getValue();

        if (name.isEmpty() || brand.isEmpty() || priceText.isEmpty() || qtyText.isEmpty() || localExpDate == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields!").show();
            return;
        }

        if (!validateInput(priceText, qtyText)) return;

        double price = Double.parseDouble(priceText);
        int qty = Integer.parseInt(qtyText);
        Date expDate = Date.from(localExpDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        MedicineDTO medicine = new MedicineDTO(0, name, brand, qty, price, expDate);

        try {
            if (medicineBO.saveMedicine(medicine)) {
                new Alert(Alert.AlertType.INFORMATION, "Medicine Saved Successfully!").show();
                loadAllMedicines();
                clearFields();

                if (DashboardController.getInstance() != null) {
                    DashboardController.getInstance().refreshDashboard();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String idText = txtId.getText().trim();
        if (idText.isEmpty()) return;

        String name = txtName.getText().trim();
        String brand = txtBrand.getText().trim();
        String priceText = txtPrice.getText().trim();
        String qtyText = txtQty.getText().trim();
        LocalDate localExpDate = dpExpDate.getValue();

        if (!validateInput(priceText, qtyText)) return;
        boolean confirmed = lk.ijse.pharmacy.util.AlertUtil.showConfirmation("Confirm Update", "Update this record?", "update-alert");
        if (!confirmed) return;

        int id = Integer.parseInt(idText);
        double price = Double.parseDouble(priceText);
        int qty = Integer.parseInt(qtyText);
        Date expDate = Date.from(localExpDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        MedicineDTO medicine = new MedicineDTO(id, name, brand, qty, price, expDate);

        try {
            if (medicineBO.updateMedicine(medicine)) {
                new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
                loadAllMedicines();
                clearFields();

                if (DashboardController.getInstance() != null) {
                    DashboardController.getInstance().refreshDashboard();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String idText = txtId.getText().trim();
        if (idText.isEmpty()) return;
        boolean confirmed = lk.ijse.pharmacy.util.AlertUtil.showConfirmation("Confirm Deletion", "Delete this medicine?", "delete-alert");
        if (!confirmed) return;

        try {
            if (medicineBO.deleteMedicine(Integer.parseInt(idText))) {
                new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully!").show();
                loadAllMedicines();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handlePressEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String idText = txtId.getText().trim();
            String nameText = txtName.getText().trim();
            MedicineDTO medicineDTO = null;

            try {
                if (!idText.isEmpty() && idText.matches("^\\d+$")) {
                    medicineDTO = medicineBO.searchMedicine(Integer.parseInt(idText));
                } else if (!nameText.isEmpty()) {
                    medicineDTO = medicineBO.searchMedicineByName(nameText);
                }

                if (medicineDTO != null) populateFields(medicineDTO);
                else {
                    new Alert(Alert.AlertType.INFORMATION, "Medicine Not Found").showAndWait();
                    clearFields();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error searching: " + e.getMessage()).show();
            }
        }
    }

    private void setupTable() {
        tblMedicine.setItems(medicineList);
        colId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("medName"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyInStock"));

        tblMedicine.setRowFactory(tv -> new TableRow<MedicineDTO>() {
            @Override
            protected void updateItem(MedicineDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty || item.getExpDate() == null) setStyle("");
                else {
                    java.time.LocalDate expDate = new java.sql.Date(item.getExpDate().getTime()).toLocalDate();
                    java.time.LocalDate today = java.time.LocalDate.now();
                    if (expDate.isBefore(today))
                        setStyle("-fx-background-color: #ff9999; -fx-text-background-color: white;");
                    else if (expDate.isBefore(today.plusDays(21))) setStyle("-fx-background-color: #ffcdd2;");
                    else if (item.getQtyInStock() <= 21)
                        setStyle("-fx-background-color: #fef08a; -fx-text-background-color: black;");
                    else setStyle("");
                }
            }
        });
    }

    private void loadAllMedicines() {
        try {
            medicineList.clear();
            medicineList.addAll(medicineBO.getAllMedicines());
            loadMedicineNames();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean validateInput(String price, String qty) {
        return price.matches("^\\d+(\\.\\d+)?$") && qty.matches("^\\d+$");
    }

    private void populateFields(MedicineDTO dto) {
        txtId.setText(String.valueOf(dto.getMedicineId()));
        txtName.setText(dto.getMedName());
        txtBrand.setText(dto.getBrand());
        txtPrice.setText(String.valueOf(dto.getUnitPrice()));
        txtQty.setText(String.valueOf(dto.getQtyInStock()));
        if (dto.getExpDate() != null) dpExpDate.setValue(new java.sql.Date(dto.getExpDate().getTime()).toLocalDate());
    }

    private void loadMedicineNames() {
        try {
            allMedicineNames.clear();
            for (MedicineDTO m : medicineBO.getAllMedicines()) allMedicineNames.add(m.getMedName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupAutoSuggestion() {
        ContextMenu suggestionsMenu = new ContextMenu();
        txtName.textProperty().addListener((obs, oldV, newV) -> {
            if (newV == null || newV.isEmpty()) {
                suggestionsMenu.hide();
                return;
            }
            List<String> matches = allMedicineNames.stream().filter(n -> n.toLowerCase().contains(newV.toLowerCase())).collect(Collectors.toList());
            if (matches.isEmpty()) {
                suggestionsMenu.hide();
                return;
            }
            suggestionsMenu.getItems().clear();
            for (String match : matches) {
                MenuItem item = new MenuItem(match);
                item.setOnAction(e -> {
                    txtName.setText(match);
                    suggestionsMenu.hide();
                    try {
                        MedicineDTO medicine = medicineBO.searchMedicineByName(match);
                        if (medicine != null) populateFields(medicine);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                suggestionsMenu.getItems().add(item);
            }
            if (!suggestionsMenu.isShowing()) suggestionsMenu.show(txtName, Side.BOTTOM, 0, 0);
        });
        txtName.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) suggestionsMenu.hide();
        });
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtBrand.clear();
        txtPrice.clear();
        txtQty.clear();
        dpExpDate.setValue(null);
    }
}
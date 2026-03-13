package lk.ijse.pharmacy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.geometry.Side;
import lk.ijse.pharmacy.bo.BOFactory;
import lk.ijse.pharmacy.bo.custom.CustomerBO;
import lk.ijse.pharmacy.bo.custom.MedicineBO;
import lk.ijse.pharmacy.bo.custom.PlaceOrderBO;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.CustomerDTO;
import lk.ijse.pharmacy.dto.MedicineDTO;
import lk.ijse.pharmacy.dto.OrderDTO;
import lk.ijse.pharmacy.dto.OrderMedicineDTO;
import lk.ijse.pharmacy.dto.PaymentDTO;
import lk.ijse.pharmacy.dto.tm.CartTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrderController {

    @FXML
    private ComboBox<String> cmbCustomerId, cmbMedicineId, cmbPaymentMethod;
    @FXML
    private TableView<CartTM> tblOrderCart;
    @FXML
    private TableColumn<CartTM, String> colMedicineId, colDescription;
    @FXML
    private TableColumn<CartTM, Integer> colQty;
    @FXML
    private TableColumn<CartTM, Double> colUnitPrice, colLineTotal;
    @FXML
    private TableColumn<CartTM, Button> colAction;
    @FXML
    private Label lblCustomerName, lblOrderDate, lblQtyOnHand, lblUnitPrice, lblNetTotal, lblBalance;
    @FXML
    private TextField txtDescription, txtOrderId, txtQty, txtCash;

    private ObservableList<CartTM> cartList = FXCollections.observableArrayList();
    private double netTotal = 0;
    private MedicineDTO selectedMedicine = null;
    private List<String> allMedicineNames = new ArrayList<>();

    CustomerBO customerBO = (CustomerBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.CUSTOMER);
    MedicineBO medicineBO = (MedicineBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.MEDICINE);
    PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PLACE_ORDER);

    @FXML
    public void initialize() {
        setCellValueFactory();
        loadCustomerIds();
        loadMedicineIds();
        lblOrderDate.setText(String.valueOf(LocalDate.now()));
        loadMedicineNames();
        setupAutoSuggestion();
        setupPaymentLogic();

        txtOrderId.setText("Auto-Generated");
        txtOrderId.setEditable(false);
    }

    private void setCellValueFactory() {
        colMedicineId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colLineTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
        tblOrderCart.setItems(cartList);
    }

    private void loadCustomerIds() {
        try {
            List<CustomerDTO> allCustomers = customerBO.getAllCustomers();
            ObservableList<String> ids = FXCollections.observableArrayList();
            for (CustomerDTO c : allCustomers) ids.add(String.valueOf(c.getCustomerId()));
            cmbCustomerId.setItems(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMedicineIds() {
        try {
            List<MedicineDTO> allMedicines = medicineBO.getAllMedicines();
            ObservableList<String> ids = FXCollections.observableArrayList();
            for (MedicineDTO m : allMedicines) ids.add(String.valueOf(m.getMedicineId()));
            cmbMedicineId.setItems(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cmbCustomerOnAction(ActionEvent actionEvent) {
        String id = cmbCustomerId.getValue();
        if (id == null) {
            lblCustomerName.setText("");
            return;
        }
        try {
            CustomerDTO customer = customerBO.searchCustomer(Integer.parseInt(id));
            if (customer != null) lblCustomerName.setText(customer.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cmbMedicineOnAction(ActionEvent actionEvent) {
        String id = cmbMedicineId.getValue();
        if (id == null) return;
        try {
            MedicineDTO medicine = medicineBO.searchMedicine(Integer.parseInt(id));
            if (medicine != null) {
                selectedMedicine = medicine;
                txtDescription.setText(medicine.getMedName());
                lblUnitPrice.setText(String.valueOf(medicine.getUnitPrice()));
                lblQtyOnHand.setText(String.valueOf(medicine.getQtyInStock()));
                txtQty.requestFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        String medId = cmbMedicineId.getValue();
        if (medId == null || selectedMedicine == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a medicine first!").show();
            return;
        }

        try {
            java.time.LocalDate expDate = new java.sql.Date(selectedMedicine.getExpDate().getTime()).toLocalDate();
            java.time.LocalDate today = java.time.LocalDate.now();
            if (expDate.isBefore(today)) {
                new Alert(Alert.AlertType.ERROR, "Cannot Sell! EXPIRED on: " + expDate).show();
                return;
            }
            if (expDate.isBefore(today.plusDays(21))) {
                new Alert(Alert.AlertType.WARNING, "Warning! Expires soon (" + expDate + "). Cannot add.").show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String qtyText = txtQty.getText().trim();
        if (qtyText.isEmpty() || !qtyText.matches("\\d+") || Integer.parseInt(qtyText) <= 0) {
            new Alert(Alert.AlertType.ERROR, "Invalid Quantity!").show();
            return;
        }

        try {
            String desc = txtDescription.getText();
            double unitPrice = Double.parseDouble(lblUnitPrice.getText());
            int qtyOnHand = Integer.parseInt(lblQtyOnHand.getText());
            int qty = Integer.parseInt(qtyText);

            if (qty > qtyOnHand) {
                new Alert(Alert.AlertType.ERROR, "Out of Stock! Only " + qtyOnHand + " left.").show();
                return;
            }

            double total = qty * unitPrice;
            CartTM existingItem = cartList.stream().filter(tm -> tm.getMedicineId().equals(medId)).findFirst().orElse(null);

            if (existingItem != null) {
                int newQty = existingItem.getQty() + qty;
                if (newQty > qtyOnHand) {
                    new Alert(Alert.AlertType.ERROR, "Not enough stock!").show();
                    return;
                }
                existingItem.setQty(newQty);
                existingItem.setTotal(newQty * unitPrice);
                tblOrderCart.refresh();
            } else {
                Button btnRemove = new Button("Remove");
                btnRemove.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");
                CartTM newTm = new CartTM(medId, desc, qty, unitPrice, total, btnRemove);
                btnRemove.setOnAction((e) -> {
                    cartList.remove(newTm);
                    calculateNetTotal();
                });
                cartList.add(newTm);
            }
            calculateNetTotal();
            txtQty.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateNetTotal() {
        netTotal = cartList.stream().mapToDouble(CartTM::getTotal).sum();
        lblNetTotal.setText(String.format("%.2f", netTotal));
    }

    @FXML
    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        String customerId = cmbCustomerId.getValue();
        if (customerId == null || cartList.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Select customer and add items!").show();
            return;
        }

        String paymentMethod = cmbPaymentMethod.getValue();
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Select a Payment Method!").show();
            return;
        }

        double cashAmount = netTotal;
        if ("Cash".equals(paymentMethod)) {
            try {
                cashAmount = Double.parseDouble(txtCash.getText());
                if (cashAmount < netTotal) {
                    new Alert(Alert.AlertType.ERROR, "Insufficient Cash!").show();
                    return;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid Cash Amount!").show();
                return;
            }
        }

        try {
            OrderDTO orderDTO = new OrderDTO(0, Integer.parseInt(customerId), 1, netTotal, new Date());

            List<OrderMedicineDTO> orderDetails = new ArrayList<>();
            for (CartTM tm : cartList) {
                orderDetails.add(new OrderMedicineDTO(0, Integer.parseInt(tm.getMedicineId()), tm.getQty(), tm.getUnitPrice(), tm.getTotal()));
            }

            PaymentDTO paymentDTO = new PaymentDTO(0, 0, cashAmount, new java.sql.Timestamp(System.currentTimeMillis()), paymentMethod);

            boolean isPlaced = placeOrderBO.placeOrder(orderDTO, orderDetails, paymentDTO);

            if (isPlaced) {

                int latestOrderId = placeOrderBO.getLatestOrderId();
                double balance = Double.parseDouble(lblBalance.getText());

                printBill(latestOrderId, balance);

                new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully!").show();

                if (DashboardController.getInstance() != null) {
                    DashboardController.getInstance().refreshDashboard();
                }

                cartList.clear();
                calculateNetTotal();
                cmbMedicineId.getSelectionModel().clearSelection();
                txtDescription.clear();
                lblQtyOnHand.setText("");
                lblUnitPrice.setText("");
                txtQty.clear();
                txtCash.clear();
                lblBalance.setText("0.00");
                cmbCustomerId.getSelectionModel().clearSelection();
                lblCustomerName.setText("");
            } else {
                new Alert(Alert.AlertType.ERROR, "Order Failed!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    public void txtSearchMedicineOnAction(ActionEvent actionEvent) {
        String name = txtDescription.getText().trim();
        try {
            MedicineDTO medicine = medicineBO.searchMedicineByName(name);
            if (medicine != null) {
                selectedMedicine = medicine;
                cmbMedicineId.setValue(String.valueOf(medicine.getMedicineId()));
                lblUnitPrice.setText(String.valueOf(medicine.getUnitPrice()));
                lblQtyOnHand.setText(String.valueOf(medicine.getQtyInStock()));
                txtQty.requestFocus();
            } else {
                new Alert(Alert.AlertType.WARNING, "Medicine not found!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupPaymentLogic() {
        cmbPaymentMethod.setItems(FXCollections.observableArrayList("Cash", "Card"));
        cmbPaymentMethod.setValue("Cash");
        txtCash.textProperty().addListener((obs, oldV, newV) -> calculateBalance());
        cmbPaymentMethod.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if ("Card".equals(newV)) {
                txtCash.setDisable(true);
                lblBalance.setDisable(true);
                txtCash.clear();
                lblBalance.setText("0.00");
            } else {
                txtCash.setDisable(false);
                lblBalance.setDisable(false);
                txtCash.requestFocus();
            }
        });
    }

    private void calculateBalance() {
        try {
            if (txtCash.getText().isEmpty()) {
                lblBalance.setText("0.00");
                return;
            }
            double balance = Double.parseDouble(txtCash.getText()) - netTotal;
            lblBalance.setText(String.format("%.2f", balance));
            lblBalance.setStyle(balance < 0 ? "-fx-text-fill: red;" : "-fx-text-fill: #22c55e;");
        } catch (NumberFormatException e) {
            lblBalance.setText("Invalid");
        }
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
        txtDescription.textProperty().addListener((obs, oldV, newV) -> {
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
                    txtDescription.setText(match);
                    suggestionsMenu.hide();
                    txtSearchMedicineOnAction(null);
                });
                suggestionsMenu.getItems().add(item);
            }
            if (!suggestionsMenu.isShowing()) suggestionsMenu.show(txtDescription, Side.BOTTOM, 0, 0);
        });
        txtDescription.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) suggestionsMenu.hide();
        });
    }

    private void printBill(int orderId, double balance) {
        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/bill.jrxml");

            if (reportStream == null) {
                new Alert(Alert.AlertType.ERROR, "Bill report file not found!").show();
                return;
            }

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("p_order_id", orderId);
            parameters.put("p_balance", balance);

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            Connection connection = DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error generating bill: " + e.getMessage()).show();
        }
    }
}
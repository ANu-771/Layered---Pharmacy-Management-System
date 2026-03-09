package lk.ijse.pharmacy.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pharmacy.bo.BOFactory;
import lk.ijse.pharmacy.bo.custom.DashboardBO;
import lk.ijse.pharmacy.dto.MedicineDTO;

import java.util.Map;
import java.util.TreeMap;

public class DashboardController {

    @FXML private Label lblTotalMedicines, lblActiveCustomers, lblTodayIncome;
    @FXML private AreaChart<String, Number> chartSales;
    @FXML private TableView<MedicineDTO> tblExpiring;
    @FXML private TableColumn<MedicineDTO, String> colExpId, colExpName, colExpDate;
    @FXML private TableColumn<MedicineDTO, Integer> colExpQty;

    // Correctly using BO from Factory
    DashboardBO dashboardBO = (DashboardBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.DASHBOARD);

    @FXML
    public void initialize() {
        loadDashboardCounts();
        loadChart();
        loadExpiringMedicines();
    }

    private void loadDashboardCounts() {
        try {
            // FIXED: Changed dashboardModel to dashboardBO
            lblTotalMedicines.setText(String.valueOf(dashboardBO.getTotalMedicineCount()));
            lblActiveCustomers.setText(String.valueOf(dashboardBO.getActiveCustomerCount()));
            lblTodayIncome.setText(String.format("Rs. %.2f", dashboardBO.getTodayIncome()));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadChart() {
        try {
            chartSales.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Daily Sales");

            // FIXED: Changed dashboardModel to dashboardBO
            Map<String, Double> trends = new TreeMap<>(dashboardBO.getIncomeTrends());
            for (Map.Entry<String, Double> entry : trends.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            chartSales.getData().add(series);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadExpiringMedicines() {
        colExpId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        colExpName.setCellValueFactory(new PropertyValueFactory<>("medName"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));
        colExpQty.setCellValueFactory(new PropertyValueFactory<>("qtyInStock"));
        try {
            // FIXED: Changed dashboardModel to dashboardBO
            tblExpiring.setItems(FXCollections.observableArrayList(dashboardBO.getExpiringMedicines()));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
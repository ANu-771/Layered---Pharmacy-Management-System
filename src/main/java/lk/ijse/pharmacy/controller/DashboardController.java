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

    private static DashboardController instance;

    @FXML
    private Label lblTotalMedicines, lblActiveCustomers, lblTodayIncome;
    @FXML
    private AreaChart<String, Number> chartSales;
    @FXML
    private TableView<MedicineDTO> tblExpiring;
    @FXML
    private TableColumn<MedicineDTO, String> colExpId, colExpName, colExpDate;
    @FXML
    private TableColumn<MedicineDTO, Integer> colExpQty;

    DashboardBO dashboardBO = (DashboardBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.DASHBOARD);

    public DashboardController() {
        instance = this;
    }

    public static DashboardController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        chartSales.setAnimated(false);
        refreshDashboard();
    }

    public void refreshDashboard() {
        loadDashboardCounts();
        loadChart();
        loadExpiringMedicines();
    }

    private void loadDashboardCounts() {
        try {
            lblTotalMedicines.setText(String.valueOf(dashboardBO.getTotalMedicineCount()));
            lblActiveCustomers.setText(String.valueOf(dashboardBO.getActiveCustomerCount()));
            lblTodayIncome.setText(String.format("Rs. %.2f", dashboardBO.getTodayIncome()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadChart() {
        try {
            Map<String, Double> trends = new TreeMap<>(dashboardBO.getIncomeTrends());
            chartSales.getData().clear();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Daily Sales");

            for (Map.Entry<String, Double> entry : trends.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            chartSales.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExpiringMedicines() {
        colExpId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        colExpName.setCellValueFactory(new PropertyValueFactory<>("medName"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));
        colExpQty.setCellValueFactory(new PropertyValueFactory<>("qtyInStock"));

        try {
            tblExpiring.setItems(FXCollections.observableArrayList(dashboardBO.getExpiringMedicines()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package lk.ijse.pharmacy.bo.custom;

import lk.ijse.pharmacy.bo.SuperBO;
import lk.ijse.pharmacy.dto.MedicineDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DashboardBO extends SuperBO {

    int getActiveCustomerCount() throws SQLException, ClassNotFoundException;

    int getTotalMedicineCount() throws SQLException, ClassNotFoundException;

    double getTodayIncome() throws SQLException, ClassNotFoundException;

    Map<String, Double> getIncomeTrends() throws SQLException, ClassNotFoundException;

    List<MedicineDTO> getExpiringMedicines() throws SQLException, ClassNotFoundException;

}
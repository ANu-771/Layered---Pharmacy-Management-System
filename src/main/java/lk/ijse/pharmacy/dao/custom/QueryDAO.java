package lk.ijse.pharmacy.dao.custom;

import lk.ijse.pharmacy.dao.SuperDAO;
import lk.ijse.pharmacy.dto.CustomDTO;
import lk.ijse.pharmacy.entity.Medicine;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface QueryDAO extends SuperDAO {

    int getActiveCustomerCount() throws SQLException, ClassNotFoundException;

    int getTotalMedicineCount() throws SQLException, ClassNotFoundException;

    double getTodayIncome() throws SQLException, ClassNotFoundException;

    Map<String, Double> getIncomeTrends() throws SQLException, ClassNotFoundException;

    List<Medicine> getExpiringMedicines() throws SQLException, ClassNotFoundException;

    int getTotalOrders() throws SQLException, ClassNotFoundException;

    int getItemsSold() throws SQLException, ClassNotFoundException;

    List<CustomDTO> getAllOrderDetails() throws SQLException, ClassNotFoundException;

    double getPaidAmount(int orderId) throws SQLException, ClassNotFoundException;

}
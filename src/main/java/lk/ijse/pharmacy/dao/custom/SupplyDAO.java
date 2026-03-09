package lk.ijse.pharmacy.dao.custom;

import lk.ijse.pharmacy.dao.SuperDAO;
import lk.ijse.pharmacy.entity.CustomEntity;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface SupplyDAO extends SuperDAO {

    int getMedicineId(String medName) throws SQLException, ClassNotFoundException;

    boolean saveSupply(int supplierId, int medicineId, LocalDate date, int qty, double unitCost, double totalCost) throws SQLException, ClassNotFoundException;

    List<CustomEntity> getAllSupplies() throws SQLException, ClassNotFoundException;

}

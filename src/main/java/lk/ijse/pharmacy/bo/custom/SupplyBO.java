package lk.ijse.pharmacy.bo.custom;

import lk.ijse.pharmacy.bo.SuperBO;
import lk.ijse.pharmacy.dto.tm.SupplyRecordTM;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface SupplyBO extends SuperBO {
    int getMedicineId(String medName) throws SQLException, ClassNotFoundException;

    boolean saveSupply(int supplierId, String medName, LocalDate date, int qty, double unitCost, double totalCost) throws SQLException, ClassNotFoundException;

    List<SupplyRecordTM> getAllSupplies() throws SQLException, ClassNotFoundException;
}
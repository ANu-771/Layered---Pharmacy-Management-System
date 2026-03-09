package lk.ijse.pharmacy.dao.custom;

import lk.ijse.pharmacy.dao.CrudDAO;
import lk.ijse.pharmacy.entity.Medicine;

import java.sql.SQLException;

public interface MedicineDAO extends CrudDAO<Medicine> {

    Medicine searchByName(String name) throws SQLException, ClassNotFoundException;

    boolean updateExactQty(int medicineId, int newQty) throws SQLException, ClassNotFoundException;

}


package lk.ijse.pharmacy.dao.custom;

import lk.ijse.pharmacy.dao.CrudDAO;
import lk.ijse.pharmacy.entity.OrderMedicine;

import java.sql.SQLException;

public interface OrderMedicineDAO extends CrudDAO<OrderMedicine> {
    int getItemsSold() throws SQLException, ClassNotFoundException;
}

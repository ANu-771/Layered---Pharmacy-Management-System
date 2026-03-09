package lk.ijse.pharmacy.dao.custom;

import lk.ijse.pharmacy.dao.CrudDAO;
import lk.ijse.pharmacy.entity.CustomEntity;
import lk.ijse.pharmacy.entity.Order;

import java.sql.SQLException;
import java.util.List;

public interface OrderDAO extends CrudDAO<Order> {

    int getLatestOrderId() throws SQLException, ClassNotFoundException;

    int getTotalOrders() throws SQLException, ClassNotFoundException;
    List<CustomEntity> getAllOrderDetails() throws SQLException, ClassNotFoundException;
}

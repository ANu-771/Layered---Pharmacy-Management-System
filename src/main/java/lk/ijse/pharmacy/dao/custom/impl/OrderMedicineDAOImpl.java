package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dao.custom.OrderMedicineDAO;
import lk.ijse.pharmacy.entity.OrderMedicine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderMedicineDAOImpl implements OrderMedicineDAO {

    @Override
    public boolean save(OrderMedicine entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO order_medicine (order_id, medicine_id, qty, unit_price, line_total) VALUES (?, ?, ?, ?, ?)",
                entity.getOrderId(), entity.getMedicineId(), entity.getQty(), entity.getUnitPrice(), entity.getLineTotal());
    }

    @Override
    public boolean update(OrderMedicine entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<OrderMedicine> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public OrderMedicine search(int id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public int getItemsSold() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT SUM(qty) FROM order_medicine");
        if (rs.next()) return rs.getInt(1);
        return 0;
    }
}
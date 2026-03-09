package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dao.custom.PaymentDAO;
import lk.ijse.pharmacy.entity.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public boolean save(Payment entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO payment (order_id, amount, payment_method) VALUES (?, ?, ?)",
                entity.getOrderId(), entity.getAmount(), entity.getPaymentMethod());
    }

    @Override
    public boolean update(Payment entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<Payment> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public Payment search(int id) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public double getPaidAmount(int orderId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT amount FROM payment WHERE order_id = ?", orderId);
        if (rs.next()) return rs.getDouble("amount");
        return 0.0;
    }
}
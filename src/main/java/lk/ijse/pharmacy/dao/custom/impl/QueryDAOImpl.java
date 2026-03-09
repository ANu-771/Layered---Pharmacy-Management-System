package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dao.custom.QueryDAO;
import lk.ijse.pharmacy.entity.CustomEntity;
import lk.ijse.pharmacy.entity.Medicine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public int getActiveCustomerCount() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM customer");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    @Override
    public int getTotalMedicineCount() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM medicine");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    @Override
    public double getTodayIncome() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT SUM(total) FROM orders WHERE order_date = CURRENT_DATE");
        if (rs.next()) {
            return rs.getDouble(1);
        }
        return 0.0;
    }

    @Override
    public Map<String, Double> getIncomeTrends() throws SQLException, ClassNotFoundException {
        Map<String, Double> trends = new HashMap<>();
        // Gets the income grouped by date for the last 7 days
        String sql = "SELECT order_date, SUM(total) as daily_total FROM orders GROUP BY order_date ORDER BY order_date DESC LIMIT 7";
        ResultSet rs = CrudUtil.execute(sql);

        while (rs.next()) {
            trends.put(rs.getString("order_date"), rs.getDouble("daily_total"));
        }
        return trends;
    }

    @Override
    public List<Medicine> getExpiringMedicines() throws SQLException, ClassNotFoundException {
        List<Medicine> list = new ArrayList<>();
        // Gets medicines expiring within the next 30 days
        String sql = "SELECT * FROM medicine WHERE exp_date <= DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY) ORDER BY exp_date ASC";
        ResultSet rs = CrudUtil.execute(sql);

        while (rs.next()) {
            list.add(new Medicine(
                    rs.getInt("medicine_id"),
                    rs.getString("med_name"),
                    rs.getString("brand"),
                    rs.getInt("qty_in_stock"),
                    rs.getDouble("unit_price"),
                    rs.getDate("exp_date")
            ));
        }
        return list;
    }

    @Override
    public int getTotalOrders() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM orders");
        if (rs.next()) return rs.getInt(1);
        return 0;
    }

    @Override
    public int getItemsSold() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT SUM(qty) FROM order_medicine");
        if (rs.next()) return rs.getInt(1);
        return 0;
    }

    @Override
    public List<CustomEntity> getAllOrderDetails() throws SQLException, ClassNotFoundException {
        List<CustomEntity> list = new ArrayList<>();
        String sql = "SELECT o.order_id, c.name, o.order_date, o.total FROM orders o JOIN customer c ON o.customer_id = c.customer_id ORDER BY o.order_date DESC, o.order_id DESC";
        ResultSet rs = CrudUtil.execute(sql);
        while (rs.next()) {
            // Use CustomEntity to hold joined data
            CustomEntity entity = new CustomEntity();
            entity.setOrderId(rs.getInt("order_id"));
            entity.setCustomerName(rs.getString("name"));
            entity.setDate(rs.getString("order_date"));
            entity.setTotalCost(rs.getDouble("total"));
            list.add(entity);
        }
        return list;
    }

    @Override
    public double getPaidAmount(int orderId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT amount FROM payment WHERE order_id = ?", orderId);
        if (rs.next()) return rs.getDouble("amount");
        return 0.0;
    }
}
package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dao.custom.OrderDAO;
import lk.ijse.pharmacy.entity.CustomEntity;
import lk.ijse.pharmacy.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean save(Order entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO orders (customer_id, user_id, total, order_date, order_time) VALUES (?, ?, ?, ?, ?)",
                entity.getCustomerId(), entity.getUserId(), entity.getTotal(),
                new java.sql.Date(entity.getOrderDate().getTime()), new java.sql.Time(entity.getOrderTime().getTime()));
    }

    @Override
    public boolean update(Order entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<Order> getAll() throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();
        ResultSet rs = CrudUtil.execute("SELECT * FROM orders");
        while(rs.next()) {
            list.add(new Order(rs.getInt("order_id"), rs.getInt("customer_id"), rs.getInt("user_id"),
                    rs.getDouble("total"), rs.getDate("order_date"), rs.getTime("order_time")));
        }
        return list;
    }

    @Override
    public Order search(int id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM orders WHERE order_id=?", id);
        if(rs.next()) {
            return new Order(rs.getInt("order_id"), rs.getInt("customer_id"), rs.getInt("user_id"),
                    rs.getDouble("total"), rs.getDate("order_date"), rs.getTime("order_time"));
        }
        return null;
    }

    @Override
    public int getLatestOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    @Override
    public int getTotalOrders() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT COUNT(*) FROM orders");
        if (rs.next()) return rs.getInt(1);
        return 0;
    }

    @Override
    public List<CustomEntity> getAllOrderDetails() throws SQLException, ClassNotFoundException {
        List<CustomEntity> list = new ArrayList<>();
        String sql = "SELECT o.order_id, c.name, o.order_date, o.total FROM orders o JOIN customer c ON o.customer_id = c.customer_id ORDER BY o.order_date DESC, o.order_id DESC";
        ResultSet rs = CrudUtil.execute(sql);
        while (rs.next()) {
            CustomEntity entity = new CustomEntity();
            entity.setOrderId(rs.getInt("order_id"));
            entity.setCustomerName(rs.getString("name"));
            entity.setDate(rs.getString("order_date"));
            entity.setTotalCost(rs.getDouble("total"));
            list.add(entity);
        }
        return list;
    }
}
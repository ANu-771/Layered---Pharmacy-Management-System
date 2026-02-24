package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.OrderDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAOImpl {

    public String getNextOrderId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery(sql);

        if (resultSet.next()) {
            return splitOrderId(resultSet.getString(1));
        }
        return "1";
    }

    private String splitOrderId(String currentId) {
        if (currentId != null) {
            return String.valueOf(Integer.parseInt(currentId) + 1);
        }
        return "1";
    }


    public boolean saveOrder(OrderDTO orderDTO) throws SQLException {
        String sqlOrder = "INSERT INTO orders (customer_id, user_id, total, order_date, order_time) VALUES (?, ?, ?, ?, ?)";

        return CrudUtil.execute(
                sqlOrder,
                orderDTO.getCustomerId(),
                orderDTO.getUserId(),
                orderDTO.getTotal(),
                new java.sql.Date(orderDTO.getOrderDate().getTime()),
                new java.sql.Time(orderDTO.getOrderDate().getTime())
        );
    }

    public int getLastOrderId() throws SQLException, ClassNotFoundException {
        String sql = "SELECT LAST_INSERT_ID() AS order_id FROM orders";
        ResultSet rs = CrudUtil.execute(sql);

        if (rs.next()) {
            return rs.getInt("order_id");
        }

        else {
            throw  new SQLException("Order ID not found");
        }
    }



}

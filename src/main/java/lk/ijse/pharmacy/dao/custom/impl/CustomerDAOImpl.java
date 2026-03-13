package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dao.custom.CustomerDAO;
import lk.ijse.pharmacy.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public boolean save(Customer entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO customer (name, contact, address) VALUES (?,?,?)",
                entity.getName(),
                entity.getContact(),
                entity.getAddress()
        );
    }

    @Override
    public boolean update(Customer entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE customer SET name = ?, contact = ?, address = ? WHERE customer_id = ?",
                entity.getName(),
                entity.getContact(),
                entity.getAddress(),
                entity.getCustomerId()
        );
    }

    @Override
    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM customer WHERE customer_id = ?", id);
    }

    @Override
    public List<Customer> getAll() throws SQLException, ClassNotFoundException {
        List<Customer> list = new ArrayList<>();

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");
        while (resultSet.next()) {
            list.add(new Customer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getString("address")
            ));
        }

        return list;
    }

    @Override
    public Customer search(int id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer WHERE customer_id = ?", id);

        if (resultSet.next()) {
            return new Customer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getString("address")
            );
        }
        return null;
    }

}
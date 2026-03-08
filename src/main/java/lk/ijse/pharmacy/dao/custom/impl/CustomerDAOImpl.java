package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.CustomerDTO;
import org.apache.commons.collections.functors.WhileClosure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl {

    // SAVE
    public boolean save(CustomerDTO customer) throws SQLException, ClassNotFoundException {

        return CrudUtil.execute(
                "INSERT INTO customer (name, contact, address) VALUES (?,?,?)",
                customer.getName(),
                customer.getContact(),
                customer.getAddress()
        );
    }

    // UPDATE
    public boolean update(CustomerDTO customer) throws SQLException, ClassNotFoundException {

        return CrudUtil.execute(
                "UPDATE customer SET name = ?, contact = ?, address = ? WHERE customer_id = ?",
                customer.getName(),
                customer.getContact(),
                customer.getAddress(),
                customer.getCustomerId()
        );
    }

    // DELETE
    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM customer WHERE customer_id = ?", id);
    }


    // GET ALL Customers TableView
    public List<CustomerDTO> getAll() throws SQLException, ClassNotFoundException {
        List<CustomerDTO> list = new ArrayList<>();

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");
        while (resultSet.next()) {
            list.add(new CustomerDTO(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getString("address")
            ));
        }

        return list;
    }

    // SEARCH Customer
    public CustomerDTO search(int id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer WHERE id = ?", id);

        if (resultSet.next()) {
            return new CustomerDTO(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("contact"),
                    resultSet.getString("address")
            );
        }
        return null;
    }

}

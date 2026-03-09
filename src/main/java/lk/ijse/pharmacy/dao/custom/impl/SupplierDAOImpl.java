package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dao.custom.SupplierDAO;
import lk.ijse.pharmacy.dto.SupplierDTO;
import lk.ijse.pharmacy.entity.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAOImpl implements SupplierDAO {

    @Override
    public boolean save(Supplier entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO supplier (supplier_name, email, contact_num) VALUES (?,?,?)",
                entity.getSupplierName(),
                entity.getEmail(),
                entity.getContactNum()
        );
    }

    @Override
    public boolean update(Supplier entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE supplier SET supplier_name = ?, email = ?, contact_num = ? WHERE supplier_id = ?",
                entity.getSupplierName(),
                entity.getEmail(),
                entity.getContactNum(),
                entity.getSupplierId()
        );
    }

    @Override
    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM supplier WHERE supplier_id = ?", id);
    }

    @Override
    public List<Supplier> getAll() throws SQLException, ClassNotFoundException {
        List<Supplier> list = new ArrayList<>();

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplier");

        while (resultSet.next()) {
            list.add(new Supplier(
                    resultSet.getInt("supplier_id"),
                    resultSet.getString("supplier_name"),
                    resultSet.getString("email"),
                    resultSet.getString("contact_num")
            ));
        }
        return list;
    }

    @Override
    public Supplier search(int id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplier WHERE supplier_id = ?", id);

        if (resultSet.next()) {
            return new Supplier(
                    resultSet.getInt("supplier_id"),
                    resultSet.getString("supplier_name"),
                    resultSet.getString("email"),
                    resultSet.getString("contact_num")
            );
        }
        return null;
    }
}

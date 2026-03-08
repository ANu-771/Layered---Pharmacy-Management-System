package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dto.SupplierDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAOImpl {

    public boolean save(SupplierDTO supplier) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO supplier (supplier_name, email, contact_num) VALUES (?,?,?)",
                supplier.getSupplierName(),
                supplier.getEmail(),
                supplier.getContactNum()
        );
    }

    public boolean update(SupplierDTO supplier) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE supplier SET supplier_name = ?, email = ?, contact_num = ? WHERE supplier_id = ?",
                supplier.getSupplierName(),
                supplier.getEmail(),
                supplier.getContactNum(),
                supplier.getSupplierId()
        );
    }

    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM supplier WHERE supplier_id = ?", id);
    }

    public List<SupplierDTO> getAll() throws SQLException, ClassNotFoundException {
        List<SupplierDTO> list = new ArrayList<>();

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplier");

        while (resultSet.next()) {
            list.add(new SupplierDTO(
                    resultSet.getInt("supplier_id"),
                    resultSet.getString("supplier_name"),
                    resultSet.getString("email"),
                    resultSet.getString("contact_num")
            ));
        }
        return list;
    }

    public SupplierDTO search(int id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM supplier WHERE supplier_id = ?", id);

        if (resultSet.next()) {
            return new SupplierDTO(
                    resultSet.getInt("supplier_id"),
                    resultSet.getString("supplier_name"),
                    resultSet.getString("email"),
                    resultSet.getString("contact_num")
            );
        }
        return null;
    }
}

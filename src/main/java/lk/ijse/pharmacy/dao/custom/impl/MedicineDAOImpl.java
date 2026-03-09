package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dao.custom.MedicineDAO;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.MedicineDTO;
import lk.ijse.pharmacy.entity.Medicine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAOImpl implements MedicineDAO {

    @Override
    public List<Medicine> getAll() throws SQLException, ClassNotFoundException {
        List<Medicine> list = new ArrayList<>();

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM medicine");
        while (resultSet.next()) {
            list.add(new Medicine(
                    resultSet.getInt("medicine_id"), // Use getInt
                    resultSet.getString("med_name"),
                    resultSet.getString("brand"),
                    resultSet.getInt("qty_in_stock"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDate("exp_date")
            ));
        }
        return list;
    }

    @Override
    public boolean save(Medicine entity) throws SQLException, ClassNotFoundException {

        return CrudUtil.execute(
                "INSERT INTO medicine (med_name, brand, unit_price, exp_date, qty_in_stock) VALUES (?, ?, ?, ?, ?)",
                entity.getMedName(),
                entity.getBrand(),
                entity.getUnitPrice(),
                new java.sql.Date(entity.getExpDate().getTime()),
                entity.getQtyInStock()
        );
    }

    @Override
    public boolean update(Medicine entity) throws SQLException, ClassNotFoundException {

        return CrudUtil.execute(
                "UPDATE medicine SET med_name=?, brand=?, unit_price=?, exp_date=?, qty_in_stock=? WHERE medicine_id=?",
                entity.getMedName(),
                entity.getBrand(),
                entity.getUnitPrice(),
                new java.sql.Date(entity.getExpDate().getTime()),
                entity.getQtyInStock(),
                entity.getMedicineId()
        );
    }

    @Override
    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM medicine WHERE medicine_id=?", id);
    }

    @Override
    public Medicine search(int id) throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM medicine WHERE medicine_id=?", id);
        if (resultSet.next()) {
            return new Medicine(
                    resultSet.getInt("medicine_id"),
                    resultSet.getString("med_name"),
                    resultSet.getString("brand"),
                    resultSet.getInt("qty_in_stock"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDate("exp_date")
            );
        }
        return null;
    }

    @Override
    public Medicine searchByName(String name) throws SQLException, ClassNotFoundException {

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM medicine WHERE med_name = ?", name);
        if (resultSet.next()) {
            return new Medicine(
                    resultSet.getInt("medicine_id"),
                    resultSet.getString("med_name"),
                    resultSet.getString("brand"),
                    resultSet.getInt("qty_in_stock"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDate("exp_date")
            );
        }
        return null;
    }

    @Override
    public boolean updateExactQty(int medicineId, int newQty) throws SQLException {
        String sql = "UPDATE medicine SET qty_in_stock = ? WHERE medicine_id = ?";
        return CrudUtil.execute(sql, newQty, medicineId);
    }

}

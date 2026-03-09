package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dao.custom.SupplyDAO;
import lk.ijse.pharmacy.entity.CustomEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplyDAOImpl implements SupplyDAO {

    @Override
    public int getMedicineId(String medName) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT medicine_id FROM medicine WHERE med_name = ?", medName);
        if (resultSet.next()) {
            return resultSet.getInt("medicine_id");
        }
        return -1;
    }

    @Override
    public boolean saveSupply(int supplierId, int medicineId, LocalDate date, int qty, double unitCost, double totalCost) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO supplier_medicine (supplier_id, medicine_id, date, qty, unit_cost, total_cost) VALUES (?, ?, ?, ?, ?, ?)",
                supplierId, medicineId, java.sql.Date.valueOf(date), qty, unitCost, totalCost
        );
    }

    @Override
    public List<CustomEntity> getAllSupplies() throws SQLException, ClassNotFoundException {
        List<CustomEntity> list = new ArrayList<>();
        String sql = "SELECT sm.date, sm.supplier_id, s.supplier_name, m.med_name, sm.qty, sm.unit_cost, sm.total_cost " +
                "FROM supplier_medicine sm " +
                "JOIN supplier s ON sm.supplier_id = s.supplier_id " +
                "JOIN medicine m ON sm.medicine_id = m.medicine_id " +
                "ORDER BY sm.date DESC, sm.supply_id DESC";

        ResultSet resultSet = CrudUtil.execute(sql);

        while (resultSet.next()) {
            list.add(new CustomEntity(
                    resultSet.getString("date"),
                    // resultSet.getDate("date"),
                    resultSet.getInt("supplier_id"),
                    resultSet.getString("supplier_name"),
                    resultSet.getString("med_name"),
                    resultSet.getInt("qty"),
                    resultSet.getDouble("unit_cost"),
                    resultSet.getDouble("total_cost")
            ));
        }
        return list;
    }
}
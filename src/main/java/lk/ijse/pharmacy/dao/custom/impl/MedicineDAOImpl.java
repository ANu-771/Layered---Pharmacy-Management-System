package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.MedicineDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAOImpl {

    public List<MedicineDTO> getAll() throws SQLException, ClassNotFoundException {
        List<MedicineDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM medicine";
        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            list.add(new MedicineDTO(
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
}

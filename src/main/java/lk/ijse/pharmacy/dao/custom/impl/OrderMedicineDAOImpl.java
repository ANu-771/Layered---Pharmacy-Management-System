package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dto.OrderMedicineDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderMedicineDAOImpl {

    public boolean saveOrderMedicine(OrderMedicineDTO orderMedicineDTO)throws SQLException {


        String sqlDetail = "INSERT INTO order_medicine (order_id, medicine_id, qty, unit_price, line_total) VALUES (?, ?, ?, ?, ?)";

        return CrudUtil.execute(sqlDetail,
                orderMedicineDTO.getOrderId(),
                orderMedicineDTO.getMedicineId(),
                orderMedicineDTO.getQty(),
                orderMedicineDTO.getUnitPrice(),
                orderMedicineDTO.getLineTotal()
                );
    }

}

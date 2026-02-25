package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.PaymentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentDAOImpl {

    public boolean savePayment(PaymentDTO paymentDTO) throws SQLException {
        String sqlPayment = "INSERT INTO payment (order_id, amount, payment_method, payment_date) VALUES (?, ?, ?, ?)";

//        // FIX: Pass 'connection' to CrudUtil
//        Connection conn = DBConnection.getInstance().getConnection();
//        PreparedStatement ptsm = conn.prepareStatement(sqlPayment);
//        ptsm.setInt(1, paymentDTO.getOrderId());
//        ptsm.setDouble(2, paymentDTO.getAmount());
//        ptsm.setString(3, paymentDTO.getPaymentMethod());
//        ptsm.setTimestamp(4,new java.sql.Timestamp(System.currentTimeMillis()));
//
//        int i = ptsm.executeUpdate();
//        return i > 0;

        return CrudUtil.execute(
                sqlPayment,
                paymentDTO.getOrderId(),
                paymentDTO.getAmount(),
                paymentDTO.getPaymentMethod(),
                new java.sql.Timestamp(System.currentTimeMillis())
        );
    }

}

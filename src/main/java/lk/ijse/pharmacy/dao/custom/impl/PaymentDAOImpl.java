package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dto.PaymentDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentDAOImpl {

    public boolean savePayment(PaymentDTO paymentDTO)throws SQLException {

        String sqlPayment = "INSERT INTO payment (order_id, amount, payment_method, payment_date) VALUES (?, ?, ?, ?)";

        return CrudUtil.execute(sqlPayment,
                paymentDTO.getOrderId(),
                paymentDTO.getAmount(),
                paymentDTO.getPaymentMethod(),
                new java.sql.Timestamp(System.currentTimeMillis())
                );
    }

}

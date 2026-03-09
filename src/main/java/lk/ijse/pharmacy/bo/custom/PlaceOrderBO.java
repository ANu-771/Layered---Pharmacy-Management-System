package lk.ijse.pharmacy.bo.custom;

import lk.ijse.pharmacy.bo.SuperBO;
import lk.ijse.pharmacy.dto.OrderDTO;
import lk.ijse.pharmacy.dto.OrderMedicineDTO;
import lk.ijse.pharmacy.dto.PaymentDTO;

import java.sql.SQLException;
import java.util.List;

public interface PlaceOrderBO extends SuperBO {
    boolean placeOrder(OrderDTO orderDTO, List<OrderMedicineDTO> orderMedicineList, PaymentDTO paymentDTO) throws SQLException, ClassNotFoundException;

    int getLatestOrderId() throws SQLException, ClassNotFoundException;
}
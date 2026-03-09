package lk.ijse.pharmacy.bo.custom.impl;

import lk.ijse.pharmacy.bo.custom.PlaceOrderBO;
import lk.ijse.pharmacy.dao.DAOFactory;
import lk.ijse.pharmacy.dao.custom.MedicineDAO;
import lk.ijse.pharmacy.dao.custom.OrderDAO;
import lk.ijse.pharmacy.dao.custom.OrderMedicineDAO;
import lk.ijse.pharmacy.dao.custom.PaymentDAO;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.OrderDTO;
import lk.ijse.pharmacy.dto.OrderMedicineDTO;
import lk.ijse.pharmacy.dto.PaymentDTO;
import lk.ijse.pharmacy.entity.Medicine;
import lk.ijse.pharmacy.entity.Order;
import lk.ijse.pharmacy.entity.OrderMedicine;
import lk.ijse.pharmacy.entity.Payment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PlaceOrderBOImpl implements PlaceOrderBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderMedicineDAO orderMedicineDAO = (OrderMedicineDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);
    MedicineDAO medicineDAO = (MedicineDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.MEDICINE);
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PAYMENT);

    @Override
    public boolean placeOrder(OrderDTO orderDTO, List<OrderMedicineDTO> orderMedicineList, PaymentDTO paymentDTO) throws SQLException, ClassNotFoundException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            Order orderEntity = new Order(0, orderDTO.getCustomerId(), orderDTO.getUserId(), orderDTO.getTotal(),
                    new java.sql.Date(orderDTO.getOrderDate().getTime()), new java.sql.Time(orderDTO.getOrderDate().getTime()));

            boolean isOrderSaved = orderDAO.save(orderEntity);

            if (isOrderSaved) {
                // Get the generated Order ID for the details and payment tables
                int currentOrderId = orderDAO.getLatestOrderId();

                for (OrderMedicineDTO detail : orderMedicineList) {
                    OrderMedicine detailEntity = new OrderMedicine(currentOrderId, detail.getMedicineId(), detail.getQty(), detail.getUnitPrice(), detail.getLineTotal());
                    boolean isDetailSaved = orderMedicineDAO.save(detailEntity);

                    if (isDetailSaved) {
                        //  Update the actual Medicine stock
                        Medicine medicine = medicineDAO.search(detail.getMedicineId());
                        if (medicine != null) {
                            int newQty = medicine.getQtyInStock() - detail.getQty();
                            boolean isStockUpdated = medicineDAO.updateExactQty(medicine.getMedicineId(), newQty);

                            if (!isStockUpdated) {
                                connection.rollback();
                                return false;
                            }
                        }
                    } else {
                        connection.rollback();
                        return false;
                    }
                }

                Payment paymentEntity = new Payment(0, currentOrderId, paymentDTO.getAmount(), null, paymentDTO.getPaymentMethod());
                boolean isPaymentSaved = paymentDAO.save(paymentEntity);

                if (isPaymentSaved) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;

        } catch (SQLException | ClassNotFoundException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public int getLatestOrderId() throws SQLException, ClassNotFoundException {
        return orderDAO.getLatestOrderId();
    }
}
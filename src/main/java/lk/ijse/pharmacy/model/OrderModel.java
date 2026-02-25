package lk.ijse.pharmacy.model;

import lk.ijse.pharmacy.dao.custom.OrderDAO;
import lk.ijse.pharmacy.dao.custom.impl.MedicineDAOImpl;
import lk.ijse.pharmacy.dao.custom.impl.OrderDAOImpl;
import lk.ijse.pharmacy.dao.custom.impl.OrderMedicineDAOImpl;
import lk.ijse.pharmacy.dao.custom.impl.PaymentDAOImpl;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.MedicineDTO;
import lk.ijse.pharmacy.dto.OrderDTO;
import lk.ijse.pharmacy.dto.OrderMedicineDTO;
import lk.ijse.pharmacy.dto.PaymentDTO;
import lk.ijse.pharmacy.dto.tm.CartTM;

import java.sql.*;
import java.util.List;

public class OrderModel {


//    public String getNextOrderId() throws SQLException, ClassNotFoundException {
//        String sql = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
//        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery(sql);
//
//        if (resultSet.next()) {
//            return splitOrderId(resultSet.getString(1));
//        }
//        return "1";
//    }
//
//    private String splitOrderId(String currentId) {
//        if (currentId != null) {
//            return String.valueOf(Integer.parseInt(currentId) + 1);
//        }
//        return "1";
//    }

    //  THE TRANSACTION METHOD
    public String placeOrder(OrderDTO order, List<CartTM> cartList, String paymentMethod, double cashAmount) throws SQLException {

        OrderDAOImpl orderDAO = new OrderDAOImpl();
        OrderMedicineDAOImpl orderMedicineDAO = new OrderMedicineDAOImpl();
        MedicineDAOImpl medicineDAO = new MedicineDAOImpl();
        PaymentDAOImpl paymentDAO = new PaymentDAOImpl();

        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);


            boolean isSaved = orderDAO.saveOrder(order);
            if (!isSaved) {
                connection.rollback();
                return null;
            }

            int orderId = orderDAO.getLastOrderId();
            if (orderId <= 0) {
                connection.rollback();
                return null;
            }

            String generatedOrderId = String.valueOf(orderId);


            System.out.println(cartList);
            for (CartTM cartItem : cartList) {
                int medicineId = Integer.parseInt(cartItem.getMedicineId());
                int qty = cartItem.getQty();


                OrderMedicineDTO orderMedicineDTO = new OrderMedicineDTO(
                        Integer.parseInt(generatedOrderId),
                        medicineId,
                        qty,
                        cartItem.getUnitPrice(),
                        cartItem.getTotal()
                );

                boolean isSavedOrderDetails = orderMedicineDAO.saveOrderMedicine(orderMedicineDTO);

                if (!isSavedOrderDetails) {
                    connection.rollback();
                    System.out.println("Failed to save order details for Medicine ID: " + medicineId);
                    return null;
                }

                int qtyInStock = medicineDAO.search(medicineId).getQtyInStock();
                int newQty = qtyInStock - qty;


                boolean isUpdated = medicineDAO.updateExactQty(medicineId, newQty);
                System.out.println("Decreasing quantity for Medicine ID: " + medicineId + ", Quantity: " + qty + ", Update Result: " + isUpdated);

                if (!isUpdated) {
                    connection.rollback();
                    System.out.println("Failed to decrease stock for Medicine ID: " + medicineId);
                    return null;
                }
            }

            PaymentDTO paymentDTO = new PaymentDTO(
                    Integer.parseInt(generatedOrderId),
                    cashAmount,
                    paymentMethod
            );
            System.out.println("Generated Order ID: " + generatedOrderId);

            System.out.println("PaymentDTO: " + paymentDTO.getOrderId() + ", " + paymentDTO.getAmount() + ", " + paymentDTO.getPaymentMethod());
            boolean isPaymentUpdated = paymentDAO.savePayment(paymentDTO);
            if (!isPaymentUpdated) {
                connection.rollback();
                System.out.println("Failed to save payment for Order ID: " + generatedOrderId);
                return null;
            }


            connection.commit();
            return generatedOrderId;

        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) connection.rollback();
            return null;
        } finally {
            if (connection != null) connection.setAutoCommit(true);
        }
    }
}


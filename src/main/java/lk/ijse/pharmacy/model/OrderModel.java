package lk.ijse.pharmacy.model;

import lk.ijse.pharmacy.dao.custom.OrderDAO;
import lk.ijse.pharmacy.dao.custom.impl.MedicineDAOImpl;
import lk.ijse.pharmacy.dao.custom.impl.OrderDAOImpl;
import lk.ijse.pharmacy.dao.custom.impl.OrderMedicineDAOImpl;
import lk.ijse.pharmacy.dao.custom.impl.PaymentDAOImpl;
import lk.ijse.pharmacy.dbconnection.DBConnection;
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


//            String sqlOrder = "INSERT INTO orders (customer_id, user_id, total, order_date, order_time) VALUES (?, ?, ?, ?, ?)";
//            PreparedStatement pstmOrder = connection.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
//            pstmOrder.setInt(1, Integer.parseInt(String.valueOf(order.getCustomerId())));
//            pstmOrder.setInt(2, 1);
//            pstmOrder.setDouble(3, order.getTotal());
//            pstmOrder.setDate(4, new java.sql.Date(order.getOrderDate().getTime()));
//
//            // SET TIME set the time from Java
//            pstmOrder.setTime(5, new java.sql.Time(order.getOrderDate().getTime()));

            boolean isSaved = orderDAO.saveOrder(order);
            if (!isSaved) {
                connection.rollback();
                return null;
            }

            int orderId = orderDAO.getLastOrderId();
            if(orderId <= 0){
                connection.rollback();
                return null;
            }



//            boolean isOrderSaved = pstmOrder.executeUpdate() > 0;
            String generatedOrderId = String.valueOf(orderId);

//            if (isSaved) {

//                ResultSet generatedKeys = pstmOrder.getGeneratedKeys();
//                if (generatedKeys.next()) {
//                    // Assign the ID to the String variable used later
//                    generatedOrderId = String.valueOf(generatedKeys.getInt(1));
//                }


                boolean isDetailsSaved = true;
                boolean isStockUpdated = true;

                for (CartTM cartItem : cartList) {
                    int medicineId = Integer.parseInt(cartItem.getMedicineId());
                    int qty = cartItem.getQty();


                    // insert ORDER DETAILS
//                    String sqlDetail = "INSERT INTO order_medicine (order_id, medicine_id, qty, unit_price, line_total) VALUES (?, ?, ?, ?, ?)";
//                    PreparedStatement pstmDetail = connection.prepareStatement(sqlDetail);
//
//                    pstmDetail.setInt(1, Integer.parseInt(generatedOrderId));
//                    pstmDetail.setInt(2, medicineId);
//                    pstmDetail.setInt(3, qty);
//                    pstmDetail.setDouble(4, cartItem.getUnitPrice());
//                    pstmDetail.setDouble(5, cartItem.getTotal());

                    OrderMedicineDTO orderMedicineDTO = new OrderMedicineDTO(
                            Integer.parseInt(generatedOrderId),
                            medicineId,
                            qty,
                            cartItem.getUnitPrice(),
                            cartItem.getTotal()
                    );

                    boolean isSavedOrderDetails = orderMedicineDAO.saveOrderMedicine(orderMedicineDTO);

//
//                    if (pstmDetail.executeUpdate() <= 0) {
//                        isDetailsSaved = false;
//                        break;
//                    }
                    if(!isSavedOrderDetails) {
                        connection.rollback();
                        return null;
                    }


                    // UPDATE MEDICINE STOCK (Decrease Qty)
//                    String sqlStock = "UPDATE medicine SET qty_in_stock = qty_in_stock - ? WHERE medicine_id = ?";
//                    PreparedStatement pstmStock = connection.prepareStatement(sqlStock);
//                    pstmStock.setInt(1, qty);
//                    pstmStock.setInt(2, medicineId);
//
//                    if (pstmStock.executeUpdate() <= 0) {
//                        isStockUpdated = false;
//                        break;
//                    }

                    boolean isUpdated =  medicineDAO.decreaseQty(qty,medicineId);

                    if(!isUpdated) {
                        connection.rollback();
                        return null;
                    }


//                }

                PaymentDTO paymentDTO = new PaymentDTO(
                        Integer.parseInt(generatedOrderId),
                        cashAmount,
                        paymentMethod
                );
                boolean isPaymentUpdated = paymentDAO.savePayment(paymentDTO);
                if(!isPaymentUpdated) {
                    connection.rollback();
                    return null;
                }

                //: SAVE PAYMENT
//                boolean isPaymentSaved = true;
//                if (isDetailsSaved && isStockUpdated) {
//
//                    String sqlPayment = "INSERT INTO payment (order_id, amount, payment_method, payment_date) VALUES (?, ?, ?, ?)";
//                    PreparedStatement pstmPayment = connection.prepareStatement(sqlPayment);
//
//                    pstmPayment.setInt(1, Integer.parseInt(generatedOrderId));
//                    pstmPayment.setDouble(2, cashAmount);
//                    pstmPayment.setString(3, paymentMethod);
//                    pstmPayment.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
//
//                    if (pstmPayment.executeUpdate() <= 0) {
//                        isPaymentSaved = false;
//
//                    }
//                }
//
//                //COMMIT IF ALL SUCCESS
//                if (isDetailsSaved && isStockUpdated && isPaymentSaved) {
//                    // Commit Transaction
//                    connection.commit();
//                    return generatedOrderId;
//                }
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


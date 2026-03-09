package lk.ijse.pharmacy.bo.custom.impl;

import lk.ijse.pharmacy.bo.custom.ReportBO;
import lk.ijse.pharmacy.dao.DAOFactory;
import lk.ijse.pharmacy.dao.custom.OrderDAO;
import lk.ijse.pharmacy.dao.custom.OrderMedicineDAO;
import lk.ijse.pharmacy.dao.custom.PaymentDAO;
import lk.ijse.pharmacy.dto.tm.ReportTM;
import lk.ijse.pharmacy.entity.CustomEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportBOImpl implements ReportBO {

    OrderDAO orderDAO = (OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER);
    OrderMedicineDAO orderMedicineDAO = (OrderMedicineDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.PAYMENT);

    @Override
    public int getTotalOrders() throws SQLException, ClassNotFoundException {
        return orderDAO.getTotalOrders();
    }

    @Override
    public int getItemsSold() throws SQLException, ClassNotFoundException {
        return orderMedicineDAO.getItemsSold();
    }

    @Override
    public double getPaidAmount(int orderId) throws SQLException, ClassNotFoundException {
        return paymentDAO.getPaidAmount(orderId);
    }

    @Override
    public List<ReportTM> getAllOrderDetails() throws SQLException, ClassNotFoundException {
        List<CustomEntity> entities = orderDAO.getAllOrderDetails();
        List<ReportTM> tmList = new ArrayList<>();

        for (CustomEntity e : entities) {
            tmList.add(new ReportTM(
                    String.valueOf(e.getOrderId()),
                    e.getCustomerName(),
                    e.getDate(),
                    e.getTotalCost()
            ));
        }
        return tmList;
    }
}
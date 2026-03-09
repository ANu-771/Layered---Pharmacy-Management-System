package lk.ijse.pharmacy.bo.custom;

import lk.ijse.pharmacy.bo.SuperBO;
import lk.ijse.pharmacy.dto.tm.ReportTM;
import java.sql.SQLException;
import java.util.List;

public interface ReportBO extends SuperBO {
    int getTotalOrders() throws SQLException, ClassNotFoundException;
    int getItemsSold() throws SQLException, ClassNotFoundException;
    List<ReportTM> getAllOrderDetails() throws SQLException, ClassNotFoundException;
    double getPaidAmount(int orderId) throws SQLException, ClassNotFoundException;
}
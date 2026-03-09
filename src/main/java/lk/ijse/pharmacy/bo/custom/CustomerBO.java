package lk.ijse.pharmacy.bo.custom;

import lk.ijse.pharmacy.bo.SuperBO;
import lk.ijse.pharmacy.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBO extends SuperBO {
    boolean saveCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException;
    boolean updateCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException;
    boolean deleteCustomer(int id) throws SQLException, ClassNotFoundException;
    List<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException;
    CustomerDTO searchCustomer(int id) throws SQLException, ClassNotFoundException;
}
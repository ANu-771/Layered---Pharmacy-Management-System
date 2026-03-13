package lk.ijse.pharmacy.bo.custom;

import lk.ijse.pharmacy.bo.SuperBO;
import lk.ijse.pharmacy.dto.SupplierDTO;

import java.sql.SQLException;
import java.util.List;

public interface SupplierBO extends SuperBO {

    boolean saveSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteSupplier(int id) throws SQLException, ClassNotFoundException;

    List<SupplierDTO> getAllSuppliers() throws SQLException, ClassNotFoundException;

    SupplierDTO searchSupplier(int id) throws SQLException, ClassNotFoundException;
}
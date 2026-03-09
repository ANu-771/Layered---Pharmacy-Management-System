package lk.ijse.pharmacy.bo.custom.impl;

import lk.ijse.pharmacy.bo.custom.SupplierBO;
import lk.ijse.pharmacy.dao.DAOFactory;
import lk.ijse.pharmacy.dao.custom.SupplierDAO;
import lk.ijse.pharmacy.dto.SupplierDTO;
import lk.ijse.pharmacy.entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierBOImpl implements SupplierBO {

    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.SUPPLIER);

    @Override
    public boolean saveSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException {
        return supplierDAO.save(new Supplier(dto.getSupplierId(), dto.getSupplierName(), dto.getEmail(), dto.getContactNum()));
    }

    @Override
    public boolean updateSupplier(SupplierDTO dto) throws SQLException, ClassNotFoundException {
        return supplierDAO.update(new Supplier(dto.getSupplierId(), dto.getSupplierName(), dto.getEmail(), dto.getContactNum()));
    }

    @Override
    public boolean deleteSupplier(int id) throws SQLException, ClassNotFoundException {
        return supplierDAO.delete(id);
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() throws SQLException, ClassNotFoundException {
        List<Supplier> entityList = supplierDAO.getAll();
        List<SupplierDTO> dtoList = new ArrayList<>();
        for (Supplier entity : entityList) {
            dtoList.add(new SupplierDTO(entity.getSupplierId(), entity.getSupplierName(), entity.getEmail(), entity.getContactNum()));
        }
        return dtoList;
    }

    @Override
    public SupplierDTO searchSupplier(int id) throws SQLException, ClassNotFoundException {
        Supplier entity = supplierDAO.search(id);
        if (entity != null) {
            return new SupplierDTO(entity.getSupplierId(), entity.getSupplierName(), entity.getEmail(), entity.getContactNum());
        }
        return null;
    }
}
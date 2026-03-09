package lk.ijse.pharmacy.bo.custom.impl;

import lk.ijse.pharmacy.bo.custom.MedicineBO;
import lk.ijse.pharmacy.dao.DAOFactory;
import lk.ijse.pharmacy.dao.custom.MedicineDAO;
import lk.ijse.pharmacy.dto.MedicineDTO;
import lk.ijse.pharmacy.entity.Medicine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineBOImpl implements MedicineBO {

    MedicineDAO medicineDAO = (MedicineDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.MEDICINE);

    @Override
    public boolean saveMedicine(MedicineDTO dto) throws SQLException, ClassNotFoundException {
        return medicineDAO.save(new Medicine(
                dto.getMedicineId(), dto.getMedName(), dto.getBrand(),
                dto.getQtyInStock(), dto.getUnitPrice(),
                new java.sql.Date(dto.getExpDate().getTime())
        ));
    }

    @Override
    public boolean updateMedicine(MedicineDTO dto) throws SQLException, ClassNotFoundException {
        return medicineDAO.update(new Medicine(
                dto.getMedicineId(), dto.getMedName(), dto.getBrand(),
                dto.getQtyInStock(), dto.getUnitPrice(),
                new java.sql.Date(dto.getExpDate().getTime())
        ));
    }

    @Override
    public boolean deleteMedicine(int id) throws SQLException, ClassNotFoundException {
        return medicineDAO.delete(id);
    }

    @Override
    public List<MedicineDTO> getAllMedicines() throws SQLException, ClassNotFoundException {
        List<Medicine> entityList = medicineDAO.getAll();
        List<MedicineDTO> dtoList = new ArrayList<>();
        for (Medicine entity : entityList) {
            dtoList.add(new MedicineDTO(
                    entity.getMedicineId(), entity.getMedName(), entity.getBrand(),
                    entity.getQtyInStock(), entity.getUnitPrice(), entity.getExpDate()
            ));
        }
        return dtoList;
    }

    @Override
    public MedicineDTO searchMedicine(int id) throws SQLException, ClassNotFoundException {
        Medicine entity = medicineDAO.search(id);
        if (entity != null) {
            return new MedicineDTO(
                    entity.getMedicineId(), entity.getMedName(), entity.getBrand(),
                    entity.getQtyInStock(), entity.getUnitPrice(), entity.getExpDate()
            );
        }
        return null;
    }

    @Override
    public MedicineDTO searchMedicineByName(String name) throws SQLException, ClassNotFoundException {
        Medicine entity = medicineDAO.searchByName(name);
        if (entity != null) {
            return new MedicineDTO(
                    entity.getMedicineId(), entity.getMedName(), entity.getBrand(),
                    entity.getQtyInStock(), entity.getUnitPrice(), entity.getExpDate()
            );
        }
        return null;
    }

    @Override
    public boolean updateExactQty(int medicineId, int newQty) throws SQLException, ClassNotFoundException {
        return medicineDAO.updateExactQty(medicineId, newQty);
    }
}
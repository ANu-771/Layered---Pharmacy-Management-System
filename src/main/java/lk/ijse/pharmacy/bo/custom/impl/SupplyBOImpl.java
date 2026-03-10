package lk.ijse.pharmacy.bo.custom.impl;

import lk.ijse.pharmacy.bo.custom.SupplyBO;
import lk.ijse.pharmacy.dao.DAOFactory;
import lk.ijse.pharmacy.dao.custom.MedicineDAO;
import lk.ijse.pharmacy.dao.custom.SupplyDAO;
import lk.ijse.pharmacy.dto.tm.SupplyRecordTM;
import lk.ijse.pharmacy.dto.CustomDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplyBOImpl implements SupplyBO {

    SupplyDAO supplyDAO = (SupplyDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.SUPPLY);
    MedicineDAO medicineDAO = (MedicineDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.MEDICINE);

    @Override
    public int getMedicineId(String medName) throws SQLException, ClassNotFoundException {
        return supplyDAO.getMedicineId(medName);
    }

    @Override
    public boolean saveSupply(int supplierId, String medName, LocalDate date, int qty, double unitCost, double totalCost) throws SQLException, ClassNotFoundException {
        int medicineId = supplyDAO.getMedicineId(medName);

        if (medicineId == -1) {
            return false;
        }

        return supplyDAO.saveSupply(supplierId, medicineId, date, qty, unitCost, totalCost);
    }

    @Override
    public List<SupplyRecordTM> getAllSupplies() throws SQLException, ClassNotFoundException {
        List<CustomDTO> entities = supplyDAO.getAllSupplies();
        List<SupplyRecordTM> tmList = new ArrayList<>();

        for (CustomDTO entity : entities) {
            tmList.add(new SupplyRecordTM(
                    entity.getDate(),
                    entity.getSupplierId(),
                    entity.getSupplierName(),
                    entity.getMedName(),
                    entity.getQty(),
                    entity.getUnitCost(),
                    entity.getTotalCost()
            ));
        }
        return tmList;
    }
}
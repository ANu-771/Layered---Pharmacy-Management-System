package lk.ijse.pharmacy.bo.custom.impl;

import lk.ijse.pharmacy.bo.custom.DashboardBO;
import lk.ijse.pharmacy.dao.DAOFactory;
import lk.ijse.pharmacy.dao.custom.QueryDAO;
import lk.ijse.pharmacy.dto.MedicineDTO;
import lk.ijse.pharmacy.entity.Medicine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardBOImpl implements DashboardBO {

    QueryDAO queryDAO = (QueryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.QUERY);

    @Override
    public int getActiveCustomerCount() throws SQLException, ClassNotFoundException {
        return queryDAO.getActiveCustomerCount();
    }

    @Override
    public int getTotalMedicineCount() throws SQLException, ClassNotFoundException {
        return queryDAO.getTotalMedicineCount();
    }

    @Override
    public double getTodayIncome() throws SQLException, ClassNotFoundException {
        return queryDAO.getTodayIncome();
    }

    @Override
    public Map<String, Double> getIncomeTrends() throws SQLException, ClassNotFoundException {
        return queryDAO.getIncomeTrends();
    }

    @Override
    public List<MedicineDTO> getExpiringMedicines() throws SQLException, ClassNotFoundException {
        List<Medicine> entities = queryDAO.getExpiringMedicines();
        List<MedicineDTO> dtos = new ArrayList<>();

        for (Medicine e : entities) {
            dtos.add(new MedicineDTO(
                    e.getMedicineId(), e.getMedName(), e.getBrand(),
                    e.getQtyInStock(), e.getUnitPrice(), e.getExpDate()
            ));
        }
        return dtos;
    }
}
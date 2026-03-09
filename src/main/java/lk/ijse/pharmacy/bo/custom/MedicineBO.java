package lk.ijse.pharmacy.bo.custom;

import lk.ijse.pharmacy.bo.SuperBO;
import lk.ijse.pharmacy.dto.MedicineDTO;

import java.sql.SQLException;
import java.util.List;

public interface MedicineBO extends SuperBO {
    boolean saveMedicine(MedicineDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateMedicine(MedicineDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteMedicine(int id) throws SQLException, ClassNotFoundException;

    List<MedicineDTO> getAllMedicines() throws SQLException, ClassNotFoundException;

    MedicineDTO searchMedicine(int id) throws SQLException, ClassNotFoundException;

    MedicineDTO searchMedicineByName(String name) throws SQLException, ClassNotFoundException;

    boolean updateExactQty(int medicineId, int newQty) throws SQLException, ClassNotFoundException;
}
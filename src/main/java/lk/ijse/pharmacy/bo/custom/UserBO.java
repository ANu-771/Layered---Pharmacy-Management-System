package lk.ijse.pharmacy.bo.custom;

import lk.ijse.pharmacy.bo.SuperBO;
import lk.ijse.pharmacy.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface UserBO extends SuperBO {
    List<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException;

    boolean saveUser(UserDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateUser(UserDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteUser(int id) throws SQLException, ClassNotFoundException;

    UserDTO searchUser(int id) throws SQLException, ClassNotFoundException;

    String checkLogin(String username, String password) throws SQLException, ClassNotFoundException;

    boolean isUsernameExists(String username) throws SQLException, ClassNotFoundException;

    UserDTO getUserByEmail(String email) throws SQLException, ClassNotFoundException;

}
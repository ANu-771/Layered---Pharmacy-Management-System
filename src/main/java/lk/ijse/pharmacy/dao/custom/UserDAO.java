package lk.ijse.pharmacy.dao.custom;

import lk.ijse.pharmacy.dao.CrudDAO;
import lk.ijse.pharmacy.entity.User;

import java.sql.SQLException;

public interface UserDAO extends CrudDAO<User> {

    String checkLogin(String username, String password) throws SQLException, ClassNotFoundException;

    boolean isUsernameExists(String username) throws SQLException, ClassNotFoundException;

    User getUserByEmail(String email) throws SQLException, ClassNotFoundException;

}

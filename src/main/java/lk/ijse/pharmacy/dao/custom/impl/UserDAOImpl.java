package lk.ijse.pharmacy.dao.custom.impl;

import lk.ijse.pharmacy.dao.CrudUtil;
import lk.ijse.pharmacy.dao.custom.UserDAO;
import lk.ijse.pharmacy.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public List<User> getAll() throws SQLException, ClassNotFoundException {
        List<User> userList = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user");

        while (resultSet.next()) {
            userList.add(new User(
                    resultSet.getInt("user_id"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            ));
        }
        return userList;
    }

    @Override
    public boolean save(User entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO user (username, email, password, role) VALUES (?, ?, ?, ?)",
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole());
    }

    @Override
    public boolean update(User entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE user SET username = ?, email = ?, password = ?, role = ? WHERE user_id = ?",
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.getUserId());
    }

    @Override
    public boolean delete(int id) throws SQLException, ClassNotFoundException { // FIXED: int id
        return CrudUtil.execute("DELETE FROM user WHERE user_id = ?", id);
    }

    @Override
    public User search(int id) throws SQLException, ClassNotFoundException { // FIXED: int id
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE user_id = ?", id);
        if (resultSet.next()) {
            return new User(
                    resultSet.getInt("user_id"), resultSet.getString("username"),
                    resultSet.getString("email"), resultSet.getString("password"),
                    resultSet.getString("role")
            );
        }
        return null;
    }

    @Override
    public String checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT role FROM user WHERE username = ? AND password = ?", username, password);
        if (resultSet.next()) {
            return resultSet.getString("role");
        }
        return null;
    }

    @Override
    public boolean isUsernameExists(String username) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT username FROM user WHERE username = ?", username);
        return resultSet.next();
    }

    @Override
    public User getUserByEmail(String email) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE email = ?", email);
        if (resultSet.next()) {
            return new User(
                    resultSet.getInt("user_id"), resultSet.getString("username"),
                    resultSet.getString("email"), resultSet.getString("password"),
                    resultSet.getString("role")
            );
        }
        return null;
    }
}
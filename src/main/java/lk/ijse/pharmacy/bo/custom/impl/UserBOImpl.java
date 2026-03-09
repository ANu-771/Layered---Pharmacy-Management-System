package lk.ijse.pharmacy.bo.custom.impl;

import lk.ijse.pharmacy.bo.custom.UserBO;
import lk.ijse.pharmacy.dao.DAOFactory;
import lk.ijse.pharmacy.dao.custom.UserDAO;
import lk.ijse.pharmacy.dto.UserDTO;
import lk.ijse.pharmacy.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserBOImpl implements UserBO {

    UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.USER);

    @Override
    public List<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException {
        List<User> entityList = userDAO.getAll();
        List<UserDTO> dtoList = new ArrayList<>();
        for (User entity : entityList) {
            dtoList.add(new UserDTO
                    (entity.getUserId(),
                            entity.getUsername(),
                            entity.getEmail(),
                            entity.getPassword(),
                            entity.getRole()));
        }
        return dtoList;
    }

    @Override
    public boolean saveUser(UserDTO dto) throws SQLException, ClassNotFoundException {
        return userDAO.save(new User(dto.getUserId(), dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getRole()));
    }

    @Override
    public boolean updateUser(UserDTO dto) throws SQLException, ClassNotFoundException {
        return userDAO.update(new User(dto.getUserId(), dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getRole()));
    }

    @Override
    public boolean deleteUser(int id) throws SQLException, ClassNotFoundException {
        return userDAO.delete(id);
    }

    @Override
    public UserDTO searchUser(int id) throws SQLException, ClassNotFoundException {
        User entity = userDAO.search(id);
        if (entity != null) {
            return new UserDTO(entity.getUserId(), entity.getUsername(), entity.getEmail(), entity.getPassword(), entity.getRole());
        }
        return null;
    }

    @Override
    public String checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        return userDAO.checkLogin(username, password);
    }

    @Override
    public boolean isUsernameExists(String username) throws SQLException, ClassNotFoundException {
        return userDAO.isUsernameExists(username);
    }

    @Override
    public UserDTO getUserByEmail(String email) throws SQLException, ClassNotFoundException {
        User entity = userDAO.getUserByEmail(email);
        if (entity != null) {
            return new UserDTO(entity.getUserId(), entity.getUsername(), entity.getEmail(), entity.getPassword(), entity.getRole());
        }
        return null;
    }
}
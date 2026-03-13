package lk.ijse.pharmacy.bo.custom.impl;

import lk.ijse.pharmacy.bo.custom.CustomerBO;
import lk.ijse.pharmacy.dao.DAOFactory;
import lk.ijse.pharmacy.dao.custom.CustomerDAO;
import lk.ijse.pharmacy.dto.CustomerDTO;
import lk.ijse.pharmacy.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {

    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public boolean saveCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        List<Customer> allCustomers = customerDAO.getAll();
        for (Customer customer : allCustomers) {
            if (customer.getContact().equals(dto.getContact())) {
                throw new RuntimeException("A customer with this Contact Number already exists!");
            }
        }

        return customerDAO.save(new Customer(dto.getCustomerId(), dto.getName(), dto.getContact(), dto.getAddress()));
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(dto.getCustomerId(), dto.getName(), dto.getContact(), dto.getAddress()));
    }

    @Override
    public boolean deleteCustomer(int id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException {
        List<Customer> entityList = customerDAO.getAll();
        List<CustomerDTO> dtoList = new ArrayList<>();

        for (Customer entity : entityList) {
            dtoList.add(new CustomerDTO(entity.getCustomerId(), entity.getName(), entity.getContact(), entity.getAddress()));
        }
        return dtoList;
    }

    @Override
    public CustomerDTO searchCustomer(int id) throws SQLException, ClassNotFoundException {
        Customer entity = customerDAO.search(id);
        if (entity != null) {
            return new CustomerDTO(entity.getCustomerId(), entity.getName(), entity.getContact(), entity.getAddress());
        }
        return null;
    }
}
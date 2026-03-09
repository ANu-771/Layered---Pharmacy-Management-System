package lk.ijse.pharmacy.dao;

import lk.ijse.pharmacy.dao.custom.impl.*;

public class DAOFactory {

    private static DAOFactory daoFactory;

    // Private constructor so no one can create a new instance from outside
    private DAOFactory() {
    }

    // Singleton getInstance method
    public static DAOFactory getInstance() {
        return daoFactory == null ? daoFactory = new DAOFactory() : daoFactory;
    }

    // Enum to hold all our DAO types
    public enum DAOTypes {
        CUSTOMER, MEDICINE, SUPPLIER, USER, ORDER, ORDER_DETAIL, PAYMENT, SUPPLY, QUERY
    }

    // The Factory method
    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            case MEDICINE:
                return new MedicineDAOImpl();
            case SUPPLIER:
                return new SupplierDAOImpl();
            case USER:
                return new UserDAOImpl();
            case ORDER:
                return new OrderDAOImpl();
            case ORDER_DETAIL:
                return new OrderMedicineDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case SUPPLY:
                return new SupplyDAOImpl();
            case QUERY:
                return new QueryDAOImpl();
            default:
                return null;
        }
    }
}
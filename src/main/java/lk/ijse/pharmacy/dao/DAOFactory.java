package lk.ijse.pharmacy.dao;

import lk.ijse.pharmacy.dao.custom.impl.*;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return daoFactory == null ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {
        CUSTOMER, MEDICINE, SUPPLIER, USER, ORDER, ORDER_DETAIL, PAYMENT, SUPPLY, QUERY
    }

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
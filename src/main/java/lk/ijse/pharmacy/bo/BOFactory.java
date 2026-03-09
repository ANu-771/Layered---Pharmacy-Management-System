package lk.ijse.pharmacy.bo;

import lk.ijse.pharmacy.bo.custom.impl.*;

public class BOFactory {

    private static BOFactory boFactory;

    // Private constructor to prevent object creation from outside
    private BOFactory() {
    }

    // Singleton getInstance method
    public static BOFactory getInstance() {
        return boFactory == null ? boFactory = new BOFactory() : boFactory;
    }

    // Enum to hold all our BO types
    public enum BOTypes {
        CUSTOMER, MEDICINE, SUPPLIER, USER, SUPPLY, PLACE_ORDER, DASHBOARD, REPORT
    }

    // The Factory method
    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case MEDICINE:
                return new MedicineBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            case SUPPLY:
                return new SupplyBOImpl();
            case USER:
                return new UserBOImpl();
            case PLACE_ORDER:
                return new PlaceOrderBOImpl();
            case DASHBOARD:
                return new DashboardBOImpl();
            case REPORT:
                return new ReportBOImpl(); // Added ReportBO for your ReportController
            default:
                return null;
        }
    }
}
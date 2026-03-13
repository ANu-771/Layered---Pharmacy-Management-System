package lk.ijse.pharmacy.bo;

import lk.ijse.pharmacy.bo.custom.impl.*;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return boFactory == null ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {
        CUSTOMER, MEDICINE, SUPPLIER, USER, SUPPLY, PLACE_ORDER, DASHBOARD, REPORT
    }

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
                return new ReportBOImpl();
            default:
                return null;
        }
    }
}
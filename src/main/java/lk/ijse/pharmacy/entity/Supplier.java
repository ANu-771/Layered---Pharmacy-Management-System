package lk.ijse.pharmacy.entity;

public class Supplier {
    private int supplierId;
    private String supplierName;
    private String email;
    private String contactNum;

    public Supplier() {
    }

    public Supplier(int supplierId, String supplierName, String email, String contactNum) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.email = email;
        this.contactNum = contactNum;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }
}

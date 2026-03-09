package lk.ijse.pharmacy.entity;

public class CustomEntity {

    private String date;
    private double totalCost;
    private int qty;

    private int supplierId;
    private String supplierName;
    private String medName;
    private double unitCost;

    private int orderId;
    private String customerName;

    public CustomEntity() {
    }

    public CustomEntity(String date, int supplierId, String supplierName, String medName, int qty, double unitCost, double totalCost) {
        this.date = date;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.medName = medName;
        this.qty = qty;
        this.unitCost = unitCost;
        this.totalCost = totalCost;
    }

    public CustomEntity(int orderId, String customerName, String date, double totalCost) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.date = date;
        this.totalCost = totalCost;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
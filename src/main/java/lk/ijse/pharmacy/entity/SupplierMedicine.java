package lk.ijse.pharmacy.entity;

import java.util.Date;

public class SupplierMedicine {
    private int supplyId;
    private int supplierId;
    private int medicineId;
    private Date date;
    private int qty;
    private double unitCost;
    private double totalCost;

    public SupplierMedicine() {
    }

    public SupplierMedicine(int supplyId, int supplierId, int medicineId, Date date, int qty, double unitCost, double totalCost) {
        this.supplyId = supplyId;
        this.supplierId = supplierId;
        this.medicineId = medicineId;
        this.date = date;
        this.qty = qty;
        this.unitCost = unitCost;
        this.totalCost = totalCost;
    }

    public int getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}

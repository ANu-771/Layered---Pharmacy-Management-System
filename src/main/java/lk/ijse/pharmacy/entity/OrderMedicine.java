package lk.ijse.pharmacy.entity;

public class OrderMedicine {
    private int orderId;
    private int medicineId;
    private int qty;
    private double unitPrice;
    private double lineTotal;

    public OrderMedicine() {
    }

    public OrderMedicine(int orderId, int medicineId, int qty, double unitPrice, double lineTotal) {
        this.orderId = orderId;
        this.medicineId = medicineId;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }
}

package dev.voleum.ordermolder.Object;

public class Order extends Document {

    private String warehouseUid;

    public Order() {

    }

    public Order(String uid, String date, String companyUid, String partnerUid, String warehouseUid, double sum) {
        super(uid, date, companyUid, partnerUid, sum);
        this.warehouseUid = warehouseUid;
    }

    public String getWarehouseUid() {
        return warehouseUid;
    }

    public void setWarehouseUid(String warehouseUid) {
        this.warehouseUid = warehouseUid;
    }
}

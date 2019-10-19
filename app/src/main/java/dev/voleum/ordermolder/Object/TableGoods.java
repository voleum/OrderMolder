package dev.voleum.ordermolder.Object;

public class TableGoods extends Table {

    private String goodUid;
    private double quantity;
    private double price;
    private double sum;

    public TableGoods(String uid, int position, String goodUid, double quantity, double price, double sum) {
        super(uid, position);
        this.goodUid = goodUid;
        this.quantity = quantity;
        this.price = price;
        this.sum = sum;
    }

    public String getGoodUid() {
        return goodUid;
    }

    public void setGoodUid(String goodUid) {
        this.goodUid = goodUid;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}

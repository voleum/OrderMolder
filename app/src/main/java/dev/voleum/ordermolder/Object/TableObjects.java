package dev.voleum.ordermolder.Object;

public class TableObjects extends Table {

    private String objectUid;
    private double sum;

    public TableObjects(String uid, int position, String objectUid, double sum) {
        super(uid, position);
        this.objectUid = objectUid;
        this.sum = sum;
    }

    public String getObjectUid() {
        return objectUid;
    }

    public void setObjectUid(String objectUid) {
        this.objectUid = objectUid;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}

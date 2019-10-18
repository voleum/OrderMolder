package dev.voleum.ordermolder.Object;

public class Good extends Catalog {

    private String unitUid;

    public Good(String uid, String name, String unitUid) {
        super(uid, name);
        this.unitUid = unitUid;
    }

    public String getUnitUid() {
        return unitUid;
    }

    public void setUnitUid(String unitUid) {
        this.unitUid = unitUid;
    }
}

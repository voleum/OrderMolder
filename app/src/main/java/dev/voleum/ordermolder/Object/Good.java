package dev.voleum.ordermolder.Object;

public class Good extends Catalog {

    private String groupUid;
    private String unitUid;

    public Good(String uid, String groupUid, String name, String unitUid) {
        super(uid, name);
        this.groupUid = groupUid;
        this.unitUid = unitUid;
    }

    public String getUnitUid() {
        return unitUid;
    }

    public void setUnitUid(String unitUid) {
        this.unitUid = unitUid;
    }
}

package dev.voleum.ordermolder.Object;

public abstract class EconomicEntity extends Catalog {

    protected String tin;

    public EconomicEntity() {

    }

    public EconomicEntity(String uid, String name, String tin) {
        super(uid, name);
        this.tin = tin;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }
}

package dev.voleum.ordermolder.models;

public abstract class EconomicEntity extends Catalog {

    protected String tin;

    protected EconomicEntity() {

    }

    protected EconomicEntity(String uid, String name, String tin) {
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

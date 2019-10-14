package dev.voleum.ordermolder.Object;

public class Good extends Catalog {

    private Unit unit;

    public Good(String uid, String name, Unit unit) {
        super(uid, name);
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}

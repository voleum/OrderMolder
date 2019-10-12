package dev.voleum.ordermolder.Object;

public class Good extends Catalog {

    private Unit unit;

    public Good(String code, String name, Unit unit) {
        super(code, name);
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}

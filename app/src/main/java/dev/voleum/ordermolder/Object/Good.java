package dev.voleum.ordermolder.Object;

public class Good extends Catalog {

    private Unit unit;

    public Good(String number, String name, Unit unit) {
        super(number, name);
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}

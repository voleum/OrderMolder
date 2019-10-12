package dev.voleum.ordermolder.Object;

public class Unit extends Catalog {

    private String shortName;

    protected Unit(String code, String name, String shortName) {
        super(code, name);
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}

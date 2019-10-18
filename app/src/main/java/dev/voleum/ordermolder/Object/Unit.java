package dev.voleum.ordermolder.Object;

public class Unit extends Catalog {

    private int code;
    private String fullName;

    public Unit(String uid, int code, String name, String fullName) {
        super(uid, name);
        this.code = code;
        this.fullName = fullName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}

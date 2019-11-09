package dev.voleum.ordermolder.enums;

import dev.voleum.ordermolder.objects.Catalog;
import dev.voleum.ordermolder.objects.Company;
import dev.voleum.ordermolder.objects.Good;
import dev.voleum.ordermolder.objects.Partner;
import dev.voleum.ordermolder.objects.Unit;
import dev.voleum.ordermolder.objects.Warehouse;

public enum CatalogTypes {
    COMPANY(Company.class),
    PARTNER(Partner.class),
    WAREHOUSE(Warehouse.class),
    GOOD(Good.class),
    UNIT(Unit.class);

//    private Class<? extends Catalog> catalogClass;

    CatalogTypes(Class<? extends Catalog> catalogClass) {
//        this.catalogClass = catalogClass;
    }

//    public Class<? extends Catalog> getValue() {
//        return catalogClass;
//    }
}

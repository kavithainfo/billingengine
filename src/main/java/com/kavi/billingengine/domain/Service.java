package com.kavi.billingengine.domain;

import java.math.BigDecimal;

public class Service {

    private final ServiceType serviceType;
    private final Integer quantity;
    private final BigDecimal defaultPrice;
    private final BigDecimal serviceCharge;

    public Service(ServiceType serviceType, Integer quantity, BigDecimal defaultPrice, BigDecimal serviceCharge) {
        this.serviceType = serviceType;
        this.quantity = quantity;
        this.defaultPrice = defaultPrice;
        this.serviceCharge = serviceCharge;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Service service = (Service) o;

        if (serviceType != service.serviceType) return false;
        if (!quantity.equals(service.quantity)) return false;
        if (!defaultPrice.equals(service.defaultPrice)) return false;
        return serviceCharge.equals(service.serviceCharge);

    }

    @Override
    public int hashCode() {
        int result = serviceType.hashCode();
        result = 31 * result + quantity.hashCode();
        result = 31 * result + defaultPrice.hashCode();
        result = 31 * result + serviceCharge.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceType=" + serviceType +
                ", quantity=" + quantity +
                ", defaultPrice=" + defaultPrice +
                ", serviceCharge=" + serviceCharge +
                '}';
    }
}

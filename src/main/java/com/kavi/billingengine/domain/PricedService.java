package com.kavi.billingengine.domain;

import java.math.BigDecimal;
import java.util.Optional;

public class PricedService extends Service {

    private final BigDecimal totalDiscountedPrice;
    private final Optional<Discount> appliedDiscount;

    public PricedService(
            ServiceType serviceType,
            Integer quantity,
            BigDecimal defaultPrice,
            BigDecimal serviceCharge,
            BigDecimal totalDiscountedPrice,
            Optional<Discount> appliedDiscount) {
        super(serviceType, quantity, defaultPrice, serviceCharge);
        this.totalDiscountedPrice = totalDiscountedPrice;
        this.appliedDiscount = appliedDiscount;
    }

    public BigDecimal getTotalDiscountedPrice() {
        return totalDiscountedPrice;
    }

    public Optional<Discount> getAppliedDiscount() {
        return appliedDiscount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PricedService that = (PricedService) o;

        if (!totalDiscountedPrice.equals(that.totalDiscountedPrice)) return false;
        return appliedDiscount.equals(that.appliedDiscount);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + totalDiscountedPrice.hashCode();
        result = 31 * result + appliedDiscount.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "Service Name: " + getServiceType() +
                ", Quantity: " + getQuantity() +
                ", Default Price: " + getDefaultPrice() +
                ", Service Charge: " + getServiceCharge() +
                ", Total price after discount: " + totalDiscountedPrice +
                ", appliedDiscount: " + appliedDiscount +
                "}\n";
    }
}
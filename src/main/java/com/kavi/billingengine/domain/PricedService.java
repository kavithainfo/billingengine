package com.kavi.billingengine.domain;

import java.math.BigDecimal;
import java.util.List;

public class PricedService extends Service {

    private final Integer finalDiscountedPrice;
    private final List<Discount> appliedDiscount;

    public PricedService(
            ServiceType serviceType,
            Integer quantity,
            BigDecimal defaultPrice,
            BigDecimal serviceCharge,
            Integer finalDiscountedPrice,
            List<Discount> appliedDiscount) {
        super(serviceType, quantity, defaultPrice, serviceCharge);
        this.finalDiscountedPrice = finalDiscountedPrice;
        this.appliedDiscount = appliedDiscount;
    }

    public Integer getFinalDiscountedPrice() {
        return finalDiscountedPrice;
    }

    public List<Discount> getAppliedDiscount() {
        return appliedDiscount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PricedService that = (PricedService) o;

        if (!finalDiscountedPrice.equals(that.finalDiscountedPrice)) return false;
        return appliedDiscount.equals(that.appliedDiscount);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + finalDiscountedPrice.hashCode();
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
                ", Total price after discount: " + finalDiscountedPrice +
                ", appliedDiscount: " + appliedDiscount +
                "}\n";
    }
}
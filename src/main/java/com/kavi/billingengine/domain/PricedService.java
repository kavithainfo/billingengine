package com.kavi.billingengine.domain;

import java.math.BigDecimal;
import java.util.Optional;

public class PricedService extends Service {

    private final BigDecimal finalTotalPrice;
    private final Optional<Discount> appliedDiscount;

    public PricedService(
            ServiceType serviceType,
            Integer quantity,
            BigDecimal defaultPrice,
            BigDecimal serviceCharge,
            BigDecimal finalTotalPrice,
            Optional<Discount> appliedDiscount) {
        super(serviceType, quantity, defaultPrice, serviceCharge);
        this.finalTotalPrice = finalTotalPrice;
        this.appliedDiscount = appliedDiscount;
    }

    public BigDecimal getFinalTotalPrice() {
        return finalTotalPrice;
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

        if (!finalTotalPrice.equals(that.finalTotalPrice)) return false;
        return appliedDiscount.equals(that.appliedDiscount);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + finalTotalPrice.hashCode();
        result = 31 * result + appliedDiscount.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PricedService{" +
                "finalTotalPrice=" + finalTotalPrice +
                ", appliedDiscount=" + appliedDiscount +
                '}';
    }
}

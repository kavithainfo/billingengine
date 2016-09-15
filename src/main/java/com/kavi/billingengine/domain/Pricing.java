package com.kavi.billingengine.domain;

import java.math.BigDecimal;
import java.util.List;

public class Pricing {

    private final BigDecimal defaultPrice;
    private final List<Discount> appliedDiscounts;
    private final BigDecimal totalDiscount;
    private final BigDecimal finalPrice;

    public Pricing(
            BigDecimal defaultPrice,
            List<Discount> appliedDiscounts,
            BigDecimal totalDiscount,
            BigDecimal finalPrice) {
        this.defaultPrice = defaultPrice;
        this.appliedDiscounts = appliedDiscounts;
        this.totalDiscount = totalDiscount;
        this.finalPrice = finalPrice;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public List<Discount> getAppliedDiscounts() {
        return appliedDiscounts;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pricing pricing = (Pricing) o;

        if (!defaultPrice.equals(pricing.defaultPrice)) return false;
        if (!appliedDiscounts.equals(pricing.appliedDiscounts)) return false;
        if (!totalDiscount.equals(pricing.totalDiscount)) return false;
        return finalPrice.equals(pricing.finalPrice);

    }

    @Override
    public int hashCode() {
        int result = defaultPrice.hashCode();
        result = 31 * result + appliedDiscounts.hashCode();
        result = 31 * result + totalDiscount.hashCode();
        result = 31 * result + finalPrice.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Pricing{" +
                "defaultPrice=" + defaultPrice +
                ", appliedDiscounts=" + appliedDiscounts +
                ", totalDiscount=" + totalDiscount +
                ", finalPrice=" + finalPrice +
                '}';
    }
}
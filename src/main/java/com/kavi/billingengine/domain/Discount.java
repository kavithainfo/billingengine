package com.kavi.billingengine.domain;

public class Discount {

    private final String name;
    private final Integer percentageDiscount;

    public Discount(String name, Integer percentageDiscount) {
        this.name = name;
        this.percentageDiscount = percentageDiscount;
    }

    public String getName() {
        return name;
    }

    public Integer getPercentageDiscount() {
        return percentageDiscount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Discount discount = (Discount) o;

        if (!name.equals(discount.name)) return false;
        return percentageDiscount.equals(discount.percentageDiscount);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + percentageDiscount.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "name='" + name + '\'' +
                ", percentageDiscount=" + percentageDiscount +
                '}';
    }
}

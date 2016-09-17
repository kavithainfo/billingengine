package com.kavi.billingengine.domain;

import java.util.List;

public class BillingRequest {

    private final List<Service> services;
    private final Integer age;
    private final Boolean hasHealthInsurance;
    private final Boolean hasBeenDiagnosed;

    public BillingRequest(
            List<Service> services,
            Integer age,
            Boolean hasHealthInsurance,
            Boolean hasBeenDiagnosed) {
        this.services = services;
        this.age = age;
        this.hasHealthInsurance = hasHealthInsurance;
        this.hasBeenDiagnosed = hasBeenDiagnosed;
    }

    public List<Service> getServices() {
        return services;
    }

    public Integer getAge() {
        return age;
    }

    public Boolean getHasHealthInsurance() {
        return hasHealthInsurance;
    }

    public Boolean getHasBeenDiagnosed() {
        return hasBeenDiagnosed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BillingRequest that = (BillingRequest) o;

        if (!services.equals(that.services)) return false;
        if (!age.equals(that.age)) return false;
        if (!hasHealthInsurance.equals(that.hasHealthInsurance)) return false;
        return hasBeenDiagnosed.equals(that.hasBeenDiagnosed);

    }

    @Override
    public int hashCode() {
        int result = services.hashCode();
        result = 31 * result + age.hashCode();
        result = 31 * result + hasHealthInsurance.hashCode();
        result = 31 * result + hasBeenDiagnosed.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BillingRequest{" +
                "services=" + services +
                ", age=" + age +
                ", hasHealthInsurance=" + hasHealthInsurance +
                ", hasBeenDiagnosed=" + hasBeenDiagnosed +
                '}';
    }
}
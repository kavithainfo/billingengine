package com.kavi.billingengine.domain;

import java.util.List;

public class BillingRequest {

    private final List<Service> services;
    private final Integer age;
    private final Boolean hasHealthInsurance;
    private final Boolean hasHadMediHealthDiagnosis;

    public BillingRequest(
            List<Service> services,
            Integer age,
            Boolean hasHealthInsurance,
            Boolean hasHadMediHealthDiagnosis) {
        this.services = services;
        this.age = age;
        this.hasHealthInsurance = hasHealthInsurance;
        this.hasHadMediHealthDiagnosis = hasHadMediHealthDiagnosis;
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

    public Boolean getHasHadMediHealthDiagnosis() {
        return hasHadMediHealthDiagnosis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BillingRequest that = (BillingRequest) o;

        if (!services.equals(that.services)) return false;
        if (!age.equals(that.age)) return false;
        if (!hasHealthInsurance.equals(that.hasHealthInsurance)) return false;
        return hasHadMediHealthDiagnosis.equals(that.hasHadMediHealthDiagnosis);

    }

    @Override
    public int hashCode() {
        int result = services.hashCode();
        result = 31 * result + age.hashCode();
        result = 31 * result + hasHealthInsurance.hashCode();
        result = 31 * result + hasHadMediHealthDiagnosis.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BillingRequest{" +
                "services=" + services +
                ", age=" + age +
                ", hasHealthInsurance=" + hasHealthInsurance +
                ", hasHadMediHealthDiagnosis=" + hasHadMediHealthDiagnosis +
                '}';
    }
}

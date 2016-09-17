package com.kavi.billingengine.billing;

import com.kavi.billingengine.discounts.ApplicableDiscountsFinder;
import com.kavi.billingengine.discounts.DiscountApplicator;
import com.kavi.billingengine.domain.BillingRequest;
import com.kavi.billingengine.domain.Discount;
import com.kavi.billingengine.domain.PricedService;
import com.kavi.billingengine.domain.Service;

import java.util.List;
import java.util.Map;

public class BillingService {

    private final ApplicableDiscountsFinder applicableDiscountsFinder;
    private final DiscountApplicator discountApplicator;

    public BillingService(ApplicableDiscountsFinder applicableDiscountsFinder, DiscountApplicator discountApplicator) {
        this.applicableDiscountsFinder = applicableDiscountsFinder;
        this.discountApplicator = discountApplicator;
    }

    public List<PricedService> chargeMedicalServices(BillingRequest billingRequest) {
        final Map<Service, List<Discount>> applicableDiscounts = applicableDiscountsFinder.find(
                billingRequest.getServices(),
                billingRequest.getAge(),
                billingRequest.getHasHealthInsurance(),
                billingRequest.getHasBeenDiagnosed()
        );

        final List<PricedService> pricedServices = discountApplicator.apply(applicableDiscounts);

        return pricedServices;
    }
}